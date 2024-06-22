package com.knowledge.graph.common.constant;

public enum CardGroupEnum {
    // 卡片类型
    LIB("其他卡片仓库源，比如百度百科、维基百科、豆瓣、网易云音乐等"),
    TIME("时间"),
    PLACE("地点"),
    EVENT("事件"),

    THING("事物"),
    THING_PERSON("事物-人物"),
    THING_BOOK("事物-书籍"),
    THING_MUSIC("事物-音乐"),
    THING_MOVIE("事物-电影"),
    THING_DRAMA("事物-电视剧"),
    THING_GAME("事物-游戏"),

    COLLECT("卡片集合"),
    COLLECT_ALBUM("合集-音乐专辑"),

    UNKNOWN("未定义"),
    ;

    CardGroupEnum(String desc) {
        this.desc = desc;
    }

    private final String desc;

    public final String desc() {
        return desc;
    }

}
