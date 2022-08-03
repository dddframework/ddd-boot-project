package com.github.ddd.security.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Redis 存放登录信息
 * @author ranger
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 登录客户端
     */
    private String clientId;
}
