package com.knowledge.graph.uitils.libs.jjwxc.entity;

import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.common.constant.CardKeyEnum;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.uitils.CrawlerUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

import static com.knowledge.graph.common.constant.ClueGroupEnum.LIB_STORE_JJWXC;

@Data
public class Author {

    public static final String LINK_PREFIX = "https://www.jjwxc.net/oneauthor.php?authorid=";
    public static final DataCard LIB = CardKeyEnum.JJWXC.card();

    String id;

    /**
     * 作者名字
     */
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
            author = CrawlerUtils.mergeDataCard(new DataCard(CardGroupEnum.THING_PERSON, name));
        }
        return author;
    }

    public DataClue authorStore() {
        DataClue store = new DataClue(LIB_STORE_JJWXC, LIB.getId(), author());
        store.setKey(id);
        store.setLink(LINK_PREFIX + id);
        return store;
    }

    public DataGraph graphItem() {
        return new DataGraph(List.of(CardKeyEnum.JJWXC.card(), author()), List.of(authorStore()));
    }

    public static List<Author> graphFlat(List<DataClue> authorStoreList) {
        return authorStoreList.stream().map(authorStore -> {
            DataCard authorCard = CrawlerUtils.getDataCardId(authorStore.getTarget());
            return new Author(authorStore.getKey(), authorCard.getKey(), authorCard);
        }).toList();
    }

    public static List<Author> graphFlat(Set<String> authorIds) {
        return graphFlat(CrawlerUtils.CLUE_MAP_ID.values().stream()
                .filter(d -> StringUtils.isNotBlank(d.getKey()))
                .filter(d -> authorIds.contains(d.getKey())).toList());
    }

}
