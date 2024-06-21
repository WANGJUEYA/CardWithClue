package com.knowledge.graph.store.entity;

import com.knowledge.graph.common.constant.CardGroupEnum;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DataCard implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    Long id;

    CardGroupEnum cardGroup;

    String cardKey;

    String cardLink;

    String cardDesc;

}
