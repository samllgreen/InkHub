package com.example.InkHub_backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.InkHub_backend.common.R;
import com.example.InkHub_backend.entity.Tag;
import com.example.InkHub_backend.mapper.TagMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@io.swagger.v3.oas.annotations.tags.Tag(name = "标签接口", description = "标签列表（公开）")
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagMapper tagMapper;

    @Operation(summary = "标签列表", description = "公开接口；返回全部标签，按 id 倒序")
    @GetMapping
    public R<List<Tag>> list() {
        return R.ok(tagMapper.selectList(new LambdaQueryWrapper<Tag>().orderByDesc(Tag::getId)));
    }
}