package threadcoreknowledge.threadobjectclasscommonmethods;

/**
 * 描述：     展示wait和notify的基本用法 1. 研究代码执行顺序 2. 证明wait释放锁
 */
public class Wait {
    // 定义一个 Object 类对象作为成员变量
    public static Object object = new Object();

    // 定义静态内部类 Thread1
    static class Thread1 extends Thread {

        @Override
        public void run() {
            // 定义同步代码块，锁对象为上面定义的成员变量 object
            synchronized (object) {
                System.out.println(Thread.currentThread().getName() + "开始执行了");
                try {
                    // 调用 Object 对象中的 wait() 方法，使得调用此方法的线程陷入阻塞状态
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("线程" + Thread.currentThread().getName() + "获取到了锁。");
            }
        }
    }

    // 定义静态内部类 Thread2
    static class Thread2 extends Thread {

        @Override
        public void run() {
            // 定义同步代码块，锁对象为上面定义的成员变量 object
            synchronized (object) {
                object.notify();

                // 注意
                // 当前线程在调用了上面的 notify() 方法之后，会把剩余的代码执行完（即执行下面的输出语句）
                // 然后
                // 在退出同步代码块之后再释放它所占用的锁
                // 此时
                // 那个被上面的 notify() 方法唤醒的线程才能获取到锁，并执行它自己同步代码块或同步方法中剩余的代码（即 wait() 方法之后的代码）
                System.out.println("线程" + Thread.currentThread().getName() + "调用了notify()");
            }
        }
    }


    public static void main(String[] args) throws InterruptedException {

        Thread1 thread1 = new Thread1();
        Thread2 thread2 = new Thread2();

        thread1.start();

        Thread.sleep(200);

        thread2.start();
    }

    // 上面的代码执行结果为
    // 当 Thread1 类对象所在线程执行了 wait() 方法后，它所占用的同步锁就被释放了，并且该线程陷入了阻塞状态
    // 此时
    // 和 Thread1 类对象所在线程使用同一个锁对象的 Thread2 类对象所在线程就获得了该锁，并开始执行它的 run() 方法中的代码
    // 当它执行了 notify() 方法之后
    // Thread1 类对象所在线程就被唤醒（此时因为就只有两个线程，所以一个调用了 notify() 方法，那么另一个肯定会被唤醒）
    // 因此
    // Thread1 类对象所在线程就会继续执行 object.wait(); 这行代码之后的代码了

}
