package chapter3;

public class ThreadLocalStudy {
    public static void main(String[] args) {
        // 创建ThreadLocal变量
        ThreadLocal<String> stringThreadLocal = new ThreadLocal<>();
        ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

        // 创建两个线程，分别使用上面的两个ThreadLocal变量
        Thread thread1 = new Thread(() -> {
            // stringThreadLocal第一次赋值
            stringThreadLocal.set("thread1 stringThreadLocal first");
            // stringThreadLocal第二次赋值
            stringThreadLocal.set("thread1 stringThreadLocal second");
            // userThreadLocal赋值
            userThreadLocal.set(new User("Tim", 20));

            // 取值
            System.out.println(stringThreadLocal.get());
            System.out.println(userThreadLocal.get());

            // 移除
            userThreadLocal.remove();
            System.out.println(userThreadLocal.get());
        });

        Thread thread2 = new Thread(() -> {
            // stringThreadLocal第一次赋值
            stringThreadLocal.set("thread2 stringThreadLocal first");
            // stringThreadLocal第二次赋值
            stringThreadLocal.set("thread2 stringThreadLocal second");
            // userThreadLocal赋值
            userThreadLocal.set(new User("Hulu", 20));

            // 取值
            System.out.println(stringThreadLocal.get());
            System.out.println(userThreadLocal.get());
        });

        // 启动线程
        thread1.start();
        thread2.start();
    }
}

class User {
    String name;
    int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}