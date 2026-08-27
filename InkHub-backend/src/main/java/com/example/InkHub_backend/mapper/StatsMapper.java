package com.example.InkHub_backend.mapper;


import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

// 统计报表：手写聚合 SQL
public interface StatsMapper {

    @Select("SELECT COUNT(*) FROM article")
    Long countArticles();

    @Select("SELECT COUNT(*) FROM user")
    Long countUsers();

    @Select("SELECT COUNT(*) FROM comment")
    Long countComments();

    @Select("SELECT COALESCE(SUM(view_count), 0) FROM article")
    Long sumViews();

    // 热门文章 Top10（按浏览量，含作者昵称）
    @Select("""
        SELECT a.id, a.title, a.view_count, u.nickname AS author_name
        FROM article a LEFT JOIN user u ON u.id = a.author_id
        WHERE a.status = 1
        ORDER BY a.view_count DESC
        LIMIT 10
    """)
    List<Map<String, Object>> hotArticles();

    // 分类分布（文章数按分类分组，供 ECharts 饼图）
    @Select("""
        SELECT c.name AS name, COUNT(a.id) AS value
        FROM category c LEFT JOIN article a ON a.category_id = c.id AND a.status = 1
        GROUP BY c.id, c.name
    """)
    List<Map<String, Object>> categoryDistribution();
}