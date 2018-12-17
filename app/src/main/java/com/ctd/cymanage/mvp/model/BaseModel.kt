package com.ctd.cymanage.mvp.model

import android.content.Context
import com.ctd.cymanage.network.ApiService
import com.ctd.cymanage.network.RetrofitClient


open class BaseModel(val context: Context) {

    val API_VERSION = 1

    val retrofitClient = RetrofitClient.getInstance(context)
    val apiService = retrofitClient.create(ApiService::class.java)


}