package com.demo.searchimagedemo.di

import com.demo.searchimagedemo.BuildConfig
import com.demo.searchimagedemo.repository.service.RemoteService
import com.demo.searchimagedemo.utils.Constants
import com.demo.searchimagedemo.utils.PrintingEventFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
object NetworkModule {

    @Provides
    @Singleton
    @JvmStatic
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpBuilder: OkHttpClient.Builder = OkHttpClient.Builder()
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpBuilder.eventListenerFactory(PrintingEventFactory.FACTORY)
            okHttpBuilder.addInterceptor(loggingInterceptor)
        }
        okHttpBuilder.addInterceptor { chain ->
            val original = chain.request()
            val httpUrl = original.url
            val newUrl = httpUrl.newBuilder()
                .addQueryParameter("api_key", "3189212285dcb4cf5b2f044edcb0544e")
                .addQueryParameter("format", "json")
                .addQueryParameter("nojsoncallback", "1").build()
            val newRequest = original.newBuilder().url(newUrl).build()
            chain.proceed(newRequest)
        }
        return okHttpBuilder.build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideRemoteService(retrofitClient: Retrofit): RemoteService {
        return retrofitClient.create(RemoteService::class.java)
    }
}