package org.soho.portal;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wesoho
 * @version 1.0
 * @description: APS-rWJpSV5xoOc7er7xBG6WwxdoqneQKPww
 * @date 2024/10/10 23:52
 */
@MapperScan("org.soho.portal.mapper")
@SpringBootApplication
public class PortalApplication {
    public static void main(String[] args) {
        SpringApplication.run(PortalApplication.class,args);
    }
}
