package com.ctd.cymanage.network


import com.ctd.cymanage.bean.HttpResult
import com.ctd.cymanage.utils.UiUtils
import io.reactivex.functions.Function


class HttpResultFunc<T> : Function<HttpResult<T>, T> {

            @Throws(Exception::class)
            override fun apply(tHttpResult: HttpResult<T>): T {

                if (tHttpResult.code != 200) {
                    throw ApiException(tHttpResult.code, tHttpResult.message)
                }


                return tHttpResult.data
            }

}