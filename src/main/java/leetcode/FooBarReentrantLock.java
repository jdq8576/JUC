package leetcode;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FooBarReentrantLock {
    private int n;

    private ReentrantLock lock = new ReentrantLock(true);

    private boolean foo = true;

    private Condition condition = lock.newCondition();

    public FooBarReentrantLock(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {

        for (int i = 0; i < n; i++) {

            lock.lock();
            try {
                while (!foo) {
                    condition.await();
                }
                // printFoo.run() outputs "foo". Do not change or remove this line.
                printFoo.run();
                foo = false;
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            lock.lock();
            try {
                while (foo) {
                    condition.await();
                }
                // printBar.run() outputs "bar". Do not change or remove this line.
                printBar.run();
                foo = true;
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
    }
}