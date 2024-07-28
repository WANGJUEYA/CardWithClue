package com.knowledge.graph.uitils.libs.music163.entity;

import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.uitils.CrawlerUtils;
import lombok.Data;

import java.util.List;

import static com.knowledge.graph.common.constant.CardKeyEnum.MUSIC_163;
import static com.knowledge.graph.common.constant.ClueGroupEnum.*;
import static com.knowledge.graph.uitils.libs.music163.LibsConstant.REQUEST_SONG_PRE;

@Data
public class Song {

    String id;

    /**
     * 专辑名称
     */
    String name;

    public Song(String id, String name) {
        this.id = id;
        this.name = name;
    }

    DataCard dataCard;

    public DataCard createCard() {
        if (dataCard == null) {
            dataCard = CrawlerUtils.mergeDataCard(new DataCard(CardGroupEnum.THING_MUSIC, name));
        }
        return dataCard;
    }

    public List<DataClue> createClue(DataCard artist, DataCard album) {
        DataClue store = new DataClue(LIB_STORE_MUSIC_163, MUSIC_163.card().getId(), createCard());
        store.setKey(id);
        store.setLink(REQUEST_SONG_PRE + id);
        DataClue singer = new DataClue(SINGER, artist.getId(), createCard());
        DataClue collectAlbum = new DataClue(COLLECT_ALBUM, album.getId(), createCard());
        return List.of(store, singer, collectAlbum);
    }

    public DataGraph graphItem(DataCard artist, DataCard album) {
        return new DataGraph(List.of(MUSIC_163.card(), artist, album, createCard()), createClue(artist, album));
    }

}
