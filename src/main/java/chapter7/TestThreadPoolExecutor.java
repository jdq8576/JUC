package chapter7;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class TestThreadPoolExecutor {
    static AtomicInteger threadId = new AtomicInteger(0);

    public static void main(String[] args) {
        // 手动创建线程池
        // 创建有界阻塞队列
        ArrayBlockingQueue<Runnable> runnable = new ArrayBlockingQueue<Runnable>(10);
        // 创建线程工厂
        ThreadFactory threadFactory = new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "working_thread_" + threadId.getAndIncrement());
                return thread;
            }
        };

        // 手动创建线程池
        // 拒绝策略采用默认策略
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 7, 10, TimeUnit.SECONDS, runnable, threadFactory);

        for (int i = 0; i < 20; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread());
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}