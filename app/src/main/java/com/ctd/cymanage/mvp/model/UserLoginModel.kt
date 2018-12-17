package com.ctd.cymanage.mvp.model

import android.content.Context
import com.ctd.cymanage.bean.BaseUser
import com.ctd.cymanage.mvp.model.BaseModel
import com.ctd.cymanage.network.HttpResultFunc
import io.reactivex.Observable


class UserLoginModel(context: Context) : BaseModel(context) {


    fun userLogin(name:String,password:String,auto:Boolean): Observable<BaseUser>?{

        return apiService?.userLogin(name, password,auto)?.map(HttpResultFunc())

    }



}