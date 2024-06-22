package com.knowledge.graph.common.constant;

public enum ClueGroupEnum {
    // 线索类型
    LIB_STORE("管理的数据"),
    RELATIVE_PATH("本地存储相对路径"),

    COLLECT("合集"),

    AUTHOR("作者"),

    DIRECTOR("导演"),
    ACTOR("参演"),

    LYRICIST("作词"),
    COMPOSER("作曲"),
    SINGER("演唱"),

    LIB_MUSIC_163_FAV("网易云音乐-关注歌手"),
    LIB_MUSIC_163_ALBUM("网易云音乐-歌手专辑"),
    LIB_MUSIC_163_ALBUM_MUSIC("网易云音乐-专辑音乐"),
    LIB_JJWXC_FAV("晋江文学城-关注作者"),
    LIB_JJWXC_NOVEL("网易云音乐-作者作品"),
    ;

    ClueGroupEnum(String desc) {
        this.desc = desc;
    }

    private final String desc;

    public final String desc() {
        return desc;
    }

}
