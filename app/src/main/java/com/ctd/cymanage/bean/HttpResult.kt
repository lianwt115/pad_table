package com.ctd.cymanage.bean


data class HttpResult<T> (var code:Int,var message:String,var data:T){
}