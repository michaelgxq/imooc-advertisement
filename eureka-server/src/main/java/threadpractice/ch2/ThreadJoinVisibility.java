/*
授权声明：
本源码系《Java多线程编程实战指南（核心篇）》一书（ISBN：978-7-121-31065-2，以下称之为“原书”）的配套源码，
欲了解本代码的更多细节，请参考原书。
本代码仅为原书的配套说明之用，并不附带任何承诺（如质量保证和收益）。
以任何形式将本代码之部分或者全部用于营利性用途需经版权人书面同意。
将本代码之部分或者全部用于非营利性用途需要在代码中保留本声明。
任何对本代码的修改需在代码中以注释的形式注明修改人、修改时间以及修改内容。
本代码可以从以下网址下载：
https://github.com/Viscent/javamtia
http://www.broadview.com.cn/31065
*/
package threadpractice.ch2;

import threadpractice.util.Tools;

public class ThreadJoinVisibility {
    // 线程间的共享变量
    static int data = 0;

    public static void main(String[] args) {

        Thread thread = new Thread(() -> {
            // 使当前线程休眠 R 毫秒（R 的值为随机数，此时 R 为 50）
            Tools.randomPause(50);

            // 更新 data 的值
            data = 1;
        });

        thread.start();

        // 等待线程 thread 结束后，main 线程才继续运行
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 读取并打印变量 data 的值
        System.out.println(data);

    }
}
// 对于上面的代码
// 子线程（即 变量 thread）运行时将共享变量 data 的值更新为 1
// 因此
// 父线程（即当前这个 main() 方法所在线程）对子线程（即 变量 thread）的 join() 方法调用结束后
// 父线程能读取到被更改后的共享变量 data 的值 1