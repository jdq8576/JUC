package chapter7;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class TestFixedThreadPool {
    public static void main(String[] args) {
        // 自定义线程工厂
        ThreadFactory factory = new ThreadFactory() {
            AtomicInteger atomicInteger = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "myThread_" + atomicInteger.getAndIncrement());
            }
        };

        // 创建核心线程数量为2的线程池
        // 通过 ThreadFactory可以给线程添加名字

        ExecutorService executorService = Executors.newFixedThreadPool(2, factory);

        // 任务
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
                System.out.println("this is fixedThreadPool");
            }
        };

        executorService.execute(runnable);
    }
}