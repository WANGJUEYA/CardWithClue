package com.knowledge.graph.common.entity;

import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
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

    public DataGraph() {
    }

    public DataGraph(List<DataCard> nodes, List<DataClue> links) {
        this.nodes = nodes;
        this.links = links;
    }

    @Schema(description = "卡片集合")
    List<DataCard> nodes;

    @Schema(description = "线索集合")
    List<DataClue> links;

}
