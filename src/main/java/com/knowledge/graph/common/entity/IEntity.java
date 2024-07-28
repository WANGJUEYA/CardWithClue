package com.knowledge.graph.common.entity;

import com.alibaba.fastjson2.JSONObject;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface IEntity {

    String getKey();

    void setKey(String key);

    String getTime();

    void setTime(String time);

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

    default String mergeJson(String newJson) {
        if (StringUtils.isBlank(newJson)) {
            return StringUtils.defaultIfBlank(getJson(), StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(getJson()) || Objects.equals(getJson(), newJson)) {
            return newJson;
        }
        JSONObject oldJsonObj = JSONObject.parseObject(getJson());
        JSONObject newJsonObj = JSONObject.parseObject(newJson);
        oldJsonObj.putAll(newJsonObj);
        return JSONObject.toJSONString(oldJsonObj);
    }

    default <R> boolean updateInfo(IEntity newData, Function<IEntity, R> get, BiConsumer<IEntity, R> set) {
        R oldInfo = get.apply(this);
        R newInfo = get.apply(newData);
        if (oldInfo instanceof String) {
            oldInfo = (R) StringUtils.defaultIfBlank((String) oldInfo, StringUtils.EMPTY);
        }
        if (newInfo instanceof String) {
            newInfo = (R) StringUtils.defaultIfBlank((String) newInfo, StringUtils.EMPTY);
        }
        if (Objects.equals(oldInfo, newInfo)) {
            return false;
        } else {
            set.accept(this, newInfo);
            return true;
        }
    }

    default void mergeFrom(@NotNull IEntity newData) {
        boolean update = Boolean.TRUE.equals(this.getUpdated());
        // 合并附加信息
        String oldJson = StringUtils.defaultIfBlank(getJson(), StringUtils.EMPTY);
        String newJson = mergeJson(newData.getJson());
        if (!Objects.equals(oldJson, newJson)) {
            update = true;
            setJson(newJson);
        }
        update = updateInfo(newData, IEntity::getKey, IEntity::setKey) || update;
        update = updateInfo(newData, IEntity::getTime, IEntity::setTime) || update;
        update = updateInfo(newData, IEntity::getAlias, IEntity::setAlias) || update;
        update = updateInfo(newData, IEntity::getLink, IEntity::setLink) || update;
        update = updateInfo(newData, IEntity::getDesc, IEntity::setDesc) || update;
        setUpdated(update);
    }

}
