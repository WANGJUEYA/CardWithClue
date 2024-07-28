package com.knowledge.graph.store.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.knowledge.graph.common.constant.CardGroupEnum;
import com.knowledge.graph.common.entity.IEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;

@Schema(description = "卡片")
@Data
public class DataCard implements Serializable, IEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    public DataCard() {

    }

    public DataCard(CardGroupEnum dataGroup, String key) {
        this.dataGroup = dataGroup;
        this.key = key;
    }

    public DataCard(CardGroupEnum dataGroup, String key, String annotation) {
        this.dataGroup = dataGroup;
        this.key = key;
        this.annotation = annotation;
    }

    @Schema(description = "卡片id")
    @TableId(type = IdType.AUTO)
    String id;

    public String getMapKey() {
        if (getDataGroup() != null && StringUtils.isNotBlank(getKey())) {
            String mapKey = getDataGroup() + "@-@" + getKey();
            if (StringUtils.isNotBlank(getAnnotation())) {
                mapKey += "@-@" + getAnnotation();
            }
            return mapKey;
        }
        return StringUtils.EMPTY;
    }

    @Schema(description = "卡片类型")
    @TableField("CARD_GROUP")
    CardGroupEnum dataGroup;

    @Schema(description = "卡片名字")
    @TableField("CARD_KEY")
    String key;

    @Schema(description = "义项说明;可为空")
    @TableField("CARD_ANNOTATION")
    String annotation;

    @Schema(description = "时间")
    @TableField("CARD_TIME")
    String time;

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
