package com.lisn.mystudy.thread;

//生产者任务 1
//class Input implements Runnable {
//    private Resource r;
//
//    public Input(Resource r) {
//        this.r = r;
//    }
//
//    public void run() {
//        //无限生产车辆
//        for (int i = 0; ; i++) {
//            if (i % 2 == 0) {
//                r.setId(i);//设置车的id
//                r.setName(i + "----帕萨特");//设置车的型号
//                r.setWhell(4);//设置车的轮子数
//            } else {
//                r.setId(i);//设置车的id
//                r.setName(i + "----爱玛电动车");//设置车的型
//                r.setWhell(2);//设置车的轮子数
//            }
//        }
//    }
//
//}

//生产者任务 2
//class Input implements Runnable {
//    private Resource r;
//
//    public Input(Resource r) {
//        this.r = r;
//    }
//
//    public void run() {
//        //无限生产车辆
//        for (int i = 0; ; i++) {
//            synchronized (r) {
//                if (i % 2 == 0) {
//                    r.setId(i);//设置车的id
//                    r.setName(i + "----帕萨特");//设置车的型号
//                    r.setWhell(4);//设置车的轮子数
//                } else {
//                    r.setId(i);//设置车的id
//                    r.setName(i + "----爱玛电动车");//设置车的型号
//                    r.setWhell(2);//设置车的轮子数
//                }
//                i++;
//            }
//            //加上sleep是为了让打印结果更明显
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }
//
//}


//生产者任务 3
class Input implements Runnable {
    private Resource r;

    public Input(Resource r) {
        this.r = r;
    }

    public void run() {
        //无限生产车辆
        for (int i = 0; ; i++) {

            synchronized (r) {
                //flag为true的时候代表已经生产过，此时将当前线程wait，等待消费者消费
                if (r.isFlag()) {
                    try {
                        r.wait();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (i % 2 == 0) {
                    r.setId(i);//设置车的id
                    r.setName("帕萨特");//设置车的型号
                    r.setWhell(4);//设置车的轮子数
                } else {
                    r.setId(i);//设置车的id
                    r.setName("爱玛电动车");//设置车的型号
                    r.setWhell(2);//设置车的轮子数
                }
                r.setFlag(true);
                //将线程池中的线程唤醒
                r.notify();
            }
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


