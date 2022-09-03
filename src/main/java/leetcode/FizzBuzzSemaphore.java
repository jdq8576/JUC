package leetcode;

import java.util.concurrent.Semaphore;
import java.util.function.IntConsumer;

public class FizzBuzzSemaphore {

    private int n;
    private int cur;
    private Semaphore fizz;
    private Semaphore buzz;
    private Semaphore fizzbuzz;
    private Semaphore other;

    public FizzBuzzSemaphore(int n) {
        this.n = n;
        this.fizz = new Semaphore(0);
        this.buzz = new Semaphore(0);
        this.fizzbuzz = new Semaphore(0);
        this.other = new Semaphore(0);
        this.cur = 1;
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        while (true) {
            this.fizz.acquire();
            if (this.cur > n) {
                break;
            }
            printFizz.run();
            this.other.release();
        }
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        while (true) {
            this.buzz.acquire();
            if (this.cur > n) {
                break;
            }
            printBuzz.run();
            this.other.release();
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        while (true) {
            this.fizzbuzz.acquire();
            if (this.cur > n) {
                break;
            }
            printFizzBuzz.run();
            this.other.release();
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        while (this.cur <= n) {
            if (cur % 3 == 0 && cur % 5 == 0) {
                fizzbuzz.release();
            } else if (this.cur % 3 == 0) {
                fizz.release();
            } else if (this.cur % 5 == 0) {
                buzz.release();
            } else {
                this.other.release();
                printNumber.accept(this.cur);
            }
            this.other.acquire();
            this.cur++;
        }
        this.fizzbuzz.release();
        this.fizz.release();
        this.buzz.release();
    }
}