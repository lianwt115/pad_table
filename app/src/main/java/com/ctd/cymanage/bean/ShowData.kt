package com.ctd.cymanage.bean



data class ShowData(
        //所属区域
        var parent:String,
        //排名序号
        var order:String,
        //网点
        var where:String,
        //金额
        var cash:Int

)