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

public class SpeculativeLoadExample {
    private boolean ready = false;
    private int[] data = new int[] { 1, 2, 3, 4, 5, 6, 7, 8 };

    public void writer() {
        int[] newData = new int[] { 1, 2, 3, 4, 5, 6, 7, 8 };
        for (int i = 0; i < newData.length; i++) {// 语句①（for循环语句）

            // 此处包含读内存的操作
            newData[i] = newData[i] - i;
        }
        data = newData;
        // 此处包含写内存的操作
        ready = true;// 语句②
    }

    public int reader() {
        int sum = 0;
        int[] snapshot;
        if (ready) {// 语句③（if语句）
            snapshot = data;
            for (int i = 0; i < snapshot.length; i++) {// 语句④（for循环语句）
                sum += snapshot[i];// 语句⑤
            }

        }
        return sum;
    }
}

// 如果多个线程运行上面的代码
// 那么
// CPU 的乱序执行（其实本质上是预测执行）会发生如下这种情况
// 即
// 当 CPU 在线程 A 执行时，在执行 reader() 方法时（此时， writer() 方法还为执行）
// 会先执行 reader() 方法中的语句⑤
//（由于此时 writer() 方法还未执行，因此成员变量 ready 的值为 false，因此是不应该执行语句⑤的，但是由于存在乱序执行，所以先执行了语句⑤）
// 因此
// 此时，变量 snapshot 中的值为  1, 2, 3, 4, 5, 6, 7, 8
// 并且变量 sum 相加后的值就是这几个值的合
// 此时
// 线程 B 执行了 writer() 方法，把成员变量 ready 的值变成了 true（并把该值刷入了主内存）
// 这样一来
// 当线程 A 再次执行时，它再执行语句③时，发现该 if 判断语句为 true
// 那么
// 它就直接把上面乱序执行后得到的 sum 变量值给返回了（即  1, 2, 3, 4, 5, 6, 7, 8 的合）
// 但是
// 如果没有没有乱序执行，当语句③的 if 判断语句为 true 时
// 变量 snapshot 中的值为  1, 1, 1, 1, 1, 1, 1, 1（即 执行完 writer() 方法之后的结果）
// 因此
// 正确情况下，sum 变量的值应该是  1, 1, 1, 1, 1, 1, 1, 1 的合
// 显然
// 乱序执行导致了，执行结果的错误