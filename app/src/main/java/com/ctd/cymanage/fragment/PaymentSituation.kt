package com.ctd.cymanage.fragment

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.DashPathEffect
import android.view.View
import androidx.core.content.ContextCompat
import com.ctd.cymanage.App
import com.ctd.cymanage.R
import com.ctd.cymanage.R.id.bt_search
import com.ctd.cymanage.R.id.chart
import com.ctd.cymanage.utils.DialogUtils
import com.ctd.cymanage.utils.applySchedulers
import com.ctd.cymanage.widget.ChooseDialog
import com.ctd.cymanage.widget.MyMarkerView
import com.ctd.cymanage.widget.SelectBarView
import com.github.mikephil.charting.components.*
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IFillFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.Utils
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_paymentsituation.*
import java.util.ArrayList
import java.util.concurrent.TimeUnit

class PaymentSituation:BaseFragment(), View.OnClickListener, OnChartValueSelectedListener {

    private var ANIMTIME = animTime[1]

    private var dataValues = ArrayList<Entry>()
    private var numberValues = arrayListOf<Float>(
        6767f,
        5658f,
        7512f,
        6588f,
        7841f,
        5210f,
        8954f,
        6312f,
        8844f,
        5252f,
        7415f,
        7856f

    )

    override fun getLayoutResources(): Int {

        return R.layout.fragment_paymentsituation
    }

    override fun initView() {


        select_area.setContent(arrayString4[0])

        select_area.setBarOnClickListener(object :SelectBarView.BarOnClickListener, ChooseDialog.ChooseDialogListen {

            override fun check(first: Boolean, content: String) {

                if (first)
                    select_area.setContent(content)
            }

            override fun barViewClick() {

                DialogUtils.showSelectOneDialog(activity!!,getString(R.string.paymentsituation_text1),arrayString4,select_area.getContent(),this)
            }
        })

        select_year.setContent(App.instanceApp().getCurrentYear())

        select_year.setBarOnClickListener(object :SelectBarView.BarOnClickListener, ChooseDialog.ChooseDialogListen {
            override fun check(first: Boolean, content: String) {

                if (first)
                    select_year.setContent(content)
            }

            override fun barViewClick() {

                DialogUtils.showSelectOneDialog(activity!!,getString(R.string.paymentsituation_text2),App.instanceApp().getYearData(),select_year.getContent(),this)

            }
        })

        //查询按钮初始化
        bt_search.text = getString(R.string.paymentsituation_text3)

        bt_search.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_20dp_0)
        bt_search.setFinalCornerRadius(20f)

        bt_search.setOnClickListener(this)

        chart_des_1.text = "${select_year.getContent()} ${getString(R.string.paymentsituation_text2)} ${select_area.getContent()} --- ${getString(R.string.paymentsituation_text5)}"

