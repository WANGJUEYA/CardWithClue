package com.knowledge.graph.uitils.libs.jjwxc.entity;

import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.knowledge.graph.common.constant.ClueGroupEnum.LIB_STORE_JJWXC;

@Data
public class Author {

    public static final String LINK_PREFIX = "https://www.jjwxc.net/oneauthor.php?authorid=";

    String id;

    String name;

    public Author(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Author(String id, String name, DataCard author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }

    DataCard author;

    public DataCard author() {
        if (author == null) {
            author = new DataCard(CardGroupEnum.THING_PERSON, name);
        }
        return author;
    }

    public DataClue authorStore(DataCard lib) {
        DataClue store = new DataClue(LIB_STORE_JJWXC, lib.getId(), author());
        store.setKey(id);
        store.setLink(LINK_PREFIX + id);
        return store;
    }

    public DataGraph graphItem(DataCard lib) {
        return new DataGraph(List.of(lib, author()), List.of(authorStore(lib)));
    }

    public static List<Author> graphFlat(DataGraph graph) {
        Map<String, DataCard> cardMap = graph.getCards().stream().filter(c -> CardGroupEnum.THING_PERSON.equals(c.getCardGroup()))
                .collect(Collectors.toMap(DataCard::getId, Function.identity()));
        return graph.getClues().stream().filter(c -> LIB_STORE_JJWXC.equals(c.getClueGroup())).map(item -> {
            DataCard author = cardMap.get(item.getTarget());
            if (author == null) {
                return null;
            } else {
                return new Author(item.getKey(), author.getKey(), author);
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
