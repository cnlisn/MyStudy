package com.lisn.mystudy.Coroutine

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenCreated
import androidx.lifecycle.whenResumed
import androidx.lifecycle.whenStarted
import com.lisn.mystudy.R
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * 协程练习
 */
class CoroutineActivity : AppCompatActivity() {

    val TAG = "CoroutineActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine)
    }

    /**
     * 查看launch源码可以看到launch方法是CoroutineScope的拓展方法，也就是说我们启动协程要在一个指定的CoroutineScope上来启动。
     * CoroutineScope翻译过来就是“协程范围”，指的是协程内的代码运行的时间周期范围，如果超出了指定的协程范围，协程会被取消执行，
     * 上面第一段代码中的GlobalScope指的是与应用进程相同的协程范围，也就是在进程没有结束之前协程内的代码都可以运行。
     *
     * 接着可以看下launch方法的其他参数：

    context：协程上下文，可以指定协程运行的线程。
    默认与指定的CoroutineScope中的coroutineContext保持一致，比如GlobalScope默认运行在一个后台工作线程内。
    也可以通过显示指定参数来更改协程运行的线程，Dispatchers提供了几个值可以指定：
    Dispatchers.Default、Dispatchers.Main、Dispatchers.IO、Dispatchers.Unconfined。
    start：协程的启动模式。默认的（也是最常用的）CoroutineStart.DEFAULT是指协程立即执行，
    除此之外还有CoroutineStart.LAZY、CoroutineStart.ATOMIC、CoroutineStart.UNDISPATCHED。
    block：协程主体。也就是要在协程内部运行的代码，可以通过lamda表达式的方式方便的编写协程内运行的代码。
    CoroutineExceptionHandler：除此之外还可以指定CoroutineExceptionHandler来处理协程内部的异常。

    返回值Job：对当前创建的协程的引用。可以通过Job的start、cancel、join等方法来控制协程的启动和取消。

    启动协程不是只有launch一个方法的，还有async等其他方法可以启动协程，不过launch是最常用的一种方法，其他的方法大家可以去自行了解。
     */
    fun test001(view: View) {
        //协程代码
        println("Start ${Thread.currentThread().name}")
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000L)
            println("Hello World ${Thread.currentThread().name}")
        }
        println("End ${Thread.currentThread().name}")
    }

    fun test002(view: View) {
        //线程代码
        println("Start ${Thread.currentThread().name}")
        Thread {
            Thread.sleep(1000L)
            println("Hello World ${Thread.currentThread().name}")
        }.start()
        println("End ${Thread.currentThread().name}")

    }

    /**
     * 线程切换
     * 执行结果为：Hello main -> World main -> End main，
     * 也就是说这个suspend函数仍然运行在主线程中，suspend并没有切换线程的作用。
     */
    fun test003(view: View) {
        GlobalScope.launch(Dispatchers.Main) {
            println("Hello ${Thread.currentThread().name}")
            test()
            println("End ${Thread.currentThread().name}")
        }
    }

    suspend fun test() {
        println("World ${Thread.currentThread().name}")
    }

    /**
     * 我们可以withContext方法来在suspend函数中进行线程的切换：
     * 执行的结果为：Hello main -> World DefaultDispatcher-worker-1 -> End main，
     * 这说明我们的suspend函数的确运行在不同的线程之中了。
     * 就是说实际是上withContext方法进行的线程切换的工作，那么suspend关键字有什么用处呢？
     *
     * 其实，忽略原理只从使用上来讲，suspend关键字只起到了标志这个函数是一个耗时操作，必须放在协程中执行的作用。
     * 关于线程切换其实还有其他方法，但是withContext是最常用的一个，其他的如感兴趣可以自行了解。
     */
    fun test004(view: View) {
        GlobalScope.launch(Dispatchers.Main) {
            println("Hello ${Thread.currentThread().name}")
            test2()
            println("End ${Thread.currentThread().name}")
        }
    }

    private suspend fun test2() {
        withContext(Dispatchers.IO) {
            SystemClock.sleep(3000)
            println("World ${Thread.currentThread().name}")
        }
    }

    /**
     *  顺序执行
     *
     * 前提是request相关的API已经改造成了适应协程的方式，并在内部进行了线程切换。
     * 这样代码看起来是不是整洁多了？没有了烦人的嵌套，所有的逻辑都体现在了代码的先后顺序上了，是不是一目了然呢？
     */
    fun test005(view: View) {
        //用协程改造回调代码
        GlobalScope.launch(Dispatchers.Main) {
            //三次请求顺序执行
//            val value1 = request1(parameter)
//            val value2 = request2(value1)
//            val value3 = request2(value2)
//            //用最终结果更新UI
//            updateUI(value3)
        }
    }

    //requestAPI适配了Kotlin协程
