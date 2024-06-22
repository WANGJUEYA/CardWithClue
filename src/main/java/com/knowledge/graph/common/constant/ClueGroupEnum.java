package com.knowledge.graph.common.constant;

public enum ClueGroupEnum {
    // 线索类型
    LIB_STORE("管理的数据"),
    LIB_STORE_MUSIC_163("网易云音乐-存储数据"),
    LIB_STORE_JJWXC("晋江文学城-存储数据"),
    RELATIVE_PATH("本地存储相对路径"),

    WRITER("写"),

    DIRECTOR("导演"),
    ACTOR("参演"),

    LYRICIST("作词"),
    COMPOSER("作曲"),
    SINGER("演唱"),
    PUBIC_ALBUM("发布专辑"),
    ;

    ClueGroupEnum(String desc) {
        this.desc = desc;
    }

    private final String desc;

    public final String desc() {
        return desc;
    }

}
