package com.knowledge.graph.uitils.libs.jjwxc.entity;

import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.uitils.CrawlerUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

import static com.knowledge.graph.common.constant.CardKeyEnum.JJWXC;
import static com.knowledge.graph.common.constant.ClueGroupEnum.LIB_STORE_JJWXC;
import static com.knowledge.graph.uitils.libs.jjwxc.LibsConstant.REQUEST_AUTHOR_PRE;

@Data
public class Author {

    String id;

    /**
     * 作者名字
     */
    String name;

    public Author(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Author(String id, DataCard dataCard) {
        this.id = id;
        this.name = dataCard.getKey();
        this.dataCard = dataCard;
    }

    DataCard dataCard;

    public DataCard createCard() {
        if (dataCard == null) {
            dataCard = CrawlerUtils.mergeDataCard(new DataCard(CardGroupEnum.THING_PERSON, name));
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

    public static List<Author> graphFlat(List<DataClue> storeList) {
        return storeList.stream().map(store -> {
            DataCard card = CrawlerUtils.getDataCardId(store.getTarget());
            return new Author(store.getKey(), card);
        }).toList();
    }

    public static List<Author> graphFlat(Set<String> storeIds) {
        return graphFlat(CrawlerUtils.CLUE_MAP_ID.values().stream()
                .filter(d -> StringUtils.isNotBlank(d.getKey()))
                .filter(d -> storeIds.contains(d.getKey())).toList());
    }

}
