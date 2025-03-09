package org.soho.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wesoho
 * @version 1.0
 * @description:
 * @date 2024/10/10 23:52
 */
@MapperScan("org.soho.portal.mapper")
@ComponentScan(basePackages = {"org.soho.portal","org.soho.common"})
@SpringBootApplication
public class PortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortalApplication.class, args);
    }

}