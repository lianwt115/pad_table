package com.ctd.cymanage.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import com.ctd.cymanage.R
import com.ctd.cymanage.bean.DeviceInfo
import com.ctd.cymanage.utils.DeviceUtil
import tabledemo.ctl.com.from.core.SmartTable
import tabledemo.ctl.com.from.core.TableConfig
import tabledemo.ctl.com.from.data.CellInfo
import tabledemo.ctl.com.from.data.column.Column
import tabledemo.ctl.com.from.data.format.bg.IBackgroundFormat
import tabledemo.ctl.com.from.data.format.draw.IDrawFormat
import tabledemo.ctl.com.from.data.style.FontStyle
import tabledemo.ctl.com.from.data.table.PageTableData
import java.util.ArrayList


/**
 * Created by Administrator on 2018\1\8 0008.
 */

class DeviceDialog : Dialog {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {}

    class Builder(private val mContext: Context) {

        private var margin = 50
        private lateinit   var mColumnTitleStyle : FontStyle
        private var mDeviceDialog: DeviceDialog? = null
        private var mTitle: TextView? = null
        private var mLayout: View? = null
        private lateinit var mTable: SmartTable<DeviceInfo>
        private lateinit var mTableData: PageTableData<DeviceInfo>
        private  var mIBackgroundFormat : IBackgroundFormat = IBackgroundFormat { canvas, rect, paint ->
            paint.color = mContext.resources.getColor(R.color.login_text_color2)
            paint.style = Paint.Style.FILL
            canvas.drawRect(rect, paint)
        }
        fun create(data:ArrayList<DeviceInfo>): DeviceDialog {

            val inflater = mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            mDeviceDialog = DeviceDialog(mContext, R.style.MyDialog1)

            mDeviceDialog!!.setCanceledOnTouchOutside(true)

            mLayout = inflater.inflate(R.layout.dialog_device, null)

            mDeviceDialog!!.addContentView(mLayout!!, ViewGroup.LayoutParams(
                    DeviceUtil.dip2px(mContext, 900f),LinearLayout.LayoutParams.WRAP_CONTENT ))


            mTable = mLayout!!.findViewById(R.id.mytable_table) as SmartTable<DeviceInfo>
            mTitle = mLayout!!.findViewById(R.id.dialog_text) as TextView

            //数据分类初始化
            mColumnTitleStyle = FontStyle(mContext, 18, Color.parseColor("#ffffff"))

            initView(data)

            return mDeviceDialog as DeviceDialog
        }

        private var bg =object : IDrawFormat<Int> {

            override fun measureWidth(column: Column<Int>?, position: Int, config: TableConfig?): Int {
                return DeviceUtil.dip2px(mContext, 12f)
            }

            override fun measureHeight(column: Column<Int>?, position: Int, config: TableConfig?): Int {

                return DeviceUtil.dip2px(mContext, 20f)
            }

            override fun draw(c: Canvas?, rect: Rect?, cellInfo: CellInfo<Int>?, config: TableConfig?) {

                var normal = cellInfo?.data!!

                val paint = config!!.paint
                val color: Int = if (normal==1) R.color.paymentinfo_color1 else R.color.tips_color
                val content: String =  if (normal==1)  mContext.getString(R.string.devicelist_text6) else mContext.getString(R.string.devicelist_text4)

                paint.style = Paint.Style.FILL
                paint.color = ContextCompat.getColor(mContext, color)
                c!!.drawRect((rect!!.left + margin).toFloat(), (rect.top + 5).toFloat(), (rect.right - margin).toFloat(), (rect.bottom - 5).toFloat(), paint)

                paint.color = ContextCompat.getColor(mContext, R.color.white)
                paint.textSize = 26f
                c.drawText(content, ((rect.left + rect.right) / 2).toFloat(), ((rect.top + rect.bottom) / 2+10).toFloat(), paint)

            }

        }

        fun initView(data: ArrayList<DeviceInfo>) {

            mTitle!!.text = "设备详细情况(${data[0].where})"

                //列名
                val column1= Column<String>("设备编号", "no")
                //是否固定
                column1.isFixed=true
                val column2= Column<String>("设备类型", "type")
                column2.isFixed=true
                val column3= Column<Boolean>("设备状态", "onLine",object : IDrawFormat<Boolean> {

                    override fun measureWidth(column: Column<Boolean>?, position: Int, config: TableConfig?): Int {
                        return DeviceUtil.dip2px(mContext, 12f)
                    }

                    override fun measureHeight(column: Column<Boolean>?, position: Int, config: TableConfig?): Int {

                        return DeviceUtil.dip2px(mContext, 20f)
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
                                mContext.getString(R.string.devicelist_text1)
                            }

                            false -> {

                                mContext.getString(R.string.devicelist_text2)

                            }

                            else->{

                                mContext.getString(R.string.devicelist_text4)
                            }
                        }


                        paint.style = Paint.Style.FILL
                        paint.color = ContextCompat.getColor(mContext, color)
                        c!!.drawRect((rect!!.left + margin).toFloat(), (rect.top + 5).toFloat(), (rect.right - margin).toFloat(), (rect.bottom - 5).toFloat(), paint)

                        paint.color = ContextCompat.getColor(mContext, R.color.white)
                        paint.textSize = 26f
                        c.drawText(content, ((rect.left + rect.right) / 2).toFloat(), ((rect.top + rect.bottom) / 2+10).toFloat(), paint)

                    }

                })

                column3.isFixed=true


                val column4= Column<Int>("读卡功能", "fun1",bg)
                column4.isFixed=true
                val column5= Column<Int>("打印功能", "fun2",bg)
                column5.isFixed=true
                val column6 = Column<Int>("摄像头", "fun3",bg)
                column6.isFixed=true
                val column7 = Column<Int>("指纹仪", "fun4",bg)
                column7.isFixed=true
                val column8 = Column<Int>("指静脉", "fun5",bg)
                column8.isFixed=true


                mTableData = PageTableData("设备信息", data,data.size,0, column1, column2, column3, column4,column5,column6,column7,column7)

                //设置行标题背景
                mTable.config.columnTitleBackground = mIBackgroundFormat

                //行标题样式
                mTable.config.columnTitleStyle = mColumnTitleStyle

                mTable.tableData = mTableData


            }

    }
}