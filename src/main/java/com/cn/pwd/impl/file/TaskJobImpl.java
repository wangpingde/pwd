package com.cn.pwd.impl.file;

import com.cn.pwd.config.MyJob;
import com.cn.pwd.service.TaskJobService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;


@Service
public class TaskJobImpl implements TaskJobService {

    @Autowired
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private Map<String,ScheduledFuture<?>> tasks = new HashMap<>();

    public Map<String, ScheduledFuture<?>> getTasks() {
        return tasks;
    }

    public void setTasks(Map<String, ScheduledFuture<?>> tasks) {
        this.tasks = tasks;
    }

    @Bean

    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {

        return new ThreadPoolTaskScheduler();

    }


    @Override
    public void startTask(String name) throws SchedulerException {
        threadPoolTaskScheduler.setPoolSize(5);
        ScheduledFuture<?> future = threadPoolTaskScheduler.schedule(new MyJob(), new CronTrigger("0/5 * * * * *"));
        tasks.put(name, future);
    }

    @Override
    public void pauseTask() {

    }

    @Override
    public void resumeTask() {

    }

    @Override
    public void closeTask(String name) {
        ScheduledFuture<?> scheduledFuture = tasks.get(name);
        if(scheduledFuture != null){
            scheduledFuture.cancel(true);
        }
    }
}
