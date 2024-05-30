package com.patternknife.pxbsample.config.queue.pool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.ThreadPoolExecutor;

@EnableAsync
@Configuration
@EnableScheduling
public class ThreadPoolConfig {
    @Bean
    public ThreadPoolTaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();

        // Size of the scheduler thread pool. Here it is set to the number of processors of the machine.
        // Runtime.getRuntime().availableProcessors()
        taskScheduler.setPoolSize(3);

        // Prefix of the scheduler thread name to be logged
        taskScheduler.setThreadNamePrefix("Scheduler-Thread-");

        // Apply all settings and initialize the ThreadPoolTaskScheduler
        taskScheduler.initialize();

        return taskScheduler;
    }

    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        // Prefix of the thread name to be logged
        taskExecutor.setThreadNamePrefix("Async-Thread-");

        // Core size of the thread pool to be maintained. The set value is the number of processors of the machine.
        taskExecutor.setCorePoolSize(3);

        // Maximum thread pool size
        taskExecutor.setMaxPoolSize(5);

        // The number of threads to wait when thread creation is to be queued after creating threads up to MaxPoolSize
        taskExecutor.setQueueCapacity(50);

        // Policy for when threads need to be created beyond MaxPoolSize and QueueCapacity
        // CallerRunsPolicy is a policy where the thread that tried to create and delegate the thread handles all processing directly
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // Wait for running threads to complete all processing before shutting down when the application exits
        taskExecutor.setWaitForTasksToCompleteOnShutdown(true);

        // Whether to kill core pool threads after their idle time (default 60s) has passed.
        // The default value is false, and if set to true, the threads are killed.
        taskExecutor.setAllowCoreThreadTimeOut(false);

        // Apply all settings and initialize the ThreadPoolTaskExecutor
        taskExecutor.initialize();

        return taskExecutor;
    }
}
