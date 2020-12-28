package com.lisn.mystudy.thread.lock;

import com.lisn.mystudy.thread.Resource;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

//消费者任务
class Output implements Runnable {
    private Resource r;
    private Lock lock;
    private Condition in_con;//生产者监视器
    private Condition out_con;//消费者监视器

    public Output(Resource r, Lock lock, Condition in_con, Condition out_con) {
        this.r = r;
        this.lock = lock;
        this.in_con = in_con;
        this.out_con = out_con;
    }

    public void run() {
        //无限消费车辆
        for (; ; ) {

            lock.lock();//获取锁
            while (!r.isFlag()) {
                try {
                    out_con.await();//将消费者线程wait
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            System.out.println("name=" +
                    r.getName() + "---whellNumber=" + r.getWhellNumber()
                    + "---id=" + r.getId());
            r.setFlag(false);
            in_con.signal();//唤醒生产者线程
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

