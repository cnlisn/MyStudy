package com.lisn.mystudy.kotlin.Lambda

import org.junit.Test

/**
 * @author : lishan
 * @e-mail : cnlishan@163.com
 * @date   : 2020/11/9 10:20 AM
 * @desc   :
 */


@Test
fun main() {

    // 这就是一个函数
    val method01: (String, String) -> Unit = { aStr, bStr -> println("a:$aStr, b:$bStr") }
//    method01("hello","wolrd")

    val method02 = { println("hello") }
//    method02.invoke()
//    method02()

    var method03: (String) -> Unit = {
        println("你传入的是：$it")
    }
//    method03("ha ha ha ")

    var method04: (Int) -> Unit = {
        when (it) {
            1 -> println("你是等于一")
            in 20..30 -> println("你是 20 ～ 30 的数字")
            else -> println("都不满足")
        }
    }
//    method04(30)

    var method05: (Int, Int) -> Unit = { aNumber, bNumber ->
        println("第一个参数：$aNumber, 第二个参数：$bNumber")
    }

//    method05(3,10)

    var method06: (Int, Int) -> Unit = { aNumber, _ ->
        println("第一个参数：$aNumber")
    }
//    method06(99,88)

    var method07: (String) -> String = { str -> str }
    val result = method07("hello")
    println(result)
}