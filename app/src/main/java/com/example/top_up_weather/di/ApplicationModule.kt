package com.example.top_up_weather.di

import android.content.Context
import com.example.top_up_weather.BuildConfig
import com.example.top_up_weather.utils.interceptors.NetworkConnectivityInterceptor
import com.example.top_up_weather.utils.interceptors.NetworkResponseInterceptor
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
class ApplicationModule {

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL


    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context) = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(NetworkConnectivityInterceptor(context))
            .addInterceptor(NetworkResponseInterceptor())
            .build()
    } else OkHttpClient
        .Builder()
        .build()


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()



//    @Provides
//    @Singleton
//    fun provideApiService(retrofit: Retrofit) = retrofit.create(WeatherService::class.java)
//
//    @Provides
//    @Singleton
//    fun provideApiHelper(apiHelper: WeatherHelperImpl): WeatherHelper = apiHelper

}