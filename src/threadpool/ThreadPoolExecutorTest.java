package threadpool;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolExecutorTest {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = new ThreadPoolExecutor(
                3,
                3,
                5,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(10),
                new MyThreadFactory("testThreadFactory"),
                new ThreadPoolExecutor.CallerRunsPolicy());
        executorService.submit(
                new Thread(() -> System.out.println(Thread.currentThread().getName() + ", 1<<2=" + (1 << 2)))
        );
        executorService.shutdown();
    }

    /**
     * 自定义线程工厂名称
     */
    static class MyThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        MyThreadFactory(String threadFactoryName) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" + threadFactoryName;
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
