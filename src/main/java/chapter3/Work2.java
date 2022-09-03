package chapter3;

public class Work2 {
    static final Object LOCK = new Object();
    //判断先执行的内容是否执行完毕
    static Boolean judge = false;

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (LOCK) {
                while (!judge) {
                    try {
                        LOCK.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("2");
            }
        }).start();

        new Thread(() -> {
            synchronized (LOCK) {
                System.out.println("1");
                judge = true;
                //执行完毕，唤醒所有等待线程
                LOCK.notifyAll();
            }
        }).start();
    }
}
