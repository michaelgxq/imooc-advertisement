package threadcoreknowledge.synchronizedmethod;

/**
 * @ClassName: DeadLock
 * @Description: TODO
 * @author: yourName
 * @date: 2020年10月24日 13:54
 */
public class DeadLock implements Runnable {
    static DeadLock deadLock1 = new DeadLock();

    static DeadLock deadLock2 = new DeadLock();


    @Override
    public void run() {
        // 同步代码块 1
        synchronized (deadLock1) {
            System.out.println("同步代码块 1 的外层同步代码块");

            synchronized (deadLock2) {
                System.out.println("同步代码块 1 的内层同步代码块One");
            }
        }

        // 同步代码块 2
        synchronized (deadLock2) {
            System.out.println("同步代码块 2 的外层同步代码块One");

            synchronized (deadLock1) {
                System.out.println("同步代码块 2 的内层同步代码块One");
            }
        }

        // 上面的这段代码会形成死锁
        // 即
        // 当线程 A 进入了同步代码块 1
        // 此时
        // 线程 A 获取到了 deadLock1 这个锁对象
        // 与此同时
        // 线程 B 进入了同步代码块 2，线程 B 获取了 deadLock2 这个锁对象
        // 当线程 A 执行完了 System.out.println("同步代码块 1 的外层同步代码块"); 这条语句后
        // 为了进入它内层的同步代码块而要获取 deadLock2 这个锁对象时
        // 与此同时
        // 线程 B 同样在执行完了 System.out.println("同步代码块 2 的外层同步代码块One"); 这条语句后
        // 为了进入它内层的同步代码块而要获取 deadLock1 这个锁对象时
        // 此时
        // 就形成了死锁
        // 因为
        // 线程 A 和 线程 B 各自都占着自己的锁对象不释放（因为各自的同步代码块没有执行完）
        // 同时又想获取到对方的锁对象（因为都要执行各自同步代码块中内部的同步代码块）
        // 这显然就陷入了死循环
    }
}
