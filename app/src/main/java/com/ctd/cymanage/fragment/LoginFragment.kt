package com.ctd.cymanage.fragment

import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat.getDrawable
import com.ctd.cymanage.App
import com.ctd.cymanage.R
import com.ctd.cymanage.bean.BaseUser
import com.ctd.cymanage.bean.EventInfo
import com.ctd.cymanage.bean.EventValue
import com.ctd.cymanage.utils.RxBus
import com.ctd.cymanage.utils.SPHelper
import com.ctd.cymanage.utils.UiUtils
import com.ctd.cymanage.utils.applySchedulers
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_login.*
import java.util.concurrent.TimeUnit

class LoginFragment:BaseFragment(), View.OnClickListener {


    override fun getLayoutResources(): Int {

        return R.layout.fragment_login
    }

    override fun initView() {


        //登录按钮初始化
        bt_login.text = getString(R.string.login_text6)

        bt_login.background =getDrawable(activity!!,R.drawable.bg_20dp_0)
        bt_login.setFinalCornerRadius(20f)

        bt_login.setOnClickListener(this)

        checkAutoLogin()

    }

    private fun checkAutoLogin() {
        //如果退出登录  则不进行自动登录
        if (arguments != null && !arguments!!.getBoolean("autoLogin"))

            return

        if (SPHelper.getInstance().get("autoLogin", false) as Boolean) {

            et_username.setText(SPHelper.getInstance().get("userName", "") as String)
            et_password.setText(SPHelper.getInstance().get("passWord", "") as String)

            login(true)

        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.bt_login -> {

                login(false)
            }
            else -> {

            }
        }
    }

    private fun login(autoLogin: Boolean) {

        val passName = et_username.text.toString()
        val passWord = et_password.text.toString()

        if (TextUtils.isEmpty(passName) || TextUtils.isEmpty(passWord)){

            UiUtils.showToast(getString(R.string.login_text7))

            return
        }

        bt_login.startAnimation()

        val loginSuccess = ((passName == "admin" && passWord == "123456") || (passName == "lwt" && passWord == "123456"))

        Observable.timer(2, TimeUnit.SECONDS).applySchedulers().subscribe({


            bt_login.doneLoadingAnimation(resources.getColor(R.color.colorAccent), BitmapFactory.decodeResource(resources,if (loginSuccess)R.mipmap.ic_done else R.mipmap.error ))


            Observable.timer(1, TimeUnit.SECONDS).applySchedulers().subscribe({

                    if (loginSuccess){

                        App.instanceApp().setLocalUser(BaseUser("110",passName,passWord))

                        SPHelper.run {
                            getInstance().put("autoLogin",cb_autologin.isChecked)
                            getInstance().put("userName",passName)
                            getInstance().put("passWord",passWord)
                        }

                        RxBus.getInstance()?.post(EventInfo(EventValue.MAIN_PAGE,true,autoLogin))

                    }else {

                        UiUtils.showToast(getString(R.string.login_text8))

                    }

                  bt_login.revertAnimation()

                },{
                    Logger.e("按钮复原异常")
                })


        },{
            Logger.e("按钮复原异常")
        })

    }

    //显示和隐藏,需更新那些数据
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden && arguments != null && !arguments!!.getBoolean("autoLogin")){

            SPHelper.getInstance().put("autoLogin",false)
            et_username.setText("")
            et_password.setText("")

        }

    }

    override fun onDestroy() {
        super.onDestroy()

        if (bt_login != null)
            bt_login.dispose()
    }
}