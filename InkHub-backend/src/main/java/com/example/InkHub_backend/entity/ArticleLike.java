package com.example.InkHub_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 点赞实体，对应数据库表 article_like
 *
 * <p>表上有唯一索引 UNIQUE(article_id, user_id)：
 * 同一用户对同一文章只能有一条点赞记录，
 * 重复点击靠唯一索引兜底，天然幂等（面试亮点）。
 *
 * @author InkHub
 */
@Data
@TableName("article_like")
public class ArticleLike {

    /** 主键，自增 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 被点赞的文章 id */
    private Long articleId;

    /** 点赞用户 id */
    private Long userId;

    /** 点赞时间 */
    private LocalDateTime createTime;
}
