package com.ctd.cymanage.mvp.contract


import com.ctd.cymanage.bean.BaseUser
import com.trello.rxlifecycle2.LifecycleTransformer
import com.ctd.cymanage.mvp.base.BasePresent
import com.ctd.cymanage.mvp.base.BaseView


interface UserLoginContract {

    interface View : BaseView<Presenter> {

        fun successLogin(baseUser: BaseUser)

    }

    interface Presenter : BasePresent {

        fun userLogin(name:String,password:String,auto:Boolean,bindToLifecycle: LifecycleTransformer<BaseUser>)

    }
}