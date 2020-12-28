package com.lisn.mystudy.kotlin.Base

/**
 * @author : lishan
 * @e-mail : cnlishan@163.com
 * @date   : 2020/11/9 9:34 AM
 * @desc   :
 */
class KT1<out T> {

    // 只能获取
    fun getData(): T? = null

    // 不能修改
//    fun setData(d:T){
//
//    }
//
//    fun addData(d: T) {
//
//    }
}