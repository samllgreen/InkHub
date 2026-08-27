package com.example.InkHub_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

// 文章流查询参数：分类/标签/搜索/排序/分页
@Data
@Schema(description = "文章列表查询参数")
public class ArticleQueryDTO {
    @Schema(description = "页码，从 1 开始", example = "1")
    private Integer pageNum = 1;
    @Schema(description = "每页条数", example = "10")
    private Integer pageSize = 10;
    @Schema(description = "分类 id（按分类过滤，选填）")
    private Long categoryId;        // 按分类过滤
    @Schema(description = "标签 id（按标签过滤，选填）")
    private Long tagId;             // 按标签过滤
    @Schema(description = "搜索关键词（匹配标题/摘要，选填）")
    private String keyword;         // 搜索关键词（标题/摘要）
    @Schema(description = "排序：latest 最新 / hot 最热", example = "latest")
    private String sort = "latest"; // latest 最新 / hot 最热
}