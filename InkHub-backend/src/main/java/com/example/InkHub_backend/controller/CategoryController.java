package com.example.InkHub_backend.controller;

import com.example.InkHub_backend.common.R;
import com.example.InkHub_backend.service.CategoryService;
import com.example.InkHub_backend.vo.CategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "分类接口", description = "分类列表（公开）")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "分类列表", description = "公开接口；返回全部分类及文章数，按 sort 排序")
    @GetMapping
    public R<List<CategoryVO>> list() {
        return R.ok(categoryService.listWithCount());
    }
}