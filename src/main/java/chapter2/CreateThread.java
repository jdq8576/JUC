package chapter2;

public class CreateThread {
    public static void main(String[] args) {
        Thread thread = new MyThread();
        thread.start();

        Thread thread1 = new Thread(new MyRunnable());
        /**
         * 通过实现Runnable接口，并且实现run()方法。在创建线程时作为参数传入该类的实例即可
         *  用Runnable 更容易与线程池等高级 API 配合 用 Runnable 让任务类脱离了 Thread 继承体系，更灵活
         */
        thread1.start();

        /**
         * 使用lambda表达式简化操作
         * 当一个接口带有@FunctionalInterface注解时，是可以使用lambda来简化操作的
         */
        Runnable r = () -> {
            System.out.println("my thread running");
        };
        Thread thread2 = new Thread(r);
        thread2.start();
    }
}

class MyThread extends Thread {
    /**
     * 在run（）方法内获取当前线程直接使用this就可以了，无须使用Thread.currentThread（）方法；
     * 不好的地方是Java不支持多继承，如果继承了Thread类，那么就不能再继承其他类。
     * 另外任务与代码没有分离，当多个线程执行一样的任务时需要多份任务代码
     */
    @Override
    public void run() {
        System.out.println("my thread running");
    }
}

class MyRunnable implements Runnable {

    @Override
    public void run() {
        System.out.println("my thread running");
    }
}
