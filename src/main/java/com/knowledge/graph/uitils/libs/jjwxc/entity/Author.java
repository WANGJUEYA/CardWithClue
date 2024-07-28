package com.knowledge.graph.uitils.libs.jjwxc.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.uitils.CrawlerUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.knowledge.graph.common.constant.CardGroupEnum.THING_PERSON;
import static com.knowledge.graph.common.constant.CardKeyEnum.JJWXC;
import static com.knowledge.graph.common.constant.ClueGroupEnum.LIB_STORE_JJWXC;
import static com.knowledge.graph.uitils.libs.jjwxc.LibsConstant.REQUEST_AUTHOR_PRE;

@Slf4j
@Data
public class Author {

    String id;

    /**
     * 作者名字
     */
    String name;

    int books;

    public Author(String id, String name, int books) {
        this.id = id;
        this.name = name;
        this.books = books;
    }

    public Author(String id, DataCard dataCard) {
        Assert.notNull(dataCard, "必须有对应卡片配置");
        this.id = id;
        this.name = dataCard.getKey();
        this.dataCard = dataCard;
        try {
            JSONObject info = JSONObject.parse(dataCard.getJson());
            this.books = info.getIntValue("books");
        } catch (Exception e) {
            log.error("获取额外信息失败 >>>> {}", e.getMessage());
        }
    }

    DataCard dataCard;

    public DataCard createCard() {
        if (dataCard == null) {
            DataCard author = new DataCard(CardGroupEnum.THING_PERSON, name);
            author.setJson(JSON.toJSONString(Map.of("books", books)));
            dataCard = CrawlerUtils.mergeDataCard(author);
        }
        return dataCard;
    }

    public DataClue createClue() {
        DataClue store = new DataClue(LIB_STORE_JJWXC, JJWXC.card().getId(), createCard());
        store.setKey(id);
        store.setLink(REQUEST_AUTHOR_PRE + id);
        return store;
    }

    public DataGraph graphItem() {
        return new DataGraph(List.of(JJWXC.card(), createCard()), List.of(createClue()));
    }

    public static List<Author> graphFlat(List<DataClue> storeList, boolean refreshAll) {
        return storeList.stream().filter(e -> LIB_STORE_JJWXC.equals(e.getDataGroup())).map(store -> {
            DataCard card = CrawlerUtils.getDataCardId(store.getTarget());
            if (card == null || !THING_PERSON.equals(card.getDataGroup()) || !(refreshAll || Boolean.TRUE.equals(card.getUpdated()))) {
                return null;
            }
            return new Author(store.getKey(), card);
        }).filter(Objects::nonNull).toList();
    }

    public static List<Author> graphFlat(Set<String> storeIds) {
        return graphFlat(CrawlerUtils.CLUE_MAP_ID.values().stream()
                .filter(d -> StringUtils.isNotBlank(d.getKey()))
                .filter(d -> storeIds.contains(d.getKey())).toList(), true);
    }

}
