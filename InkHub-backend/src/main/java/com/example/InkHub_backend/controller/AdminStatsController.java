package com.example.InkHub_backend.controller;

import com.example.InkHub_backend.common.R;
import com.example.InkHub_backend.mapper.StatsMapper;
import com.example.InkHub_backend.vo.StatsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "数据统计接口", description = "管理端数据看板（需管理员角色）")
@RestController
@RequestMapping("/api/admin/stats")
@RequiredArgsConstructor
public class AdminStatsController {

    private final StatsMapper statsMapper;

    @Operation(summary = "数据总览", description = "管理员接口；返回文章/用户/评论总数、总浏览量、热门文章 Top10、分类分布")
    @GetMapping("/overview")
    public R<StatsVO> overview() {
        StatsVO vo = new StatsVO();
        vo.setArticleCount(statsMapper.countArticles());
        vo.setUserCount(statsMapper.countUsers());
        vo.setCommentCount(statsMapper.countComments());
        vo.setTotalViews(statsMapper.sumViews());
        vo.setHotArticles(statsMapper.hotArticles());
        vo.setCategoryDistribution(statsMapper.categoryDistribution());
        return R.ok(vo);
    }
}