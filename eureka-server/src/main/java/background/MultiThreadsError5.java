package background;

/**
 * 描述： 下面采用了观察者模式
 */
public class MultiThreadsError5 {

    // 定义成员变量 count
    int count;

    /**
     * 定义构造方法
     * @param source
     */
    public MultiThreadsError5(MySource source) {
        // 调用形参 MySource 接收到的具体目标类对象中的 registerListener() 方法注册观察者
        // 并且
        // 通过匿名内部类的方式往该 registerListener() 方法中传入实现了观察者类 EventListener 类中的 onEvent() 方法的 EventListener 类对象
        source.registerListener(new EventListener() {
            @Override
            public void onEvent(Event e) {
                // 输出成员变量 count 的值
                System.out.println("\n我得到的数字是" + count);
            }

        });

        // 这个 for 循环仅仅是表示当前这个构造方法又做了点其他的事而已，没有其他意义
        for (int i = 0; i < 10000; i++) {
            System.out.print(i);
        }

        // 为成员变量 count 赋值
        count = 100;
    }

    public static void main(String[] args) {
        // 创建具体目标类 MySource 类对象
        MySource mySource = new MySource();

        // 创建 Thread 类对象，构造方法中传入 Runnable 接口实现类对象，然后启动该线程类对象所在线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 让当前线程（即该 Thread 类对象所在线程）休眠 10 毫秒
                    // （这是为了让下面的创建当前类 MultiThreadsError5 类对象那行代码先执行）
                    Thread.sleep(10);

                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 调用上面创建的 MySource 类对象 mySource 中的 eventCome() 方法（表示此时某个事件被触发了）
                // 这里传入此方法中的 Event 接口实现类对象是一个空的，因为它不是这里的关键因素，所以就这么写了
                mySource.eventCome(new Event() {

                });
            }
        }).start();

        // 创建当前类 MultiThreadsError5 类对象，构造方法中传入上面创建的 MySource 类对象
        MultiThreadsError5 multiThreadsError5 = new MultiThreadsError5(mySource);

        // 此时
        // 上面代码的执行结果是 count 输出的结果是 0 而不是 100
        // 这是因为
        // 上面创建的 Thread 类对象所在线程和当前 main() 方法所在线程共用同一个成员变量 count
        // 所以
        // 当 main() 方法所在线程在执行 MultiThreadsError5 multiThreadsError5 = new MultiThreadsError5(mySource); 这行代码时
        // 如果
        // main() 方法所在线程在执行完该构造方法中的 source.registerListener() 方法完成观察者注册，并实现了 onEvent() 方法之后
        // CPU 就切换到上面创建的 Thread 类对象所在线程
        // 然后
        // 然后执行 run() 方法中的 mySource.eventCome() 触发通知方法
        // 而此时该方法中会执行 onEvent() 方法（此时该方法以及在上面执行构造方法时被实现）
        // 因此
        // 此时就会执行 onEvent() 方法中的 System.out.println("\n我得到的数字是" + count); 这行代码
        // 由于
        // 此时 MultiThreadsError5 类的构造方法没有执行完（即 count 没有被赋值为 100）
        // 所以
        // 此时的输出结果就变成了 0
        // 这就是事件注册导致的隐式逸出
    }

    // 定义内部类 MySource 类，它算是观察者模式中的具体目标类
    static class MySource {
        // 定义数据类型为下面定义的 EventListener 类的成员变量 listener（此成员变量就是观察者）
        private EventListener listener;

        /**
         * 定义 registerListener() 方法，此方法用于注册观察者（即此时的 EventListener 类对象）
         * @param eventListener
         */
        void registerListener(EventListener eventListener) {
            this.listener = eventListener;
        }

        /**
         * 定义 eventCome() 方法，此方法为观察者模式中的通知方法
         * 此方法被调用时就表示某个事件被触发了，从而需要让那些观察者进行更新操作（即此时的调用观察者类中的 onEvent() 方法）
         * @param e
         */
        void eventCome(Event e) {
            if (listener != null) {
                listener.onEvent(e);
            } else {
                System.out.println("还未初始化完毕");
            }
        }

    }

    // 定义观察者类 EventListener
    interface EventListener {

        /**
         * 定义抽象类 onEvent() 作为更新观察者自身的一个方法
         * @param e
         */
        void onEvent(Event e);
    }

    // 定义被监听的事件类 Event
    interface Event {

    }
}
