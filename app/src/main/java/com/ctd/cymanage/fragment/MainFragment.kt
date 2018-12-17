package com.ctd.cymanage.fragment


import android.os.Bundle
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.ctd.cymanage.App
import com.ctd.cymanage.R
import com.ctd.cymanage.adapter.VPAdatpter
import com.ctd.cymanage.bean.EventInfo
import com.ctd.cymanage.bean.EventValue
import com.ctd.cymanage.bean.SimpleItemList
import com.ctd.cymanage.utils.RxBus
import com.ctd.cymanage.utils.applySchedulers
import com.ctd.cymanage.widget.BarView
import com.ctd.cymanage.widget.NoticeDialog
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.concurrent.TimeUnit


class MainFragment:BaseFragment(), BarView.BarOnClickListener{


    var app = App.instanceApp()


    private  var mTabs:ArrayList<String> = ArrayList<String>()

    private  var mFragments: ArrayList<androidx.fragment.app.Fragment> =  ArrayList()

    private  var mDataList = listOf<SimpleItemList>(

        SimpleItemList(app.getString(R.string.main_text2_1),true),
        SimpleItemList(app.getString(R.string.main_text2_2),false)

    ).toMutableList()

    private var currentIndex = 0
    private lateinit var mAdapter:VPAdatpter
    override fun getLayoutResources(): Int {

        return R.layout.fragment_main
    }

    override fun initView() {


        main_barview.setDataList(mDataList)

        main_barview.setBarOnClickListener(this)

        initTabList(0)

        initFragment0()
        //initFragment1()

        mAdapter = VPAdatpter(fragmentManager!!, mFragments, mTabs)

        vp_content.adapter = mAdapter

        tabs.setupWithViewPager(vp_content)

    }

    private fun initTabList(index: Int=0) {

        mTabs.clear()

        when (index) {
            0 -> {
                mTabs.add(getString(R.string.main_text5))
                mTabs.add(getString(R.string.main_text6))
                mTabs.add(getString(R.string.main_text7))
                mTabs.add(getString(R.string.main_text8))
                mTabs.add(getString(R.string.main_text9))
                mTabs.add(getString(R.string.main_text10))
            }

            1 -> {
                mTabs.add(getString(R.string.main_text11))
                mTabs.add(getString(R.string.main_text12))
            }
        }


    }

    //显示和隐藏,需更新那些数据
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)

        if (!hidden){

            val user = App.instanceApp().getLocalUser()

            main_barview.setUserName(user?.name)

            //返回第一页  并刷新数据
            Observable.timer(100,TimeUnit.MILLISECONDS).applySchedulers().subscribe({

                vp_content.currentItem = 0

            },{

                Logger.e(it.localizedMessage)

            })

        }

    }

    private fun initFragment0() {

        mFragments.clear()

        var paymentSituation: PaymentSituation = PaymentSituation()



        //第二页
        var paymentInfo: PaymentInfo = PaymentInfo()

        val paymentBundle = Bundle()

        paymentBundle.putBoolean("payment",true)

        paymentInfo.arguments = paymentBundle




        var collectSituation: CollectAndAuthenSituation = CollectAndAuthenSituation()

        val collectBundle = Bundle()

        collectBundle.putBoolean("collect",true)

        collectSituation.arguments = collectBundle




        var authenSituation: CollectAndAuthenSituation = CollectAndAuthenSituation()

        val authenBundle = Bundle()

        authenBundle.putBoolean("collect",false)

        authenSituation.arguments = authenBundle



        var giveCard: GiveCard = GiveCard()

        //第六页
        var printSituation: PaymentInfo = PaymentInfo()

        val printBundle = Bundle()

        printBundle.putBoolean("payment",false)

        printSituation.arguments = printBundle


        mFragments.add(paymentSituation as androidx.fragment.app.Fragment)

        mFragments.add(paymentInfo as androidx.fragment.app.Fragment)

        //设置参数
        mFragments.add(collectSituation as androidx.fragment.app.Fragment)

        mFragments.add(authenSituation as androidx.fragment.app.Fragment)


        mFragments.add(giveCard as androidx.fragment.app.Fragment)
        mFragments.add(printSituation as androidx.fragment.app.Fragment)


    }
    private fun initFragment1() {

        mFragments.clear()

        var deviceListInfo:DeviceListInfo = DeviceListInfo()
        var deviceMapInfo:DeviceMapInfo = DeviceMapInfo()


        mFragments.add(deviceListInfo as androidx.fragment.app.Fragment)
        mFragments.add(deviceMapInfo as androidx.fragment.app.Fragment)



    }

    override fun barViewClick(type: BarView.ClickType, index: Int, data: Any?) {

        when (type) {


            BarView.ClickType.RECYCLEVIEW_CLICK-> {

                var localData = data as SimpleItemList

                Logger.e("RECYCLEVIEW_CLICK $index  ${localData.content}")

                if (index == this.currentIndex)
                    return

                when (index) {

                    0-> {

                        initFragment0()
                        vp_content.setScroll(true)
                    }

                    1 -> {

                        initFragment1()
                        vp_content.setScroll(false)
                    }
                }

                initTabList(index)

                mAdapter = VPAdatpter(fragmentManager!!, mFragments, mTabs)

                vp_content.adapter = mAdapter


                this.currentIndex = index

            }

            BarView.ClickType.LOGIN_CLICK-> {


                showProgressDialog(getString(R.string.main_text3),true,3, object : NoticeDialog.Builder.BtClickListen {

                    override fun btClick(): Boolean {

                        dismissProgressDialog()
                        RxBus.getInstance()?.post(EventInfo(EventValue.LOGIN_PAGE,true,false))

                        return false
                    }

                }

                )

            }
        }
    }



}