//    suspend fun request1(parameter : Parameter){...}
//    suspend fun request2(parameter : Parameter){...}
//    suspend fun request3(parameter : Parameter){...}

    /**
     * 并发执行
     *
     * 代码中我们用async方法包裹执行了suspend方法，接着在用到结果的时候使用了await方法来获取请求结果，
     * 这样三次请求就是并发进行的，而且三次请求的结果都返回之后就会切回主线程来更新UI。
     */
    fun test006(view: View) {
        //并发请求
        GlobalScope.launch(Dispatchers.Main) {
            //三次请求并发进行
//            val value1 = async { request1(parameter1) }
//            val value2 = async { request2(parameter2) }
//            val value3 = async { request3(parameter3) }
//            //所有结果全部返回后更新UI
//            updateUI(value1.await(), value2.await(), value3.await())
        }
    }

    //requestAPI适配了Kotlin协程
//    suspend fun request1(parameter : Parameter){...}
//    suspend fun request2(parameter : Parameter){...}
//    suspend fun request3(parameter : Parameter){...}

    /**
     * 复杂业务逻辑
     *
     *实际开发遇到了串行与并行混合的复杂业务逻辑，那么我们当然也可以混合使用上面介绍的方法来编写对应的代码。
     * 比如这样的业务逻辑：request2和request3都依赖于request1的请求结果才能进行，
     * request2和request3要并发进行，
     * 更新UI依赖request2和request3的请求结果。
     */
    fun test007(view: View) {
        //复杂业务逻辑的Kotlin协程实现
        GlobalScope.launch(Dispatchers.Main) {
            //首先拿到request1的请求结果
//            val value1 = request1(parameter1)
//            //将request1的请求结果用于request2和request3两个请求的并发进行
//            val value2 = async { request2(value1) }
//            val value3 = async { request2(value1) }
//            //用request2和request3两个请求结果更新UI
//            updateUI(value2.await(), value3.await())
        }
    }

    //requestAPI适配了Kotlin协程
