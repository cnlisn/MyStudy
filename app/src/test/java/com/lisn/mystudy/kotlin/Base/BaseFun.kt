package com.lisn.mystudy.kotlin.Base


import kotlinx.coroutines.*
import org.junit.Test

/**
 * @author : lishan
 * @e-mail : cnlishan@163.com
 * @date   : 2020/11/9 9:12 AM
 * @desc   :
 */
class BaseFun {

    /**
     * Kotlin是静态编程语言并支持类型推导，允许维护正确性和性能的同时保持代码的简洁。
     * Kotlin的空安全机制，可以很大程度的减少空指针异常，保证app的健壮性。
     * 与Java的互操作性极强，可以让我们在现有的项目中很好的使用Kotlin来慢慢过渡。
     */

    @Test
    fun test() {

        //  空检查机制
        var name: String? = null

        // 第一种补救措施：如果name是null，后面的代码就不执行（内部控制不执行）
        // name?.length

        // 第二种补救措施：不管name是不是null，都必须执行（类似Java）
        // name!!.length

        // 第三种补救措施：如同Java
//        if (name != null) {
//            name.length
//        }

        // var 可读，可改
        var info: String = "hello"
        info = "world"

        // val 可读，不可改
        val info2: String = "DDD"
        // info2="aaa"

        // 面试题：KT是 动态语言 还是 静态语言，为什么？

        // 类型推断   就算看起来这种类型推断很像JS，KT 是编译期决定类型 所以是 静态语言

        var age = 99
        // 如果JS下面不报错
        // age="aa"
        // age=1234.5

        var sex = "M"  // Char
    }

    @Test
    fun Test02() {
//        for (i in 1..4) print(i) // 输出“1234”

//        for (i in 4..1) print(i) // 什么都不输出

//        var i = 5
//        if (i in 1..10) { // 等同于 1 <= i && i <= 10
//            println(i)
//        }

        // 使用 step 指定步长
//        for (i in 1..8 step 2) print(i) // 输出“1357”

//        for (i in 4 downTo 1 step 2) print(i) // 输出“42”

        // 使用 until 函数排除结束元素
        for (i in 1 until 10) {   // i in [1, 10) 排除了 10
            println(i)
        }
    }

    @Test
    fun Test03() {
        val a: Int = 10000
        println(a === a) // true，值相等，对象地址相等

        //经过了装箱，创建了两个不同的对象
        val boxedA: Int? = a
        val anotherBoxedA: Int? = a

        //虽然经过了装箱，但是值是相等的，都是10000
        println(boxedA === anotherBoxedA) //  false，值相等，对象地址不一样
        println(boxedA == anotherBoxedA) // true，值相等
    }

    // 对集合进行迭代：
    @Test
    fun Test04() {
        val items = listOf("apple", "banana", "kiwi")
        for (item in items) {
            println(item)
        }

        println("items.indices = " + items.indices)

        // 循环索引
        for (index in items.indices) {
            println("item at $index is ${items[index]}")
        }
    }

    @Test
    fun testBean() {

        // 字段的引用
        val person = Person("张三", 20)
        println(person)
        println(person.age)
        println(person.name)

        person.age = 30
        println(person.age)

    }

}