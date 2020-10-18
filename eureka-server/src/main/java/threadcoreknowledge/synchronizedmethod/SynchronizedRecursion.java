package threadcoreknowledge.synchronizedmethod;

/**
 * @ClassName: SynchronizedRecursion
 * @Description: TODO
 * @author: yourName
 * @date: 2020年10月18日 21:40
 */
public class SynchronizedRecursion {
    int a = 0;

    /**
     * 定义同步方法 method1()
     */
    public synchronized void method1() {
        System.out.println("这是 method1，a = " + a);

        if (a == 0) {
            a++;

            // 递归调用此 method1() 方法
            method1();
        }
    }

    public static void main(String[] args) {
        // 创建当前类的对象
        SynchronizedRecursion synchronizedRecursion = new SynchronizedRecursion();

        synchronizedRecursion.method1();
    }

    // 此时
    // 上面代码的执行结果是
    // 同步方法 method1() 可以正常递归调用
    // 这就证明了 synchronized 关键字对于同一个方法具有可重入性
    // 即
    // 当线程获取到锁对象，并执行 method1() 这个同步方法时，无需重新获取锁也能再次递归调用该 method1() 同步方法
}
