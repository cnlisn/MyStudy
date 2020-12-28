package com.lisn.mystudy.kotlin.Base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.junit.Test

/**
 * @author : lishan
 * @e-mail : cnlishan@163.com
 * @date   : 2020/11/10 2:32 PM
 * @desc   :
 */
class XC_test {
    @Test
    fun Test001() {
        println("Start")
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000L)
            println("Hello World")
        }
        println("End")
        Thread.sleep(1500L)
        println("-----")
    }

    @Test
    fun Test002() {
        println("Start")
        Thread {
            Thread.sleep(1000L)
            println("Hello World")
        }.start()
        println("End")
        Thread.sleep(1500L)
        println("-----")
    }

    @Test
    fun Test003() {
        //协程代码
        println("Start ${Thread.currentThread().name}")
        GlobalScope.launch(Dispatchers.Main) {
            delay(1000L)
            println("Hello World ${Thread.currentThread().name}")
        }
        println("End ${Thread.currentThread().name}")


        Thread.sleep(1500L)
        println("-----")
    }
}

