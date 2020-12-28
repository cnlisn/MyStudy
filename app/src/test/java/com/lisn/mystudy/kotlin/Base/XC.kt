package com.lisn.mystudy.kotlin.Base

import kotlinx.coroutines.*
import org.junit.Test

/**
 * @author : lishan
 * @e-mail : cnlishan@163.com
 * @date   : 2020/11/9 7:09 PM
 * @desc   : 协程
 */


class XC {

    @Test
    fun Test001() {
        GlobalScope.launch {
            delay(1000L)
            println("Hello,World!")
        }

        Thread.sleep(1500L)
        println("-------")
    }

    @Test
    fun Test002() {
        println("Start")
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000L)
            println("Hello World")
        }
        println("End")

    }

    @Test
    fun Test003() {

        println("Start")
        Thread {
            Thread.sleep(1000L)
            println("Hello World")
        }.start()
        println("End")


        Thread.sleep(1500L)
        println("-------")
    }


}



//region Description
/**
 * 协程
 *
 * kotlin 中 GlobalScope 类提供了几个携程构造函数：
    launch - 创建协程
    async - 创建带返回值的协程，返回的是 Deferred 类
    withContext - 不创建新的协程，在指定协程上运行代码块
    runBlocking - 不是 GlobalScope 的 API，可以独立使用，区别是 runBlocking 里面的 delay 会阻塞线程，而 launch 创建的不会

 * 协程运行的线程调度器，有 4种线程模式：
    Dispatchers.Default
    Dispatchers.IO -
    Dispatchers.Main - 主线程
    Dispatchers.Unconfined - 没指定，就是在当前线程
 */

@Test
fun TestGlobalScope() {
    println("协程初始化开始，时间: " + System.currentTimeMillis())

    GlobalScope.launch(Dispatchers.Unconfined) {
        println("协程初始化完成，时间: " + System.currentTimeMillis())
        for (i in 1..3) {
            println("协程任务1打印第$i 次，时间: " + System.currentTimeMillis())
        }
        delay(500)
        for (i in 1..3) {
            println("协程任务2打印第$i 次，时间: " + System.currentTimeMillis())
        }
    }

    println("主线程 sleep ，时间: " + System.currentTimeMillis())
    Thread.sleep(1000)
    println("主线程运行，时间: " + System.currentTimeMillis())

    for (i in 1..3) {
        println("主线程打印第$i 次，时间: " + System.currentTimeMillis())
    }
}

/**
 *  CoroutineStart - 启动模式，默认是DEAFAULT，也就是创建就启动；还有一个是LAZY，意思是等你需要它的时候，再调用启动
DEAFAULT - 模式模式，不写就是默认
ATOMIC -
UNDISPATCHED
LAZY - 懒加载模式，你需要它的时候，再调用启动，看这个例子
 */
@Test
fun test001() {
    var job: Job = GlobalScope.launch(start = CoroutineStart.LAZY) {
        println("协程开始运行，时间: " + System.currentTimeMillis())
    }

    Thread.sleep(1000L)
    // 手动启动协程
    job.start()

}
//endregion