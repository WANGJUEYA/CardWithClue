package com.knowledge.graph.uitils.libs.music163.entity;

import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.uitils.CrawlerUtils;
import lombok.Data;

import java.util.List;

import static com.knowledge.graph.common.constant.CardKeyEnum.MUSIC_163;
import static com.knowledge.graph.common.constant.ClueGroupEnum.LIB_STORE_MUSIC_163;
import static com.knowledge.graph.common.constant.ClueGroupEnum.PUBIC_ALBUM;
import static com.knowledge.graph.uitils.libs.music163.LibsConstant.REQUEST_ALBUM_PRE;

@Data
public class Album {

    String id;

    /**
     * 专辑名称
     */
    String name;

    String time;

    public Album(String id, String name, String time) {
        this.id = id;
        this.name = name;
        this.time = time;
    }

    DataCard dataCard;

    public DataCard createCard() {
        if (dataCard == null) {
            DataCard newData = new DataCard(CardGroupEnum.COLLECT_ALBUM, name);
            newData.setTime(time);
            dataCard = CrawlerUtils.mergeDataCard(newData);
        }
        return dataCard;
    }

    public List<DataClue> createClue(DataCard artist) {
        DataClue store = new DataClue(LIB_STORE_MUSIC_163, MUSIC_163.card().getId(), createCard());
        store.setKey(id);
        store.setLink(REQUEST_ALBUM_PRE + id);
        DataClue pubicAlbum = new DataClue(PUBIC_ALBUM, artist.getId(), createCard());
        return List.of(store, pubicAlbum);
    }

    public DataGraph graphItem(DataCard artist) {
        return new DataGraph(List.of(MUSIC_163.card(), artist, createCard()), createClue(artist));
    }

}
