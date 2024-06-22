package com.knowledge.graph.common.entity;

import com.knowledge.graph.store.entity.DataCard;
import com.knowledge.graph.store.entity.DataClue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Schema(description = "给定源卡片和线索类型，发现所有相关线索和卡片")
@Data
public class DataGraphItem implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "源卡片")
    DataCard source;

    @Schema(description = "源卡片生成的相关线索; 线索中的目标卡片来自于源卡片")
    List<DataClue> clues;

    @Schema(description = "源卡片生成的新卡片")
    List<DataCard> cards;

}
