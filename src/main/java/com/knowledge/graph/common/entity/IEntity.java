package com.knowledge.graph.common.entity;

import com.alibaba.fastjson2.JSONObject;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public interface IEntity<G> {

    String getId();

    void setId(String id);

    G getGroup();

    void setGroup(G group);

    String getKey();

    void setKey(String key);

    String getAlias();

    void setAlias(String alias);

    default void setAlias(Set<String> alias) {
        setAlias(alias.stream().filter(n -> !Objects.equals(n, getKey())).collect(Collectors.joining(",")));
    }

    String getLink();

    void setLink(String link);

    String getDesc();

    void setDesc(String desc);

    String getJson();

    void setJson(String json);

    Boolean getUpdated();

    void setUpdated(Boolean updated);

    default Stream<String> names() {
        Stream<String> keyStream = Stream.of(getKey());
        if (StringUtils.isBlank(getAlias())) {
            return keyStream;
        } else {
            return Stream.concat(keyStream, Stream.of(getAlias().split(","))).filter(StringUtils::isNotBlank).distinct();
        }
    }

    default <T extends IEntity<?>> boolean nameEquals(T item) {
        if (item == null) {
            return false;
        }
        Set<String> exist = item.names().collect(Collectors.toSet());
        return names().anyMatch(exist::contains);
    }

    default <T extends IEntity<?>> boolean findEquals(T item) {
        if (item == null) {
            return false;
        }
        return (StringUtils.isNotBlank(getId()) && Objects.equals(getId(), item.getId()))
                || (Objects.equals(getGroup(), item.getGroup()) && nameEquals(item));
    }

    default <T extends IEntity<?>> T find(List<T> exist) {
        return exist.stream().filter(this::findEquals).findFirst().orElse(null);
    }

    default String mergeJson(String oldJson) {
        if (StringUtils.isBlank(getJson()) || Objects.equals(getJson(), oldJson)) {
            return StringUtils.defaultIfBlank(oldJson, StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(oldJson)) {
            return StringUtils.defaultIfBlank(getJson(), StringUtils.EMPTY);
        }
        JSONObject oldJsonObj = JSONObject.parseObject(oldJson);
        JSONObject newJsonObj = JSONObject.parseObject(getJson());
        oldJsonObj.putAll(newJsonObj);
        return JSONObject.toJSONString(oldJsonObj);
    }

    default <T extends IEntity<?>, R> boolean updateInfo(T exist, Function<T, R> get, BiConsumer<T, R> set) {
        R oldInfo = get.apply(exist);
        R newInfo = get.apply((T) this);
        if (oldInfo instanceof String) {
            oldInfo = (R) StringUtils.defaultIfBlank((String) oldInfo, StringUtils.EMPTY);
        }
        if (newInfo instanceof String) {
            newInfo = (R) StringUtils.defaultIfBlank((String) newInfo, StringUtils.EMPTY);
        }
        if (Objects.equals(oldInfo, newInfo)) {
            return false;
        } else {
            set.accept(exist, newInfo);
            return true;
        }
    }

    default <T extends IEntity<?>> T mergeTo(@NotNull T exist) {
        boolean update = false;

        // 更新别名
        String oldAlias = StringUtils.defaultIfBlank(exist.getAlias(), StringUtils.EMPTY);
        Set<String> names = exist.names().collect(Collectors.toSet());
        names().forEach(names::add);
        exist.setAlias(names);
        if (!Objects.equals(oldAlias, StringUtils.defaultIfBlank(exist.getAlias(), StringUtils.EMPTY))) {
            update = true;
        }

        // 合并附加信息
        String oldJson = StringUtils.defaultIfBlank(exist.getJson(), StringUtils.EMPTY);
        String newJson = mergeJson(oldJson);
        if (!Objects.equals(oldJson, newJson)) {
            update = true;
            setJson(newJson);
        }
        update = updateInfo(exist, IEntity::getLink, IEntity::setLink) || update;
        update = updateInfo(exist, IEntity::getDesc, IEntity::setDesc) || update;
        exist.setUpdated(update);
        return exist;
    }

    default <T extends IEntity<?>> T findAndMergeTo(List<T> exist) {
        T find = find(exist);
        if (find == null) {
            setUpdated(true);
            return (T) this;
        } else {
            return mergeTo(find);
        }
    }

    static <T extends IEntity<?>> void mergeToList(List<T> exist, List<T> appends) {
        for (T a : appends) {
            if (a != null) {
                T find = a.find(exist);
                if (find == null) {
                    a.setUpdated(true);
                    exist.add(a);
                } else {
                    a.mergeTo(find);
                }
            }
        }
    }

}
