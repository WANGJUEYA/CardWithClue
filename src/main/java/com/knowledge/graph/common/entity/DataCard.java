package com.knowledge.graph.common.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DataCard implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    String id;

    String group;

    String text;

}
