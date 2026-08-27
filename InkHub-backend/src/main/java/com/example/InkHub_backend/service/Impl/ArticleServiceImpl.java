package com.example.InkHub_backend.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.InkHub_backend.common.BusinessException;
import com.example.InkHub_backend.dto.ArticleDTO;
import com.example.InkHub_backend.dto.ArticleQueryDTO;
import com.example.InkHub_backend.entity.Article;
import com.example.InkHub_backend.entity.ArticleTag;
import com.example.InkHub_backend.mapper.ArticleMapper;
import com.example.InkHub_backend.mapper.ArticleTagMapper;
import com.example.InkHub_backend.service.ArticleService;
import com.example.InkHub_backend.utils.RedisKeys;
import com.example.InkHub_backend.vo.ArticleDetailVO;
import com.example.InkHub_backend.vo.ArticleVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleMapper articleMapper;
    private final ArticleTagMapper articleTagMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Page<ArticleVO> page(ArticleQueryDTO q) {
        int offset = (q.getPageNum() - 1) * q.getPageSize();
        List<ArticleVO> records;
        long total;
        if (q.getTagId() != null) {
            // 按标签：单独 SQL（中间表联查）
            records = articleMapper.selectPageByTag(q.getTagId(), offset, q.getPageSize());
            total = articleMapper.countByTag(q.getTagId());
        } else {
            records = articleMapper.selectArticlePage(q, offset, q.getPageSize());
            total = articleMapper.countArticlePage(q);
        }
        // 补标签名（每篇文章查一次，文章数少，够用；要优化可以用循环外批量查）
        records.forEach(this::fillTags);
        Page<ArticleVO> page = new Page<>(q.getPageNum(), q.getPageSize(), total);
        page.setRecords(records);
        return page;
    }

    @Override
    public ArticleDetailVO detail(Long id) {
        ArticleDetailVO vo = articleMapper.selectDetail(id);
        if (vo == null) {
            throw new BusinessException("文章不存在");
        }
        if (vo.getStatus() != 1) {
            throw new BusinessException("文章不存在");
        }
        // 浏览量：Redis INCR（不直接写库），返回值 = 本次 + 未落库增量
        Long redisInc = redisTemplate.opsForValue().increment(RedisKeys.articleView(vo.getId()));
        // 展示值 = 已落库(MySQL) + 未落库(Redis)，viewCount 是 Integer 可能为 null，先判空
        vo.setViewCount((vo.getViewCount() == null ? 0 : vo.getViewCount()) + redisInc.intValue());
        fillTags(vo);
        vo.setTagIds(articleTagMapper.selectTagIdsByArticle(vo.getId()));
        return vo;
    }

    @Override
    @Transactional   // 文章 + 标签中间表要一起成功/失败
    public Long create(Long userId, ArticleDTO dto) {
        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setSummary(dto.getSummary());
        article.setContentMd(dto.getContentMd());
        article.setCover(dto.getCover());
        article.setCategoryId(dto.getCategoryId());
        article.setAuthorId(userId);
        int status = dto.getStatus() != null ? dto.getStatus() : 0;
        article.setStatus(status);
        article.setTop(0);
        article.setViewCount(0);
        article.setLikeCount(0);
        article.setFavoriteCount(0);
        article.setCommentCount(0);
        article.setPublishTime(status == 1 ? LocalDateTime.now() : null);   // 发布才填时间
        article.setCreateTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.insert(article);
        saveTags(article.getId(), dto.getTagIds());
        return article.getId();
    }

    @Override
    @Transactional
    public void update(Long userId, Long articleId, ArticleDTO dto) {
        Article article = articleMapper.selectById(articleId);
        if (article == null || !article.getAuthorId().equals(userId)) {
            throw new BusinessException(403, "只能编辑自己的文章");
        }
        article.setTitle(dto.getTitle());
        article.setSummary(dto.getSummary());
        article.setContentMd(dto.getContentMd());
        article.setCover(dto.getCover());
        article.setCategoryId(dto.getCategoryId());
        int status = dto.getStatus() != null ? dto.getStatus() : article.getStatus();
        // 状态机：草稿 → 发布（第一次发布填 publish_time）；已发布不改状态；下架不能自己恢复（走管理员）
        if (status == 1 && article.getStatus() != 1) {
            article.setStatus(1);
            article.setPublishTime(LocalDateTime.now());
        }
        article.setUpdateTime(LocalDateTime.now());
        articleMapper.updateById(article);
        // 标签全量替换：先删后插
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleId));
        saveTags(articleId, dto.getTagIds());
    }

    @Override
    public void delete(Long userId, Long articleId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null || !article.getAuthorId().equals(userId)) {
            throw new BusinessException(403, "只能删除自己的文章");
        }
        articleMapper.deleteById(articleId);
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleId));
    }

    @Override
    public Page<ArticleVO> myArticles(Long userId, int pageNum, int pageSize) {
        // 我的文章：全部状态（含草稿），不联表也能看，但为了复用 VO 还是走联表
        Page<Article> p = articleMapper.selectPage(new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<Article>()
                        .eq(Article::getAuthorId, userId)
                        .orderByDesc(Article::getUpdateTime));
        List<ArticleVO> records = new ArrayList<>();
        p.getRecords().forEach(a -> {
            ArticleVO vo = new ArticleVO();
            vo.setId(a.getId());
            vo.setTitle(a.getTitle());
            vo.setSummary(a.getSummary());
            vo.setCover(a.getCover());
            vo.setCategoryId(a.getCategoryId());
            vo.setAuthorId(a.getAuthorId());
            vo.setStatus(a.getStatus());       // VO 里没这个字段？见下方补充说明
            vo.setTop(a.getTop());
            vo.setViewCount(a.getViewCount());
            vo.setLikeCount(a.getLikeCount());
            vo.setFavoriteCount(a.getFavoriteCount());
            vo.setCommentCount(a.getCommentCount());
            vo.setPublishTime(a.getPublishTime());
            fillTags(vo);
            records.add(vo);
        });
        Page<ArticleVO> page = new Page<>(pageNum, pageSize, p.getTotal());
        page.setRecords(records);
        return page;
    }

    // 私有方法：保存文章标签（去重）
    private void saveTags(Long articleId, List<Long> tagIds) {
        if (tagIds == null) return;
        tagIds.stream().distinct().forEach(tagId -> {
            ArticleTag at = new ArticleTag();
            at.setArticleId(articleId);
            at.setTagId(tagId);
            articleTagMapper.insert(at);
        });
    }

    // 私有方法：填充文章的标签名列表
    private void fillTags(ArticleVO vo) {
        List<String> tags = articleTagMapper.selectTagNamesByArticle(vo.getId());
        vo.setTags(tags);
    }
    @Override
    public List<ArticleVO> related(Long id) {
        ArticleDetailVO detail = articleMapper.selectDetail(id);
        if (detail == null || detail.getStatus() != 1) {
            throw new BusinessException("文章不存在");
        }
        List<ArticleVO> result = new ArrayList<>();
        // 1. 同分类推荐 5 条
        if (detail.getCategoryId() != null) {
            result = articleMapper.selectRelatedByCategory(detail.getCategoryId(), id, 5);
        }
        // 2. 不够 5 条，用同标签补
        if (result.size() < 5) {
            List<Long> tagIds = articleTagMapper.selectTagIdsByArticle(id);
            for (Long tagId : tagIds) {
                if (result.size() >= 5) break;
                for (ArticleVO vo : articleMapper.selectRelatedByTag(tagId, id, 5 - result.size())) {
                    boolean dup = result.stream().anyMatch(r -> r.getId().equals(vo.getId()));
                    if (!dup) result.add(vo);
                }
            }
        }
        return result;
    }
}