package questions.two;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CreateBaozi2 {

    //定义蒸容大小(使用有界阻塞队列)
    private static Integer capacity = 20;
    private static ArrayBlockingQueue<String> zhenglong = new ArrayBlockingQueue<>(capacity);

    //定义生产包子的方法，蒸笼满了停止生产包子
    private static void createBaozi() {
        while (true) {
            synchronized (capacity) {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String baoziName = "包子" + UUID.randomUUID().toString().replace("-", "");
                boolean result = zhenglong.offer(baoziName);
//                System.out.println("线程" + Thread.currentThread().getId() + "造" + baoziName + ", result=" + result);

                if (result) {
                    System.out.println("线程" + Thread.currentThread().getId() + "【造】" + baoziName + ",蒸笼包子数：" + zhenglong.size());
                } else {
                    System.err.println("线程" + Thread.currentThread().getId() + "【【造】】包子,蒸笼满了。");
                    capacity.notifyAll();
                    try {
                        capacity.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //定义吃包子的方法，蒸笼空了停止吃包子
    private static void eatBaozi() {
        while (true) {
            synchronized (capacity) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String poll = zhenglong.poll();
//                System.out.println("线程" + Thread.currentThread().getId() + "吃" + poll);
                if (null != poll) {
                    System.out.println("线程" + Thread.currentThread().getId() + "吃" + poll + "。蒸笼包子剩余个数：" + zhenglong.size());
                } else {
                    System.err.println("线程" + Thread.currentThread().getId() + "吃包子，蒸笼空了。");
                    capacity.notifyAll();
                    try {
                        capacity.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 5, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));
        threadPoolExecutor.execute(() -> new Thread(() -> createBaozi()).start());
        threadPoolExecutor.execute(() -> new Thread(() -> createBaozi()).start());
        threadPoolExecutor.execute(() -> new Thread(() -> createBaozi()).start());
        try {
            Thread.sleep(3000L);//先让生产包子
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        threadPoolExecutor.execute(() -> new Thread(() -> eatBaozi()).start());
        threadPoolExecutor.execute(() -> new Thread(() -> eatBaozi()).start());

        threadPoolExecutor.shutdown();
    }
}
