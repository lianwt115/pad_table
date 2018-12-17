package com.ctd.cymanage.network



import com.ctd.cymanage.bean.BaseUser
import com.ctd.cymanage.bean.HttpResult
import com.ctd.cymanage.network.ApiConst
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface ApiService {
    companion object{

        val IP = "192.168.2.10:9898"

        val BASE_URL_Api : String
            get() = "http://$IP/api/"
        val BASE_URL_Api_Download : String
            get() = "http://$IP/robotManagement/static/file/"
    }


    @Streaming
    @POST(ApiConst.Download)
    fun download(@Query("id") id:String): Observable<ResponseBody>


    @Streaming
    @GET("{filePath}")
    fun downloadPath(@Path("filePath") filePath:String): Observable<ResponseBody>

    @POST(ApiConst.USER_Login)
    fun userLogin(@Query("name") name:String,@Query("password") password:String,@Query("auto") auto:Boolean):Observable<HttpResult<BaseUser>>


}
