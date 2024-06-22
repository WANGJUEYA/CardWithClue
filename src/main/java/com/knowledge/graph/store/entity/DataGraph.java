package com.knowledge.graph.store.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Schema(description = "所有卡片线索集合")
@Data
public class DataGraph implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private DataGraph() {
    }

    public DataGraph(List<DataCard> cards, List<DataClue> clues) {
        this.cards = cards;
        this.clues = clues;
    }

    @Schema(description = "卡片集合")
    List<DataCard> cards;

    @Schema(description = "线索集合")
    List<DataClue> clues;

}
