package com.ctd.cymanage.download


import okhttp3.Interceptor
import okhttp3.Response

class DownloadInterceptor(downloadListener: DownloadListen): Interceptor {

    private var downloadListener: DownloadListen =downloadListener

    override fun  intercept(chain: Interceptor.Chain?): Response {

        var response = chain?.proceed(chain.request())


        return response?.newBuilder()?.body(
                 DownloadResponseBody(response.body()!!, downloadListener)
        )!!.build()
    }

}