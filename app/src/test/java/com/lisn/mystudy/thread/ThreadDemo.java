package com.lisn.mystudy.thread;

import org.junit.Test;

public class ThreadDemo {
    private Resource r;

    public static void main(String[] args) {
        Resource r = new Resource();
        Input in = new Input(r);
        Output out = new Output(r);
        Thread t1 = new Thread(in);
        Thread t2 = new Thread(out);
        t1.start();//开启生产者线程
        t2.start();//开启消费者线程
    }

    @Test
    public void test001() {
        Resource r = new Resource();
        Input in = new Input(r);
        Output out = new Output(r);
        Thread t1 = new Thread(in);
        Thread t2 = new Thread(out);
        t1.start();//开启生产者线程
        t2.start();//开启消费者线程
    }

    //生产消费者任务三：
	//完美运行。我再来带着大家捋一遍：
	//
	//生产者生产汽车的时候先判断标记flag，如果flag为true代表生产的汽车还未被消费，不能再进行生产了，将当前线程wait()掉。
	// 如果标记flag为false代表生产的汽车已经被消费掉，此时可以进行生产，生产完毕后将标记置为true并notify将消费者唤醒。
	//消费者生消费汽车的时候先判断标记flag，如果flag为true代表生产的汽车还未被消费，此时可以进行消费，
	// 消费完毕后将标记置为false并notify将生产者唤醒。如果标记flag为false代表生产的汽车已经被消费掉，
	// 不能再进行消费了，将当前线程wait()掉。
	//
	//
	//疑点
	//
	//通过wait()可以将线程进入等待状态并释放锁，并将线程放入线程池当中，notify()会唤醒线程池中的任意一个线程，
	// 并将线程从线程池中移出。需要注意的是一把锁对应一个线程池。
	//wait()和notify()为什么要用r来调用？因为r为同步的锁，一把锁对应一个线程池，所以wait()和notify()必须要由锁来调用。
	//
	//作者：Bezier
	//链接：https://juejin.cn/post/6844904202255237134
	//来源：掘金
	//著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
}

