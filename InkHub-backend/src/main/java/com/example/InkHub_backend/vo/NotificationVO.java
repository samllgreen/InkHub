package com.example.InkHub_backend.vo;

import lombok.Data;

import java.time.LocalDateTime;

// 通知项：通知 + 触发者昵称/头像 + 文章标题
@Data
public class NotificationVO {
    private Long id;
    private Integer type;
    private String content;
    private Long articleId;
    private Long commentId;
    private Integer isRead;
    private LocalDateTime createTime;
    private String fromNickname;    // 触发者昵称
    private String fromAvatar;      // 触发者头像
    private String articleTitle;    // 相关文章标题（点击跳转用）
}
