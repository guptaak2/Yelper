package com.yelp.yelper.repository.retrofit

import com.example.yelp_td.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    private val PRIVATE_API_KEY_ARG = "Authorization"

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val headers = request.headers.newBuilder()
            .add(
                PRIVATE_API_KEY_ARG,
                BuildConfig.API_KEY_VALUE
            ).build()

        val requestBuilder = request.newBuilder().headers(headers)

        return chain.proceed(requestBuilder.build())
    }
}