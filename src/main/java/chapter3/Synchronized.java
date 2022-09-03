package chapter3;


import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

public class Synchronized {

    static int counter = 0;

    static final Object room = new Object();

    static Logger logger = LoggerFactory.getLogger(Synchronized.class);


    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (room) {
                    counter++;
                }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (room) {
                    counter--;
                }
            }
        }, "t2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(counter);
        logger.debug("{}", counter);
    }
}

class Method {
    //在方法上加上synchronized关键字
    public synchronized void test1() {

    }

    //等价于
    public void test() {
        synchronized (this) {

        }
    }
}

class StaticMethod {
    //在静态方法上加上synchronized关键字
    public synchronized static void test1() {

    }

    //等价于
    public void test() {
        synchronized (Class.class) {

        }
    }
}

