package com.ctd.cymanage.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton
import com.bumptech.glide.Glide
import com.ctd.cymanage.R
import com.ctd.cymanage.utils.DeviceUtil
import com.ctd.cymanage.utils.UiUtils
import com.ctd.cymanage.utils.applySchedulers
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


/**
 * Created by Administrator on 2018\1\8 0008.
 */

class NoticeDialog : Dialog {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {}

    class Builder(private val mContext: Context,private val cancle: Boolean) : DialogInterface.OnDismissListener {


        private var mLoginDialog: NoticeDialog? = null
        private var mLayout: View? = null
        private var mNoticeTVNotice: TextView? = null
        private var mProgressBar: ProgressBar? = null
        private var mTopRoot: LinearLayout? = null
        private var mLine: View? = null
        private var mType  = 0

        private var mBtNext: CircularProgressButton? = null
        private var mListen: BtClickListen? = null
        private var index = 0

        fun create(notice:String): NoticeDialog {

            val inflater = mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            mLoginDialog = NoticeDialog(mContext, R.style.MyDialog)

            mLoginDialog!!.setCanceledOnTouchOutside(cancle)
            mLoginDialog!!.setOnDismissListener(this)
            mLayout = inflater.inflate(R.layout.dialog_notice, null)

            mLoginDialog!!.addContentView(mLayout!!, ViewGroup.LayoutParams(
                    DeviceUtil.dip2px(mContext, 180f),LinearLayout.LayoutParams.WRAP_CONTENT ))


            mNoticeTVNotice = mLayout!!.findViewById(R.id.dialog_text) as TextView
            mProgressBar = mLayout!!.findViewById(R.id.progress) as ProgressBar
            mBtNext = mLayout!!.findViewById(R.id.bt_go) as CircularProgressButton
            mLine = mLayout!!.findViewById(R.id.line) as View
            mTopRoot = mLayout!!.findViewById(R.id.top_root) as LinearLayout


            mBtNext?.setFinalCornerRadius(8F)
            mBtNext?.text = mContext.getString(R.string.next)
            mBtNext?.background = mContext.getDrawable(R.drawable.bg_acc_8)

            mBtNext!!.setOnClickListener {


                if (mListen !=null) {

                    if (mListen!!.btClick()) {

                        mBtNext!!.startAnimation()
                    }
                }

            }

            initView(notice,cancle)

            return mLoginDialog as NoticeDialog
        }

        fun initView(notice: String,cancle: Boolean) {

            mNoticeTVNotice!!.text=notice

            mLoginDialog!!.setCanceledOnTouchOutside(cancle)
        }
        fun setListen(listen:BtClickListen?,type:Int){

            this.mListen = listen
            this.mType = type

            when (type) {
                //只要标题
                1 -> {

                    mBtNext!!.visibility = View.GONE

                    mLine!!.visibility = View.GONE

                    mProgressBar!!.visibility =  View.GONE

                }
                //标题和缓冲进度

                2 -> {
                    mBtNext!!.visibility = View.GONE

                    mLine!!.visibility = View.VISIBLE

                    mProgressBar!!.visibility =  View.VISIBLE

                }
                //标题 按钮
                3 -> {
                    mBtNext!!.visibility = View.VISIBLE

                    mLine!!.visibility = View.VISIBLE

                    mProgressBar!!.visibility =  View.GONE

                }

            }

            mTopRoot!!.background =mContext.getDrawable(if (type == 1)R.drawable.bg_20dp_0 else R.drawable.bg_20dp_0_top)

        }

        fun btFinish(boolean: Boolean){

            if (mType == 3)
                mBtNext!!.doneLoadingAnimation(mContext.resources.getColor(R.color.white),if (boolean) BitmapFactory.decodeResource(mContext.resources,R.mipmap.ic_done) else BitmapFactory.decodeResource(mContext.resources,R.mipmap.error))

            Observable.timer(1,TimeUnit.SECONDS).applySchedulers().subscribe({
                mLoginDialog!!.dismiss()
            },{
                Logger.e("延迟关闭对话框异常")
            })
        }

        override fun onDismiss(dialog: DialogInterface?) {

            mBtNext?.revertAnimation()
        }

        interface BtClickListen{

            fun  btClick():Boolean
        }


    }
}