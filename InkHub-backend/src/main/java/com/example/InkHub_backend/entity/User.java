package com.example.InkHub_backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体，对应数据库表 user
 *
 * <p>@TableName 把实体映射到表；@TableId 标记主键（AUTO = 数据库自增）。
 * @Data 由 Lombok 自动生成所有 getter/setter（getId、setUsername...），
 * 所以代码里能直接 user.getId()。
 *
 * @author InkHub
 */
@Data
@TableName("user")
public class User {

    /** 主键，自增 */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户名（登录用，唯一） */
    private String username;

    /** 密码（存 BCrypt 加密后的密文，不是明文） */
    private String password;

    /** 昵称（展示用） */
    private String nickname;

    /** 头像 URL */
    private String avatar;

    /** 邮箱 */
    private String email;

    /** 角色：1 普通用户（作者） 2 管理员 */
    private Integer role;

    /** 状态：0 正常 1 禁用 */
    private Integer status;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;
}
