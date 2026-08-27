package com.example.InkHub_backend.utils;

// Redis key 统一管理，避免散落魔法字符串
public class RedisKeys {

    // 文章浏览量计数：INCR 后定时落库，key: article:view:{articleId}
    public static String articleView(Long articleId) {
        return "article:view:" + articleId;
    }
}
