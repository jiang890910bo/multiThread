package threadpool;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public class CompletableFutureTask {

    public static void sleep(){
        try {
            Thread.sleep(new Random().nextInt(500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static double priceOfTM() {
        sleep();
        return 1.00;
    }
    public static double priceOfTB() {
        sleep();
        return 2.00;
    }
    public static double priceOfJD() {
        sleep();
        return 3.00;
    }

    public static void main(String[] args) throws IOException {
        try {
            long startTime = System.currentTimeMillis();
            CompletableFuture<Double> futureOfTM = CompletableFuture.supplyAsync(()->priceOfTM());
            CompletableFuture<Double> futureOfTB = CompletableFuture.supplyAsync(()->priceOfTB());
            CompletableFuture<Double> futureOfJD = CompletableFuture.supplyAsync(()->priceOfJD());

            double priceOfTM = futureOfTM.get();
            double priceOfTB = futureOfTB.get();
            double priceOfJD = futureOfJD.get();
//            double priceOfTM = priceOfTM();
//            double priceOfTB = priceOfTB();
//            double priceOfJD = priceOfJD();

            System.out.println("天猫：" + priceOfTM +
                    "淘宝：" + priceOfTB +
                    "京东：" + priceOfJD +
                    " 耗时：" + (System.currentTimeMillis() - startTime) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