        initChart()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.bt_search -> {

                bt_search.startAnimation()

                Observable.timer(1, TimeUnit.SECONDS).applySchedulers().subscribe({

                    bt_search.doneLoadingAnimation(resources.getColor(R.color.colorAccent), BitmapFactory.decodeResource(resources,if (true)R.mipmap.ic_done else R.mipmap.error ))

                    Observable.timer(1, TimeUnit.SECONDS).applySchedulers().subscribe({

                        bt_search.revertAnimation()

                        //重新设置数据
                        setData()

                        // 重新绘制图标
                        chart.animateX(ANIMTIME)

                        //重新描述
                        chart_des_1.text = "${select_year.getContent()} ${getString(R.string.paymentsituation_text2)} ${select_area.getContent()} --- ${getString(R.string.paymentsituation_text5)}"


                    },{
                        Logger.e("按钮复原异常")
                    })


                },{
                    Logger.e("按钮复原异常")
                })
            }

        }
    }

    private fun initChart() {

        //图标背景色
        chart.setBackgroundColor(Color.WHITE)
        //图标背景
        //chart.background = getDrawable(activity!!,R.drawable.bg_white_8)
        // 是否显示图标描述
        chart.description.isEnabled = false

        //使用手势操作
        chart.setTouchEnabled(true)

        //设置图标数据选择监听
        chart.setOnChartValueSelectedListener(this)
        //是否绘制区块背景
        chart.setDrawGridBackground(false)

        //当数据被点击是显示的视图
        val mv = MyMarkerView(activity!!, R.layout.custom_marker_view)

        // Set the marker to the chart
        mv.chartView = chart

        chart.marker = mv

        //多点触控
        chart.isDragEnabled = false
        //缩放
        chart.setScaleEnabled(false)
        // chart.setScaleXEnabled(true);
        // chart.setScaleYEnabled(true);

        // force pinch zoom along both axis
        chart.setPinchZoom(true)


        var xAxis: XAxis
        run {   // // X-Axis Style // //

            xAxis = chart.xAxis

            //数字坐标线
            xAxis.enableGridDashedLine(10f, 0f, 0f)

            //x轴描述的位置
            xAxis.position = XAxis.XAxisPosition.BOTTOM

            //格式化x轴显示
            xAxis.valueFormatter = IAxisValueFormatter { value, axis ->

                if (value == 1.0f)

                    ""
                else
                    "${value.toInt()-1}月"

            }



            //字体大小
            xAxis.textSize = 16F


            xAxis.mAxisMaximum = 13f

            xAxis.axisMinimum = 1f

            //共显示多少个条目
            xAxis.setLabelCount(12,false)

            //x轴间距
            xAxis.granularity = 1f

            //设置X轴上每个竖线是否显示
            xAxis.setDrawGridLines(true)

            //设置是否绘制X轴上的对应值(标签)
            xAxis.setDrawLabels(true)

            //字面意思
            xAxis.setAvoidFirstLastClipping(true)


        }

            var yAxis: YAxis
        run {   // // Y-Axis Style // //
            yAxis = chart.axisLeft

            //只绘制左边的y轴
            chart.axisRight.isEnabled = false

            // 绘制横向轴线
            //yAxis.enableGridDashedLine(10f, 0f, 0f)
            //不会只横向轴线
            yAxis.setDrawGridLines(false)
            // y轴的最大值  根据实际值确定

            //yAxis.axisMaximum = 5000f

            //最小值限制
            yAxis.axisMinimum = 0f

            //最大显示为最大值的130%
            yAxis.spaceTop = 30F

            //数值大小
            yAxis.textSize = 16F


        }


        //设置数据
        setData()

        // draw points over time
        chart.animateX(ANIMTIME)

        // get the legend (only possible after setting data)
        val l = chart.legend

        // draw legend entries as lines
        l.form = Legend.LegendForm.LINE
    }

    private fun setData() {


        initDataValues()


        val set1: LineDataSet

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as LineDataSet
            set1.values = dataValues
            set1.notifyDataSetChanged()
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {

            //设置图标说明
            set1 = LineDataSet(dataValues, "")

            set1.setDrawIcons(false)

            // 绘制连接线
            set1.enableDashedLine(10f, 0f, 0f)

            //连接线的颜色
            set1.color = resources.getColor(R.color.chart_color1)

            //点的颜色
            set1.setCircleColor(resources.getColor(R.color.chart_color1))

            //连接线 线宽
            set1.lineWidth = 1f

            //圆点的大小
            set1.circleRadius = 3f

            // 绘制实心点
            set1.setDrawCircleHole(false)

            // 说明
            set1.formLineWidth = 0f

            set1.formLineDashEffect = DashPathEffect(floatArrayOf(0f, 0f), 0f)

            set1.formSize = 0f

            // 数值大小
            set1.valueTextSize = 12f

            //数值颜色
            set1.valueTextColor = resources.getColor(R.color.chart_color1)


            //数值格式化
            /*set1.setValueFormatter { value, entry, dataSetIndex, viewPortHandler ->

                "${value}万元"

            }*/

            // 绘制选择的轴线
            set1.enableDashedHighlightLine(10f, 5f, 0f)

            //高亮线的颜色
            set1.highLightColor = resources.getColor(R.color.chart_color1)
            // 填充数据底部颜色
            set1.setDrawFilled(true)

            set1.fillFormatter = IFillFormatter { _, _ -> chart.axisLeft.axisMinimum }

            // 填充数据底部颜色
            if (Utils.getSDKInt() >= 18) {
                // drawables only supported on api level 18 and above
                val drawable = ContextCompat.getDrawable(activity!!, R.drawable.fade_blue)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.BLACK
            }

            //数据设置
            val dataSets = ArrayList<ILineDataSet>()

            dataSets.add(set1) // add the data sets

            // create a data object with the data sets
            val data = LineData(dataSets)

            // set data
            chart.data = data
        }
    }

    private fun initDataValues() {

            dataValues.clear()

            numberValues.shuffle()

            //dataValues.add(Entry(1f, numberValues[0], resources.getDrawable(R.mipmap.star)))
            dataValues.add(Entry(2f, numberValues[1]))
            dataValues.add(Entry(3f, numberValues[2]))
            dataValues.add(Entry(4f, numberValues[3]))
            dataValues.add(Entry(5f, numberValues[4]))
            dataValues.add(Entry(6f, numberValues[5]))
            dataValues.add(Entry(7f, numberValues[6]))
            dataValues.add(Entry(8f, numberValues[7]))
            dataValues.add(Entry(9f, numberValues[8]))
            dataValues.add(Entry(10f, numberValues[9]))
            dataValues.add(Entry(11f, numberValues[10]))
            dataValues.add(Entry(12f, numberValues[11]))
            dataValues.add(Entry(13f, numberValues[0]))


    }

    //表格数字选择
    override fun onNothingSelected() {

        Logger.e("Nothing selected.")
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

        Logger.e("Entry selected:${e.toString()}" )
        Logger.e("low:${chart.lowestVisibleX} , high:${chart.highestVisibleX}")
        Logger.e("xMin:${ chart.xChartMin} , xMax: ${chart.xChartMax} , yMin:${chart.yChartMin} , yMax: ${chart.yChartMax}")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bt_search != null)
            bt_search.dispose()
    }
}