package com.example.InkHub_backend.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

// 评论项：评论 + 评论人昵称/头像
@Data
@Schema(description = "评论项")
public class CommentVO {
    @Schema(description = "评论 id")
    private Long id;
    @Schema(description = "所属文章 id")
    private Long articleId;
    @Schema(description = "评论人 id")
    private Long userId;
    @Schema(description = "评论人昵称")
    private String nickname;
    @Schema(description = "评论人头像")
    private String avatar;
    @Schema(description = "父评论 id（二级回复有值，一级评论为空）")
    private Long parentId;
    @Schema(description = "评论内容")
    private String content;
    @Schema(description = "评论时间")
    private LocalDateTime createTime;
}
