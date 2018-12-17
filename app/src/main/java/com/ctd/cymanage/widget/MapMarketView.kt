package com.ctd.cymanage.widget

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ctd.cymanage.R
import com.ctd.cymanage.adapter.TextBgAdapter
import com.ctd.cymanage.bean.SimpleItemList
import com.ctd.cymanage.utils.UiUtils



/**
 * Created by Administrator on 2018\1\6 0006.
 */
 class MapMarketView(context: Context, attrs: AttributeSet?, defStyleAttr:Int) : RelativeLayout(context,attrs,defStyleAttr){

    private var mContext: Context = context
    private lateinit var mTVMaker: TextView
    private lateinit var mTVMakerNum: TextView
    private lateinit var mIMGMaker: ImageView


    constructor(context: Context,attrs: AttributeSet): this(context,attrs,0) {
        initView(context)
    }

    constructor(context: Context): this(context,null,0){
        initView(context)
    }


    private fun initView(context: Context) {

        val view = View.inflate(context, R.layout.widget_mapmarketview, null)

        mTVMaker = view.findViewById(R.id.tv_maker) as TextView
        mTVMakerNum = view.findViewById(R.id.tv_maker_num) as TextView
        mIMGMaker = view.findViewById(R.id.net_maker) as ImageView

        addView(view, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT))

    }

    fun setContent(name:String?,num:Int,status:Int = 0){

        if (!TextUtils.isEmpty(name))
            mTVMaker.text = name

        if (num>0)
            mTVMakerNum.text = num.toString()


        when (status) {

            1 -> {
                mIMGMaker.setImageDrawable(mContext.getDrawable(R.mipmap.net_maker_off1))
            }

            2 -> {
                mIMGMaker.setImageDrawable(mContext.getDrawable(R.mipmap.net_maker_err1))
            }

        }
    }









}