package chapter7;

import java.util.HashSet;
import java.util.concurrent.TimeUnit;

/**
 * 自定义线程池
 */
public class ThreadPool {
    /**
     * 自定义阻塞队列
     */
    private BlockingQueue<Runnable> blockingQueue;

    /**
     * 核心线程数
     */
    private int coreSize;

    private HashSet<Worker> workers = new HashSet<>();

    /**
     * 用于指定线程最大存活时间
     */
    private TimeUnit timeUnit;
    private long timeout;

    /**
     * 工作线程类
     * 内部封装了Thread类，并且添加了一些属性
     */
    private class Worker extends Thread {
        Runnable task;

        public Worker(Runnable task) {
            System.out.println("初始化任务");
            this.task = task;
        }

        @Override
        public void run() {
            // 如果有任务就执行
            // 如果阻塞队列中有任务，就继续执行
            while (task != null || (task = blockingQueue.take()) != null) {
                try {
                    System.out.println("执行任务");
                    task.run();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    // 任务执行完毕，设为空
                    System.out.println("任务执行完毕");
                    task = null;
                }
            }
            // 移除任务
            synchronized (workers) {
                System.out.println("移除任务");
                workers.remove(this);
            }
        }
    }

    public ThreadPool(int coreSize, TimeUnit timeUnit, long timeout, int capacity) {
        this.coreSize = coreSize;
        this.timeUnit = timeUnit;
        blockingQueue = new BlockingQueue<>(capacity);
        this.timeout = timeout;
    }

    public void execute(Runnable task) {
        synchronized (workers) {
            // 创建任务
            // 池中还有空余线程时，可以运行任务
            // 否则阻塞
            if (workers.size() < coreSize) {
                Worker worker = new Worker(task);
                workers.add(worker);
                worker.start();
            } else {
                System.out.println("线程池中线程已用完，请稍等");
                blockingQueue.put(task);
            }
        }
    }
}