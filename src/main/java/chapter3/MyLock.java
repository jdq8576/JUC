package chapter3;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class MyLock implements Lock {

    private static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            if (getExclusiveOwnerThread() == null) {
                if (compareAndSetState(0, 1)) {
                    setExclusiveOwnerThread(Thread.currentThread());
                    return true;
                }
                return false;
            }

            if (getExclusiveOwnerThread() == Thread.currentThread()) {
                int state = getState();
                compareAndSetState(state, state + 1);
                return true;
            }

            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState() <= 0) {
                throw new IllegalMonitorStateException();
            }

            if (getExclusiveOwnerThread() != Thread.currentThread()) {
                throw new IllegalMonitorStateException();
            }

            int state = getState();
            if (state == 1) {
                setExclusiveOwnerThread(null);
                compareAndSetState(state, 0);
            } else {
                compareAndSetState(state, state - 1);
            }
            return true;
        }

        @Override
        protected boolean isHeldExclusively() {
            return getState() >= 1;
        }

        public Condition newCondition() {
            return new ConditionObject();
        }

    }

    Sync sync = new Sync();

    @Override
    public void lock() {
        sync.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireNanos(1, time);
    }

    @Override
    public void unlock() {
        sync.release(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }
}

class Main {
    static int num = 0;

    public static void main(String[] args) throws InterruptedException, IOException {
        MyLock lock = new MyLock();

        Object syncLock = new Object();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                lock.lock();
                try {
                    lock.lock();
                    try {
                        lock.lock();
                        try {
                            num++;
                        } finally {
                            lock.unlock();
                        }
                    } finally {
                        lock.unlock();
                    }
                } finally {
                    lock.unlock();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                lock.lock();
                try {
                    lock.lock();
                    try {
                        lock.lock();
                        try {
                            num--;
                        } finally {
                            lock.unlock();
                        }
                    } finally {
                        lock.unlock();
                    }
                } finally {
                    lock.unlock();
                }
            }
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        int x = 0;
    }
}