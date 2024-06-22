package com.knowledge.graph.uitils.libs.music163.entity;

import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.common.entity.DataGraphItem;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import lombok.Data;

import java.util.List;
import java.util.Set;

import static com.knowledge.graph.common.constant.ClueGroupEnum.LIB_MUSIC_163_FAV;

@Data
public class Artist {

    public static final String LINK_PREFIX = "https://music.163.com/#/artist?id=";

    String id;

    String name;

    Set<String> alias;

    DataCard artist;

    public DataCard artist() {
        if (artist == null) {
            artist = new DataCard();
            artist.setGroup(CardGroupEnum.THING_PERSON);
            artist.setKey(name);
            artist.setAlias(alias);
        }
        return artist;
    }

    public DataClue artistStore(DataCard source) {
        DataClue store = new DataClue();
        store.setSource(source.getId());
        store.setTargetCard(artist());

        store.setGroup(LIB_MUSIC_163_FAV);
        store.setKey(id);
        store.setLink(LINK_PREFIX + id);
        return store;
    }

    public DataGraphItem graphItem(DataCard source) {
        DataGraphItem res = new DataGraphItem();
        res.setSource(source);
        res.setCards(List.of(artist()));
        res.setClues(List.of(artistStore(source)));
        return res;
    }

}
