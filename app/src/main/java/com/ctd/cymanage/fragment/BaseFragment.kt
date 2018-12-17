package com.ctd.cymanage.fragment

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctd.cymanage.R
import com.ctd.cymanage.bean.DeviceInfo
import com.ctd.cymanage.widget.DeviceDialog
import com.ctd.cymanage.widget.NoticeDialog
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.ArrayList


abstract class BaseFragment : androidx.fragment.app.Fragment(),LifecycleProvider<FragmentEvent> {
    protected val parties = arrayOf(
        "硚口区",
        "江岸区",
        "江汉区",
        "武昌区",
        "洪山区",
        "汉阳区"
    )

    protected val arrayString1 = arrayOf("安卓终端","立式终端","桌面终端","柜台终端")
    protected val arrayString2 = arrayOf("养老保险","医疗保险","工伤保险","失业保险","生育保险")
    protected val arrayString3 = arrayOf("指静脉","指纹","人脸")
    protected val arrayString4 = arrayOf("应城市","云梦市","汉川市","安陆市","孝南区","孝昌区","大悟市")
    protected val arrayString5 = arrayOf("新制卡","补换卡")
    protected val arrayString6 = arrayOf("自助发卡","即时发卡","自助领卡")
    protected val arrayString7 = arrayOf("养老信息","医疗信息","工伤信息","失业信息","生育信息")
    protected val arrayString8 = arrayOf("设备编号","部署网点","设备类型","设备状态")

    protected val colors = arrayListOf<Int>(
        Color.rgb(95,247,255),
        Color.rgb(25,38,222),
        Color.rgb(130,4,233),
        Color.rgb(0,167,255),
        Color.rgb(0,220,164),
        Color.rgb(85,224,251),
        Color.rgb(255,187,71)
    )

    protected val colors1 = arrayListOf<Int>(
        Color.rgb(50,134,255),
        Color.rgb(226,54,255)
    )
    protected val animTime = arrayListOf<Int>(500,1000,1500)


    var mDestroy:Boolean=false
    private var mNoticeDialog: NoticeDialog?=null
    private var mNoticeDialogBuilder: NoticeDialog.Builder?=null

    private  var mDeviceDialog: DeviceDialog?=null
    private  var mDeviceDialogBuilder: DeviceDialog.Builder?=null
    val lifecycleSubject = BehaviorSubject.create<FragmentEvent>()
    protected lateinit   var tfRegular: Typeface
    protected lateinit var tfLight: Typeface
    override fun lifecycle(): Observable<FragmentEvent> {

        return lifecycleSubject.hide()
    }

    override fun <T : Any?> bindUntilEvent(event: FragmentEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event)
    }

    override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject)
    }


    var isFirst : Boolean = false
    var rootView :View? = null
    var isFragmentVisiable :Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW)
        if(rootView==null){
            rootView = inflater?.inflate(getLayoutResources(),container,false)
        }
        return  rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }



    override fun onAttach(context: Context?) {
        super.onAttach(context)
        lifecycleSubject.onNext(FragmentEvent.ATTACH)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleSubject.onNext(FragmentEvent.CREATE)
        tfRegular = Typeface.createFromAsset(activity?.assets, "OpenSans-Regular.ttf")
        tfLight = Typeface.createFromAsset(activity?.assets, "OpenSans-Light.ttf")
    }

    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(FragmentEvent.START)
    }

    override fun onPause() {
        super.onPause()
        lifecycleSubject.onNext(FragmentEvent.PAUSE)
    }

    override fun onStop() {
        super.onStop()
        lifecycleSubject.onNext(FragmentEvent.STOP)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mDestroy=true
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW)
        if (mNoticeDialog != null) {
            mNoticeDialog?.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleSubject.onNext(FragmentEvent.DESTROY)
    }

    override fun onDetach() {
        super.onDetach()
        lifecycleSubject.onNext(FragmentEvent.DETACH)
    }

    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(FragmentEvent.RESUME)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            isFragmentVisiable = true
        }
        if (rootView == null) {
            return
        }
        //可见，并且没有加载过
        if (!isFirst&&isFragmentVisiable) {
            onFragmentVisiableChange(true);
            return
        }
        //由可见——>不可见 已经加载过
        if (isFragmentVisiable) {
            onFragmentVisiableChange(false)
            isFragmentVisiable = false
        }
    }
    open protected fun onFragmentVisiableChange(b: Boolean) {

    }

    abstract fun getLayoutResources(): Int

    abstract fun initView()
    fun backgroundAlpha(activity: Activity, bgAlpha: Float) {
        val lp = activity.window.attributes
        lp.alpha = bgAlpha
        activity.window.attributes = lp
    }

    protected fun showProgressDialog(message: String = getString(R.string.loading), cancle:Boolean = true, type:Int = 1, listen: NoticeDialog.Builder.BtClickListen? = null) {

        if (mNoticeDialogBuilder == null)
            mNoticeDialogBuilder= NoticeDialog.Builder(activity!!,cancle)

        if (mNoticeDialog == null)

            mNoticeDialog=mNoticeDialogBuilder!!.create(message)

        else
            mNoticeDialogBuilder!!.initView( message,cancle)


        mNoticeDialogBuilder!!.setListen(listen,type)

        mNoticeDialog!!.show()
    }

    protected fun showDeviceDialog(data: ArrayList<DeviceInfo>) {

        if (mDeviceDialogBuilder == null)
            mDeviceDialogBuilder= DeviceDialog.Builder(activity!!)

        if (mDeviceDialog == null)

            mDeviceDialog=mDeviceDialogBuilder!!.create(data)

        else
            mDeviceDialogBuilder!!.initView(data)

        mDeviceDialog!!.show()
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

    protected fun dismissDeviceDialog() {
        if (!mDestroy && mDeviceDialog != null) {
            mDeviceDialog!!.dismiss()
        }
    }

    protected fun checkData(start:String,end:String):Boolean{

        val startArr = start.split("/")
        val endArr = end.split("/")

        if (startArr.size>=3 && endArr.size>=3){

            //2017/12/4    2018/11/03
            if (endArr[0]<startArr[0]){

                return false

            }else if (endArr[0] == startArr[0]){


                if (endArr[1]<startArr[1]){

                    return false

                }else if (endArr[1] == startArr[1]){

                    if (endArr[2]<startArr[2])

                        return false

                }

            }

            return true

        }

        return false
    }
}