package deadlock;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 描述：     用tryLock来避免死锁
 */
public class TryLockDeadlock implements Runnable {

    // 定义成员变量 flag 用作标识符
    int flag = 1;

    // 创建两个静态的数据类型为 ReentrantLock （可重入锁）类的成员变量
    static Lock lock1 = new ReentrantLock();
    static Lock lock2 = new ReentrantLock();

    @Override
    public void run() {
        // 定义一个 for 循环，循环 100 次
        for (int i = 0; i < 100; i++) {

            if (flag == 1) {
                try {
                    // 通过对象 lock1 调用 ReentrantLock 类中的 tryLock() 方法，尝试锁住下面一直到 lock1.unlock() 之前的一段代码
                    // 其实
                    // tryLock() 方法的功能基本和 Lock 类中的 lock() 方法相同，只不过 tryLock() 可以设置过期时间
                    // 即
                    // 如果此时线程 B 已经通过对象 lock1 调用 ReentrantLock 类中的 tryLock() 方法锁住了这段代码
                    // 那么
                    // 此时线程 A 是无法再次锁住该段代码的
                    // 但是
                    // 线程 A 会在我们调用 tryLock() 方法时传入此方法的过期时间内（如此时的 800 毫秒），陷入阻塞状态，并等待线程 B 释放该锁，以便自己能锁住该段代码
                    // 不过
                    // 一旦超过了该过期时间
                    // 那么线程 A 就会被唤醒，以便执行那段需要被锁住的代码之后的代码
                    // 注意
                    // 如果当前线程获取锁成功，那么 tryLock() 方法会返回 true
                    // 如果当前线程因为达到了过期时间，从而被从阻塞状态中唤醒，那么 tryLock() 方法会返回 false
                    // 如果当前线程因为要等待其他线程释放锁而陷入阻塞状态，那么此时当前线程就是卡在 tryLock() 方法这里的，不会执行其他代码（因为是陷入阻塞状态的嘛）
                    if (lock1.tryLock(800, TimeUnit.MILLISECONDS)) {

                        System.out.println("线程1获取到了锁1");

                        // 让当前线程睡眠一个 0~1000 之间的一个随机时间
                        //（这样是为了模拟多个线程在不同时间并发执行这段代码的情况，如果不让当前线程睡眠一段时间，那么会产生多个线程基本都在同一时间执行这段代码的情况）
                        Thread.sleep(new Random().nextInt(1000));

                        // 通过对象 lock2 调用 ReentrantLock 类中的 tryLock() 方法，尝试锁住下面一直到 lock2.unlock() 之前的一段代码
                        // 此时
                        // 如果获取锁成功
                        // 那么
                        // 当前线程就拥有了两把锁
                        // 即
                        // 外层通过对象 lock1 调用 tryLock() 方法获取的锁，以及内层通过对象 lock2 调用 tryLock() 方法获取的锁
                        if (lock2.tryLock(800, TimeUnit.MILLISECONDS)) {

                            System.out.println("线程1获取到了锁2");
                            System.out.println("线程1成功获取到了两把锁");

                            // 释放两把锁
                            // 注意
                            // 一定要先释放内层的锁，再释放外层的锁！
                            lock2.unlock();
                            lock1.unlock();

                            break;
                        }
                        else {
                            // 如果内层通过对象 lock2 调用 ReentrantLock 类中的 tryLock() 方法获取锁失败
                            // 那么就会这里面的代码
                            System.out.println("线程1尝试获取锁2失败，已重试");

                            // 通过对象 lock1 调用 unlock() 方法，释放外层的锁
                            // 以便其他线程在执行下面的 if (flag == 0) 代码块中的语句时，能够顺利地通过对象 lock1 获取内层的锁
                            lock1.unlock();

                            // 让当前线程睡眠一个 0~1000 之间的一个随机时间
                            //（这样是为了模拟多个线程在不同时间并发执行这段代码的情况，如果不让当前线程睡眠一段时间，那么会产生多个线程基本都在同一时间执行这段代码的情况）
                            Thread.sleep(new Random().nextInt(1000));
                        }
                    }
                    else {
                        // 如果外层通过对象 lock1 调用 ReentrantLock 类中的 tryLock() 方法获取锁失败
                        // 那么就会这里面的代码
                        System.out.println("线程1获取锁1失败，已重试");
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // 下面这段代码和上面 if (flag == 1) 中的代码基本一样，只不过获取锁的顺序不一样了
            // 即
            // 这里是外层使用对象 lock2 调用 tryLock() 方法获取锁，内层是用对象 lock1 调用 tryLock() 方法获取锁
            // 并且
            // 这段代码中，传入 tryLock() 方法中的过期时间是 3000，而上面那段代码是 800
            if (flag == 0) {
                try {
                    if (lock2.tryLock(3000, TimeUnit.MILLISECONDS)) {
                        System.out.println("线程2获取到了锁2");

                        Thread.sleep(new Random().nextInt(1000));
                        if (lock1.tryLock(3000, TimeUnit.MILLISECONDS)) {
                            System.out.println("线程2获取到了锁1");
                            System.out.println("线程2成功获取到了两把锁");
                            lock1.unlock();
                            lock2.unlock();
                            break;
                        }
                        else {
                            System.out.println("线程2尝试获取锁1失败，已重试");

                            // 以便其他线程在执行上面的 if (flag == 1) 代码块中的语句时，能够顺利地通过对象 lock2 获取内层的锁
                            lock2.unlock();

                            Thread.sleep(new Random().nextInt(1000));
                        }
                    }
                    else {
                        System.out.println("线程2获取锁2失败，已重试");
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        // 创建当前 TryLockDeadlock 类的对象
        TryLockDeadlock r1 = new TryLockDeadlock();
        TryLockDeadlock r2 = new TryLockDeadlock();

        // 分别为对象 r1 和 r2 的 flag 属性设置不同的值
        r1.flag = 1;
        r2.flag = 0;

        // 创建两个线程对象，并启动（构造方法中传入上面创建的两个 TryLockDeadlock 类对象）
        new Thread(r1).start();
        new Thread(r2).start();
    }

    // 此时
    // 上面代码在执行时，是不会发生死锁的
    // 因为
    // 如果对象 r1 所在线程在执行 if (flag == 1) 代码块中的代码时，通过对象 lock1 获取到了锁
    // 并且
    // 对象 r2 所在线程在执行 if (flag == 0) 代码块中的代码时，通过对象 lock2 获取到了锁
    // 此时
    // 对象 r1 所在线程想要进一步在内层通过对象 lock2 获取锁时，发现此时对象 r2 所在线程已经通过对象 lock2 获取到了锁
    // 并且
    // 此时对象 r2 所在线程想要进一步在内层通过对象 lock1 获取锁时，发现此时对象 r1 所在线程已经通过对象 lock1 获取到了锁
    // 那么
    // 按照之前的代码，此时肯定会发生死锁
    // 但是
    // 由于我们在这里为每个线程都设置了超时时间（即往 tryLock() 中传入的第一个参数）
    // 因此
    // 一旦其中一个线程到了超时时间，那么它就会被从阻塞状态中唤醒，然后执行 else 代码块中的代码，释放当前它所拥有的锁，以便另一个线程能够获取到该把锁
    // 所以
    // 上面的代码是不会出现由于两个线程同时占有对方想要获取的锁而陷入死锁的情况的
    // 这也就是通过设置超时时间避免死锁的手段
}
