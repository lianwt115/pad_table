package com.ctd.cymanage.activity


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.KeyEvent
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import android.view.WindowManager
import android.os.Build
import com.ctd.cymanage.R
import com.ctd.cymanage.widget.NoticeDialog


open class BaseActivity : AppCompatActivity(),LifecycleProvider<ActivityEvent> {


    protected val lifecycleSubject = BehaviorSubject.create<ActivityEvent>()

    protected var mDestroy:Boolean=false
    private var mNoticeDialog: NoticeDialog?=null
    private var mNoticeDialogBuilder: NoticeDialog.Builder?=null

    override fun <T : Any?> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T> {

        return RxLifecycle.bindUntilEvent(lifecycleSubject, event)

    }

    override fun lifecycle(): Observable<ActivityEvent> {
        return lifecycleSubject.hide()
    }

    override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject)
    }

    init {

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        lifecycleSubject.onNext(ActivityEvent.CREATE)

    }

    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(ActivityEvent.START)
    }

    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(ActivityEvent.RESUME)

    }


    override fun onPause() {
        super.onPause()
        lifecycleSubject.onNext(ActivityEvent.PAUSE)
    }
    override fun onStop() {
        super.onStop()
        lifecycleSubject.onNext(ActivityEvent.STOP)

    }


    protected fun changeStatusColor(color: Int){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = resources.getColor(color)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        mDestroy=true

        lifecycleSubject.onNext(ActivityEvent.DESTROY)
        if (mNoticeDialog != null) {
            mNoticeDialog?.dismiss()
        }

    }


    protected fun showProgressDialog(message: String = getString(R.string.loading), cancle:Boolean = true, type:Int = 1, listen: NoticeDialog.Builder.BtClickListen?=null) {

        if (mNoticeDialogBuilder == null)
            mNoticeDialogBuilder= NoticeDialog.Builder(this,cancle)

        if (mNoticeDialog == null)

            mNoticeDialog=mNoticeDialogBuilder!!.create(message)

        else
            mNoticeDialogBuilder!!.initView( message,cancle)



        mNoticeDialogBuilder!!.setListen(listen,type)

        mNoticeDialog!!.show()
    }


    protected fun showProgressDialogSuccess(boolean: Boolean){

        if (!mDestroy && mNoticeDialog != null && mNoticeDialogBuilder != null) {
            mNoticeDialogBuilder!!.btFinish(boolean)
        }
    }


    protected fun dismissProgressDialog() {
        if (!mDestroy && mNoticeDialog != null) {
            mNoticeDialog!!.dismiss()
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            super.onBackPressed()

        }
        return super.onKeyDown(keyCode, event)
    }

}