package com.ctd.cymanage.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.ctd.choose.view.OnWheelChangedListener
import com.ctd.choose.view.OnWheelClickedListener
import com.ctd.choose.view.WheelView
import com.ctd.choose.view.adapters.ArrayWheelAdapter
import com.ctd.cymanage.R

/**
 * Created by Administrator on 2018\1\8 0008.
 */

class ChooseDialog : Dialog {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {}

    interface ChooseDialogListen {
        fun check(first:Boolean,content:String)
    }


    class Builder(private val mContext: Context) : OnWheelClickedListener {

        private var mChooseDialogListen: ChooseDialogListen? = null
        private var mChooseDialog: ChooseDialog? = null
        private var layout: View? = null
        private var wheelview_one: WheelView? = null
        private var wheelview_two: WheelView? = null
        private var dialog_title: TextView? = null
        private var num: Int? = 1
        protected var mOneDatas: Array<String>? = null
        protected var mTwoDatas: Array<String>? = null
        protected var data:Map<String,Array<String>>? = null

        fun create(chooseDialogListen: ChooseDialogListen,num:Int,data:Map<String,Array<String>>,index:Array<Int>,title:String): ChooseDialog {

            this.mChooseDialogListen = chooseDialogListen
            this.num=if (num<=1) 1 else 2
            this.mOneDatas= data["one"]
            this.mTwoDatas= data["two"]

            val inflater = mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            mChooseDialog = ChooseDialog(mContext, R.style.MyDialog)


            mChooseDialog!!.setCanceledOnTouchOutside(true)

            layout = inflater.inflate(R.layout.dialog_choose, null)

            mChooseDialog!!.addContentView(layout!!, ViewGroup.LayoutParams(

                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

            initView(title)

            setUpData(index)

            return mChooseDialog as ChooseDialog
        }


        override fun onItemClicked(wheel: WheelView?, itemIndex: Int) {


            if (this.mChooseDialogListen !=null)
                this.mChooseDialogListen!!.check(wheel == wheelview_one,if (wheel == wheelview_one) mOneDatas!![itemIndex] else mTwoDatas!![itemIndex] )


            mChooseDialog?.dismiss()

        }

        private fun initView(title: String) {


            wheelview_one = layout!!.findViewById(R.id.wheelview_one) as WheelView
            dialog_title = layout!!.findViewById(R.id.dialog_title) as TextView

            if (num==2) {
                wheelview_two = layout!!.findViewById(R.id.wheelview_two) as WheelView
                wheelview_two!!.visibility=View.VISIBLE
                wheelview_one!!.addClickingListener(this)

            }

            dialog_title!!.text=title
            wheelview_one!!.setDrawShadows(false)
            wheelview_one!!.addClickingListener(this)

        }

        private fun setUpData(index: Array<Int>) {
            wheelview_one!!.setViewAdapter(ArrayWheelAdapter<String>(mContext, mOneDatas))
            // 设置可见条目数量   mOneDatas
            wheelview_one!!.visibleItems = 5

            //初始位置
            wheelview_one!!.currentItem = index[0]

            if (num==2) {

                wheelview_two!!.setViewAdapter(ArrayWheelAdapter<String>(mContext, mTwoDatas))
                wheelview_two!!.visibleItems = 7
                wheelview_two!!.currentItem = index[1]
            }

        }

    }
}