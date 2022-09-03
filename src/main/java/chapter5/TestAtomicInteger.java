package chapter5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

interface Account {
    Integer getBalance();

    void withdraw(Integer amount);

    /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(Account account) {
        List<Thread> ts = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withdraw(10);
            }));
        }
        ts.forEach((t) -> {
            t.start();
        });
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.println(account.getBalance() + " cost: " + (end - start) / 1000_000 + " ms");
    }
}

//线程不安全的做法
class AccountUnsafe implements Account {
    private Integer balance;

    public AccountUnsafe(Integer balance) {
        this.balance = balance;
    }


    @Override
    public Integer getBalance() {
        return this.balance;
    }

    @Override
    public synchronized void withdraw(Integer amount) {
        balance -= amount;
    }
}

//线程安全的做法
class AccountCas implements Account {
    //使用原子整数
    private AtomicInteger balance;

    public AccountCas(int balance) {
        this.balance = new AtomicInteger(balance);
    }

    @Override
    public Integer getBalance() {
        //得到原子整数的值
        return balance.get();
    }

    @Override
    public void withdraw(Integer amount) {
        while (true) {
            //获得修改前的值
            int prev = balance.get();
            //获得修改后的值
            int next = prev - amount;
            //比较并设值
            if (balance.compareAndSet(prev, next)) {
                break;
            }
        }
    }
}

public class TestAtomicInteger {
    public static void main(String[] args) {
        Account.demo(new AccountUnsafe(10000));
        Account.demo(new AccountCas(10000));
    }
}