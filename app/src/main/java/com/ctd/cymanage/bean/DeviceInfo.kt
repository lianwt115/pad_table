package com.ctd.cymanage.bean

data class DeviceInfo(

    var no:String,
    var where:String,
    var type:String,
    var onLine:Boolean?,
    var info:String = "详情",
    var fun1:Int = if (onLine ==null) 0 else 1,
    var fun2:Int = 1,
    var fun3:Int = 1,
    var fun4:Int = if (onLine ==null) 0 else 1,
    var fun5:Int = 1


) {
}