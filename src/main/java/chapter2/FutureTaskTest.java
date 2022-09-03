package chapter2;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        /**
         * 使用FutureTask可以用泛型指定线程的返回值类型（Runnable的run方法没有返回值）
         */

        //需要传入一个Callable对象
        FutureTask<Integer> task = new FutureTask<Integer>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("线程执行!");
                Thread.sleep(1000);
                return 100;
            }
        });

        Thread r1 = new Thread(task, "t2");
        r1.start();
        //获取线程中方法执行后的返回结果
        System.out.println(task.get());
    }
}
