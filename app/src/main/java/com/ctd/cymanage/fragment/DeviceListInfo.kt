package com.ctd.cymanage.fragment

import android.graphics.*
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import com.ctd.cymanage.App
import com.ctd.cymanage.R
import com.ctd.cymanage.R.color.tips_color
import com.ctd.cymanage.bean.DeviceInfo
import com.ctd.cymanage.utils.DeviceUtil
import com.ctd.cymanage.utils.DialogUtils
import com.ctd.cymanage.utils.UiUtils
import com.ctd.cymanage.utils.applySchedulers
import com.ctd.cymanage.widget.ChooseDialog
import com.ctd.cymanage.widget.DeviceDialog
import com.ctd.cymanage.widget.SelectBarView
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_devicelistinfo.*
import tabledemo.ctl.com.from.core.SmartTable
import tabledemo.ctl.com.from.core.TableConfig
import tabledemo.ctl.com.from.data.CellInfo
import tabledemo.ctl.com.from.data.column.Column
import tabledemo.ctl.com.from.data.format.bg.IBackgroundFormat
import tabledemo.ctl.com.from.data.format.draw.IDrawFormat
import tabledemo.ctl.com.from.data.style.FontStyle
import tabledemo.ctl.com.from.data.table.PageTableData
import tabledemo.ctl.com.from.listener.OnColumnItemClickListener
import java.util.*
import java.util.concurrent.TimeUnit


class DeviceListInfo:BaseFragment(), View.OnClickListener {
    private lateinit   var mColumnTitleStyle : FontStyle
    private lateinit var mTable: SmartTable<DeviceInfo>
    private lateinit var mTableData: PageTableData<DeviceInfo>
    private  var mIBackgroundFormat : IBackgroundFormat = IBackgroundFormat { canvas, rect, paint ->
        paint.color = resources.getColor(R.color.login_text_color2)
        paint.style = Paint.Style.FILL
        canvas.drawRect(rect, paint)
    }
    private var totalData = ArrayList<DeviceInfo>()
    private var selectData = ArrayList<DeviceInfo>()
    private var whereData = ArrayList<String>()
    private var typeData = ArrayList<String>()
    private var statusData = ArrayList<Boolean?>()
    private var searchType = 0

    override fun getLayoutResources(): Int {

         return R.layout.fragment_devicelistinfo

    }

    override fun initView() {

        select_type.setContent(arrayString8[0])

        select_type.setBarOnClickListener(object : SelectBarView.BarOnClickListener, ChooseDialog.ChooseDialogListen {

            override fun check(first: Boolean, content: String) {

                if (first){

                    select_type.setContent(content)
                    searchType = arrayString8.indexOf(content)

                    et_search.visibility = if (searchType == 0) View.VISIBLE else View.GONE
                    tv_search.visibility = if (searchType == 0) View.GONE else View.VISIBLE
                }


            }

            override fun barViewClick() {

                DialogUtils.showSelectOneDialog(activity!!,getString(R.string.paymentsituation_text6),arrayString8,select_type.getContent(),this)
            }
        })

        //查询按钮初始化
        bt_search.text = getString(R.string.paymentsituation_text3)

        bt_search.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_20dp_0)
        bt_search.setFinalCornerRadius(20f)

        bt_search.setOnClickListener(this)

        //数据分类初始化
        mColumnTitleStyle = FontStyle(activity, 18, Color.parseColor("#ffffff"))

        mTable = mytable_table as SmartTable<DeviceInfo>

        initListData()

        initTable()

