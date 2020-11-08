package deadlock;


/**
 * 描述：     演示哲学家就餐问题导致的死锁
 */
public class DiningPhilosophers {
    // 定义静态内部类 Philosopher，它实现了 Runnable 接口
    public static class Philosopher implements Runnable {
        // 定义一个 Object 类对象 leftChopstick，表示左餐叉（它其实也用于下面 run() 方法中的锁对象）
        private Object leftChopstick;
        // 定义一个 Object 类对象 leftChopstick，表示右餐叉（它其实也用于下面 run() 方法中的锁对象）
        private Object rightChopstick;

        // 构造方法
        public Philosopher(Object leftChopstick, Object rightChopstick) {
            this.leftChopstick = leftChopstick;
            this.rightChopstick = rightChopstick;
        }


        @Override
        public void run() {
            try {
                while (true) {
                    // 调用 doAction() 方法，传入字符串 Thinking（表示思考）
                    doAction("Thinking");

                    // 定义嵌套的同步代码块
                    synchronized (leftChopstick) {
                        // 调用 doAction() 方法，传入字符串 Picked up left chopstick（表示拿起左边的餐叉）
                        doAction("Picked up left chopstick");

                        synchronized (rightChopstick) {
                            // 调用 doAction() 方法，传入字符串 Picked up right chopstick - eating（表示拿起右边的餐叉，然后吃东西）
                            doAction("Picked up right chopstick - eating");

                            // 调用 doAction() 方法，传入字符串 Put down right chopstick（表示放下右边的餐叉）
                            doAction("Put down right chopstick");
                        }

                        // 调用 doAction() 方法，传入字符串 Put down left chopstick（表示放下左边的餐叉）
                        doAction("Put down left chopstick");
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // 定义 doAction() 方法
        private void doAction(String action) throws InterruptedException {
            // 输出调用此方法的线程名字，以及形参 action 接收到的字符串
            System.out.println(Thread.currentThread().getName() + " " + action);

            // 让调用此方法的线程随机睡眠一段时间（用随机数是为了表示哲学家们各自思考和吃东西的时间各不相同）
            Thread.sleep((long) (Math.random() * 10));
        }
    } // 内部类 Philosopher 定义结束

    public static void main(String[] args) {
        // 定义一个元素的数据类型为 Philosopher 类的数组
        // 该数组的长度为 5（即表示有 5 个哲学家）
        Philosopher[] philosophers = new Philosopher[5];

        // 定义一个元素的数据类型为 Object 的数组（用于存放餐叉），该数组的长度也是 5（即 5 个餐叉）
        Object[] chopsticks = new Object[philosophers.length];

        // 遍历数组 chopsticks，来创建 Object 类对象
        for (int i = 0; i < chopsticks.length; i++) {
            chopsticks[i] = new Object();
        }

        // 遍历数组 philosophers，如果把 5 个哲学家定义为 1 ~ 5 号， 5 个餐叉也是 1 ~ 5 号
        // 那么
        // 这个 for 循环中我们就要确定每个哲学家左右两边分别是哪几号餐叉
        for (int i = 0; i < philosophers.length; i++) {
            // 从数组 chopsticks 中获取当前 i 所表示的下标元素作为左餐叉
            // 因为
            // 每位哲学家左手边的餐叉编号肯定是和自己的编号一样的（可以参看笔记中的图）
            Object leftChopstick = chopsticks[i];

            // 从数组 chopsticks 中获取 (i + 1) % chopsticks.length 计算得出的数所表示的下标元素作为右餐叉
            Object rightChopstick = chopsticks[(i + 1) % chopsticks.length];

            // 版本一
            philosophers[i] = new Philosopher(rightChopstick, leftChopstick);
            // 版本一结束

            // 版本二
            // 即
            // 挑其中一个哲学家，让他先拿右边的餐叉，再拿左边的餐叉吃饭（即构造方法中先传入 rightChopstick，再传入 leftChopstick）
            if (i == philosophers.length - 1) { // 这里 if 的判断条件是这样定义的，但是其实我们也可以定义为其他的，只要能挑出一个哲学家即可
                philosophers[i] = new Philosopher(rightChopstick, leftChopstick);
            }
            else {
                // 其他的哲学家还是像原来那样，先拿左边的餐叉，再拿右边的餐叉吃饭（即构造方法中先传入 leftChopstick，再传入 rightChopstick）
                philosophers[i] = new Philosopher(leftChopstick, rightChopstick);
            }

            // 创建 Thread 类对象，传入当前下标的 Philosopher 类对象，并为该线程起名（即构造方法中传入的第二个形参），并启动该线程
            new Thread(philosophers[i], "哲学家" + (i + 1) + "号").start();
        }
    }

    // 如果我们运行 main() 方法中的版本一的代码
    // 那么
    // 程序必然会在某一时刻发生死锁（即笔记中的情况一）

    // 如果我们运行 main() 方法中的版本二的代码
    // 即采用换手策略
    // 那么
    // 我们就能避免死锁的发生
}
