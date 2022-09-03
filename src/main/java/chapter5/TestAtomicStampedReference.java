package chapter5;

import java.util.concurrent.atomic.AtomicStampedReference;

public class TestAtomicStampedReference {
    /**
     * AtomicStampedReference 需要我们传入整型变量作为版本号，来判定是否被更改过
     */
    // 指定版本号
    static AtomicStampedReference<String> str = new AtomicStampedReference<>("A", 0);

    public static void main(String[] args) {
        new Thread(() -> {
            String pre = str.getReference();
            //获得版本号
            int stamp = str.getStamp();
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
            //把str中的A改为C,并比对版本号，如果版本号相同，就执行替换，并让版本号+1
            System.out.println("change A->C stamp " + stamp + str.compareAndSet(pre, "C", stamp, stamp + 1));
        }).start();
    }

    static void other() throws InterruptedException {
        new Thread(() -> {
            int stamp = str.getStamp();
            System.out.println("change A->B stamp " + stamp + str.compareAndSet("A", "B", stamp, stamp + 1));
        }).start();
        Thread.sleep(500);
        new Thread(() -> {
            int stamp = str.getStamp();
            System.out.println("change B->A stamp " + stamp + str.compareAndSet("B", "A", stamp, stamp + 1));
        }).start();
    }
}