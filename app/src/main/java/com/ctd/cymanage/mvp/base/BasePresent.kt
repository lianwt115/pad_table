package com.ctd.cymanage.mvp.base


interface BasePresent {

    //统一错误处理
    fun handleErr(throwable: Throwable,type:Int)
}