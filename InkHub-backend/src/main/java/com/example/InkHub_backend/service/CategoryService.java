package com.example.InkHub_backend.service;

import com.example.InkHub_backend.vo.CategoryVO;

import java.util.List;

public interface CategoryService {

    List<CategoryVO> listWithCount();   // 分类列表 + 每个分类的文章数
}