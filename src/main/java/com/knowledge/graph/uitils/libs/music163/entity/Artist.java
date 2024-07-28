package com.knowledge.graph.uitils.libs.music163.entity;

import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.uitils.CrawlerUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.knowledge.graph.common.constant.CardKeyEnum.MUSIC_163;
import static com.knowledge.graph.common.constant.ClueGroupEnum.LIB_STORE_MUSIC_163;
import static com.knowledge.graph.uitils.libs.music163.LibsConstant.REQUEST_ARTIST_PRE;

@Data
public class Artist {

    String id;

    /**
     * 歌手名字
     */
    String name;

    Set<String> alias;

    public Artist() {
    }

    public Artist(String id, DataCard dataCard) {
        this.id = id;
        this.name = dataCard.getKey();
        if (StringUtils.isNotBlank(dataCard.getAlias())) {
            alias = Set.of(dataCard.getAlias().split(","));
        } else {
            alias = new HashSet<>();
        }
        this.dataCard = dataCard;
    }

    DataCard dataCard;

    public DataCard createCard() {
        if (dataCard == null) {
            DataCard newData = new DataCard(CardGroupEnum.THING_PERSON, name);
            newData.setAlias(alias);
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

    public static List<Artist> graphFlat(List<DataClue> storeList) {
        return storeList.stream().map(store -> {
            DataCard card = CrawlerUtils.getDataCardId(store.getTarget());
            return new Artist(store.getKey(), card);
        }).toList();
    }

    public static List<Artist> graphFlat(Set<String> storeIds) {
        return graphFlat(CrawlerUtils.CLUE_MAP_ID.values().stream()
                .filter(d -> StringUtils.isNotBlank(d.getKey()))
                .filter(d -> storeIds.contains(d.getKey())).toList());
    }

}
