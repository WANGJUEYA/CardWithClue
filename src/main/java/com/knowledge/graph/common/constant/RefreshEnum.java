package com.knowledge.graph.common.constant;

public enum RefreshEnum {
    // 刷新子表类型
    ALL("全量刷新"),
    UPDATE("仅刷新主数据有更新的数据"),
    NONE("不刷新子表"),
    ;

    RefreshEnum(String desc) {
        this.desc = desc;
    }

    private final String desc;

    public final String desc() {
        return desc;
    }
}
