package background;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：     构造函数中新建线程
 */
public class MultiThreadsError6 {

    // 定义 Map 集合类型的成员变量 states
    private Map<String, String> states;

    public MultiThreadsError6() {
        // 在构造方法中新开一个线程往成员变量 states 中存放数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                states = new HashMap<>();
                states.put("1", "周一");
                states.put("2", "周二");
                states.put("3", "周三");
                states.put("4", "周四");
            }
        }).start();
    }

    public Map<String, String> getStates() {
        return states;
    }

    public static void main(String[] args) throws InterruptedException {
        // 创建当前类 MultiThreadsError6 类对象
        MultiThreadsError6 multiThreadsError6 = new MultiThreadsError6();

        // 让当前 main() 方法所在线程休眠 1000 毫秒
        Thread.sleep(1000);

        // 获取成员变量 states 中键为 1 的 value 值
        System.out.println(multiThreadsError6.getStates().get("1"));

        // 此时
        // 上面代码的执行结果是
        // 执行到 System.out.println(multiThreadsError6.getStates().get("1")); 这行代码时会报空指针异常
        // 这是因为
        // 我们把往成员变量 states 中存放数据的操作是放在新开的线程中的
        // 所以
        // 当 main() 方法所在线程执行 System.out.println(multiThreadsError6.getStates().get("1")); 这行代码时
        // 那个新开线程可能都还没有开始被 CPU 执行
        // 这就会造成上面的输出语句时，由于 JVM 无法获取到 1 这个 键，从而报空指针异常
        // 但是
        // 如果我们让 main() 方法所在线程休眠 1000 毫秒（如上面那行被注释的代码那样）
        // 那么
        // 此时 main() 方法所在线程再执行 System.out.println(multiThreadsError6.getStates().get("1")); 这行代码时
        // 那个新开的线程可能已经执行完，从而成员变量 states 中已经有数据了
        // 那么
        // 此时该输出语句就可能不会报空指针异常，而是正常输出对应的值了
        // 可见
        // 如果我们在构造方法中开新线程就可能造成这种时间不同，得到的结果不同的现象
        // 因此
        // 我们要避免在构造方法中新开线程


        // 但是
        // 其实，我们在实际工作中一般没人会这样做
        // 不过
        // 由于获取线程池对象，或者数据库连接池对象，这些池中对象的过程在底层也是通过新开线程来完成的
        // 因此
        // 如果我们在构造方法中获取这些池中对象，其实也就是像上面那样，在构造方法中开新线程
        // 这同样也会造成上面这种报空指针的隐患
    }
}
