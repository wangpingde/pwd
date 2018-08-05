package com.cn.pwd.service;

import org.quartz.SchedulerException;

public interface TaskJobService {

    void startTask(String name) throws SchedulerException;

    void pauseTask();

    void resumeTask();

    void closeTask(String name);

}
