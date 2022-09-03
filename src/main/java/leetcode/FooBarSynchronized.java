package leetcode;

public class FooBarSynchronized {

    private Object lock = new Object();

    private boolean type = true;

    private int n;

    public FooBarSynchronized(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {

        for (int i = 0; i < n; i++) {

            synchronized (lock) {
                while (!type) {
                    lock.wait();
                }
                // printFoo.run() outputs "foo". Do not change or remove this line.
                printFoo.run();
                type = false;
                lock.notify();
            }

        }
    }

    public void bar(Runnable printBar) throws InterruptedException {

        for (int i = 0; i < n; i++) {

            synchronized (lock) {
                while (type) {
                    lock.wait();
                }
                // printBar.run() outputs "bar". Do not change or remove this line.
                printBar.run();
                type = true;
                lock.notify();
            }
        }
    }
}