package com.knowledge.graph.uitils.libs.jjwxc.entity;

import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import lombok.Data;

import java.util.List;

import static com.knowledge.graph.common.constant.ClueGroupEnum.LIB_STORE_JJWXC;
import static com.knowledge.graph.common.constant.ClueGroupEnum.WRITER;

@Data
public class Novel {

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
            novel = new DataCard(CardGroupEnum.THING_BOOK, name);
        }
        return novel;
    }

    public List<DataClue> novelStore(DataCard lib, DataCard author) {
        DataClue store = new DataClue(LIB_STORE_JJWXC, lib.getId(), novel());
        store.setKey(id);
        DataClue write = new DataClue(WRITER, author.getId(), novel());
        return List.of(store, write);
    }

    public DataGraph graphItem(DataCard lib, DataCard author) {
        return new DataGraph(List.of(lib, author, novel()), novelStore(lib, author));
    }

}
