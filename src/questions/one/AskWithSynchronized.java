package questions.one;

/**
 * 使用Object的方法实现问题1
 */
public class AskWithSynchronized {

    static String[] wordArray = {"A","B","C","D","E","F","G","H","I","J","K"};
    static int[] numArray = {0,1,2,3,4,5,6,7,8,9,0};

    public static void main(String[] args) {

        Object objectLock = new Object();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < wordArray.length; i++) {

                synchronized (objectLock) {
                    System.out.print(wordArray[i] + " ");
                    try{
                        objectLock.notify();
                        objectLock.wait();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    objectLock.notify();//唤醒线程2，必须，否则无法停止程序，因为最后肯定有个在wait
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < numArray.length; i++) {

                synchronized (objectLock){
                    System.out.print(numArray[i] + " ");
                    try{
                        objectLock.notify();
                        objectLock.wait();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    objectLock.notify();//唤醒线程1，必须，否则无法停止程序，因为最后肯定有个在wait
                }
            }
        });

        thread1.start();
        thread2.start();
    }
}
