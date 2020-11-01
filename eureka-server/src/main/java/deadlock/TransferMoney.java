package deadlock;

/**
 * 描述：     转账时候遇到死锁，一旦打开注释，便会发生死锁
 */
public class TransferMoney implements Runnable {

    // 定义一个标识符 flag
    int flag = 1;

    // 创建账户类对象，构造方法中传入 500，表示当前 a 和 b 的账户中的余额都是 500
    static Account a = new Account(500);
    static Account b = new Account(500);

    // 创建 Object 类对象，它的作用仅仅是作为锁对象
    static Object lock = new Object();


    @Override
    public void run() {
        if (flag == 1) {
            transferMoney(a, b, 200);
        }
        if (flag == 0) {
            transferMoney(b, a, 200);
        }
    }


    /**
     * 定义 transferMoney() 方法用于转账操作
     * @param from 此形参接收转出人的账户
     * @param to 此形参接收收到钱的人的账户
     * @param amount 此形参接收转账的金额
     */
    public static void transferMoney(Account from, Account to, int amount) {
        // 以下为版本一的代码
        synchronized (from) {
            synchronized (to) {
                // 先判断传出人的账户中，当前的余额 - 要转出的钱 是否小于 0
                if (from.balance - amount < 0) {
                    System.out.println("余额不足，转账失败。");
                }

                from.balance -= amount;
                to.balance = to.balance + amount;
                System.out.println("成功转账" + amount + "元");
            }
        }


//        class Helper {
//
//            public void transfer() {
//                if (from.balance - amount < 0) {
//                    System.out.println("余额不足，转账失败。");
//                    return;
//                }
//                from.balance -= amount;
//                to.balance = to.balance + amount;
//                System.out.println("成功转账" + amount + "元");
//            }
//        }
//
//        int fromHash = System.identityHashCode(from);
//        int toHash = System.identityHashCode(to);
//        if (fromHash < toHash) {
//            synchronized (from) {
//                synchronized (to) {
//                    new Helper().transfer();
//                }
//            }
//        }
//        else if (fromHash > toHash) {
//            synchronized (to) {
//                synchronized (from) {
//                    new Helper().transfer();
//                }
//            }
//        }else  {
//            synchronized (lock) {
//                synchronized (to) {
//                    synchronized (from) {
//                        new Helper().transfer();
//                    }
//                }
//            }
//        }

    }

    public static void main(String[] args) throws InterruptedException {
        // 创建当前类 TransferMoney 类对象 r1 和 r2
        TransferMoney r1 = new TransferMoney();
        TransferMoney r2 = new TransferMoney();

        // 定义对象 r1 中的成员变量 flag 的值为 1
        r1.flag = 1;

        // 定义对象 r1 中的成员变量 flag 的值为 2
        r2.flag = 0;


        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("a的余额" + a.balance);
        System.out.println("b的余额" + b.balance);
    }


    // 定义静态内部类 Account（它表示一个人的账户）
    static class Account {

        // 定义成员变量 balance，它表示账户余额
        int balance;

        // 定义构造方法
        public Account(int balance) {
            this.balance = balance;
        }

    }
}
