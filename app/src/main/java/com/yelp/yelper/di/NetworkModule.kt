package com.yelp.yelper.di

import android.content.Context
import com.example.yelp_td.BuildConfig
import com.yelp.yelper.ConnectivityChecker
import com.yelp.yelper.repository.retrofit.AuthInterceptor
import com.yelp.yelper.repository.retrofit.YelpApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    fun provideBaseUrl() = BuildConfig.YELP_BASE_URL

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addNetworkInterceptor(logging)
        }

        builder.addNetworkInterceptor(AuthInterceptor())
        return builder.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        BASE_URL: String
    ): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideYelpApi(retrofit: Retrofit): YelpApi = retrofit.create(YelpApi::class.java)

    @Provides
    @Singleton
    fun connectivityChecker(@ApplicationContext context: Context) = ConnectivityChecker(context)
}