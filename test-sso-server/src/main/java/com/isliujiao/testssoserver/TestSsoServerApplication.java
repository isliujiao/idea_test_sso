package com.isliujiao.testssoserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * 1、中央认证服务器
 * 2、其他系统，想要登录去中央服务器进行认证，登录成功跳转回来
 * 3、只要有一个登录，其他系统都不用登录
 * 4、全系统统一个sso-sessionid;所有系统可能域名都不相同|
 */
@SpringBootApplication
public class TestSsoServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestSsoServerApplication.class, args);
    }

}
