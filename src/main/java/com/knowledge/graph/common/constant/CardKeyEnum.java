package com.knowledge.graph.common.constant;

import com.knowledge.graph.store.entity.DataCard;

public enum CardKeyEnum {
    // 初始化的卡片信息
    MUSIC_163("1001", CardGroupEnum.LIB, "网易云音乐", "https://music.163.com", ""),
    JJWXC("1002", CardGroupEnum.LIB, "晋江文学城", "https://www.jjwxc.net", ""),
    INDEX("10000", CardGroupEnum.UNKNOWN, "起始游标", "", ""),
    ;

    CardKeyEnum(String id, CardGroupEnum group, String key, String link, String desc) {
        this.id = id;
        this.group = group;
        this.key = key;
        this.link = link;
        this.desc = desc;

        card = new DataCard(group, key);
        card.setId(id);
        card.setLink(link);
        card.setDesc(desc);
    }

    private final String id;
    private final CardGroupEnum group;
    private final String key;
    private final String link;
    private final String desc;
    private final DataCard card;

    public String id() {
        return id;
    }

    public final CardGroupEnum group() {
        return group;
    }

    public final String key() {
        return key;
    }

    public final String link() {
        return link;
    }

    public final String desc() {
        return desc;
    }

    public DataCard card() {
        return card;
    }

}
