package com.ctd.cymanage.fragment

import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.graphics.Typeface
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ctd.cymanage.App
import com.ctd.cymanage.R
import com.ctd.cymanage.adapter.ShowTextAdapter
import com.ctd.cymanage.bean.ShowData
import com.ctd.cymanage.utils.DialogUtils
import com.ctd.cymanage.utils.UiUtils
import com.ctd.cymanage.utils.applySchedulers
import com.ctd.cymanage.widget.ChooseDialog
import com.ctd.cymanage.widget.MyMarkerView
import com.ctd.cymanage.widget.PickDateDialog
import com.ctd.cymanage.widget.SelectBarView
import com.github.mikephil.charting.animation.Easing
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
import kotlinx.android.synthetic.main.fragment_paymentinfo.*
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class PaymentInfo:BaseFragment(), View.OnClickListener, OnChartValueSelectedListener, ShowTextAdapter.TextClickListen {


    private var ANIMTIME = animTime[1]
    private var mTypePayment = true
    private lateinit var mShowTextAdapter: ShowTextAdapter
    private  var mShowTextList = ArrayList<ShowData>()
    override fun getLayoutResources(): Int {

        return R.layout.fragment_paymentinfo
    }

    override fun initView() {

        if (arguments != null)

            mTypePayment = arguments!!.getBoolean("payment")

        chart_des_4.text = getString(if (mTypePayment) R.string.paymentinfo_text10 else R.string.paymentinfo_text13)
        tv_right.text = getString(if (mTypePayment) R.string.paymentinfo_text9 else R.string.paymentinfo_text13)


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

        select_client.setContent(arrayString1[0])

        select_client.setBarOnClickListener(object : SelectBarView.BarOnClickListener, ChooseDialog.ChooseDialogListen {
            override fun check(first: Boolean, content: String) {
                if (first)
                    select_client.setContent(content)
            }

            override fun barViewClick() {


                DialogUtils.showSelectOneDialog(activity!!,getString(R.string.paymentinfo_text2),arrayString1,select_client.getContent(),this)

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

        changeTitle(getString(R.string.paymentinfo_text11),false)

        //初始化Pie
        initPieChart()
        //初始化Bar
        initBarChart()
        //初始化list
        initRecycleList()
    }


    private fun initBarChart() {

        barchart.description.isEnabled = false

        //如果柱状图个数超过这个数则,则柱状图上的数值不显示
        barchart.setMaxVisibleValueCount(6)

        // scaling can now only be done on x- and y-axis separately
        barchart.setPinchZoom(false)

        barchart.setDrawBarShadow(false)

        barchart.setDrawGridBackground(false)

        //使用手势操作
        barchart.setTouchEnabled(true)

        //当数据被点击是显示的视图
        val mv = MyMarkerView(activity!!, R.layout.custom_marker_view,if (mTypePayment) 1 else 5)

        // Set the marker to the chart
        mv.chartView = barchart

        barchart.marker = mv

        //多点触控
        barchart.isDragEnabled = false
        //缩放
        barchart.setScaleEnabled(false)
        val xAxis = barchart.xAxis

        xAxis.position = XAxis.XAxisPosition.BOTTOM

        xAxis.setDrawGridLines(false)

        //x轴显示数组
        var array = if (mTypePayment) arrayString2 else arrayString7

        //格式化x轴显示
        xAxis.valueFormatter = IAxisValueFormatter { value, axis ->

            if (value.toInt()>array.size)
                ""
            else
                array[value.toInt()]

        }


        xAxis.labelRotationAngle =  -45f

        barchart.axisLeft.setDrawGridLines(true)

        barchart.axisLeft.axisMinimum = 0f

        barchart.axisLeft.spaceTop = 30f
        //不绘制右边轴线
        barchart.axisRight.isEnabled = false


        setBarData()
        // add a nice and smooth animation
        barchart.animateY(ANIMTIME)

        //数据描述
        barchart.legend.isEnabled = false

    }

    private fun setBarData(){
        val values = java.util.ArrayList<BarEntry>()

        for (i in 0 .. 4) {
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
            //set1.setColors(*ColorTemplate.VORDIPLOM_COLORS)
            set1.setColors(colors[5])

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

    private fun setPieData(count: Int, range: Float) {
        val entries = ArrayList<PieEntry>()

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (i in 0 until count) {
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


    private fun generateCenterSpannableText(): SpannableString {

        val s = SpannableString("楚天龙\n版权所有")
        s.setSpan(RelativeSizeSpan(1.7f), 0, 3, 0)

      /*  s.setSpan(StyleSpan(Typeface.NORMAL), 3, s.length - 3, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 3, s.length - 3, 0)
        s.setSpan(RelativeSizeSpan(.8f), 3, s.length - 3, 0)*/

        s.setSpan(StyleSpan(Typeface.ITALIC), s.length - 4, s.length, 0)
        s.setSpan(ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length - 4, s.length, 0)
        return s
    }

    //表格数字选择
    override fun onNothingSelected() {

        Logger.e("Nothing selected.")

        changeTitle(getString(R.string.paymentinfo_text11))

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {

        var index = h!!.x.toInt()
        //需要请求数据
        var value = (piechart.data.dataSet.getEntryForIndex(h.x.toInt()) as PieEntry).label

        Logger.e("Entry selected:${h.toString()} $value" )

        changeTitle(value)

    }

    private fun changeTitle(plusString: String, initData:Boolean = true){

        //区县选择 默认选择第一个
        chart_des_2.text = plusString

        chart_des_3.text = getString(if (mTypePayment) R.string.paymentinfo_text6 else R.string.paymentinfo_text12).plus("(${chart_des_2.text})")


        if (initData){

            initListData(plusString)

            setBarData()
            // add a nice and smooth animation
            barchart.animateY(ANIMTIME)

        }



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


                            setPieData(6, 10f)

                            piechart.animateY(ANIMTIME, Easing.EaseInOutQuad)

                            changeTitle(getString(R.string.paymentinfo_text11))


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


    private fun initRecycleList() {

        val linearLayoutManager = object : LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false){
            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }

        cashinfo_recyclerView.layoutManager=linearLayoutManager

        mShowTextAdapter = ShowTextAdapter(activity!!,mShowTextList,this)

        cashinfo_recyclerView.adapter= mShowTextAdapter

        cashinfo_recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.top = 0
                outRect.bottom = 0
                outRect.left = 0
                outRect.right = 0
            }
        })

        initListData(chart_des_2.text.toString())
    }

    private fun initListData(where:String){

        mShowTextList.clear()


        var weight = if (mTypePayment) 1000000000 else 100000

        for (i in 1 .. 8){

            mShowTextList.add(ShowData(where,i.toString(),where.plus("网点$i"), (Math.random()*weight).toInt()))

        }


        mShowTextAdapter.notifyDataSetChanged()

    }

    //list点击
    override fun textClick(content: ShowData, position: Int) {

        Logger.e(content.toString())
    }

    override fun onDestroy() {
        super.onDestroy()
        if (bt_search != null)
            bt_search.dispose()

        if (bt_output != null)
            bt_output.dispose()
    }

}