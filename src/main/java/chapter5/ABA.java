package chapter5;

import java.util.concurrent.atomic.AtomicReference;

public class ABA {
    static AtomicReference<String> str = new AtomicReference<>("A");

    public static void main(String[] args) {
        new Thread(() -> {
            String pre = str.get();
            System.out.println("change");
            try {
                other();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //把str中的A改为C
            System.out.println("change A->C " + str.compareAndSet(pre, "C"));
        }).start();
    }

    static void other() throws InterruptedException {
        new Thread(() -> {
            System.out.println("change A->B " + str.compareAndSet("A", "B"));
        }).start();
        Thread.sleep(500);
        new Thread(() -> {
            System.out.println("change B->A " + str.compareAndSet("B", "A"));
        }).start();
    }
}