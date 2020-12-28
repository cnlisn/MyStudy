package com.lisn.mystudy.NIO;

import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author : lishan
 * @e-mail : cnlishan@163.com
 * @date : 2020/11/6 5:45 PM
 * @desc :
 */
public class TestTreah {


    /**
     * 三个线程顺序执行，你来说说有几种实现方式？
     *
     先说下要求，就是三个线程，假设是线程 1，2，3， 现在的要求是：必须是线程 1 先执行，然后线程 2 再执行，最后是线程 3 执行
     然后有几种实现方法呢？
     其实它的本质就是实现，让线程 2，3 等待线程 1 执行完毕，所以重点就是有哪些方法可以让线程 2，3 等待
     */


    //region join

    /**
     * 第一反应应该就是使用 join 方法，因为 join 本来就是支持这种机制的
     * 比如，我在线程 B 中调用了线程 A 的 join 方法，那么线程 B 就会等线程 A 执行结束之后再执行
     * 那么具体应该怎么使用嘞？
     * 示例如下：
     */
    static class Work implements Runnable {
        private Thread beforeThread;

        public Work(Thread beforeThread) {
            this.beforeThread = beforeThread;
        }

        @Override
        public void run() {
            // 如果有线程,就 join 进来,没有的话就直接输出
            if (beforeThread != null) {
                try {
                    beforeThread.join();
                    System.out.println("thread start : " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("thread start : " + Thread.currentThread().getName());
            }
        }
    }

    @Test
    public void join() {
        Thread t1 = new Thread(new Work(null));
        Thread t2 = new Thread(new Work(t1));
        Thread t3 = new Thread(new Work(t2));

        t1.start();
        t2.start();
        t3.start();
    }
    //endregion

    //region CountDownLatch 信号量
    public class Work1 implements Runnable {
        CountDownLatch cOne;
        CountDownLatch cTwo;

        public Work1(CountDownLatch cOne, CountDownLatch cTwo) {
            super();
            this.cOne = cOne;
            this.cTwo = cTwo;
        }

        @Override
        public void run() {
            try {
                // 当前一个线程信号量为 0 时,才执行
                cOne.await();
                System.out.println("thread start : " + Thread.currentThread().getName());
                // 后一个线程信号量减 1
                cTwo.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void ThreadLoopTwo() {

        // 设置线程 1 的信号量为 0
        CountDownLatch cOne = new CountDownLatch(0);
        // 设置线程 2 的信号量为 1
        CountDownLatch cTwo = new CountDownLatch(1);
        // 设置线程 3 的信号量为 1
        CountDownLatch cThree = new CountDownLatch(1);

        // 因为 cOne 为 0 ,故 t1 可以直接执行
        Thread t1 = new Thread(new Work1(cOne, cTwo));
        // 线程 t1 执行完毕之后,此时的 cTwo 为 0 , t2 开始执行
        Thread t2 = new Thread(new Work1(cTwo, cThree));
        // 线程 t2 执行完毕,此时 cThree 为 0 , t3 开始执行
        Thread t3 = new Thread(new Work1(cThree, cThree));

        t1.start();
        t2.start();
        t3.start();

    }
    //endregion


    //region 使用单个线程池

    /**
     * 使用单个线程池
     * <p>
     * 之所以线程 1，2，3 的执行顺序无法保证，是因为在编译器可能会去做一些优化，导致没有办法按照顺序执行
     * 如果我们使用单个线程池去执行的话，那就没有这样的问题了
     * 具体实现：
     */
    @Test
    public void ThreadLoopThree() {

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread start : " + Thread.currentThread().getName() + " run one");
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread start : " + Thread.currentThread().getName() + " run two");
            }
        });

        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread start : " + Thread.currentThread().getName() + " run three");
            }
        });

        ExecutorService executor = Executors.newSingleThreadExecutor();
        // 将线程依次加入到线程池中
        executor.submit(t1);
        executor.submit(t2);
        executor.submit(t3);
        // 及时将线程池关闭
        executor.shutdown();

    }
    //endregion


    //region CompletableFuture

    //    如果使用 CompletableFuture 来实现的话，代码就非常简洁了
    static class Work4 implements Runnable {
        private int id;

        public Work4(int id) {

            this.id = id;
        }

        @Override
        public void run() {
            System.out.println("thread start : " + id + "  " + Thread.currentThread().getName());
        }
    }

    @Test
    public void ThreadLoopFour() {

        Thread t2 = new Thread(new Work4(2));
        Thread t1 = new Thread(new Work4(1));
        Thread t3 = new Thread(new Work4(3));

        CompletableFuture.runAsync(() -> t1.start())
                .thenRun(() -> t2.start())
                .thenRun(() -> t3.start());

    }
    //endregion
}
