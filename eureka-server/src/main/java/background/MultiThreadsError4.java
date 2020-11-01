package background;

/**
 * 描述：     初始化未完毕，就this赋值
 */
public class MultiThreadsError4 {

    // 定义静态的数据类型为下面定义的 Point 类的成员变量 point
    static Point point;

    public static void main(String[] args) throws InterruptedException {
        // 创建下面定义的 PointMaker 类对象，并启动线程
        new PointMaker().start();

//        Thread.sleep(10);

        // 让当前线程（即 main() 方法所在线程）休眠 105 毫秒
        Thread.sleep(105);
        if (point != null) {
            // 打印当前 MultiThreadsError4 类的静态成员变量 point 的值
            System.out.println(point);
        }
    }

    // 此时
    // 上面代码的执行结果为
    // 如果
    // 我们让当前线程（即 main() 方法所在线程）休眠 10 毫秒（即上面那行被注释掉的代码）
    // 那么
    // 此时上面那行打印当前 MultiThreadsError4 类的静态成员变量 point 的值的输出结果为 1,0
    // 这是因为
    // 根据 Java 的初始化原理，int 类型的数据的初始化值为 0
    // 由于
    // 下面 Point 类的构造方法中，当给 Point 类中的成员变量 x 赋完值之后，还没等 Point 类中的成员变量 y 赋完值
    // 我们就直接把当前 Point 类的对象赋值给 MultiThreadsError4 类的静态成员变量 point 了
    // 并且还让执行下面 Point 类的构造方法的所在线程线程休眠了 100 毫秒
    // 因此
    // 当执行到上面 System.out.println(point); 这行语句时，由于下面 Point 类的构造方法所在线程仍处于休眠状态
    // 所以
    // 构造方法中 this.y = y; 这行代码还没执行
    // 这就造成了此时的输出结果为 1,0
    // 但是
    // 如果我们让当前线程（即 main() 方法所在线程）休眠 105 毫秒
    // 此时
    // 由于当前线程（即 main() 方法所在线程）休眠的时间要长于 Point 类的构造方法中设置的休眠时间
    // 因此
    // 等当前线程（即 main() 方法所在线程）被唤醒之后
    // Point 类的构造方法中的 this.y = y; 这行代码以及执行完了
    // 所以
    // 此时执行到上面 System.out.println(point); 这行语句时的输出结果为 1,1
    // 可见
    // 不同的线程休眠时间，让我们输出静态成员变量 point 的值变得不一样，这显然是有问题的
    // 这就是在构造方法中未初始化完毕就 this 赋值所造成的严重后果
}

// 定义 Point 类
class Point {

    // 定义成员变量 x，y
    private final int x, y;

    // 构造方法
    public Point(int x, int y) throws InterruptedException {
        // 先给当前 Point 类对象的成员变量 x 赋值
        this.x = x;

        // 把当前 Point 类对象的 this 赋值给 MultiThreadsError4 类中的静态成员变量 point
        MultiThreadsError4.point = this;

        // 让当前线程（即执行此构造方法的所在线程）休眠 100 毫秒
        Thread.sleep(100);

        // 当前线程休眠结束后，在给当前 Point 类对象的成员变量 y 赋值
        this.y = y;
    }

    /**
     * 重写 toString() 方法
     * @return
     */
    @Override
    public String toString() {
        return x + "," + y;
    }
}

// 定义 PointMaker 类，它继承了 Thread 类
class PointMaker extends Thread {

    @Override
    public void run() {
        try {
            // 创建 Point 类对象，调用上面的有参构造方法
            new Point(1, 1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}