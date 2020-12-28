package com.lisn.mystudy.thread;

//消费者任务 1
//class Output implements Runnable {
//    private Resource r;
//
//    public Output(Resource r) {
//        this.r = r;
//    }
//
//    public void run() {
//        //无限消费车辆
//        for (; ; ) {
//            System.out.println("name=" + r.getName() + "---whellNumber=" + r.getWhellNumber()
//                    + "---id=" + r.getId());
//        }
//
//    }
//}

//消费者任务 2
//class Output implements Runnable {
//    private Resource r;
//
//    public Output(Resource r) {
//        this.r = r;
//    }
//
//    public void run() {
//        //无限消费车辆
//        for (; ; ) {
//            synchronized (r) {
//                System.out.println("name=" + r.getName() + "---whellNumber=" + r.getWhellNumber()
//                        + "---id=" + r.getId());
//            }
//            //加上sleep是为了让打印结果更明显
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//    }
//}


//消费者任务 3
class Output implements Runnable {
    private Resource r;

    public Output(Resource r) {
        this.r = r;
    }

    public void run() {
        //无限消费车辆
        for (; ; ) {
            synchronized (r) {
                //flag为false，代表当前生产的汽车已经被消费掉，
                //进入wait状态等待生产者生产
                if (!r.isFlag()) {
                    try {
                        r.wait();
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                System.out.println("name=" + r.getName() + "---whellNumber=" + r.getWhellNumber()
                        + "---id=" + r.getId());
                r.setFlag(false);
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



