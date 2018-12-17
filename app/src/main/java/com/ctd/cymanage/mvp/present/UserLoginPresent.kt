package com.ctd.cymanage.mvp.present

import android.content.Context
import com.ctd.cymanage.bean.BaseUser
import com.ctd.cymanage.mvp.contract.UserLoginContract
import com.ctd.cymanage.mvp.model.UserLoginModel
import com.ctd.cymanage.network.ApiException
import com.ctd.cymanage.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleTransformer
import io.reactivex.Observable


class UserLoginPresent(context: Context, view: UserLoginContract.View) : UserLoginContract.Presenter{

    var mContext : Context? = null
    var mView : UserLoginContract.View? = null
    val mModel : UserLoginModel by lazy {
        UserLoginModel(context)
    }
    init {
        mView = view
        mContext = context
    }



    override fun userLogin(name: String, password: String, auto: Boolean, bindToLifecycle: LifecycleTransformer<BaseUser>) {
        val observable : Observable<BaseUser>? = mContext?.let {
            mModel.userLogin(name,password,auto) }


        observable?.applySchedulers()?.compose(bindToLifecycle)?.subscribe(

                {
                    mView?.successLogin(it)
                }, {

                handleErr(it,0)

        }

        )

    }

    override fun handleErr(throwable: Throwable, type: Int) {

        Logger.e(throwable.message?:throwable.localizedMessage)

        mView?.err(if (throwable is ApiException)throwable.getResultCode()!! else -1,throwable.message,type)

    }



}