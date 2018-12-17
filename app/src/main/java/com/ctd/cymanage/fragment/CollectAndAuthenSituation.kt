package com.ctd.cymanage.fragment

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.ContextCompat
import com.ctd.cymanage.App
import com.ctd.cymanage.R
import com.ctd.cymanage.utils.DialogUtils
import com.ctd.cymanage.utils.UiUtils
import com.ctd.cymanage.utils.applySchedulers
import com.ctd.cymanage.widget.MyMarkerView
import com.ctd.cymanage.widget.PickDateDialog
import com.ctd.cymanage.widget.SelectBarView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.MPPointF
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_collectandanthensituation.*
import kotlinx.android.synthetic.main.fragment_login.*
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit

class CollectAndAuthenSituation:BaseFragment(), View.OnClickListener, OnChartValueSelectedListener {


    private var mTypeCollect = true

    private var ANIMTIME = animTime[1]

    override fun getLayoutResources(): Int {

        return R.layout.fragment_collectandanthensituation
    }

    override fun initView() {

        if (arguments != null){

            mTypeCollect = arguments!!.getBoolean("collect")

            changTitle(getString(R.string.paymentinfo_text11),false)

        }

        var arrayNow = App.instanceApp().getTimeNow()

        select_year_start.setContent("${arrayNow[0]-1}/${arrayNow[1]}/${arrayNow[2]}")

        select_year_start.setBarOnClickListener(object : SelectBarView.BarOnClickListener,
            PickDateDialog.PickDateDialogListen {
            override fun dataCheck(pickDateDialog: PickDateDialog?, content: String) {

                select_year_start.setContent(content)
            }

            override fun barViewClick() {

                DialogUtils.showPickDateDialogDialog(activity!!,select_year_start.getContent(),this)

            }
        })

        select_year_end.setContent("${arrayNow[0]}/${arrayNow[1]}/${arrayNow[2]}")

        select_year_end.setBarOnClickListener(object : SelectBarView.BarOnClickListener,
            PickDateDialog.PickDateDialogListen {
            override fun dataCheck(pickDateDialog: PickDateDialog?, content: String) {
                select_year_end.setContent(content)
            }

            override fun barViewClick() {

                DialogUtils.showPickDateDialogDialog(activity!!,select_year_end.getContent(),this)

            }
        })

        //登录按钮初始化
        bt_search.text = getString(R.string.paymentsituation_text3)

        bt_search.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_20dp_0)
        bt_search.setFinalCornerRadius(20f)

        bt_search.setOnClickListener(this)

        //导出按钮初始化
        bt_output.text = getString(R.string.paymentinfo_text1)

        bt_output.background = ContextCompat.getDrawable(activity!!, R.drawable.bg_20dp_1)
        bt_output.setFinalCornerRadius(20f)

        bt_output.setOnClickListener(this)

