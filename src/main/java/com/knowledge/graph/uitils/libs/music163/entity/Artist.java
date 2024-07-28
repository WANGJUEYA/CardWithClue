package com.knowledge.graph.uitils.libs.music163.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.uitils.CrawlerUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.*;

import static com.knowledge.graph.common.constant.CardGroupEnum.THING_PERSON;
import static com.knowledge.graph.common.constant.CardKeyEnum.MUSIC_163;
import static com.knowledge.graph.common.constant.ClueGroupEnum.LIB_STORE_MUSIC_163;
import static com.knowledge.graph.uitils.libs.music163.LibsConstant.REQUEST_ARTIST_PRE;

@Slf4j
@Data
public class Artist {

    String id;

    /**
     * 歌手名字
     */
    String name;

    Set<String> alias;

    String time;

    int albumSize;

    public Artist() {
    }

    public Artist(String id, DataCard dataCard) {
        Assert.notNull(dataCard, "必须有对应卡片配置");
        this.id = id;
        this.name = dataCard.getKey();
        if (StringUtils.isNotBlank(dataCard.getAlias())) {
            alias = Set.of(dataCard.getAlias().split(","));
        } else {
            alias = new HashSet<>();
        }
        time = dataCard.getTime();
        this.dataCard = dataCard;
        try {
            JSONObject info = JSONObject.parse(dataCard.getJson());
            this.albumSize = info.getIntValue("albums");
        } catch (Exception e) {
            log.error("获取额外信息失败 >>>> {}", e.getMessage());
        }
    }

    DataCard dataCard;

    public DataCard createCard() {
        if (dataCard == null) {
            DataCard newData = new DataCard(THING_PERSON, name);
            newData.setAlias(alias);
            newData.setTime(time);
            newData.setJson(JSON.toJSONString(Map.of("albums", albumSize)));
            dataCard = CrawlerUtils.mergeDataCard(newData);
        }
        return dataCard;
    }

    public DataClue createClue() {
        DataClue store = new DataClue(LIB_STORE_MUSIC_163, MUSIC_163.card().getId(), createCard());
        store.setKey(id);
        store.setLink(REQUEST_ARTIST_PRE + id);
        return store;
    }

    public DataGraph graphItem() {
        return new DataGraph(List.of(MUSIC_163.card(), createCard()), List.of(createClue()));
    }

    public static List<Artist> graphFlat(List<DataClue> storeList, boolean refreshAll) {
        return storeList.stream().filter(e -> LIB_STORE_MUSIC_163.equals(e.getDataGroup())).map(store -> {
            DataCard card = CrawlerUtils.getDataCardId(store.getTarget());
            if (card == null || !THING_PERSON.equals(card.getDataGroup()) || !(refreshAll || Boolean.TRUE.equals(card.getUpdated()))) {
                return null;
            }
            return new Artist(store.getKey(), card);
        }).filter(Objects::nonNull).toList();
    }

    public static List<Artist> graphFlat(Set<String> storeIds) {
        return graphFlat(CrawlerUtils.CLUE_MAP_ID.values().stream()
                .filter(d -> StringUtils.isNotBlank(d.getKey()))
                .filter(d -> storeIds.contains(d.getKey())).toList(), true);
    }

}
