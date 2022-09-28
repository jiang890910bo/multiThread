package questions.one;

import java.util.concurrent.locks.LockSupport;

public class AskWithLockSupport {

    static String[] wordArray = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
    static int[] numArray = {0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5};
    static Thread thread1 = null;
    static Thread thread2 = null;

    public static void main(String[] args) {


        thread1 = new Thread(){
            @Override
            public void run() {
                for (int i = 0; i < wordArray.length; i++) {
                    System.out.print(wordArray[i] + " ");
                    LockSupport.unpark(thread2);
                    LockSupport.park();
                }
                LockSupport.unpark(thread2);//唤醒线程2，必须，否则无法停止程序，因为最后肯定有个在park
            }
        };
        thread2 = new Thread(){
            @Override
            public void run() {
                for (int i = 0; i < numArray.length; i++) {
                    System.out.print(numArray[i] + " ");
                    LockSupport.unpark(thread1);
                    LockSupport.park();
                }
                LockSupport.unpark(thread1);//唤醒线程1，必须，否则无法停止程序，因为最后肯定有个在park
            }
        };

        thread1.start();
        thread2.start();
    }
}
