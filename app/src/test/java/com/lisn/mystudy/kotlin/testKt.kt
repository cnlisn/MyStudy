package com.lisn.mystudy.kotlin

import org.junit.Test

/**
 * @author : lishan
 * @e-mail : cnlishan@163.com
 * @date   : 2020/11/26 11:34 AM
 * @desc   :
 */
class testKt {

    @Test
    fun testList() {
        test001()
        println("\n----------------------")

        test002()
        println("\n----------------------")

        test003()
        println("\n----------------------")

        test004()
        println("\n----------------------")


        test005()
        println("\n----------------------")

        test_let()
        println("\n----------------------")

        test_with()
        println("\n----------------------")

        test_run()
        println("\n----------------------")

        test_apply()
        println("\n----------------------")

        test_repeat()
        println("\n----------------------")

    }

    private fun test_repeat() {
        // repeat 接收两个参数，第一个参数是一个 Int 类型的参数，用于指定循环次数。第二个参数是一个 Lambda 表达式。
        // repeat 可以将其中的 Lambda 表达式重复执行指定的次数。
        // 示例如下：
        repeat(4) { it ->
            println(" it = $it")
        }

        //运行结果
        // it = 0
        // it = 1
        // it = 2
        // it = 3
    }

    private fun test_apply() {
        var obj = "hello"
        // 与 run 函数极为相似，需在某个对象上调用，只接收一个 Lambda 参数，在Lambda表达式中提供调用对象的上下文，
        // apply 函数无法指定返回值，**而是会自动返回调用对象本身，**示例代码如下：
        val result = obj.apply {
            //这里式obj的上下文

            println("this = $this")
        }
        // result == obj
        println(result == obj)
    }

    private fun test_run() {
        var obj = "hello"
        // 与 with 函数较为相似，run 函数不能直接的用，而是一定要调用某个对象的 run 函数才行，
        // run 函数只接受一个 Lambda 参数，并且会在 **Lambda **表达式中提供调用对象的上下文，
        // 会使用 Lambda 表达式的最后一行作为返回值，示例代码如下：
        val result = obj.run {

            println("this = $this")
            //这里式obj的上下文
            "value" //最后一行 with 函数的返回值
        }
        println("result = $result")
    }

    private fun test_with() {
        var obj = "hello"
        // with 接收两个参数，第一个参数可以是一个任意类型的对象，第二个参数是一个Lambda表达式，
        // with 函数会在 Lambda 表达式中提供第一个参数对象的上下文，并使用Lambda表达式中的最后一行作为返回值返回，示例如下：
        val result = with(obj) {

            println("this = $this")
            //这里式obj的上下文
            "value" //最后一行 with 函数的返回值
        }

        println("result = $result")
    }

    private fun test_let() {
        var obj = "hello"

        // 将原始调用对象作为参数传递到 Lambda 表达式中，可配合 ?.操作符进行辅助判空处理
        obj.let { obj2 ->
            //业务逻辑
            println("obj2 = $obj2")
        }
    }

    private fun test005() {
        val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")

        // 接收一个 Lambda 类型参数，在遍历集合时将每次遍历的值作为参数传给 Lambda 表达式。
        // 根据我们传入的条件来遍历集合，从而找到该条件下的最大值
        // 示例，找到集合中字符串最长的一个：
        var maxLengthString = list.maxBy { it.length }
        println(maxLengthString)
    }

    private fun test004() {
        // 接收一个 Lambda 类型参数，将集合中的每个元素都映射成一个另外的值，
        // 映射的规则在 Lambda 表达式中指定，最终生成一个新的集合。
        // 示例：将集合中的字母都变成大写：
        val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
        var upperCase = list.map { it.toUpperCase() }
        println(upperCase)
    }

    private fun test003() {
        // 用于过滤集合中的数据可单独使用，也可配合其他函数（如map函数）一起使用
        // 示例，保留5个字母以内的单词：
        val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
        var newList = list.filter { it.length <= 5 }
                .map { it.toUpperCase() }
        println(newList)
    }

    private fun test002() {
        // 用于判断集合中是否所有元素都满足指定条件
        // 示例，是否所有单词都在5个字母以内
        val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")
        var anyResult = list.all { it.length <= 5 }
        //运行结果为 false
        println(anyResult)
    }

    private fun test001() {
        val list = listOf("Apple", "Banana", "Orange", "Pear", "Grape")

        // 用于判断集合中是否至少存在一个元素满足条件
        // 示例，是否存在5个字母以内的单词
        var anyResult = list.any { it.length <= 5 }
        //运行结果为 true
        println(anyResult)
    }
}