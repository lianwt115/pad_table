package com.ctd.cymanage.widget

import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import com.ctd.cymanage.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.github.mikephil.charting.utils.Utils
import com.orhanobut.logger.Logger

/**
 * Custom implementation of the MarkerView.
 *
 *
 */
@SuppressLint("ViewConstructor")
class MyMarkerView(context: Context, layoutResource: Int,type:Int = 0) : MarkerView(context, layoutResource) {

    private var tvContent_top: TextView = findViewById(R.id.tvContent_top)
    private var tvContent_bottom: TextView = findViewById(R.id.tvContent_bottom)
    private var type = type
    private val arrayString1 = arrayOf("养老保险","医疗保险","工伤保险","失业保险","生育保险")
    private val arrayString2 = arrayOf("指静脉","指纹","人脸")
    private val arrayString3 = arrayOf("安卓终端","立式终端","桌面终端","柜台终端")
    private val arrayString4 = arrayOf("自助发卡","即时发卡","自助领卡")
    private val arrayString5 = arrayOf("养老信息","医疗信息","工伤信息","失业信息","生育信息")
    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry?, highlight: Highlight?) {

        //柱状图
        if (e is CandleEntry) {

            val ce = e as CandleEntry?

            tvContent_bottom.text = Utils.formatNumber(ce!!.high, 0, true)

            show(e)

        } else {

            show(e)
        }

        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {
        return MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
    }

    private fun show(e: Entry?) {

        when (type) {

            0 -> {

                tvContent_top.text = "${e!!.x.toInt()-1}月"
                tvContent_bottom.text = "缴费:${e!!.y}万元"

            }

            1 -> {

                tvContent_top.text = arrayString1[e!!.x.toInt()]

                tvContent_bottom.text = "笔数: ${e!!.y.toInt()}"

                Logger.e(e.toString())

            }
            2 -> {

                tvContent_top.text = arrayString2[e!!.x.toInt()]

                tvContent_bottom.text = "次数: ${e!!.y.toInt()}"

                Logger.e(e.toString())

            }
            3 -> {

                tvContent_top.text = arrayString3[e!!.x.toInt()]

                tvContent_bottom.text = "次数: ${e!!.y.toInt()}"

                Logger.e(e.toString())

            }
            4 -> {

                tvContent_top.text = arrayString4[e!!.x.toInt()]

                tvContent_bottom.text = "发卡数: ${e!!.y.toInt()}"

                Logger.e(e.toString())

            }

            5 -> {

                tvContent_top.text = arrayString5[e!!.x.toInt()]

                tvContent_bottom.text = "打印份数: ${e!!.y.toInt()}"

                Logger.e(e.toString())

            }
        }

    }
}
