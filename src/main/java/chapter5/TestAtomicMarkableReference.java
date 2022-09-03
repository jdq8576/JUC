package chapter5;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class TestAtomicMarkableReference {
    /**
     * AtomicMarkableReference需要我们传入布尔变量作为标记，来判断是否被更改过
     */
    // 指定标记
    static AtomicMarkableReference<String> str = new AtomicMarkableReference<>("A", true);

    public static void main(String[] args) {
        new Thread(() -> {
            String pre = str.getReference();
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
            System.out.println("change A->C mark " + str.compareAndSet(pre, "C", true, false));
        }).start();
    }

    static void other() throws InterruptedException {
        new Thread(() -> {
            System.out.println("change A->A mark " + str.compareAndSet("A", "A", true, false));
        }).start();
    }
}