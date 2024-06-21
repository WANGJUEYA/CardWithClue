package com.knowledge.graph.store.entity;

import com.knowledge.graph.common.constant.ClueGroupEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DataClue implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    Long id;

    Long source;

    Long target;

    ClueGroupEnum clueGroup;

    String clueKey;

    String clueLink;

    String clueDesc;

}
