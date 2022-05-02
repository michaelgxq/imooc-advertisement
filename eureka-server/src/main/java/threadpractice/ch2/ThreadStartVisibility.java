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

public class ThreadStartVisibility {
    // 线程间的共享变量
    static int data = 0;

    public static void main(String[] args) {

        Thread thread = new Thread(() -> {
            // 使当前线程休眠R毫秒（R的值为随机数）
            Tools.randomPause(50);

            // 读取并打印变量data的值
            System.out.println(data);
        });

        // 在子线程thread启动前更新变量data的值
        data = 1; // 语句 1
        thread.start();

        // 使当前线程休眠 R 毫秒（R 的值为随机数，此时 R 为 50）
        Tools.randomPause(50);

        // 在子线程thread启动后更新变量data的值
        data = 2; // 语句 2

    }
}

// 对于上面的代码
// 如果我们把上述程序中的语句 2 注释掉
// 那么
// 由于 main 线程在启动其子线程（即 此时的变量 thread ）之前将共享变量 data 的值更新为1 （即 语句 1）
// 因此
// 子线程中所读取到的共享变量 data 的值一定为 1 （即 此时，子线程中打印的值为 1）
// 这是由于父线程在子线程启动前对共享变量的更新对子线程的可见性是有保证的
// 如果我们没有将语句 2 注释掉
// 那么
// 由于父线程在子线程启动之后对共享变量的更新对子线程的可见性是没有保证的
// 因此
// 子线程（即 此时的变量 thread ）此时读取到的共享变量 data 的值可能为 2, 也可能仍然为 1
// 这就解释了为什么多次运行上述程序可以发现其输出可能是“ 1" ，也可能是“ 2”
