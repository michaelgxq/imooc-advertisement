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
package threadpractice.ch3;

public class DCLSingleton {
    /*
     * 保存该类的唯一实例，使用volatile关键字修饰instance。
     */
    private static volatile DCLSingleton instance;

    /*
     * 私有构造器使其他类无法直接通过new创建该类的实例
     */
    private DCLSingleton() {
        // 什么也不做
    }

    /**
     * 创建并返回该类的唯一实例 <BR>
     * 即只有该方法被调用时该类的唯一实例才会被创建
     *
     * @return
     */
    public static DCLSingleton getInstance() {
        if (null == instance) {// 操作①：第1次检查
            synchronized (DCLSingleton.class) {
                if (null == instance) {// 操作②：第2次检查
                    instance = new DCLSingleton();// 操作③
                }
            }
        }
        return instance;
    }

    public void someService() {
        // 省略其他代码
    }
}
