package com.ctd.cymanage.fragment


import android.os.Bundle
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.ctd.cymanage.R
import kotlinx.android.synthetic.main.fragment_devicemapinfo.*
import com.baidu.mapapi.map.MarkerOptions
import com.baidu.mapapi.map.BitmapDescriptorFactory
import com.ctd.cymanage.bean.MakerInfo
import com.baidu.mapapi.map.InfoWindow
import com.ctd.cymanage.bean.DeviceInfo
import com.ctd.cymanage.widget.MapMarketView
import com.ctd.cymanage.widget.MapShowWindowView


class DeviceMapInfo:BaseFragment() {


    private lateinit var mBaiduMap: BaiduMap

    private var mMarkerList = ArrayList<MakerInfo>()
    private var totalData = java.util.ArrayList<DeviceInfo>()
    override fun getLayoutResources(): Int {

         return R.layout.fragment_devicemapinfo

    }

    override fun initView() {

        initList()
        //不显示缩放控制
        bmapView.showZoomControls(false)

        mBaiduMap = bmapView.map

        mBaiduMap.setMaxAndMinZoomLevel(13f,11f)
       /*
        13: 2000;

        12: 5000;

        11: 10000;

        10: 20000;

        9: 25000;

        8: 50000;

        7: 100000;
            ;*/

        var uiSetting = mBaiduMap.uiSettings

        //不允许平移
        uiSetting.isScrollGesturesEnabled = true
        //不允许缩放
        uiSetting.isZoomGesturesEnabled = true
        //不允许旋转
        uiSetting.isRotateGesturesEnabled = false

        moveToLocation(29.41000,114.1011)

        for (makerInfo in mMarkerList) {
            addMaker(makerInfo)
        }

        map_center.setOnClickListener {

            moveToLocation(29.41000,114.1011)
        }

        mBaiduMap.setOnMarkerClickListener {

           showInfoWindow(it)

            true
        }
    }

    private fun initList() {

        mMarkerList.add(MakerInfo(29.41000,113.9011,"天城镇",0,3))
        mMarkerList.add(MakerInfo(29.41000,113.8011,"石城镇",0,6))
        mMarkerList.add(MakerInfo(29.31000,114.1011,"桂花泉镇",1,2))
        mMarkerList.add(MakerInfo(29.31000,113.6011,"白霓镇",0,1))
        mMarkerList.add(MakerInfo(29.21000,114.1011,"青山镇",1,1))
        mMarkerList.add(MakerInfo(29.29000,113.9011,"沙坪镇",2,2))
        mMarkerList.add(MakerInfo(29.26000,114.2011,"肖岭乡",0,5))



    }

    override fun onDestroyView() {
        super.onDestroyView()
        bmapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        bmapView.onPause()
    }

    override fun onResume() {
        super.onResume()

        bmapView.onResume()

    }

    //移动中心点到当前位置
    fun moveToLocation(latitude: Double, longitude: Double){

        val ll =  LatLng(latitude,longitude)
        val builder =  MapStatus.Builder()
        builder.target(ll).zoom(11.0f)
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))

    }

    fun addMaker(makerInfo: MakerInfo){

        val ll =  LatLng(makerInfo.latitude,makerInfo.longitude)

        var view = MapMarketView(activity!!)

        view.setContent(makerInfo.title,makerInfo.num,makerInfo.status)

        val bitmap = BitmapDescriptorFactory
            .fromView(view)

        //构建MarkerOption，用于在地图上添加Marker

        var buddle = Bundle()

        buddle.putParcelable("data",makerInfo)

        val option = MarkerOptions()
            .position(ll)
            .icon(bitmap)
            .alpha(1.0f)
            .perspective(true)
            .visible(true)
            .extraInfo(buddle)

        //在地图上添加Marker，并显示

        mBaiduMap.addOverlay(option)

    }

    fun showInfoWindow(maker: Marker){

        var data = maker.extraInfo.getParcelable("data") as MakerInfo

        var windowView =MapShowWindowView(activity!!)

        windowView.setContent(data.title.plus("(${data.num})"),data.imgPath,object :MapShowWindowView.WindowInfoClick{
            override fun windowClick(close: Boolean) {

                if (!close){

                    totalData.clear()

                    for (i in 1 .. data.num){

                        var status = when(data.status) {

                            0-> {
                                true
                            }

                            1 -> {
                                false
                            }
                            else ->{
                                null
                            }
                        }


                        var obj = DeviceInfo("965729852$i", data.title, arrayString1[i %4], status)

                        totalData.add(obj)

                    }

                    showDeviceDialog(totalData)
                }

                mBaiduMap.hideInfoWindow()
            }

        })

        //定义用于显示该InfoWindow的坐标点
        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        val mInfoWindow = InfoWindow(windowView, maker.position, -120)

        //显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow)

    }


}