package questions.one;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AskWithReentrantLock {
    static String[] wordArray = {"A","B","C","D","E","F","G","H","I","J","K"};
    static int[] numArray = {0,1,2,3,4,5,6,7,8,9,0};

    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Condition condition1 = lock.newCondition();
        Condition condition2 = lock.newCondition();

        Thread thread1  = new Thread(){
            @Override
            public void run() {
                try{
                    lock.lock();
                    for (int i = 0; i < wordArray.length; i++) {
                        System.out.print(wordArray[i] + " ");
                        try {
                            condition2.signal();
                            System.out.println("线程1执行了signal");
                            condition1.await();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                    condition2.signal();//唤醒线程2，必须，否则无法停止程序，因为最后肯定有个在await
                }finally {
                    lock.unlock();
                }
            }
        };

        Thread thread2 = new Thread(){
            @Override
            public void run() {
                try{
                    lock.lock();
                    for (int i = 0; i < numArray.length; i++) {
                        System.out.print(numArray[i] + " ");
                        try {
                            condition1.signal();
                            System.out.println("线程2执行了signal");
                            condition2.await();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    condition1.signal();//唤醒线程1，必须，否则无法停止程序，因为最后肯定有个在await
                }finally {
                    lock.unlock();
                }
            }
        };

        thread1.start();
        thread2.start();
    }
}
