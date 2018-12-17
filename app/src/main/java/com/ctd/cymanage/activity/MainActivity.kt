package com.ctd.cymanage.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import com.ctd.cymanage.BuildConfig
import com.ctd.cymanage.R
import com.ctd.cymanage.bean.EventInfo
import com.ctd.cymanage.bean.EventValue
import com.ctd.cymanage.fragment.BaseFragment
import com.ctd.cymanage.fragment.LoginFragment
import com.ctd.cymanage.fragment.MainFragment
import com.ctd.cymanage.utils.RxBus
import com.ctd.cymanage.utils.UiUtils
import com.ctd.cymanage.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions
import com.tencent.bugly.beta.Beta
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity() {


    private var mExitTime: Long = 0
    private var mDisposable: Disposable? = null
    private lateinit var mainFragment: MainFragment
    private lateinit var loginFragment: LoginFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Beta.init(applicationContext, BuildConfig.DEBUG)
        //权限检查
        checkAndRequirePermissions()
    }


    private fun initView() {

        mDisposable = RxBus.getInstance()!!.toObservale(EventInfo::class.java).subscribe({

            if (it.state)
                changeView(it.type, it.data)

        }, {
            Logger.e(it.message)
        })

        //初始化时  需要判断是否自动登录和账号密码问题  实际要请求接口

        initFragment()

    }

    private fun initFragment() {


            mainFragment = MainFragment()
            loginFragment = LoginFragment()

            val fragmentTrans = supportFragmentManager.beginTransaction()

            fragmentTrans.add(R.id.main_contain, mainFragment)
            fragmentTrans.add(R.id.main_contain, loginFragment)

            fragmentTrans.commit()

        supportFragmentManager.beginTransaction().show(loginFragment)
            .hide(mainFragment)
            .commit()

    }


    //权限检查
    fun checkAndRequirePermissions() {

        //判断系统版本是否是6.0以上

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            var per: RxPermissions?= RxPermissions(this)
            per?.request( Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE)
                ?.subscribe {
                    if (it) {

                        // Logger.e("权限全部同意")
                        initView()
                    } else {
                        //至少一个没有同意
                        showProgressDialog("权限被拒绝,稍后将退出")

                        Observable.timer(3, TimeUnit.SECONDS).applySchedulers().subscribe({

                            finish()

                        }, {

                            Logger.e(it.message)

                        })
                    }
                }
        }else{

            initView()

            Logger.e("系统版本低于6.0,无需动态申请权限")

        }

    }

    private fun changeView(type: Int, data: Any?) {

        when (type) {

            EventValue.LOGIN_PAGE ->{

                if (data !=null && data is Boolean){

                    val myBundle = Bundle()

                    myBundle.putBoolean("autoLogin",data)

                    loginFragment.arguments = myBundle
                }

                supportFragmentManager.beginTransaction().show(loginFragment)
                    .hide(mainFragment)
                    .commit()

            }

            EventValue.MAIN_PAGE ->{

                supportFragmentManager.beginTransaction().show(mainFragment)
                    .hide(loginFragment)
                    .commit()

            }

        }


    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (System.currentTimeMillis().minus(mExitTime) <= 3000 ) {

                finish()

            } else {
                mExitTime = System.currentTimeMillis()

                UiUtils.showToast(getString(R.string.exit_notice))
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
