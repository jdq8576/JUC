package chapter7;

import java.util.concurrent.TimeUnit;

public class DiyThreadPool {
    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(2, TimeUnit.SECONDS, 1, 4);
        for (int i = 0; i < 10; i++) {
            threadPool.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("任务正在执行!");
            });
        }
    }
}


