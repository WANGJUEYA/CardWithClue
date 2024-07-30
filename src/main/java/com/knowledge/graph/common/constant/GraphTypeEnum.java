package com.knowledge.graph.common.constant;

public enum GraphTypeEnum {
    // 展示的图类型
    ALL("除了其他库存储的所有数据"),
    AUTHOR("作者+文章"),
    ARTIST("歌手+专辑+歌曲"),
    ARTIST_SONG("歌手+歌曲"),
    ;

    GraphTypeEnum(String desc) {
        this.desc = desc;
    }

    private final String desc;

    public final String desc() {
        return desc;
    }

}
