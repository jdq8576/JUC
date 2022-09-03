package leetcode;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

public class ZeroEvenOddReentrantLock {
    private int n;

    private int current = 1;

    private int state = 0;

    private final Lock lock = new ReentrantLock();

    private final Condition zeroCondition = lock.newCondition();
    private final Condition oddCondition = lock.newCondition();
    private final Condition evenCondition = lock.newCondition();

    public ZeroEvenOddReentrantLock(int n) {
        this.n = n;
    }

    public void zero(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        try {
            while (current <= n) {
                if (state != 0) {
                    zeroCondition.await();
                }
                if (current > n) {
                    break;
                }
                printNumber.accept(0);
                if (current % 2 == 1) {
                    state = 1;
                    oddCondition.signal();
                } else {
                    state = 2;
                    evenCondition.signal();
                }
            }
            evenCondition.signal();
            oddCondition.signal();
        } finally {
            lock.unlock();
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        try {
            while (current <= n) {
                if (state != 2) {
                    evenCondition.await();
                }
                if (current > n) {
                    break;
                }
                printNumber.accept(current++);
                state = 0;
                zeroCondition.signal();
            }
        } finally {
            lock.unlock();
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        try {
            while (current <= n) {
                if (state != 1) {
                    oddCondition.await();
                }
                if (current > n) {
                    break;
                }
                printNumber.accept(current++);
                state = 0;
                zeroCondition.signal();
            }
        } finally {
            lock.unlock();
        }
    }
}