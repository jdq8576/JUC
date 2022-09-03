package chapter7;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 阻塞队列
 * 用于存放主线程或其他线程产生的任务
 */
public class BlockingQueue<T> {
    /**
     * 阻塞队列
     */
    private Deque<T> blockingQueue;

    /**
     * 阻塞队列容量
     */
    private int capacity;

    /**
     * 锁
     */
    private ReentrantLock lock;

    /**
     * 条件队列
     */
    private Condition fullQueue;
    private Condition emptyQueue;


    public BlockingQueue(int capacity) {
        blockingQueue = new ArrayDeque<>(capacity);
        lock = new ReentrantLock();
        fullQueue = lock.newCondition();
        emptyQueue = lock.newCondition();
        this.capacity = capacity;
    }

    /**
     * 获取任务的方法
     */
    public T take() {
        // 加锁
        lock.lock();
        try {
            // 如果阻塞队列为空（没有任务），就一直等待
            while (blockingQueue.isEmpty()) {
                try {
                    emptyQueue.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 获取任务并唤醒生产者线程
            T task = blockingQueue.removeFirst();
            fullQueue.signalAll();
            return task;
        } finally {
            lock.unlock();
        }
    }

    public T takeNanos(long timeout, TimeUnit unit) {
        // 转换等待时间
        lock.lock();
        try {
            long nanos = unit.toNanos(timeout);
            while (blockingQueue.isEmpty()) {
                try {
                    // awaitNanos会返回剩下的等待时间
                    nanos = emptyQueue.awaitNanos(nanos);
                    if (nanos < 0) {
                        return null;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T task = blockingQueue.removeFirst();
            fullQueue.signalAll();
            return task;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 放入任务的方法
     *
     * @param task 放入阻塞队列的任务
     */
    public void put(T task) {
        lock.lock();
        try {
            while (blockingQueue.size() == capacity) {
                try {
                    System.out.println("阻塞队列已满");
                    fullQueue.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            blockingQueue.add(task);
            // 唤醒等待的消费者
            emptyQueue.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public int getSize() {
        lock.lock();
        try {
            return blockingQueue.size();
        } finally {
            lock.unlock();
        }
    }
}