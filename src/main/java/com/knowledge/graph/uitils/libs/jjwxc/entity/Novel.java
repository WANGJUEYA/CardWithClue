package com.knowledge.graph.uitils.libs.jjwxc.entity;

import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.common.constant.CardKeyEnum;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import com.knowledge.graph.uitils.CrawlerUtils;
import lombok.Data;

import java.util.List;

import static com.knowledge.graph.common.constant.ClueGroupEnum.LIB_STORE_JJWXC;
import static com.knowledge.graph.common.constant.ClueGroupEnum.WRITER;

@Data
public class Novel {

    public static final String LINK_PREFIX = "https://www.jjwxc.net/onebook.php?novelid=";
    public static final DataCard LIB = CardKeyEnum.JJWXC.card();

    String id;

    /**
     * 作品名称
     */
    String name;

    public Novel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    DataCard novel;

    public DataCard novel() {
        if (novel == null) {
            novel = CrawlerUtils.mergeDataCard(new DataCard(CardGroupEnum.THING_BOOK, name));
        }
        return novel;
    }

    public List<DataClue> novelStore(DataCard author) {
        DataClue store = new DataClue(LIB_STORE_JJWXC, LIB.getId(), novel());
        store.setKey(id);
        store.setLink(LINK_PREFIX + id);
        DataClue write = new DataClue(WRITER, author.getId(), novel());
        return List.of(store, write);
    }

    public DataGraph graphItem(DataCard author) {
        return new DataGraph(List.of(LIB, author, novel()), novelStore(author));
    }

}
