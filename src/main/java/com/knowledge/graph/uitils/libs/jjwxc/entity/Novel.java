package com.knowledge.graph.uitils.libs.jjwxc.entity;

import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.uitils.CrawlerUtils;
import lombok.Data;

import java.util.List;

import static com.knowledge.graph.common.constant.CardKeyEnum.JJWXC;
import static com.knowledge.graph.common.constant.ClueGroupEnum.LIB_STORE_JJWXC;
import static com.knowledge.graph.common.constant.ClueGroupEnum.WRITER;
import static com.knowledge.graph.uitils.libs.jjwxc.LibsConstant.REQUEST_NOVEL_PRE;

@Data
public class Novel {

    String id;

    /**
     * 作者名字
     */
    String author;

    /**
     * 作品名称
     */
    String name;

    public Novel(String id, String author, String name) {
        this.id = id;
        this.author = author;
        this.name = name;
    }

    DataCard dataCard;

    public DataCard createCard() {
        if (dataCard == null) {
            dataCard = CrawlerUtils.mergeDataCard(new DataCard(CardGroupEnum.THING_BOOK, name, author));
        }
        return dataCard;
    }

    public List<DataClue> createClue(DataCard author) {
        DataClue store = new DataClue(LIB_STORE_JJWXC, JJWXC.card().getId(), createCard());
        store.setKey(id);
        store.setLink(REQUEST_NOVEL_PRE + id);
        DataClue write = new DataClue(WRITER, author.getId(), createCard());
        return List.of(store, write);
    }

    public DataGraph graphItem(DataCard author) {
        return new DataGraph(List.of(JJWXC.card(), author, createCard()), createClue(author));
    }

}
