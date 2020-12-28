package com.lisn.mystudy.thread.Deadlock;


import org.junit.Test;

/**
 * 死锁
 */
public class DeadlockDemo {

    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

	/**
	 * 运行后打印结果
	 * Deadlock1----lock1
	 * Deadlock2----lock2
	 * 复制代码注意：run()中写的是无限循环，所以按理来说应该是无限打印，我们运行程序后只打印了这两行数据，而且我没有终止控制台打印
	 * 我来跟大家分析一下原理：
	 *
	 *
	 * 任务Deadlock1执行，判断了第一个同步代码块，此时锁lock1可用，于是持着锁lock1进入了第一个同步代码块，
	 * 打印了：Deadlock1----lock1，就在此时，线程切换到了任务Deadlock2
	 * 任务Deadlock2执行，判断第一个同步代码块，此时锁lock2可用，于是持着锁lock2进入了第一个同步代码块，
	 * 打印了：Deadlock2----lock2，接着向下执行，判断锁lock1不可用(因为所lock1已经被任务Deadlock1所占用)，
	 * 于是就进行等待，线程任务再次切换到任务Deadlock1
	 * 任务Deadlock1执行，判断第二个同步代码块，此时锁lock2不可用(因为所lock2已经被任务Deadlock2所占用)，
	 * 任务Deadlock1也进入了等待状态
	 *
	 *
	 * 通过以上描述：Deadlock1持着Deadlock2需要的锁进行等待，Deadlock2持着Deadlock1所需要的锁进行等待，
	 * 这时候两个任务各自拿着对方需要的锁处于一种僵持现象，这种现象就可以称为死锁现象。
	 *
	 *
	 * 作者：Bezier
	 * 链接：https://juejin.cn/post/6844904202255237134
	 * 来源：掘金
	 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
	 */
	@Test
    public void Test001() {
        // TODO Auto-generated method stub
        Deadlock1 d1 = new Deadlock1(lock1, lock2);
        Deadlock2 d2 = new Deadlock2(lock1, lock2);
        Thread t1 = new Thread(d1);
        Thread t2 = new Thread(d2);
        t1.start();
        t2.start();
    }
}

class Deadlock1 implements Runnable {
    private Object lock1;
    private Object lock2;

    public Deadlock1(Object obj1, Object obj2) {
        this.lock1 = obj1;
        this.lock2 = obj2;
    }

    public void run() {

        while (true) {
            synchronized (lock1) {
                System.out.println("Deadlock1----lock1");
                synchronized (lock2) {
                    System.out.println("Deadlock1----lock2");
                }
            }
        }
    }

}

class Deadlock2 implements Runnable {
    private Object lock1;
    private Object lock2;

    public Deadlock2(Object obj1, Object obj2) {
        this.lock1 = obj1;
        this.lock2 = obj2;
    }

    public void run() {

        while (true) {
            synchronized (lock2) {
                System.out.println("Deadlock2----lock2");
                synchronized (lock1) {
                    System.out.println("Deadlock2----lock1");
                }
            }
        }
    }

}

