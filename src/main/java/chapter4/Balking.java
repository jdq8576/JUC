package chapter4;

public class Balking {
    public static void main(String[] args) throws InterruptedException {
        Monitor monitor = new Monitor();
        monitor.start();
        monitor.start();
        Thread.sleep(3500);
        monitor.stop();
    }

    static class Monitor {

        Thread monitor;
        //设置标记，用于判断是否被终止了
        private volatile boolean stop = false;
        //设置标记，用于判断是否已经启动过了
        private boolean starting = false;

        /**
         * 启动监控器线程
         */
        public void start() {
            //上锁，避免多线程运行时出现线程安全问题
            synchronized (this) {
                if (starting) {
                    //已被启动，直接返回
                    return;
                }
                //启动监视器，改变标记
                starting = true;
            }
            //设置线控器线程，用于监控线程状态
            monitor = new Thread(() -> {
                //开始不停的监控
                while (true) {
                    if (stop) {
                        System.out.println("处理后续任务");
                        break;
                    }
                    System.out.println("监控器运行中...");
                    try {
                        //线程休眠
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        System.out.println("被打断了");
                    }
                }
            });
            monitor.start();
        }

        /**
         * 用于停止监控器线程
         */
        public void stop() {
            //打断线程
            monitor.interrupt();
            stop = true;
        }
    }
}