        tv_search.setOnClickListener {

            when (searchType) {

                1 -> {

                    var array = Array<String>(whereData.size){

                        whereData[it]
                    }

                    DialogUtils.showSelectOneDialog(activity!!,getString(R.string.paymentsituation_text6),array,whereData[0],object : ChooseDialog.ChooseDialogListen{
                        override fun check(first: Boolean, content: String) {
                            if (first)
                                tv_search.text = content
                        }

                    })
                }

                2 -> {

                    var array = Array<String>(typeData.size){

                        typeData[it]
                    }

                    DialogUtils.showSelectOneDialog(activity!!,getString(R.string.paymentsituation_text6),array,typeData[0],object : ChooseDialog.ChooseDialogListen{
                        override fun check(first: Boolean, content: String) {
                            if (first)
                                tv_search.text = content
                        }

                    })

                }

                3 -> {

                    var array = Array<String>(statusData.size){

                        when (statusData[it]) {
                            true -> {
                                getString(R.string.devicelist_text1)
                            }
                            false -> {
                                getString(R.string.devicelist_text2)
                            }

                            else -> {
                                getString(R.string.devicelist_text4)
                            }
                        }
                    }

                    DialogUtils.showSelectOneDialog(activity!!,getString(R.string.paymentsituation_text6),array,array[0],object : ChooseDialog.ChooseDialogListen{
                        override fun check(first: Boolean, content: String) {
                            if (first)
                                tv_search.text = content
                        }

                    })

                }
            }


        }
    }

    private fun initTable() {

        //列名
        val column1= Column<String>("       ${arrayString8[0]}       ", "no")
        //是否固定
        column1.isFixed=true
        val column2= Column<String>("       ${arrayString8[1]}       ", "where")
        column2.isFixed=true
        val column3= Column<String>("       ${arrayString8[2]}       ", "type")


        column3.isFixed=true
        val column4= Column<Boolean>("       ${arrayString8[3]}       ", "onLine",object : IDrawFormat<Boolean> {

            override fun measureWidth(column: Column<Boolean>?, position: Int, config: TableConfig?): Int {
                return DeviceUtil.dip2px(activity!!, 12f)
            }

            override fun measureHeight(column: Column<Boolean>?, position: Int, config: TableConfig?): Int {

                return DeviceUtil.dip2px(activity!!, 20f)
            }

            override fun draw(c: Canvas?, rect: Rect?, cellInfo: CellInfo<Boolean>?, config: TableConfig?) {

                var onLine = cellInfo?.data

                val paint = config!!.paint
                //val color: Int = if (onLine) R.color.paymentinfo_color1 else  R.color.bt_bg4
                val color: Int = when (onLine) {
                    true -> {

                        R.color.paymentinfo_color1

                    }

                    false -> {

                        R.color.top_bg_right

                    }

                    else->{

                        R.color.tips_color
                    }
                }


                val content: String = when (onLine) {
                    true -> {
                        getString(R.string.devicelist_text1)
                    }

                    false -> {

                        getString(R.string.devicelist_text2)

                    }

                    else->{

                        getString(R.string.devicelist_text4)
                    }
                }


                paint.style = Paint.Style.FILL
                paint.color = ContextCompat.getColor(activity!!, color)
                c!!.drawRect((rect!!.left + 100).toFloat(), (rect.top + 5).toFloat(), (rect.right - 100).toFloat(), (rect.bottom - 5).toFloat(), paint)

                paint.color = ContextCompat.getColor(activity!!, R.color.white)
                paint.textSize = 26f
                c.drawText(content, ((rect.left + rect.right) / 2).toFloat(), ((rect.top + rect.bottom) / 2+10).toFloat(), paint)

            }

        })

        column4.isFixed=true


        val column5 = Column<String>("       设备详情       ", "info",object : IDrawFormat<String> {

            override fun measureWidth(column: Column<String>?, position: Int, config: TableConfig?): Int {
                return DeviceUtil.dip2px(activity!!, 12f)
            }

            override fun measureHeight(column: Column<String>?, position: Int, config: TableConfig?): Int {

                return DeviceUtil.dip2px(activity!!, 20f)
            }

            override fun draw(c: Canvas?, rect: Rect?, cellInfo: CellInfo<String>?, config: TableConfig?) {

                var onLine = cellInfo?.data!!

                val paint = config!!.paint
                val color: Int =  R.color.paymentinfo_color1
                val content: String =  cellInfo.data

                paint.style = Paint.Style.FILL
                paint.color = ContextCompat.getColor(activity!!, color)
                c!!.drawRect((rect!!.left + 100).toFloat(), (rect.top + 5).toFloat(), (rect.right - 100).toFloat(), (rect.bottom - 5).toFloat(), paint)

                paint.color = ContextCompat.getColor(activity!!, R.color.white)
                paint.textSize = 26f
                c.drawText(content, ((rect.left + rect.right) / 2).toFloat(), ((rect.top + rect.bottom) / 2+10).toFloat(), paint)

            }

        })

        //点击
        column5.onColumnItemClickListener = OnColumnItemClickListener { column, value, t, position ->

            //进入养老四级界面  column 对象  value值   t值对象 position 列表位置   传递记录谓一致
           // UiUtils.showToast(column1.datas[position])
           // UiUtils.showToast(selectData[position].toString())
            showDeviceDialog(arrayListOf(selectData[position]))

        }

        column5.isFixed=true


        mTableData = PageTableData("设备信息", selectData,selectData.size,0, column1, column2, column3, column4,column5)

        //设置行标题背景
        mTable.config.columnTitleBackground = mIBackgroundFormat

        //行标题样式
        mTable.config.columnTitleStyle = mColumnTitleStyle

        mTable.tableData = mTableData

    }

    private fun initListData() {

        whereData.clear()
        typeData.clear()

        for (i in 0..89) {

            var  onLine:Boolean?

            var  typeName:String

            when (i%3) {
                0 -> {
                    onLine = true
                }
                1 -> {
                    onLine = false
                }
                else -> {
                    onLine = null
                }
            }

            var obj = DeviceInfo("965729852$i", "崇阳县A${i%4}", arrayString1[i%4], onLine)
            totalData.add(obj)

            if (!whereData.contains(obj.where))
                whereData.add(obj.where)

            if (!typeData.contains(obj.type))
                typeData.add(obj.type)

        }

        //对数据进行分类

        statusData.add(true)
        statusData.add(false)
        statusData.add(null)

        selectData = totalData
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.bt_search -> {

                if (TextUtils.isEmpty(et_search.text) && TextUtils.isEmpty(tv_search.text)){

                    UiUtils.showToast(getString(R.string.devicelist_text5))

                    return
                }

                bt_search.startAnimation()

                Observable.timer(1, TimeUnit.SECONDS).applySchedulers().subscribe({

                    bt_search.doneLoadingAnimation(resources.getColor(R.color.colorAccent), BitmapFactory.decodeResource(resources,if (true)R.mipmap.ic_done else R.mipmap.error ))

                    Observable.timer(1, TimeUnit.SECONDS).applySchedulers().subscribe({

                        bt_search.revertAnimation()

                        changeData()

                    },{
                        Logger.e("按钮复原异常")
                    })


                },{
                    Logger.e("按钮复原异常")
                })
            }

        }
    }

    private fun changeData() {

            when (searchType) {

                0 -> {

                    selectData = (totalData.filter {

                        it.no == et_search.text.toString()

                    }) as ArrayList<DeviceInfo>

                }

                1 -> {

                    selectData = (totalData.filter {

                        it.where == tv_search.text.toString()

                    }) as ArrayList<DeviceInfo>

                }

                2 -> {

                    selectData = (totalData.filter {

                        it.type == tv_search.text.toString()

                    }) as ArrayList<DeviceInfo>

                }

                3 -> {

                    selectData = (totalData.filter {

                        it.onLine ==  when (tv_search.text.toString()) {
                            getString(R.string.devicelist_text1) -> {
                                true
                            }
                            getString(R.string.devicelist_text2) -> {
                                false
                            }

                            else -> {
                                null
                            }
                        }

                    }) as ArrayList<DeviceInfo>

                }
            }


        if (selectData.size == 0){

            UiUtils.showToast(getString(R.string.devicelist_text3))

            selectData = totalData
        }

        initTable()

    }

}