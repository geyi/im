package com.airing.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadPoolUtils {
    private static final Logger log = LoggerFactory.getLogger(ThreadPoolUtils.class);
    private static volatile ThreadPoolUtils ThreadPoolUtils = null;
    private static ThreadPoolExecutor executor;
    private static final AtomicLongFieldUpdater<ThreadPoolUtils> WAITING_TIME_UPDATER =
            AtomicLongFieldUpdater.newUpdater(ThreadPoolUtils.class, "waitingTime");
    private volatile long waitingTime = 0;
    private static final AtomicLongFieldUpdater<ThreadPoolUtils> TOTAL_TIME_UPDATER =
            AtomicLongFieldUpdater.newUpdater(ThreadPoolUtils.class, "totalTime");
    private volatile long totalTime = 0;
    private static final long KAT = 1000;

    private ThreadPoolUtils() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int corePoolSize;
        int maxPoolSize;
        if (availableProcessors <= 2) {
            corePoolSize = availableProcessors << 2;
            maxPoolSize = availableProcessors << 3;
        } else {
            corePoolSize = availableProcessors << 1;
            maxPoolSize = availableProcessors << 2;
        }
        executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, KAT, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(2000),
                new ThreadFactoryBuilder().setNameFormat("MY-THREAD-%d").build());
    }

    public static ThreadPoolUtils getSingle() {
        if (ThreadPoolUtils == null) {
            synchronized (ThreadPoolUtils.class) {
                if (ThreadPoolUtils == null) {
                    ThreadPoolUtils = new ThreadPoolUtils();
                }
            }
        }
        return ThreadPoolUtils;
    }

    public void execute(Runnable runnable) {
        executor.execute(runnable);
        this.printLog();
    }

    public <T> Future<T> execute(Runnable runnable, T result) {
        Future<T> future = executor.submit(runnable, result);
        this.printLog();
        return future;
    }

    public void printLog() {
        log.info("activeCount: {}, taskCount: {}, completedTaskCount: {}, queueSize: {}",
                executor.getActiveCount(),
                executor.getTaskCount(),
                executor.getCompletedTaskCount(),
                executor.getQueue().size());
        int queueSize = executor.getQueue().size();
        if (queueSize >= 10) {
            log.warn("queueSize: {}", queueSize);
        }
    }

    public void updateWaitingTime(long time) {
        long newTime = WAITING_TIME_UPDATER.addAndGet(this, time);
        log.info("new waiting time: {}", newTime);
    }

    public void updateTotalTime(long time) {
        long newTime = TOTAL_TIME_UPDATER.addAndGet(this, time);
        log.info("new total time: {}", newTime);
    }
}
