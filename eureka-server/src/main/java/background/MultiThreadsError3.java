package background;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：     发布逸出
 */
public class MultiThreadsError3 {

    // 定义一个 Map 集合（它的访问修饰符为 private，即表示该成员变量不想让外界直接访问到）
    private Map<String, String> states;

    // 构造方法
    public MultiThreadsError3() {
        states = new HashMap<>();
        states.put("1", "周一");
        states.put("2", "周二");
        states.put("3", "周三");
        states.put("4", "周四");
    }

    // 定义一个 get 方法，它返回上面定义的 Map 集合 states
    public Map<String, String> getStates() {
        return states;
    }


    /**
     * 定义 getStatesImproved() 方法
     * 此方法返回一个新建的 HashMap 集合对象，它就是成员变量 states 的副本
     * 此时
     * 如果外部获取到该副本，并且对里面的数据进行修改，也不会影响成员变量 states 本身了
     * @return
     */
    public Map<String, String> getStatesImproved() {
        return new HashMap<>(states);
    }

    public static void main(String[] args) {
        // 创建当前类 MultiThreadsError3 类对象
        MultiThreadsError3 multiThreadsError3 = new MultiThreadsError3();

        // 调用上面定义的 get 方法获取成员变量 states
        Map<String, String> states = multiThreadsError3.getStates();

        // 调用 Map 集合中的 remove() 方法，去除当前集合对象 states 中的 key 为 1 的元素
        states.remove("1");

        // 显然
        // 上面这一行代码是有违我们的初衷的
        // 因为
        // 我们对这个成员变量只是设置了一个 get 方法，而没有定义 set 方法
        // 说明对于这个成员变量，我们仅仅是想让外部能够访问它，但是不能修改它
        // 但是
        // 由于这个成员变量是一个集合
        // 因此
        // 当我们通过 get 方法获取到该成员变量后，我们就能通过调用集合中对应的方法来对该集合中元素进行操作，从而修改该集合中的元素
        // 这显然就不是我们此时所希望
        // 而这个就是所谓的逸出情况




//        System.out.println(states.get("1"));

//        System.out.println(multiThreadsError3.getStatesImproved().get("1"));
//        multiThreadsError3.getStatesImproved().remove("1");
//        System.out.println(multiThreadsError3.getStatesImproved().get("1"));

    }
}
