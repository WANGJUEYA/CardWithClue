package com.knowledge.graph.store.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.common.entity.IEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Schema(description = "卡片")
@Data
public class DataCard implements Serializable, IEntity<CardGroupEnum> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "卡片id")
    Long id;

    @Schema(description = "卡片类型")
    CardGroupEnum cardGroup;

    @Override
    public CardGroupEnum getGroup() {
        return cardGroup;
    }

    @Override
    public void setGroup(CardGroupEnum group) {
        this.cardGroup = group;
    }

    @Schema(description = "卡片名字")
    @TableField("CARD_KEY")
    String key;

    @Schema(description = "别名")
    @TableField("CARD_ALIAS")
    String alias;

    @Schema(description = "网址")
    @TableField("CARD_LINK")
    String link;

    @Schema(description = "卡片描述")
    @TableField("CARD_DESC")
    String desc;

    @Schema(description = "卡片附加信息")
    @TableField("CARD_JSON")
    String json;

    @Schema(description = "数据是否被更新")
    @TableField(exist = false)
    Boolean updated;

}
