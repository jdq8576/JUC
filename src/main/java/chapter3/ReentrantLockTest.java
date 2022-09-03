package chapter3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTest {
    static Boolean judge = false;

    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        //获得条件变量
        Condition condition = lock.newCondition();
        new Thread(() -> {
            lock.lock();
            try {
                while (!judge) {
                    System.out.println("不满足条件，等待...");
                    //等待
                    condition.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("执行完毕！");
                lock.unlock();
            }
        }).start();

        new Thread(() -> {
            lock.lock();
            try {
                Thread.sleep(1);
                judge = true;
                //释放
                condition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }).start();
    }
}
