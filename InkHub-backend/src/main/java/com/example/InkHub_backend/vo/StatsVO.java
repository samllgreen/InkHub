package com.example.InkHub_backend.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

// 统计总览：卡片数字 + 两个图表数据
@Data
@Schema(description = "数据统计总览")
public class StatsVO {
    @Schema(description = "文章总数")
    private Long articleCount;          // 文章总数
    @Schema(description = "用户总数")
    private Long userCount;             // 用户总数
    @Schema(description = "评论总数")
    private Long commentCount;          // 评论总数
    @Schema(description = "总浏览量")
    private Long totalViews;            // 总浏览量
    @Schema(description = "热门文章 Top10（title + view_count）")
    private List<Map<String, Object>> hotArticles;        // 热门文章 Top10
    @Schema(description = "分类分布（name + value）")
    private List<Map<String, Object>> categoryDistribution; // 分类分布
}