package chapter2;

import java.util.concurrent.TimeUnit;

public class TwoStage {
    public static void main(String[] args) {
        Monitor monitor = new Monitor();
        monitor.start();
        try {
            TimeUnit.SECONDS.sleep(3500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        monitor.stop();
    }
}

class Monitor {
    Thread monitor;

    public void start() {
        monitor = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("处理后续任务");
                    break;
                }
                System.out.println("监控执行中");
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    /**
                     * 如果一个线程在在运行中被打断，打断标记会被置为true
                     * 如果是打断因sleep wait join方法而被阻塞的线程，会将打断标记置为false
                     */
                    Thread.currentThread().interrupt();
                }
            }
        });
        monitor.start();
    }

    /**
     * 用于停止监控器线程
     */
    public void stop() {
        monitor.interrupt();
    }
}
