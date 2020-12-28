package com.lisn.mystudy.kotlin.Base

import org.junit.Test

/**
 * @author : lishan
 * @e-mail : cnlishan@163.com
 * @date   : 2020/11/9 10:02 AM
 * @desc   :
 */
class Login {

    @Test
    fun main() {
        loginAction("admin1", "123456") {
            if (it) {
                println("登录成功 ")
            } else {
                println("登录失败 ")
            }
        }
    }

}

public fun loginAction(userName: String, userPwd: String, loginResponseResult: (Boolean) -> Unit) {

    // 校验
    if (userName == null || userPwd == null) {
        return
    }
    loginEngine(userName, userPwd, loginResponseResult)
}

// 真正的登录引擎
private fun loginEngine(userName: String, userPwd: String, loginResponseResult: (Boolean) -> Unit) {

    if ("admin" == userName && "123456" == userPwd) {
        loginResponseResult(true)
    } else {
        loginResponseResult(false)
    }

}