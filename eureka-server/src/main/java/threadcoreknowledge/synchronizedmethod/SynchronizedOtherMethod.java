package threadcoreknowledge.synchronizedmethod;

/**
 * @ClassName: SynchronizedOtherMethod
 * @Description: TODO
 * @author: yourName
 * @date: 2020年10月18日 22:09
 */
public class SynchronizedOtherMethod {
    /**
     * 定义同步方法 method1()
     */
    public synchronized void method1() {
        System.out.println("我是 method1");

        // 调用下面定义的同步方法 method2()
        method2();
    }

    /**
     * 定义同步方法 method2()
     */
    public synchronized void method2() {
        System.out.println("我是 method2");
    }

    public static void main(String[] args) {
        SynchronizedOtherMethod synchronizedOtherMethod = new SynchronizedOtherMethod();

        synchronizedOtherMethod.method1();
    }

    // 此时
    // 上面代码的执行结果是
    // 线程可以在同步方法 method1() 中正常调用另一个同步方法 method2() 而无需重新获取锁
    // 因此
    // synchronized 关键字的锁对于同一个类中的不同方法是可重入的
}
