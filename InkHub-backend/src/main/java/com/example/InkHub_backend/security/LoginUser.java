package com.example.InkHub_backend.security;

import lombok.AllArgsConstructor;
import lombok.Data;

// 当前登录用户信息：存在 SecurityContext 里，接口里随时取
@Data
@AllArgsConstructor
public class LoginUser {
    private Long userId;
    private Integer role;
}
