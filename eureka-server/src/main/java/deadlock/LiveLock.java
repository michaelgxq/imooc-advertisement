package deadlock;

import java.util.Random;

/**
 * 描述：     演示活锁问题
 */
public class LiveLock {

    // 定义静态内部类 Spoon 表示勺子
    static class Spoon {
        // 定义一个数据类型为下面定义的 Diner 类的成员变量 owner，用于表示勺子（即这个 Spoon 类对象）的拥有者
        private Diner owner;

        // 构造方法
        public Spoon(Diner owner) {
            this.owner = owner;
        }

        // get，set 方法
        public Diner getOwner() {
            return owner;
        }

        public void setOwner(Diner owner) {
            this.owner = owner;
        }

        // 定义同步方法 use()，表示当前这个勺子拥有者吃饭
        public synchronized void use() {
            // 该输出语句表示当前这个勺子的拥有者吃完了
            System.out.printf("%s吃完了!", owner.name);
        }
    }

    // 定义静态内部类 Diner，表示晚餐
    static class Diner {
        // 定义成员变量 name，表示当前用餐人（即这个 Diner 类对象）的名字
        private String name;

        // 定义成员变量 isHungry，表示是否饿了
        private boolean isHungry;

        // 构造方法
        public Diner(String name) {
            this.name = name;
            isHungry = true;
        }

        // get 方法
        public String getName() {
            return name;
        }

        // 定义 eatWith() 方法，表示和某人吃饭（spouse 为配偶的意思）
        // 由于勺子（即形参 spoon 所接收的 Spoon 类对象）只有一把，所以一次只能有一个人吃
        public void eatWith(Spoon spoon, Diner spouse) {

            // 判断当前就餐者（即当前 Diner 类对象）饿不饿
            while (isHungry) {
                // 判断当前勺子的拥有者是否是当前就餐的人（这个 this 就是当前 Diner 类对象）
                // 如果不是就让当前调用此 eatWith() 方法的线程休眠 1 毫秒，并且跳过该 while 循环之后的所有代码
                if (spoon.owner != this) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    continue;
                }

                // 版本一
                // 判断形参 spouse 接收的 Diner 类对象的成员变量 isHungry 是否为 true（即配偶是不是饿了）
//                if (spouse.isHungry) {
//
//                    // 如果配偶饿了，就把勺子给他
//                    // 即
//                    // 把形参 spoon 接收的 Spoon 类对象的成员变量 owner 设置为形参 spouse 所接收的 Diner 类对象
//                    spoon.setOwner(spouse);
//
//                    System.out.println(name + ": 亲爱的" + spouse.getName() + "你先吃吧");
//
//                }
//                else {
//
//                    // 如果 isHungry 是 false（即配偶不饿）
//                    // 那么就通过对象 spoon 调用 use() 方法（表示当前勺子的拥有者自己先吃饭）
//                    spoon.use();
//
//                    // 此输出语句表示当前勺子的拥有者自己已经吃完饭
//                    System.out.println(name + ": 我吃完了");
//
//                    // 设置成员变量 isHungry 为 false（即当前勺子的拥有者已经不饿了）
//                    isHungry = false;
//
//                    // 把形参 spoon 接收的 Spoon 类对象的成员变量 owner 设置为形参 spouse 所接收的 Diner 类对象（即把勺子给配偶）
//                    spoon.setOwner(spouse);
//                }


                // 版本二
                // 创建一个 Random 类对象
                Random random = new Random();

                // 判断形参 spouse 接收的 Diner 类对象的成员变量 isHungry 是否为 true（即配偶是不是饿了）
                // 并且
                // 通过对象 random 调用 nextInt() 方法获取的 0~10 之间的随机数小于 9 的话，便执行下面代码块中的内容
                if (spouse.isHungry && random.nextInt(10) < 9) {
                    // 如果配偶饿了，就把勺子给他
                    // 即
                    // 把形参 spoon 接收的 Spoon 类对象的成员变量 owner 设置为形参 spouse 所接收的 Diner 类对象
                    spoon.setOwner(spouse);

                    System.out.println(name + ": 亲爱的" + spouse.name + "你先吃吧");
                }
                else {
                    //如果 isHungry 是 false（即配偶不饿）
                    // 那么就通过对象 spoon 调用 use() 方法（表示当前勺子的拥有者自己先吃饭）
                    spoon.use();

                    // 此输出语句表示当前勺子的拥有者自己已经吃完饭
                    System.out.println(name + ": 我吃完了");

                    // 设置成员变量 isHungry 为 false（即当前勺子的拥有者已经不饿了）
                    isHungry = false;

                    // 把形参 spoon 接收的 Spoon 类对象的成员变量 owner 设置为形参 spouse 所接收的 Diner 类对象（即把勺子给配偶）
                    spoon.setOwner(spouse);
                }

            }
        }
    }


    public static void main(String[] args) {
        // 创建两个 Diner 类对象
        Diner husband = new Diner("牛郎");
        Diner wife = new Diner("织女");

        // 创建 Spoon 类对象，构造方法中传入上面定义的 Diner 类对象 husband（即当前勺子的拥有者是 husband）
        Spoon spoon = new Spoon(husband);

        // 创建两个线程对象并启动
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 通过对象 husband 调用 eatWith() 方法，表示是丈夫和妻子吃饭
                husband.eatWith(spoon, wife);
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 通过对象 wife 调用 eatWith() 方法，表示是妻子和丈夫吃饭
                wife.eatWith(spoon, husband);
            }
        }).start();
    }

    // 此时
    // 上面版本一的代码的执行结果是
    // 由于对象 husband 和 wife 在初始化的时候成员变量 isHungry 都是 true
    // 所以
    // 它们都会执行 eatWith() 方法中 if (spouse.isHungry) 判断为 true 的那部分代码
    // 即
    // 他们都会不停的把勺子给对方，如此循环往复，从而导致两个人都没办法吃饭
    // 显然
    // 这就是一种死循环
    // 而这就是活锁的一种体现

    // 为了解决这个活锁的问题
    // 我们可以设置一个随机变量
    // 即如上面版本二中的代码
    // 此时
    // 只要有一个线程在执行 random.nextInt(10) 时，获取的随机数等于 9
    // 那么
    // 它就能进入 else 代码块中，执行代码
    // 从而跳出了上面版本一中出现的循环往复的死循环
}
