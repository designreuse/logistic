package com.frame;

import com.frame.web.base.baseRepository.BaseRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.frame.web.business.dao",repositoryFactoryBeanClass = BaseRepositoryFactoryBean.class)
public class FrameApplication {

    public static void main(String[] args) {
        SpringApplication.run(FrameApplication.class,args);
    }
}
