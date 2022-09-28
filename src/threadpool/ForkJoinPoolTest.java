package threadpool;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 计算一百万个随机数的总和
 */
public class ForkJoinPoolTest {
    static int[] nums = new int[1000000];
    static final int MAX_NUM = 50000;

    static {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < nums.length; i++) {
            nums[i] = new Random().nextInt(100);
        }

        System.out.println("单线程计算总和:"+Arrays.stream(nums).sum()
                + ", 耗时：" + (System.currentTimeMillis() - startTime));
    }

    static class RecursiveTaskTest extends RecursiveTask<Long>{
        int start, end;

        public RecursiveTaskTest(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if(end -start <= MAX_NUM){
                long sum = 0;
                for (int i = start; i<end; i++) sum+=nums[i];
                return sum;
            }

            int middle= start + (end - start) /2;
            RecursiveTaskTest subTask1 = new RecursiveTaskTest(start, middle);
            RecursiveTaskTest subTask2 = new RecursiveTaskTest(middle, end);
            subTask1.fork();//让任务subTask1进行分叉处理
            subTask2.fork();//让任务subTask2进行分叉处理
            return subTask1.join() + subTask2.join();//这里的join()方法表示汇总计算结果
        }
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        RecursiveTaskTest taskTest = new RecursiveTaskTest(0, nums.length);
        forkJoinPool.execute(taskTest);
        long result = taskTest.join();//获得汇总计算结果
        System.out.println("使用forkJoinPool计算总和：" + result + ", 耗时："
                + (System.currentTimeMillis() - startTime));
    }
}
