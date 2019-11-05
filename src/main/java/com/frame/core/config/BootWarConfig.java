package com.frame.core.config;

import com.frame.FrameApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * 这里是打包成war包的时候的启动类,仅支持tomcat8以上版本
 */
public class BootWarConfig extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 注意这里要指向原先用main方法执行的Application启动类
        return builder.sources(FrameApplication.class);
    }
}
