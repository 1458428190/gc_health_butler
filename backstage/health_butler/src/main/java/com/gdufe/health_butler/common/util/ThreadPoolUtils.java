package com.gdufe.health_butler.common.util;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: laichengfeng
 * @Description:
 * @Date: 2019/3/5 17:11
 */
public class ThreadPoolUtils {

    /**
     * 创建线程池
     * 部分代码参考JDK 1.5
     * {@link Executors.DefaultThreadFactory}
     *
     *  构造线程池的7大参数
     *  int corePoolSize,                  - 核心池大小
     *  int maximumPoolSize,               - 池的最大线程数
     *  long keepAliveTime,                - 空闲线程等待时间
     *  TimeUnit unit,                     - 时间单位
     *  BlockingQueue<Runnable> workQueue, - 储存等待执行的任务队列
     *     + {@link ArrayBlockingQueue}:                           - 一个由数组结构组成的有界阻塞队列。
     *     + {@link LinkedBlockingQueue}:                          - 一个由链表结构组成的无界阻塞队列。
     *     + {@link PriorityBlockingQueue}:                        - 一个支持优先级排序的无界阻塞队列。
     *     + {@link DelayQueue}:                                   - 一个使用优先级队列实现的无界阻塞队列。
     *     + {@link SynchronousQueue}:                             - 一个不存储元素的阻塞队列。
     *     + {@link LinkedTransferQueue}:                          - 一个由链表结构组成的无界阻塞队列。
     *     + {@link LinkedBlockingDeque}:                          - 一个由链表结构组成的双向阻塞队列。
     *  ThreadFactory threadFactory,       - 线程工厂
     *  RejectedExecutionHandler handler   - 拒绝策略               - 可自定义拒绝策略, 通过实现{@link RejectedExecutionHandler}
     *     + {@link ThreadPoolExecutor.AbortPolicy}:               - 丢弃任务并抛出RejectedExecutionException异常。 (默认)
     *     + {@link ThreadPoolExecutor.DiscardPolicy}:             - 也是丢弃任务，但是不抛出异常。
     *     + {@link ThreadPoolExecutor.DiscardOldestPolicy}:       - 丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
     *     + {@link ThreadPoolExecutor.CallerRunsPolicy}:          - 由调用线程处理该任务
     */
    public static ThreadPoolExecutor createDefaultThreadPoolExecutor(int corePoolSize, int maximumPoolSize) {
        ThreadPoolExecutor threadPoolExecutor = null;
        long keepAliveTime = 0;
        TimeUnit unit = TimeUnit.MILLISECONDS;
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ThreadFactory threadFactory = new ThreadFactory() {
            // Java安全管理器, 主要是一些系统的权限管理
            SecurityManager s = System.getSecurityManager();
            // 线程安全, 使用CAS实现
            private final AtomicInteger poolNumber = new AtomicInteger(1);
            private final ThreadGroup threadGroup = (s != null) ? s.getThreadGroup()
                    : Thread.currentThread().getThreadGroup();
            private final AtomicInteger threadNumber = new AtomicInteger(1);
            private final String namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(threadGroup, r, namePrefix + threadNumber.getAndIncrement());
                // 设置为非守护线程, 随着主线程结束而结束
                if(thread.isDaemon()) {
                    thread.setDaemon(false);
                }
                // 将线程的优先级都设为5 (即正常的优先级)
                // 小提示: 线程优先级高, 并不一定代表此线程先调度
                if(thread.getPriority() != Thread.NORM_PRIORITY) {
                    thread.setPriority(Thread.NORM_PRIORITY);
                }
                return thread;
            }
        };
        RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
                threadFactory, handler);
        return threadPoolExecutor;
    }
}