//    suspend fun request1(parameter : Parameter){...}
//    suspend fun request2(parameter : Parameter){...}
//    suspend fun request3(parameter : Parameter){...}


    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////


    fun test008(view: View) {
        main()
    }

    fun main() = runBlocking<Unit> {
        launch { // 运行在父协程的上下文中，即 runBlocking 主协程
            println("main runBlocking      : I'm working in thread 	${Thread.currentThread().name}")
        }
        /* 非受限调度器 vs 受限调度器
    Dispatchers.Unconfined 协程调度器在调用它的线程启动了一个协程，但它仅仅只是运行到第一个挂起点。
    挂起后，它恢复线程中的协程，而这完全由被调用的挂起函数来决定。非受限的调度器非常适用于执行不消耗
    CPU 时间的任务，以及不更新局限于特定线程的任何共享数据（如UI）的协程。*/
        launch(Dispatchers.Unconfined) { //  不受限的： 将工作在主线程中
            println("Unconfined            : I'm working in thread ${Thread.currentThread().name}")
        }
        launch(Dispatchers.Default) { // 将会获取默认调度器
            println("Default               : I'm working in thread ${Thread.currentThread().name}")
        }
        launch(newSingleThreadContext("MyOwnThread")) { // 将使它获得一个新的线程
            /* newSingleThreadContext 为协程的运行启动了一个线程。 一个专用的线程是一种非常昂贵的资源。
             在真实的应用程序中两者都必须被释放，当不再需要的时候，使用 close 函数，
             或存储在一个顶层变量中使它在整个应用程序中被重用。*/
            println("newSingleThreadContext: I'm working in thread ${Thread.currentThread().name}")
        }
    }

    /**
     *
     * async函数返回一个Deferred类型，该类继承自Job，并且该类提供了wait函数，getCompleted函数等函数来获取协程返回值；
     * 其中getCompleted函数如果协程任务还没有执行完成则会抛出IllegalStateException
     *
     * 输出为：
    协程1开始执行
    协程2开始执行
    协程执行完毕GlobalScope.async 1 + GlobalScope.async 2
     */
    fun test009(view: View) {
        runBlocking {
            var deferred = GlobalScope.async {
                println("协程1开始执行")
                return@async "GlobalScope.async 1"
            }
            var deferred2 = GlobalScope.async {
                println("协程2开始执行")
                return@async "GlobalScope.async 2"
            }
            Thread.sleep(2000L)
            if (deferred.isCompleted && deferred2.isCompleted) {
                var result1 = deferred.await();
                var result2 = deferred2.await();
                println("协程执行完毕$result1 + $result2")
            } else {
                println("协程未执行")
            }
        }
    }

    /**
     * async函数可以通过将 start 参数设置为 CoroutineStart.LAZY 而变为惰性的。
     * 在这个模式下，只有结果通过 await 获取的时候协程才会启动，或者在 Job 的 start 函数调用的时候
     */
    fun test010(view: View) {
        var deferred = GlobalScope.async {
            asyncValue()
        }
        Thread.sleep(2000L)
        if (deferred.isCompleted) {
            var result = deferred.getCompleted();
            println("协程执行完毕$result")
        } else {
            println("协程未执行")
        }
    }


    fun asyncValue(): String {
        return "GlobalScope.async"
    }


    // 1. 创建一个 MainScope
    val scope = MainScope()

    fun test011(view: View) {

        // 2. 启动协程
        scope.launch(Dispatchers.Unconfined) {
            Log.e(TAG, "test011: launch")
            val one = async { getResult(20) }
            val two = async { getResult(40) }
            val toString = (one.await() + two.await()).toString()
            Log.e(TAG, "test011: " + toString)
        }
        Log.e(TAG, "test011: end")
    }

    // 3. 销毁的时候释放
    override fun onDestroy() {
        super.onDestroy()

        scope.cancel()
    }

    private suspend fun getResult(num: Int): Int {
        delay(5000)
        return num * num
    }

    init {

        lifecycleScope.launch {
            // 代表当前生命周期处于 Resumed 的时候才会执行(选择性使用)
            whenResumed {
                // ... 具体的协程代码
                Log.e(TAG, "whenResumed: ")
            }

            whenStarted {
                Log.e(TAG, "whenStarted: ")
            }

            whenCreated {
                Log.e(TAG, "whenCreated: ")
            }

        }
    }

    fun test012(view: View) {
        lifecycleScope.launch {
            flow {
                for (i in 1..3) {
                    Log.e("Flow", "$i emit")
                    emit(i)
                }
            }.filter {
                Log.e("Flow", "$it filter")
                it % 2 != 0
            }.map {
                Log.e("Flow", "$it map")
                "${it * it} money"
            }.collect {
                Log.e("Flow", "i get $it")
            }
        }
    }

    /**
     * 协作取消

    Flow 采用和协程一样的协作取消，也就是说，Flow 的 collect 只能在可取消的挂起函数中挂起的时候取消，否则不能取消。
    如果我们想取消 Flow 得借助 withTimeoutOrNull 之类的顶层函数
     */
    fun test013(view: View) {
        lifecycleScope.launch {
            val f = flow {
                for (i in 1..3) {
                    delay(500)
                    Log.e(TAG, "emit $i")
                    emit(i)
                }
            }
            withTimeoutOrNull(1600) {
                f.collect {
                    delay(500)
                    Log.e(TAG, "consume $it")
                }
            }
            Log.e(TAG, "cancel")
        }
    }

    /**
     * Channel是一个面向多协程之间数据传输的 BlockQueue。它的使用方式超级简单：
     */
    fun test014(view: View) {
        lifecycleScope.launch {
            // 1. 生成一个 Channel
            val channel = Channel<Int>()

            // 或： 1. 生成一个 Channel
//            val channel = produce<Int> {
//                for(i in 1..5){
//                    delay(200)
//                    send(i * i)
//                }
//                close()
//            }

            // 2. Channel 发送数据
            launch {
                for (i in 1..5) {
                    delay(500)
                    channel.send(i * i)
                }
                // 发送完毕的时候，可以使用 Channel.close() 来表明通道已经结束数据的发送。
                channel.close()
            }

            // 3. Channel 接收数据
            // 正常情况下，我们仅需要调用 Channel.receive() 获取数据，但是该方法只能获取一次传递的数据
            launch {
                // 但如果发送的数据不可以预估,这个时候我们就需要迭代 Channel 了
                for (y in channel)
                    Log.e(TAG, "get $y")
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // 高阶函数
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 高阶函数也是函数，所以同样需要使用 fun 关键字来修饰，示例中的高阶函数 onlyIf，需要接收两个参数，
     * 第一个参数为 Boolean 的变量，第二个参数是参数为空返回值为 Unit 的函数。
     */
    fun test015(view: View) {
        var flag: Boolean = true

        Log.e(TAG, "test015: flag = " + flag)

        onlyIf(flag) {
            println("hello world")
        }


    }

    fun onlyIf(isDebug: Boolean, block: () -> Unit) {
        if (isDebug) {
            block()
        }
    }

    /**
     * 在 Kotlin 中函数是“一等公民”，可以直接使用对象加两个冒号（::）进行分割，后面紧跟着方法名，
     * 使用这种方式来引用一个函数声明，而不是像一个对象一样使用 . 来执行函数。可以通过示例体会一下：
     */
    fun test016(view: View) {
        val runnable = Runnable {
            println("Runnable::run")
        }

        val function: () -> Unit

        function = runnable::run

        // 注意传入的是function，而不是function()
        onlyIf(true, function)

        //【注】在高阶函数的参数中要想作为参数传递给高阶函数时，必须传的是函数的声明，
        // 如果传的是直接执行一个函数的话，那么实际上传递的是函数的返回值！
    }


}