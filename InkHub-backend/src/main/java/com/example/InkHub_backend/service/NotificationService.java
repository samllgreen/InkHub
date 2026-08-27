package com.example.InkHub_backend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.InkHub_backend.vo.NotificationVO;

public interface NotificationService {

    /** 我的通知（分页） */
    Page<NotificationVO> page(Long userId, int pageNum, int pageSize);

    /** 未读数 */
    long unreadCount(Long userId);

    /** 单条已读 */
    void read(Long userId, Long id);

    /** 全部已读 */
    void readAll(Long userId);
}