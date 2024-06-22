package com.knowledge.graph.uitils.libs;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.knowledge.graph.common.entity.DataGraph;
import com.knowledge.graph.common.entity.DataGraphItem;
import com.knowledge.graph.common.entity.IEntity;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractCrawler {

    @Getter
    @Setter
    private DataGraph existGraph;

    public abstract DataCard getSource();

    public abstract void setSource(DataCard source);

    public boolean clearCard() {
        return false;
    }

    public boolean clearClue() {
        return false;
    }

    public abstract Wrapper<DataCard> wrapperCard();

    public abstract Wrapper<DataClue> wrapperClue();

    public abstract List<DataGraphItem> crawler();

    public void execute() {
        DataGraph graph = getExistGraph();
        if (graph.getNodes() == null) {
            graph.setNodes(new ArrayList<>());
        }
        if (graph.getLinks() == null) {
            graph.setLinks(new ArrayList<>());
        }
        Optional.ofNullable(crawler()).orElse(new ArrayList<>()).forEach(item -> {
            IEntity.mergeToList(graph.getNodes(), item.getCards());
            IEntity.mergeToList(graph.getLinks(), item.getClues());
        });
    }

}
