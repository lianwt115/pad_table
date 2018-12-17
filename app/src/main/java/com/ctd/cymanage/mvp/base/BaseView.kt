package com.ctd.cymanage.mvp.base


interface BaseView<in T> {

    //统一错误处理
    fun err(code:Int,errMessage:String?,type:Int)
}