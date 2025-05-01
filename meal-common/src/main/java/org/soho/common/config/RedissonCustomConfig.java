package org.soho.common.config;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author wesoho
 * @version 1.0
 * @description: Redis配置类
 * @date 2025/3/13 8:17
 */

@Configuration
@ConfigurationProperties(prefix = "redisson.config")
@Data
public class RedissonCustomConfig {
    private String address;
    private String password;
    private int database;

    @Bean
    public Config redissonConfig() {
        Config config = new Config();
        config.useSingleServer().setAddress(address).setPassword(StringUtils.isBlank(password) ? null : password).setDatabase(database);
        return config;
    }

    @Bean
    public RedissonClient redissonClient(Config redissonConfig) {
        return Redisson.create(redissonConfig);
    }
}