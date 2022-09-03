package chapter3;

public class Work1 {
    static Symbol symbol = new Symbol();

    public static void main(String[] args) {
        new Thread(() -> {
            symbol.run("a", 1, 2);
        }).start();

        new Thread(() -> {
            symbol.run("b", 2, 3);

        }).start();
        symbol.run("c", 3, 1);
        new Thread(() -> {

        }).start();
    }
}

class Symbol {
    public synchronized void run(String str, int flag, int nextFlag) {
        for (int i = 0; i < loopNumber; i++) {
            while (flag != this.flag) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(str);
            //设置下一个运行的线程标记
            this.flag = nextFlag;
            //唤醒所有线程
            this.notifyAll();
        }
    }

    /**
     * 线程的执行标记， 1->a 2->b 3->c
     */
    private int flag = 1;
    private int loopNumber = 5;

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getLoopNumber() {
        return loopNumber;
    }

    public void setLoopNumber(int loopNumber) {
        this.loopNumber = loopNumber;
    }
}