        //初始化Pie
        initPieChart()
        //初始化TypeBar
        initBarChart(barchart_type,3,2,colors[6],arrayString3)
        //初始化CountBar
        initBarChart(barchart_count,4,3,colors[5],arrayString1)


    }
    private fun initBarChart(barchart: BarChart,count: Int,showType:Int,colros:Int,arrays:Array<String>) {


        barchart.run {


            description.isEnabled = false

            //如果柱状图个数超过这个数则,则柱状图上的数值不显示
            setMaxVisibleValueCount(count+1)

            // scaling can now only be done on x- and y-axis separately
            setPinchZoom(false)

            setDrawBarShadow(false)

            setDrawGridBackground(false)

            //使用手势操作
            setTouchEnabled(true)

            //当数据被点击是显示的视图
            val mv = MyMarkerView(activity!!, R.layout.custom_marker_view,showType)

            // Set the marker to the chart
            mv.chartView = this

            marker = mv

            //多点触控
            isDragEnabled = false
            //缩放
            setScaleEnabled(false)
            val xAxis = xAxis

            xAxis.position = XAxis.XAxisPosition.BOTTOM

            xAxis.setDrawGridLines(false)

            //格式化x轴显示
            xAxis.valueFormatter = IAxisValueFormatter { value, axis ->

                if (value.toInt()>arrays.size)
                    ""
                else
                    arrays[value.toInt()]

            }

            xAxis.labelRotationAngle = -45f


            //共显示多少个条目
            xAxis.setLabelCount(count,false)

            //x轴间距
            xAxis.granularity = 1f

            axisLeft.setDrawGridLines(true)

            axisLeft.axisMinimum = 0f

            axisLeft.spaceTop = 30f
            //不绘制右边轴线
            axisRight.isEnabled = false

            setBarData(barchart,colros,count)
            // add a nice and smooth animation
            animateY(ANIMTIME)

            //数据描述
            legend.isEnabled = false

        }
    }

    private fun setBarData(barchart: BarChart, colros: Int, count: Int) {
        val values = java.util.ArrayList<BarEntry>()

        for (i in 0 until  count) {
            val multi = (Math.random()*100).toFloat()
            val value = (Math.random() * multi).toFloat() + multi / 3
            values.add(BarEntry(i.toFloat(), value))
        }

        val set1: BarDataSet

        if (barchart.data != null && barchart.data.dataSetCount > 0) {
            set1 = barchart.data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            barchart.data.notifyDataChanged()
            barchart.notifyDataSetChanged()
        } else {

            set1 = BarDataSet(values, "Data Set")

            set1.setColors(colros)

            set1.setDrawValues(true)

            set1.setValueFormatter { value, entry, dataSetIndex, viewPortHandler ->

                value.toInt().toString()

            }
            val dataSets = java.util.ArrayList<IBarDataSet>()
            dataSets.add(set1)

            val data = BarData(dataSets)
            barchart.data = data
            barchart.setFitBars(true)
        }

            barchart.invalidate()
    }



    private fun initPieChart() {
        piechart.setUsePercentValues(true)
        piechart.description.isEnabled = false
        piechart.setExtraOffsets(5f, 10f, 5f, 5f)

        piechart.dragDecelerationFrictionCoef = 0.95f

        //中间字体
        piechart.setCenterTextTypeface(tfLight)
        //中间文字
        piechart.centerText = generateCenterSpannableText()

        //中部相关设置
        piechart.isDrawHoleEnabled = true
        piechart.setHoleColor(Color.WHITE)

        piechart.setTransparentCircleColor(Color.WHITE)
        piechart.setTransparentCircleAlpha(110)

        //中部圆环内外半径
        piechart.holeRadius = 58f
        piechart.transparentCircleRadius = 61f

        piechart.setDrawCenterText(true)

        //是否旋转
        piechart.rotationAngle = 0f
        // enable rotation of the chart by touch
        piechart.isRotationEnabled = false
        piechart.isHighlightPerTapEnabled = true

        // chart.setUnit(" €");
        // chart.setDrawUnitsInChart(true);

        // add a selection listener
        piechart.setOnChartValueSelectedListener(this)

        setPieData(6, 10f)

        piechart.animateY(ANIMTIME, Easing.EaseInOutQuad)
        // chart.spin(2000, 0, 360);

        //数据说明
        val l = piechart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(true)
        l.xEntrySpace = 5f
        l.yEntrySpace = 0f
        l.yOffset = 10f

        //数据说明颜色
        piechart.setEntryLabelColor(Color.BLACK)
        piechart.setEntryLabelTypeface(tfRegular)
        piechart.setEntryLabelTextSize(12f)

    }

    private fun generateCenterSpannableText(): SpannableString {

        val s = SpannableString("楚天龙\n版权所有")
        s.setSpan(RelativeSizeSpan(1.7f), 0, 3, 0)

        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 4, s.length, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length - 4, s.length, 0)
        return s
    }

    private fun setPieData(count: Int, range: Float) {
        val entries = ArrayList<PieEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until  count) {
            entries.add(
                PieEntry(
                    (Math.random() * range + range / 5).toFloat(),
                    parties[i % parties.size],
                    resources.getDrawable(R.mipmap.star)
                )
            )
        }

        val dataSet = PieDataSet(entries, "")

        dataSet.setDrawIcons(false)

        //圆弧之间的空隙
        dataSet.sliceSpace = 3f

        dataSet.iconsOffset = MPPointF(0f, 40f)
        //选中后,弧形圆环扩大的半径
        dataSet.selectionShift = 10f

        // add a lot of colors

        dataSet.colors = colors
        //dataSet.setSelectionShift(0f);

        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter(DecimalFormat()))
        data.setValueTextSize(11f)
        //数据数值
        data.setValueTextColor(Color.BLACK)
        data.setValueTypeface(tfRegular)
        piechart.data = data

        // undo all highlights
        piechart.highlightValues(null)

        piechart.invalidate()
    }

    //表格数字选择
    override fun onNothingSelected() {

        Logger.e("Nothing selected.")


        //数据说明
        changTitle(getString(R.string.paymentinfo_text11))

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

        var index = h!!.x.toInt()
        //需要请求数据
        var value = (piechart.data.dataSet.getEntryForIndex(h.x.toInt()) as PieEntry).label

        Logger.e("Entry selected:${h.toString()} $value" )


        changTitle(value)

    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.bt_search -> {

                if (checkData(select_year_start.getContent(),select_year_end.getContent())) {

                    bt_search.startAnimation()

                    Observable.timer(2, TimeUnit.SECONDS).applySchedulers().subscribe({

                        bt_search.doneLoadingAnimation(resources.getColor(R.color.colorAccent), BitmapFactory.decodeResource(resources,if (true)R.mipmap.ic_done else R.mipmap.error ))

                        Observable.timer(1, TimeUnit.SECONDS).applySchedulers().subscribe({

                            bt_search.revertAnimation()


                            setPieData(5, 10f)

                            piechart.animateY(ANIMTIME, Easing.EaseInOutQuad)

                            changTitle(getString(R.string.paymentinfo_text11))



                        },{
                            Logger.e("按钮复原异常")
                        })


                    },{
                        Logger.e("按钮复原异常")
                    })

                }else{

                    UiUtils.showToast(getString(R.string.paymentinfo_text3))

                }
            }
            R.id.bt_output -> {

                bt_output.startAnimation()

                Observable.timer(2, TimeUnit.SECONDS).applySchedulers().subscribe({

                    bt_output.doneLoadingAnimation(resources.getColor(R.color.paymentinfo_color1), BitmapFactory.decodeResource(resources,if (true)R.mipmap.ic_done else R.mipmap.error ))

                    Observable.timer(1, TimeUnit.SECONDS).applySchedulers().subscribe({

                        bt_output.revertAnimation()


                    },{
                        Logger.e("按钮复原异常")
                    })


                },{
                    Logger.e("按钮复原异常")
                })
            }

        }
    }

    //数据说明
    private fun changTitle(plusString: String,initData:Boolean = true) {

        chart_des_2.text = getString(if (mTypeCollect) R.string.collectsituation_text1 else R.string.collectsituation_text3).plus("($plusString)")

        chart_des_3.text = getString(if (mTypeCollect) R.string.collectsituation_text2 else R.string.collectsituation_text4).plus("($plusString)")

        if (initData){

            //分区数据
            setBarData(barchart_type, colors[6], 3)
            // add a nice and smooth animation
            barchart_type.animateY(ANIMTIME)

            setBarData(barchart_count, colors[5], 4)
            // add a nice and smooth animation
            barchart_count.animateY(ANIMTIME)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bt_search != null)
            bt_search.dispose()
        if (bt_output != null)
            bt_output.dispose()
    }
}