package threadcoreknowledge.stopthreads.volatiledemo;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 当线程陷入阻塞时，volatile 关键字所修饰的标记位是无法停止该线程的
 * 生产者的生产速度很快，消费者消费速度慢，所以阻塞队列满了以后，生产者会阻塞，等待消费者进一步消费
 */
public class WrongWayVolatileCantStop {

    public static void main(String[] args) throws InterruptedException {
        // 创建一个 ArrayBlockingQueue 类（即阻塞队列类）对象
        // ( 阻塞队列的特点就是当该队列满了或空了之后，都会让使用此队列的线程进入阻塞状态 )
        ArrayBlockingQueue storage = new ArrayBlockingQueue(10);

        // 创建 Producer 类对象，构造方法中传入阻塞队列对象 storage
        Producer producer = new Producer(storage);

        // 创建线程对象，构造方法中传入上面创建的生产者类对象 producer
        Thread producerThread = new Thread(producer);

        // 启动上面创建的线程对象 producerThread
        producerThread.start();

        // 让当前 main() 方法所在线程睡眠 1 秒
        // 此时
        // 生产者类对象 producer，就会不停的往阻塞队列中存放元素，直到队列满了，阻塞队列让当前这个 生产者类对象 producer 所在线程进入阻塞状态
        Thread.sleep(1000);

        // 创建 Consumer 类对象，构造方法中传入阻塞队列对象 storage
        Consumer consumer = new Consumer(storage);

        // 在 while 循环判断语句中调用 Consumer 类中的 needMoreNums() 方法，随机返回 true 或者 false
        while (consumer.needMoreNums()) {

            // 调用对象 consumer 中的阻塞队列对象 storage 的 take() 方法，从队列中取出元素
            System.out.println(consumer.storage.take()+"被消费了");

            // 让当前 main() 方法所在线程睡眠 0.1 秒
            Thread.sleep(100);
        }


        System.out.println("消费者不需要更多数据了。");

        // 一旦消费不需要更多数据了，我们应该让生产者也停下来（即 把生产者类对象中的用 volatile 关键字修饰的成员变量 canceled 设置为 true）
        producer.canceled=true;

        // 输出对象 producer 中成员变量 canceled 的值
        System.out.println(producer.canceled);
    }

    // 上面代码的执行结果是
    // 在对象 producer 中成员变量 canceled 的值被设置为 true 之后，对象 producer 所在的线程并没有被停止（即下面控制台左边表示 stop 的红色方框还是亮着的）
    // 显然
    // 这种使用 volatile 修饰一个布尔类型的成员变量作为标记位的方式，无法在当前情况下让线程停止
    // 这是因为
    // 一旦线程陷入阻塞状态（如下面当阻塞队列中存放满元素之后，Producer 类对象所在的线程就会陷入阻塞状态）
    // 那么
    // 如果我们没有调用 invoke() 方法，把线程唤醒，那么该线程就会一直陷入阻塞状态
    // 即
    // 此时，当前 Producer 类对象所在线程就会一直卡在 storage.put(num); 这行代码这里，而不会执行下面的 System.out.println(num + "是100的倍数,被放到仓库中了。"); 这行代码
    // 这样一来
    // 当前这次循环也就无法走完
    // 那么
    // while 循环中的判断语句 num <= 100000 && !canceled 也就无法执行
    // 所以
    // 即使我们把成员变量 canceled 设置为 true，也无法跳出当前循环，从而 run() 方法中的代码也就无法执行完成，从而当前线程就一直处于阻塞状态
    // 这也就是为什么此时使用 canceled 这个标记位无法让当前线程停止的原因
}

// 此类为生产者类，它实现了 Runnable 接口
class Producer implements Runnable {

    // 使用 volatile 修饰一个布尔类型的成员变量 canceled 作为标记位
    public volatile boolean canceled = false;

    // 设置数据类型为阻塞队列的成员变量 storage
    BlockingQueue storage;


    public Producer(BlockingQueue storage) {
        this.storage = storage;
    }


    @Override
    public void run() {
        int num = 0;

        // 下面这段代码用于打印 100 的倍数
        // 并且
        // 这里判断上面设置的标记位（即 成员变量 cancel）是否不为 true
        // 如果不为 true （即表示该线程没有被停止）就执行该 while 循环中的语句
        try {
            while (num <= 100000 && !canceled) {
                if (num % 100 == 0) {

                    // 把 100 的倍数放到上面创建的阻塞队列中
                    storage.put(num);


                    System.out.println(num + "是100的倍数,被放到仓库中了。");
                }
                num++;
            }
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            System.out.println("生产者结束运行");
        }
    }
}


// 此类为消费者类
class Consumer {

    // 设置数据类型为阻塞队列的成员变量 storage
    BlockingQueue storage;

    public Consumer(BlockingQueue storage) {
        this.storage = storage;
    }


    // 此 needMoreNums() 方法用于判断随机数是否大于 0.95
    // 如果
    // 大于 0.95，当前 needMoreNums() 方法就返回 false
    // 否则
    // 就返回 true
    public boolean needMoreNums() {
        if (Math.random() > 0.95) {
            return false;
        }
        return true;
    }
}