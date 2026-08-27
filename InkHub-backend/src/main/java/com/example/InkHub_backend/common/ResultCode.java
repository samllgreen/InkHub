package com.example.InkHub_backend.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

// 状态码枚举：统一管理，避免魔法数字
@Getter
@AllArgsConstructor
public enum ResultCode {
    SUCCESS(200, "成功"),
    UNAUTHORIZED(401, "未登录或登录已过期"),
    FORBIDDEN(403, "没有权限"),
    ERROR(500, "系统繁忙，请稍后再试");

    private final int code;
    private final String msg;
}