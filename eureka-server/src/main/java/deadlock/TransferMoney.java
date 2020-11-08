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
        // 如果上面上面定义的成员变量 flag 的值为 1，那么让账户 a 给账户 b 转账
        if (flag == 1) {
            // 调用下面定义的 transferMoney() 方法
            transferMoney(a, b, 200);
        }

        // 如果上面上面定义的成员变量 flag 的值为 1，那么让账户 a 给账户 b 转账
        if (flag == 0) {
            // 调用下面定义的 transferMoney() 方法
            transferMoney(b, a, 200);
        }
    }


    /**
     * 定义 transferMoney() 方法用于转账操作
     * @param from 此形参接收付款人的账户（即 Account 类对象）
     * @param to 此形参接收收到钱的人的账户（即 Account 类对象）
     * @param amount 此形参接收转账的金额
     */
    public static void transferMoney(Account from, Account to, int amount) {
        // 以下为版本一的代码
//        synchronized (from) {
//            try {
//                // 让当前线程（即调用此方法的线程）休眠 500 毫秒
//                // 这行代码其实是在模拟实际工作中，在这两个嵌套的同步代码块之间发生了一些比较耗时的操作（如请求接口，I/O 操作等）
//                Thread.sleep(500);
//            }
//            catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            synchronized (to) {
//                // 先判断传出人的账户中，当前的余额 - 要转出的钱 是否小于 0
//                if (from.balance - amount < 0) {
//                    System.out.println("余额不足，转账失败。");
//                }
//
//                // 付款账户的余额为：账户中的余额 - 转账的金额
//                from.balance -= amount;
//
//                // 收款账户的余额为：账户中的余额 + 转账的金额
//                to.balance = to.balance + amount;
//
//                System.out.println("成功转账" + amount + "元");
//            }
//        }
        // 版本一代码结束


        // 以下为版本二的代码
        // 定义内部类 Helper
        class Helper {

            // 定义 transfer() 方法
            public void transfer() {
                // 先判断传出人的账户中，当前的余额 - 要转出的钱 是否小于 0
                if (from.balance - amount < 0) {
                    System.out.println("余额不足，转账失败。");
                    return;
                }
                // 付款账户的余额为：账户中的余额 - 转账的金额
                from.balance -= amount;
                // 收款账户的余额为：账户中的余额 + 转账的金额
                to.balance = to.balance + amount;

                System.out.println("成功转账" + amount + "元");
            }
        } // 内部类 Helper 定义结束


        // 由于每一个对象都有唯一的 Hash 值
        // 所以
        // 这里通过调用 System 类中的 identityHashCode() 方法来分别获取我们传入此 transferMoney() 方法中的两个锁对象（即形参 from 和 to 接收的对象）的 Hash 值
        int fromHash = System.identityHashCode(from);
        int toHash = System.identityHashCode(to);



        // 根据形参 from 所接收的对象的 Hash 值 和 形参 to 所接收的对象的 Hash 值进行比较，执行不同的 synchronized 同步代码块
        // 此时
        // 即使 A 转账给 B，B 转账给 A，同时发生
        // 由于
        // 这个 if 语句中的形参 from 所接收的对象和下面一个 else if 语句中形参 to 所接收的对象其实是同一个 Account 类对象
        //（即要么同时是上面定义的 Account 类对象 a，要么同时是上面定义的 Account 类对象 b）
        // 因为
        // 假设对象 a 的 Hash 值大，对象 b 的 Hash 值小
        // 那么
        // 如果是 a 转账给 b 500 元，那么就是执行的 transferMoney(a,b,500)
        // 即
        // 由于形参 from 接收的对象 a 的 Hash 值 > 形参 to 接收的对象 b 的 Hash 值
        // 所以
        // 走的是下面 else if 中的语句，即外层 synchronized 同步代码块的锁对象 形参 to，就是对象 b
        // 此时
        // 如果同时发生 b 转账给 a 500 元，那么就是执行的 transferMoney(b,a,500)
        // 由于形参 from 接收的对象 b 的 Hash 值 < 形参 to 接收的对象 a 的 Hash 值
        // 所以
        // 走的是下面 if 中的语句，即外层 synchronized 同步代码块的锁对象 形参 to，就是对象 b
        // 这样
        // 一旦执行 a 转账给 b 500 元的线程 A 先进入同步代码块，从而获取到了对象 b 作为外层同步代码块的锁对象
        // 那么
        // 执行 b 转账给 a 500 元的线程 B 由于也需要获取对象 b 以便进入外层的同步代码块
        // 所以
        // 所以它只有等线程 A 把嵌套同步块中的代码都执行完，然后释放锁对象 b，才能执行自己的嵌套同步代码块
        // 这样
        // 就不会发生版本一代码中出现的死锁的情况了
        if (fromHash < toHash) {

            synchronized (from) {
                try {
                    // 让当前线程（即调用此方法的线程）休眠 500 毫秒
                    // 这行代码其实是在模拟实际工作中，在这两个嵌套的同步代码块之间发生了一些比较耗时的操作（如请求接口，I/O 操作等）
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (to) {
                    new Helper().transfer();
                }
            }
        }
        else if (fromHash > toHash) {

            synchronized (to) {
                try {
                    // 让当前线程（即调用此方法的线程）休眠 500 毫秒
                    // 这行代码其实是在模拟实际工作中，在这两个嵌套的同步代码块之间发生了一些比较耗时的操作（如请求接口，I/O 操作等）
                    Thread.sleep(500);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (from) {
                    new Helper().transfer();
                }
            }
        }
        // 如果两个 Account 类对象的哈希值相同，那么就在嵌套的 synchronized 同步代码块外面再包一层 synchronized 同步代码块
        // 保证只有一个线程能把嵌套的同步代码块都走完
        // 防止发生版本一代码中出现的死锁情况
        else {
            synchronized (lock) {
                synchronized (to) {
                    synchronized (from) {
                        new Helper().transfer();
                    }
                }
            }
        }
        // 版本二代码结束

    } // transferMoney() 方法定义完毕

    // 定义静态内部类 Account（它表示一个人的账户）
    static class Account {

        // 定义成员变量 balance，它表示账户余额
        int balance;

        // 定义构造方法
        public Account(int balance) {
            this.balance = balance;
        }

    }

    public static void main(String[] args) throws InterruptedException {
        // 创建当前类 TransferMoney 类对象 r1 和 r2
        TransferMoney r1 = new TransferMoney();
        TransferMoney r2 = new TransferMoney();



        // 创建线程对象 t1，t2 构造方法中分别传入上面创建的对象 r1，r2
        Thread t1 = new Thread(r1);
        Thread t2 = new Thread(r2);

        // 定义对象 r1 中的成员变量 flag 的值为 1
        // 此时
        // 对象 r1 所在线程就会走上面 run() 中 flag == 1 这个判断语句中的代码
        r1.flag = 1;

        // 定义对象 r1 中的成员变量 flag 的值为 2
        // 此时
        // 对象 r1 所在线程就会走上面 run() 中 flag == 1 这个判断语句中的代码
        r2.flag = 0;

        // 启动线程对象 t1，t2
        t1.start();
        t2.start();

        // 分别用线程对象 t1，t2 调用 Thread 类中的 join() 方法，以便让这两个线程都执行完毕后，再执行 main() 方法中剩余的代码
        t1.join();
        t2.join();

        // 分别输出账户对象 a，账户对象 b 中的余额
        System.out.println("a的余额" + a.balance);
        System.out.println("b的余额" + b.balance);
    }


    // 此时
    // 在执行版本 1 的代码时，就会发生死锁
    // 因为
    // 两个线程在运行时，分别调用的是两个不同形参的 transferMoney() 方法
    // 即
    // 对象 r1 所在线程调用的是 transferMoney(a, b, 200);，此时它在此方法中的外层同步代码的锁对象为对象 a
    // 对象 r2 所在线程调用的是 transferMoney(b, a, 200);，此时它在此方法中的外层同步代码的锁对象为对象 b
    // 由于
    // 对象 r1 所在线程在获取到锁对象 a 后，会陷入休眠
    // 所以
    // 在对象 r1 处于休眠的这段时间里，对象 r2 所在线程会获取到锁对象 b，然后它才会休眠
    // 此时
    // 当两个线程都被唤醒之后，并且都为了要进入内层同步代码块而准备获取锁对象时（对象 r1 是要获取锁对象 b，对象 r2 是要获取锁对象 a）
    // 却都发现对方线程都没有释放自己的锁
    // 从而
    // 这两个线程都因为无法获取它们所需要的锁而卡死在外层的同步代码块中
    // 这就造成了死锁

    // 其实
    // 如果我们把上面的 Thread.sleep(500); 这行代码注释掉的话，上面的代码几乎是可以正常运行的
    // 因为
    // 两个同步代码块之间没有任何代码，从而从外层同步代码进入到内层同步代码块几乎不会产生时间损耗
    // 因此
    // 一般在对象 r1 所在线程要获取锁对象 b 从而进入内层同步代码块时，对象 r2 所在线程可能还没获取到锁对象 b 从而进入到外层同步代码块这一步
    // 所以
    // 两个线程几乎不会因为抢夺锁而发生冲突，从而造成死锁
    // ( 但是这样其实还是有非常小的概率发生死锁的 )


    // 但是
    // 如果我们使用版本二的代码，那么就不会发生死锁
    // 因为
    // 我们通过判断 transferMoney() 方法中的两个锁对象（即 Account 类对象）的 Hash 值来对版本一中的嵌套同步代码块进行了细分
    // 从而保证了，在两个锁对象的 Hash 值不同的情况下
    // if 语句和 else if 语句中的两组嵌套同步代码的内外层锁对象都是同一个
    //（即要么外层锁对象都是对象 a，内层锁对象都是对象 b，那么外层锁对象都是对象 b，内层锁对象都是对象 a）
    // 从而避免了版本一中，由于两个线程同时要获取对方的锁对象，但又不释放自己占有的锁对象而导致的死锁
    //（具体见上面版本二中代码的注释）
}
