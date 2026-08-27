package com.example.InkHub_backend.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

// 文章详情：继承列表项，加上 contentMd 全文 + tagIds（编辑回填用）
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "文章详情（继承列表项字段）")
public class ArticleDetailVO extends ArticleVO {
    @Schema(description = "Markdown 全文")
    private String contentMd;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "标签 id 列表（编辑回填用）")
    private List<Long> tagIds;   // 编辑回填用：这篇文章的所有标签 id
}