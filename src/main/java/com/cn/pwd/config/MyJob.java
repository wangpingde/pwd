package com.cn.pwd.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class MyJob  implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MyJob.class);

    @Override

    public void run() {

        logger.info("DynamicTask.MyRunnable.run() {}", new Date());

    }

}




