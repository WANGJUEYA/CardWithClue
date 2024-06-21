package com.knowledge.graph.common.entity;

import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class DataGraph implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    List<DataCard> nodes;

    List<DataClue> links;

}
