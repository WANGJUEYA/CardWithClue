package com.knowledge.graph.common.constant;

import com.knowledge.graph.store.entity.DataCard;

public enum CardKeyEnum {
    // 初始化的卡片信息
    MUSIC_163(1001L, CardGroupEnum.LIB, "网易云音乐", "https://music.163.com", ""),
    JJWXC(1002L, CardGroupEnum.LIB, "晋江文学城", "https://www.jjwxc.net", ""),
    ;

    CardKeyEnum(Long id, CardGroupEnum group, String key, String link, String desc) {
        this.id = id;
        this.group = group;
        this.key = key;
        this.link = link;
        this.desc = desc;

        card = new DataCard();
        card.setId(id);
        card.setGroup(group);
        card.setKey(key);
        card.setLink(link);
        card.setDesc(desc);
    }

    private final Long id;
    private final CardGroupEnum group;
    private final String key;
    private final String link;
    private final String desc;
    private final DataCard card;

    public Long id() {
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
