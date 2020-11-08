package deadlock;

import deadlock.TransferMoney.Account;
import java.util.Random;

/**
 * 多人同时转账时发生死锁的情况
 */
public class MultiTransferMoney {

    // 定义常量 NUM_ACCOUNTS，它的值为 500（表示有 500 个账户）
    private static final int NUM_ACCOUNTS = 500;

    // 定义常量 NUM_MONEY，它的值为 1000（它用于表示账户余额）
    private static final int NUM_MONEY = 1000;

    // 定义常量 NUM_ITERATIONS，它的值为 1000000（它用于下面 run() 方法中的 for 循环，表示转账次数）
    private static final int NUM_ITERATIONS = 1000000;

    // 定义常量 NUM_THREADS，它的值为 20，用于表示线程数
    private static final int NUM_THREADS = 20;

    public static void main(String[] args) {
        // 定义一个 Random 类对象，用于创建随机数
        Random rnd = new Random();

        // 创建一个元素数据类型为 Account 类的数组（即账户数组），该数组总共有 500 个元素（因为 NUM_ACCOUNTS 的值为 500）（即表示有 500 个账户）
        Account[] accounts = new Account[NUM_ACCOUNTS];

        // 该 for 循环用于遍历，初始化上面创建的数组中的每一个元素（即 Account 类对象）
        for (int i = 0; i < accounts.length; i++) {
            // 创建 Account 类对象（即让当前数组中的元素指向该对象），设置它的余额为 NUM_MONEY 表示的 500
            accounts[i] = new Account(NUM_MONEY);
        }


        // 创建内部类 TransferThread，它继承 Thread 类
        // 注意
        // 此类是定义在 main() 方法中的
        class TransferThread extends Thread {

            @Override
            public void run() {
                for (int i = 0; i < NUM_ITERATIONS; i++) {
                    // 调用 Random 类中的 nextInt() 方法获取一个最大值不超过 NUM_ACCOUNTS 的随机数，用于从上面定义的账户中获取对应下标的元素作为付款账户
                    int fromAcct = rnd.nextInt(NUM_ACCOUNTS);
                    // 调用 Random 类中的 nextInt() 方法获取一个最大值不超过 NUM_ACCOUNTS 的随机数，用于从上面定义的账户中获取对应下标的元素作为收款账户
                    int toAcct = rnd.nextInt(NUM_ACCOUNTS);
                    // 调用 Random 类中的 nextInt() 方法获取一个最大值不超过 NUM_MONEY 的随机数，用于表示转账金额
                    int amount = rnd.nextInt(NUM_MONEY);

                    // 调用此类所在包下的 TransferMoney 类中的 transferMoney()，方法中传入通过上面随机数获取的 付款账户，收款账户，转账金额
                    TransferMoney.transferMoney(accounts[fromAcct], accounts[toAcct], amount);
                }

                System.out.println("运行结束");
            }
        } // 内部类 TransferThread 定义完毕

        // 此 for 循环用于创建常量 NUM_THREADS 所表示的数量（即 20）的上面定义的线程类 TransferThread 对象，并启动它们
        for (int i = 0; i < NUM_THREADS; i++) {
            new TransferThread().start();
        }
    }

    // 上面的代码运行结果是
    // 当程序运行一段时间之后，就会因为所有线程都因为发生死锁而导致整个程序卡死（即控制台不再打印任何东西）
    // 此时
    // 这些线程之间要么是发生了 两两之间的死锁，要么就是发生了多个线程之间的环路死锁
    // 会发生这种情况的原因是
    // transferMoney() 方法中，是用我们传入该方法中的付款账户和收款账户作为锁对象的
    // 而这些付款账户和收款账户是存放在数组 accounts 中供多个线程共用的
    // 所以，就会发生
    // 线程 A 是账户 1 转给账户 2，线程 B 是账户 2 转给账户 1，这种会发生死锁的情况
    // 或者
    // 线程 A 是账户 1 转给账户 2，线程 B 是账户 2 转给账户 3，线程 C 是账户 3 转给账户 1，这种会发生死锁的情况
}
