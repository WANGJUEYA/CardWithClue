package com.knowledge.graph.uitils.libs.music163.entity;

import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.knowledge.graph.common.constant.ClueGroupEnum.LIB_STORE_MUSIC_163;

@Data
public class Artist {

    public static final String LINK_PREFIX = "https://music.163.com/#/artist?id=";

    String id;

    String name;

    Set<String> alias;

    DataCard artist;

    public DataCard artist() {
        if (artist == null) {
            artist = new DataCard(CardGroupEnum.THING_PERSON, name);
            artist.setAlias(alias);
        }
        return artist;
    }

    public DataClue artistStore(DataCard lib) {
        DataClue store = new DataClue(LIB_STORE_MUSIC_163, lib.getId(), artist());
        store.setKey(id);
        store.setLink(LINK_PREFIX + id);
        return store;
    }

    public DataGraph graphItem(DataCard lib) {
        return new DataGraph(List.of(lib, artist()), List.of(artistStore(lib)));
    }

    public static List<Artist> graphFlat(DataGraph graph) {
        Map<String, DataCard> cardMap = graph.getCards().stream().filter(c -> CardGroupEnum.THING_PERSON.equals(c.getCardGroup()))
                .collect(Collectors.toMap(DataCard::getId, Function.identity()));
        return graph.getClues().stream().filter(c -> LIB_STORE_MUSIC_163.equals(c.getClueGroup())).map(item -> {
            DataCard artist = cardMap.get(item.getTarget());
            if (artist == null) {
                return null;
            } else {
                Artist i = new Artist();
                i.setId(item.getKey());
                i.setName(artist.getKey());
                i.setArtist(artist);
                return i;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }

}
