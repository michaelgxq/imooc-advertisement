package threadcoreknowledge.synchronizedmethod;

/**
 * @ClassName: SynchronizedSuperClass
 * @Description: TODO
 * @author: yourName
 * @date: 2020年10月18日 22:17
 */
public class SynchronizedSuperClass {

    /**
     * 定义同步方法 method1()
     */
    public synchronized void method1() {
        System.out.println("我是 SynchronizedSuperClass 类中的 method1() 方法");

        // 创建 SynchronizedRecursion 类对象
        SynchronizedRecursion synchronizedRecursion = new SynchronizedRecursion();

        // 调用 SynchronizedRecursion 类中的 method1() 这个同步方法
        synchronizedRecursion.method1();
    }


    public static void main(String[] args) {
        SynchronizedSuperClass synchronizedSuperClass = new SynchronizedSuperClass();

        synchronizedSuperClass.method1();
    }

    // 此时
    // 上面代码的执行结果是
    // SynchronizedSuperClass 类中的同步方法 method1() 可以正常调用其他类中的同步方法而无需重新获取锁

}
