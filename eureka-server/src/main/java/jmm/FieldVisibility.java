package jmm;

/**
 * 描述：     演示可见性带来的问题
 */
public class FieldVisibility {


//    volatile int a = 1;
//    volatile int b = 2;

    // 定义成员变量 a, b
    int a = 1;
    int b = 2;

    /**
     * 定义 change() 方法
     * 此方法用于给上面的成员变量重新赋值
     */
    private void change() {
        a = 3;
        b = a;
    }

    /**
     * 定义 print() 方法
     * 此方法用于打印成员变量 a，b
     */
    private void print() {
        System.out.println("a=" + a + ";b=" + b);
    }


    public static void main(String[] args) {
        // 定义一个死循环用于不停的执行下面的代码
        while (true) {
            // 创建当前类 FieldVisibility 类对象
            // 此时
            // 该对象 test 的成员变量 a，b 都是初始值（即 a 为 1，b 为 2）
            FieldVisibility test = new FieldVisibility();

            // 创建一个新线程，并启动
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {

                        // 让当前这个线程对象所在线程休眠 1 毫秒（其实没什么含义，就是让当前线程执行的慢一点）
                        Thread.sleep(1);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // 调用上面定义的 change() 方法
                    test.change();
                }
            }).start();

            // 创建一个新线程，并启动
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 让当前这个线程对象所在线程休眠 1 毫秒（其实没什么含义，就是让当前线程执行的慢一点）
                        Thread.sleep(1);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // 调用上面定义的 print() 方法
                    test.print();
                }
            }).start();
        }

    }


    // 此时
    // 上面的代码执行结果为
    // 上面第二个线程在执行它 run() 方法里的 test.print(); 这行代码时
    // 除了会输出常规的结果
    // 即
    // a = 3,b = 2
    // 或者
    // a = 1,b = 2
    // 或者
    // a = 3,b = 3
    // 之外
    // 还会出现一种很特殊的情况
    // 即
    // a = 1,b = 3
    // 出现这个输出结果的原因就是
    // 由于每个线程在工作的时候，都是先把自己的用到的 "多线程间共享的数据" 存放到自己独有的本地缓存中
    // 然后
    // 再把这些数据存放到主内存中，以供其他线程使用
    // 如果
    // 当上面第一个创建的线程对象所在线程在执行 test.change(); 方法时
    // 如果在执行了该方法中的  a = 3;  b = a; 这两行语句之后
    // 仅仅是把此时改变后的变量 b 的值（即此时 b 的值为 3）从自己的本地内存写入到主内存中
    // 然后
    // 还没来得及把改变后的变量 a 的值（即此时 a 的值也为 3）从自己的本地内存写入到主内存中，CPU 就切换到上面第二个创建的线程对象所在线程去执行它的 run() 方法了
    // 那么
    // 上面第二个创建的线程对象所在线程在执行它 run() 方法中的 test.print(); 这行代码时
    // 它所能从主内存获取到的 b 的值是第一个创建的线程对象所在线程修改后的新值
    // 但是
    // 它所能获取的变量 a 的值，仍然是初始值 1（因为第一个创建的线程对象所在线程还没把新的值写入到主内存中）
    // 这就出现了上面说的那种特殊的输出结果了
    // 这就是可见性问题的体现（即某个线程修改的共享变量的新值无法被另一个线程看见）

    // 对于这种情况
    // 我们可以使用 volatile 关键字来解决

    // 其实
    // 这里我们只需要给变量 b 加 volatile 关键字即可
    // 因为
    // 根据可见性问题中的 happens-before 原则
    // 当 JVM 在某个线程执行到 b = a; 这行代码时，发现变量 b 是 volatile 关键字修饰的（volatile 关键字是 happens-before 原则的手段之一）
    // 那么
    // JVM 就认为 b = a; 这行代码之前的所有操作结果都是要对其他线程可见
    // 所以
    // JVM 就会自动把当前线程的工作内存中的数据刷入主内存中（即此时 a = 3; 这个赋值结果会被刷入到主内存中）
    // 并且
    // 由于变量 b 是 volatile 关键字修饰的
    // 而 volatile 关键字另一个功能就是，如果它所修饰的变量的值发生了修改，那么就会把新值马上刷入主内存中
    // 所以
    // 变量 b 的新值也是对其他线程可见的
    // 这样一来
    // 其他线程就会读取到变量 a 的值为 3
    // 这也就不会出现上面那种特殊的输出结果了

    // 其实
    // 此时，变量 b 的角色就像是一个触发器，它触发变量 a 的值被刷入主内存中，供其他线程读取
}
