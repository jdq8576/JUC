package chapter3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Work3 {
    static AwaitSignal awaitSignal = new AwaitSignal();
    static Condition conditionA = awaitSignal.newCondition();
    static Condition conditionB = awaitSignal.newCondition();
    static Condition conditionC = awaitSignal.newCondition();

    public static void main(String[] args) {
        new Thread(() -> {
            awaitSignal.run("a", conditionA, conditionB);
        }).start();

        new Thread(() -> {
            awaitSignal.run("b", conditionB, conditionC);
        }).start();

        new Thread(() -> {
            awaitSignal.run("c", conditionC, conditionA);
        }).start();


        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        awaitSignal.lock();
        try {
            //唤醒一个等待的线程
            conditionA.signal();
        } finally {
            awaitSignal.unlock();
        }
    }
}

class AwaitSignal extends ReentrantLock {
    public void run(String str, Condition thisCondition, Condition nextCondition) {
        for (int i = 0; i < loopNumber; i++) {
            lock();
            try {
                //全部进入等待状态
                thisCondition.await();
                System.out.print(str);
                nextCondition.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                unlock();
            }
        }
    }

    private int loopNumber = 5;

    public int getLoopNumber() {
        return loopNumber;
    }

    public void setLoopNumber(int loopNumber) {
        this.loopNumber = loopNumber;
    }
}
