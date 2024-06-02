package com.knowledge.graph.common.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class DataResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private int errCode;
    private String errMsg;
    private transient T data;

    public static <T2> DataResponse<T2> ok(T2 data) {
        return new DataResponse<>(0, null, data);
    }

    public static DataResponse<Void> ok() {
        return new DataResponse<>(0, null, null);
    }

    public static <T2> DataResponse<T2> ok(T2 data, String errMsg) {
        return new DataResponse<>(0, errMsg, data);
    }

    public DataResponse(final int errCode, final String errMsg, final T data) {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.data = data;
    }

    public DataResponse() {
    }

}
