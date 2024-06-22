package com.knowledge.graph.uitils.libs;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.knowledge.graph.common.entity.IEntity;
import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractCrawler {

    @Getter
    @Setter
    private DataGraph existGraph;

    public abstract Wrapper<DataCard> wrapperCard();

    public abstract Wrapper<DataClue> wrapperClue();

    public abstract List<DataGraph> crawler();

    public void execute() {
        DataGraph graph = getExistGraph();
        if (graph.getCards() == null) {
            graph.setCards(new ArrayList<>());
        }
        if (graph.getClues() == null) {
            graph.setClues(new ArrayList<>());
        }
        Optional.ofNullable(crawler()).orElse(new ArrayList<>()).forEach(item -> {
            IEntity.mergeToList(graph.getCards(), item.getCards());
            IEntity.mergeToList(graph.getClues(), item.getClues());
        });
    }

}
