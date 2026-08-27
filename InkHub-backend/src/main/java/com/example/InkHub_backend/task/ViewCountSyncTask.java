package com.example.InkHub_backend.task;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.InkHub_backend.entity.Article;
import com.example.InkHub_backend.mapper.ArticleMapper;
import com.example.InkHub_backend.utils.RedisKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Map;

// 每 10 分钟把 Redis 里的浏览量增量写回 MySQL
@Component
@EnableScheduling
@RequiredArgsConstructor
public class ViewCountSyncTask {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ArticleMapper articleMapper;

    @Scheduled(fixedDelay = 600000)   // 10 分钟一次
    public void sync() {
        // SCAN 匹配所有 article:view:* 的 key（生产别用 KEYS，会阻塞 Redis）
        try (Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection()
                .scan(ScanOptions.scanOptions().match("article:view:*").count(100).build())) {
            while (cursor.hasNext()) {
                String key = new String(cursor.next());
                Long articleId = Long.valueOf(key.substring(key.lastIndexOf(':') + 1));
                Object val = redisTemplate.opsForValue().get(key);
                if (val instanceof Number n && n.longValue() > 0) {
                    // 增量写回：view_count = view_count + 当前 Redis 值
                    articleMapper.update(null, new LambdaUpdateWrapper<Article>()
                            .setSql("view_count = view_count + " + n.longValue())
                            .eq(Article::getId, articleId));
                    // 写回后清零，下次继续累计增量
                    redisTemplate.opsForValue().set(key, 0);
                }
            }
        } catch (Exception e) {
            // 落库失败不要影响主流程，下轮再试
        }
    }
}
