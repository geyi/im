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
    private ThreadPoolExecutor executor = null;
    private static final AtomicLongFieldUpdater<ThreadPoolUtils> WAITING_TIME_UPDATER =
            AtomicLongFieldUpdater.newUpdater(ThreadPoolUtils.class, "waitingTime");
    private volatile long waitingTime = 0;
    private static final AtomicLongFieldUpdater<ThreadPoolUtils> TOTAL_TIME_UPDATER =
            AtomicLongFieldUpdater.newUpdater(ThreadPoolUtils.class, "totalTime");
    private volatile long totalTime = 0;

    private ThreadPoolUtils() {
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

    private void initParam() {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        int corePoolSize;
        int maxPoolSize;
        if (availableProcessors <= 2) {
            corePoolSize = availableProcessors << 4;
            maxPoolSize = availableProcessors << 5;
        } else {
            corePoolSize = availableProcessors << 2;
            maxPoolSize = availableProcessors << 3;
        }
        initThreadPool(corePoolSize, maxPoolSize, 2000);
    }

    public void execute(Runnable runnable) {
        if (executor == null) {
            initParam();
        }
        executor.execute(runnable);
        this.print();
    }

    public <T> Future<T> execute(Runnable runnable, T result) {
        if (executor == null) {
            initParam();
        }
        Future<T> future = executor.submit(runnable, result);
        this.print();
        return future;
    }

    public void initThreadPool(int corePoolSize, int maxPoolSize, int queueSize) {
        log.info("corePoolSize: {}, maxPoolSize: {}, queueSize: {}", corePoolSize, maxPoolSize, queueSize);
        if (executor == null) {
            executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 200, TimeUnit.MILLISECONDS, new
                    ArrayBlockingQueue<Runnable>(queueSize), new ThreadFactoryBuilder().setNameFormat
                    ("MY-THREAD-%d").build());
        }
    }

    public void print() {
        log.info("activeCount: {}", executor.getActiveCount());
        log.info("completedTaskCount: {}", executor.getCompletedTaskCount());
        log.info("taskCount: {}", executor.getTaskCount());
        log.info("queueSize: {}", executor.getQueue().size());
        int queueSize = executor.getQueue().size();
        if (queueSize >= 10) {
            log.error("queueSize: {}", queueSize);
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
