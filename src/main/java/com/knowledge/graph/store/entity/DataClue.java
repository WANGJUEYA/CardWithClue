package com.knowledge.graph.store.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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

    public DataClue() {

    }

    public DataClue(ClueGroupEnum clueGroup, String source, String target) {
        this.clueGroup = clueGroup;
        this.source = source;
        this.target = target;
    }

    public DataClue(ClueGroupEnum clueGroup, String source, DataCard targetCard) {
        this.clueGroup = clueGroup;
        this.source = source;
        this.targetCard = targetCard;
    }

    @Schema(description = "线索id")
    @TableId(type = IdType.AUTO)
    String id;

    @Schema(description = "线索类型")
    ClueGroupEnum clueGroup;

    @Schema(description = "线索源")
    String source;

    public String getSource() {
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
    String target;

    public String getTarget() {
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

    @Override
    public ClueGroupEnum getGroup() {
        return clueGroup;
    }

    @Override
    public void setGroup(ClueGroupEnum group) {
        this.clueGroup = group;
    }

    @Schema(description = "线索名称; 非必填，其他仓库的id")
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
