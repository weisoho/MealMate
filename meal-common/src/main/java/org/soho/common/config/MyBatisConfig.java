package org.soho.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author wesoho
 * @version 1.0
 * @description: TODO
 * @date 2025/4/30 1:01
 */
@Configuration
public class MyBatisConfig {
    @Bean
    public Scheduler jdbcScheduler() {
        return Schedulers.newBoundedElastic(10, 100, "jdbc-pool");
    }
}

