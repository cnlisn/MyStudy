package com.lisn.mystudy.kotlin.Base

import org.junit.Test

/**
 * @author : lishan
 * @e-mail : cnlishan@163.com
 * @date   : 2020/11/9 8:43 AM
 * @desc   :
 */
class FunC {

    fun add(number1: Int, number2: Int): Int {
        return number1 + number2;
    }

    fun add2(number1: Int, number2: Int): Int = number1 + number2

    //类型推断 Int
    fun add3(number1: Int, number2: Int) = number1 + number2

    // public 方法则必须明确写出返回类型
    public fun sum(a: Int, b: Int): Int = a + b

    //类型推断 Double
    fun add4(number1: Int, number2: Int) = number1 + number2.toDouble()

    //类型推断 Long
    fun add5(number1: Int, number2: Int) = number1 + number2.toLong()

    // 需求：如果传入的是 大于9 返回null 否则就返回100
    fun getInt(number: Int): Int? {
        return if (number > 9) {
            null
        } else {
            100
        }
    }

    @Test
    fun test() {

//        val add = add(2, 3);
//        val add = add3(2, 3);

//        val add = add4(2, 3);
//        val add = add5(2, 3);
        val add = getInt(1)

        println(add)
    }
}