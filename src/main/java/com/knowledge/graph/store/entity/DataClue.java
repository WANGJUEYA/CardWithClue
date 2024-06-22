package com.knowledge.graph.store.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.knowledge.graph.common.constant.ClueGroupEnum;
import com.knowledge.graph.common.entity.IEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Schema(description = "线索")
@Data
public class DataClue implements Serializable, IEntity<ClueGroupEnum> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "线索id")
    Long id;

    @Schema(description = "线索源")
    Long source;

    public Long getSource() {
        if (source != null) {
            return source;
        } else if (sourceCard != null) {
            return sourceCard.getId();
        }
        return null;
    }

    @Schema(description = "源卡片实体")
    @TableField(exist = false)
    DataCard sourceCard;

    @Schema(description = "线索指向")
    Long target;

    public Long getTarget() {
        if (target != null) {
            return target;
        } else if (targetCard != null) {
            return targetCard.getId();
        }
        return null;
    }

    @Schema(description = "目标卡片实体")
    @TableField(exist = false)
    DataCard targetCard;

    @Schema(description = "线索类型")
    ClueGroupEnum clueGroup;

    @Override
    public ClueGroupEnum getGroup() {
        return clueGroup;
    }

    @Override
    public void setGroup(ClueGroupEnum group) {
        this.clueGroup = group;
    }

    @Schema(description = "线索名称")
    @TableField("CLUE_KEY")
    String key;

    @Schema(description = "别名")
    @TableField("CLUE_ALIAS")
    String alias;

    @Schema(description = "线索网址")
    @TableField("CLUE_LINK")
    String link;

    @Schema(description = "线索描述")
    @TableField("CLUE_DESC")
    String desc;

    @Schema(description = "线索附加信息")
    @TableField("CLUE_JSON")
    String json;

    @Schema(description = "数据是否被更新")
    @TableField(exist = false)
    Boolean updated;

}
