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
import com.bumptech.glide.Glide
import com.ctd.cymanage.R
import com.ctd.cymanage.adapter.TextBgAdapter
import com.ctd.cymanage.bean.SimpleItemList
import com.ctd.cymanage.utils.UiUtils



/**
 * Created by Administrator on 2018\1\6 0006.
 */
 class MapShowWindowView(context: Context, attrs: AttributeSet?, defStyleAttr:Int) : RelativeLayout(context,attrs,defStyleAttr){

    private var mContext: Context = context
    private lateinit var mTVTitle: TextView
    private lateinit var mIMG: ImageView
    private lateinit var mIMGClose: ImageView


    constructor(context: Context,attrs: AttributeSet): this(context,attrs,0) {
        initView(context)
    }

    constructor(context: Context): this(context,null,0){
        initView(context)
    }

    private var mListen :WindowInfoClick?=null

    private fun initView(context: Context) {

        val view = View.inflate(context, R.layout.widget_mapshowwindowview, null)

        mTVTitle = view.findViewById(R.id.window_title) as TextView
        mIMG = view.findViewById(R.id.window_img) as ImageView
        mIMGClose = view.findViewById(R.id.close) as ImageView

        view.setOnClickListener{

            if (this.mListen!=null)
                this.mListen?.windowClick(false)
        }
        mIMGClose.setOnClickListener{

            if (this.mListen!=null)
                this.mListen?.windowClick(true)
        }


        addView(view, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT))

    }

    fun setContent(title:String?,imgPath:String,listen:WindowInfoClick?=null){

        if (!TextUtils.isEmpty(title))
            mTVTitle.text = title

        if (!TextUtils.isEmpty(imgPath))
            Glide.with(mContext).load(imgPath).into(mIMG)

        this.mListen = listen

    }

    interface WindowInfoClick{

        fun windowClick(close:Boolean)
    }









}