package leetcode;

import java.util.concurrent.Semaphore;

public class FooBarSemaphore {

    private Semaphore one = new Semaphore(1);
    private Semaphore two = new Semaphore(0);

    private int n;

    public FooBarSemaphore(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            one.acquire();
            // printFoo.run() outputs "foo". Do not change or remove this line.
            printFoo.run();
            two.release();
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            two.acquire();
            // printBar.run() outputs "bar". Do not change or remove this line.
            printBar.run();
            one.release();
        }
    }
}