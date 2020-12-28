package com.lisn.mystudy.thread.lock;

import com.lisn.mystudy.thread.Resource;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

//生产者任务
	class Input implements Runnable{
		private Resource r;
		private int i = 0;
		private Lock lock;
		private Condition in_con;//生产者监视器
		private Condition out_con;//消费者监视器
		public Input(Resource r,Lock lock,Condition in_con,Condition out_con){
			this.r = r;
			this.lock = lock;
			this.in_con = in_con;
			this.out_con = out_con;
		}
		public void run() {
			//无限生产车辆
			for(;;){
				lock.lock();//获取锁
					//flag为true的时候代表已经生产过，此时将当前线程wait，等待消费者消费
					while(r.isFlag()){
						try {
							in_con.await();//跟wait作用相同
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(i%2==0){
						r.setId(i);//设置车的id
						r.setName("帕萨特");//设置车的型号
						r.setWhell(4);//设置车的轮子数
					}else{
						r.setId(i);//设置车的id
						r.setName("爱玛电动车");//设置车的型号
						r.setWhell(2);//设置车的轮子数
					}
					i++;
					r.setFlag(true);
					//将线程池中的消费者线程唤醒
					out_con.signal();
				lock.unlock();//释放锁
				 //加上sleep是为了让打印结果更明显
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

