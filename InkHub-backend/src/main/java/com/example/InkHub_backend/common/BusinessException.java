package com.example.InkHub_backend.common;

// 业务异常：Service 层判断业务不满足时抛它，全局处理器转成 R 返回
public class BusinessException extends RuntimeException {
    private int code;

    public BusinessException(String msg) {
        super(msg);
        this.code = 500;
    }

    public BusinessException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}