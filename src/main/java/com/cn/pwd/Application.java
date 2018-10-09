package com.cn.pwd;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.Environment;

import java.util.HashMap;

@SpringBootApplication
@ComponentScan({"com.cn.pwd"})
@ImportResource(locations = { "classpath:druid-bean.xml" })
public class Application {

    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(Application.class);
        app.setDefaultProperties(new HashMap<>());//设置默认启动配置把key-value放在map中
        String[] ne = {"--root.path=D:/project/dist"};//类似于此的键值对配置 --不能缺失
        ApplicationContext context  = app.run(ne);//添加到应用中
        Environment environment= context.getEnvironment();//获取环境的配置


    }

}
