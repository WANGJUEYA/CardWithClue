package com.knowledge.graph.common.constant;

public enum ClueGroupEnum {
    // 卡片类型
    LIB_STORE("管理的数据"),
    RELATIVE_PATH("本地存储相对路径"),
    COLLECT("合集"),
    COLLECT_MUSIC_ALBUM("所属音乐专辑"),

    AUTHOR("作者"),

    DIRECTOR("导演"),
    ACTOR("参演"),

    LYRICIST("作词"),
    COMPOSER("作曲"),
    SINGER("演唱"),
    ;

    ClueGroupEnum(String desc) {
        this.desc = desc;
    }

    private final String desc;

    public final String desc() {
        return desc;
    }

}
