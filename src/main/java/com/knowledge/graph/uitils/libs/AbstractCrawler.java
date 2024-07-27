package com.knowledge.graph.uitils.libs;

import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import com.knowledge.graph.store.entity.DataGraph;

import java.util.*;

public abstract class AbstractCrawler {

    public abstract List<DataGraph> crawler();

    public DataGraph execute() {
        Map<String, DataCard> cards = new HashMap<>();
        List<DataClue> clues = new ArrayList<>();
        Optional.ofNullable(crawler()).orElse(new ArrayList<>()).forEach(item -> {
            Optional.ofNullable(item.getCards()).orElse(new ArrayList<>()).forEach(e -> cards.put(e.getMapKey(), e));
            clues.addAll(item.getClues());
        });
        return new DataGraph(cards.values().stream().toList(), clues);
    }

}
