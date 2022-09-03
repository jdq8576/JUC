package chapter3;

public class GuardedSuspension {
    public static void main(String[] args) {
        String hello = "hello thread!";
        Guarded guarded = new Guarded();
        new Thread(() -> {
            System.out.println("想要得到结果");
            synchronized (guarded) {
                System.out.println("结果是：" + guarded.getResponse());
            }
            System.out.println("得到结果");
        }).start();

        new Thread(() -> {
            System.out.println("设置结果");
            synchronized (guarded) {
                guarded.setResponse(hello);
            }
        }).start();
    }
}

class Guarded {
    /**
     * 要返回的结果
     */
    private Object response;

    //优雅地使用wait/notify
    public Object getResponse() {
        //如果返回结果为空就一直等待，避免虚假唤醒
        while (response == null) {
            synchronized (this) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
        synchronized (this) {
            //唤醒休眠的线程
            this.notifyAll();
        }
    }

    @Override
    public String toString() {
        return "Guarded{" +
                "response=" + response +
                '}';
    }
}
