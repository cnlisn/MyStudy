package com.lisn.mystudy.thread.lock;

import com.lisn.mystudy.thread.Resource;

import org.junit.Test;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockDemo {


	/**
	 * 打印结果
	 * name=帕萨特---whellNumber=4---id=3970
	 * name=爱玛电动车---whellNumber=2---id=3971
	 * name=帕萨特---whellNumber=4---id=3972
	 * name=爱玛电动车---whellNumber=2---id=3973
	 * name=帕萨特---whellNumber=4---id=3974
	 * name=爱玛电动车---whellNumber=2---id=3975
	 * name=帕萨特---whellNumber=4---id=3976
	 * name=爱玛电动车---whellNumber=2---id=3977
	 * 复制代码这次是真的完美运行，所以以后碰到多生产多消费的问题可以通过显示锁Lock配合其监视器来实现。
	 *
	 * 作者：Bezier
	 * 链接：https://juejin.cn/post/6844904202255237134
	 * 来源：掘金
	 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
	 */
	@Test
	public  void test001() {
		// TODO Auto-generated method stub
		Resource r = new Resource();
		Lock lock = new ReentrantLock();
        //生产者监视器
		Condition in_con = lock.newCondition();
        //消费者监视器
		Condition out_con = lock.newCondition();
		Input in = new Input(r,lock,in_con,out_con);
		Output out = new Output(r,lock,in_con,out_con);
		Thread t1 = new Thread(in);
		Thread t2 = new Thread(in);
		Thread t3 = new Thread(out);
		Thread t4 = new Thread(out);
		t1.start();//开启生产者线程
		t2.start();//开启生产者线程
		t3.start();//开启消费者线程
		t4.start();//开启消费者线程
	}
}

