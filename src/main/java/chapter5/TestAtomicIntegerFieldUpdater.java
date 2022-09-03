package chapter5;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class TestAtomicIntegerFieldUpdater {
    private static AtomicIntegerFieldUpdater<Node> update = AtomicIntegerFieldUpdater.newUpdater(Node.class, "a");
    private static Node test = new Node();
    public volatile int a = 100;

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    if (update.compareAndSet(test, 100, 120)) {
                        System.out.print("已修改");
                    }
                }
            });
            t.start();
        }
    }
}

class Node {
    volatile int a = 100;
}
