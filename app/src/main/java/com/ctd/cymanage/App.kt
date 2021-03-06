package com.ctd.cymanage

import android.app.Application
import android.content.Context
import android.icu.util.Calendar
import android.os.Environment
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import androidx.multidex.MultiDex
import com.baidu.mapapi.CoordType
import com.baidu.mapapi.SDKInitializer
import com.tencent.bugly.Bugly
import com.ctd.cymanage.activity.MainActivity
import com.ctd.cymanage.bean.BaseUser
import com.ctd.cymanage.greendao.DaoMaster
import com.ctd.cymanage.greendao.DaoSession
import com.tencent.bugly.beta.Beta


class App : Application() {

    private lateinit var daoSession: DaoSession
    private  var mLocalUser: BaseUser? = null
    private val APP_ID = "ae8304b0bc" // TODO 替换成bugly上注册的appid
    private lateinit var mYearArray: Array<String>
    private lateinit var mTimeNow: Array<Int>

    companion object {
        private var instance: App? = null
        fun instanceApp() = instance!!

    }


    init {

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initLog()

        initBuglyAndUP()

        initDb()

        initYearData()

        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this)
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.GCJ02)


    }


    private fun initDb(){

        // regular SQLite database
        val helper = DaoMaster.DevOpenHelper(instance, "cymanage")
        val db = helper.writableDb

        // encrypted SQLCipher database
        // note: you need to add SQLCipher to your dependencies, check the build.gradle file
        // DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db-encrypted");
        // Database db = helper.getEncryptedWritableDb("encryption-key");

        daoSession = DaoMaster(db).newSession()
    }

    fun getDaoSession(): DaoSession {
        return daoSession
    }

    private fun  initBuglyAndUP(){
        /***** Beta高级设置 *****/
        /**
         * true表示app启动自动初始化升级模块;
         * false不会自动初始化;
         * 开发者如果担心sdk初始化影响app启动速度，可以设置为false，
         * 在后面某个时刻手动调用Beta.init(getApplicationContext(),false);
         */
        Beta.autoInit = true

        /**
         * true表示初始化时自动检查升级;
         * false表示不会自动检查升级,需要手动调用Beta.checkUpgrade()方法;
         */
        Beta.autoCheckUpgrade = true

        /**
         * 设置升级检查周期为60s(默认检查周期为0s)，60s内SDK不重复向后台请求策略);
         */
        Beta.upgradeCheckPeriod = 3*60 * 1000

        /**
         * 设置启动延时为1s（默认延时3s），APP启动1s后初始化SDK，避免影响APP启动速度;
         */
        Beta.initDelay = 1 * 1000

        /**
         * 设置通知栏大图标，largeIconId为项目中的图片资源;
         */
        Beta.largeIconId = R.mipmap.ic_launcher_round

        /**
         * 设置状态栏小图标，smallIconId为项目中的图片资源Id;
         */
        Beta.smallIconId = R.mipmap.ic_launcher_round

        /**
         * 设置更新弹窗默认展示的banner，defaultBannerId为项目中的图片资源Id;
         * 当后台配置的banner拉取失败时显示此banner，默认不设置则展示“loading“;
         */
        Beta.defaultBannerId = R.mipmap.ic_launcher_round

        /**
         * 设置sd卡的Download为更新资源保存目录;
         * 后续更新资源会保存在此目录，需要在manifest中添加WRITE_EXTERNAL_STORAGE权限;
         */
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        /**
         * 点击过确认的弹窗在APP下次启动自动检查更新时会再次显示;
         */
        Beta.showInterruptedStrategy = true

        /**
         * 只允许在MainActivity上显示更新弹窗，其他activity上不显示弹窗;
         * 不设置会默认所有activity都可以显示弹窗;
         */
        Beta.canShowUpgradeActs.add(MainActivity::class.java)

        /***** 统一初始化Bugly产品，包含Beta *****/
        Bugly.init(applicationContext, APP_ID, BuildConfig.DEBUG)
    }


    private fun initLog() {
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .tag("CYLOG")   // (Optional) Global tag for every log. Default PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(object : AndroidLogAdapter(formatStrategy) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {

                return BuildConfig.DEBUG
            }
        })
    }

    fun setLocalUser(user: BaseUser){

        this.mLocalUser = user
    }

    fun getLocalUser(): BaseUser? {

        return this.mLocalUser
    }

    private fun initYearData() {
        val c = Calendar.getInstance()//
        val mCurrentYear= c.get(Calendar.YEAR) // 获取当前年份


        mYearArray = Array<String>(10) {

            (mCurrentYear-9+it).toString()

        }

        mTimeNow = arrayOf(mCurrentYear,c.get(Calendar.MONTH)+1,c.get(Calendar.DAY_OF_MONTH))

    }

    fun getCurrentYear(): String {

        return mYearArray[mYearArray.size-1]
    }

    fun getYearData(): Array<String> {

        return mYearArray
    }
    fun getTimeNow(): Array<Int> {

        return mTimeNow
    }
}