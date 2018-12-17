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
import com.ctd.cymanage.utils.DeviceUtil
import com.ctd.cymanage.utils.UiUtils



/**
 * Created by Administrator on 2018\1\6 0006.
 */
 class BarView(context: Context,attrs: AttributeSet?, defStyleAttr:Int) : RelativeLayout(context,attrs,defStyleAttr),
    TextBgAdapter.TextClickListen, View.OnClickListener {

    private var mBarOnClickListener: BarOnClickListener? = null
    private var mContext: Context = context
    private lateinit var mTextBgAdapter: TextBgAdapter
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mTVUser: TextView
    private lateinit var mTVVersion: TextView

    enum class ClickType(var type: Int){
        RECYCLEVIEW_CLICK(0),LOGIN_CLICK(1)
    }

    private  var mAdapterList =ArrayList<SimpleItemList>()



    constructor(context: Context,attrs: AttributeSet): this(context,attrs,0) {
        initAttrs(context, attrs)
        initView(context)
    }

    constructor(context: Context): this(context,null,0)


    fun setBarOnClickListener(mBarOnClickListener: BarOnClickListener) {

        this.mBarOnClickListener = mBarOnClickListener

    }

    private fun initView(context: Context) {

        val view = View.inflate(context, R.layout.widget_barview, null)

        mRecyclerView = view.findViewById(R.id.barview_recyclerView) as RecyclerView
        mTVUser = view.findViewById(R.id.barview_user) as TextView
        mTVVersion = view.findViewById(R.id.versionname) as TextView
        mTVUser.setOnClickListener(this)
        initRecycleList()

        addView(view, RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT))

    }

    private fun initRecycleList() {

          val linearLayoutManager = object : LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false){
              override fun canScrollHorizontally(): Boolean {
                  return false
              }
          }

        mRecyclerView.layoutManager=linearLayoutManager

        mTextBgAdapter= TextBgAdapter(mContext,mAdapterList,this)

        mRecyclerView.adapter= mTextBgAdapter

        mRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = 0
                outRect.bottom = 0
                outRect.left = 20
                outRect.right = 40
            }
        })

    }

    fun setDataList(list:List<SimpleItemList>){

        this.mAdapterList.clear()

        this.mAdapterList.addAll(list)

        mTextBgAdapter.notifyDataSetChanged()

    }

    fun setUserName(name:String?){

        if (!TextUtils.isEmpty(name))
            mTVUser.text = name

        var  versionName = DeviceUtil.getVersionName(mContext)

        if (!TextUtils.isEmpty(versionName))
            mTVVersion.text = versionName
    }


    override fun textClick(content: SimpleItemList, position: Int) {

       if (this.mBarOnClickListener !=null)

           this.mBarOnClickListener!!.barViewClick(ClickType.RECYCLEVIEW_CLICK,position,content)


    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.barview_user-> {

                if (this.mBarOnClickListener !=null)

                    this.mBarOnClickListener!!.barViewClick(ClickType.LOGIN_CLICK)
            }

        }
    }


    private fun initAttrs(context: Context, attrs: AttributeSet?) {

        val ta = context.obtainStyledAttributes(attrs,
                R.styleable.BarView)


        ta.recycle()

    }


    interface BarOnClickListener {

        fun barViewClick(type:ClickType,index:Int=0,data:Any?=null)

    }

}