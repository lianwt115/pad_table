package com.ctd.cymanage.widget

import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import android.widget.ImageButton
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
 class SelectBarView(context: Context, attrs: AttributeSet?, defStyleAttr:Int) : RelativeLayout(context,attrs,defStyleAttr),
   View.OnClickListener {

    private var mBarOnClickListener: BarOnClickListener? = null
    private var mContext: Context = context
    private lateinit var mTVType: TextView
    private lateinit var mTVContent: TextView
    private lateinit var mRvSelect: RelativeLayout
    private var mTypeName:String? = "类别"

    constructor(context: Context,attrs: AttributeSet): this(context,attrs,0) {
        initAttrs(context, attrs)
        initView(context)
    }

    constructor(context: Context): this(context,null,0)


    fun setBarOnClickListener(mBarOnClickListener: BarOnClickListener) {

        this.mBarOnClickListener = mBarOnClickListener

    }

    private fun initView(context: Context) {

        val view = View.inflate(context, R.layout.widget_selectbarview, null)

        mTVType = view.findViewById(R.id.selectbarview_type) as TextView
        mTVContent= view.findViewById(R.id.selectbarview_content) as TextView
        mRvSelect= view.findViewById(R.id.select_root) as RelativeLayout
        mTVType.text = mTypeName
        mRvSelect.setOnClickListener(this)

        addView(view, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT))

    }


    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.select_root-> {

                if (this.mBarOnClickListener !=null)

                    this.mBarOnClickListener!!.barViewClick()
            }

        }
    }

    fun setContent(contentName:String){

        mTVContent.text = contentName
    }
    fun getContent(): String {

        return mTVContent.text.toString()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {

        val ta = context.obtainStyledAttributes(attrs,
            R.styleable.SelectBarView)

        mTypeName = ta.getString(R.styleable.SelectBarView_typeName)

        ta.recycle()

    }

    interface BarOnClickListener {

        fun barViewClick()

    }

}