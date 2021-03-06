package com.example.top_up_weather.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.example.top_up_weather.AppCoroutineDispatcher
import com.example.top_up_weather.BuildConfig
import com.example.top_up_weather.data.local.LocalDataSource
import com.example.top_up_weather.data.local.WeatherDatabase
import com.example.top_up_weather.data.local.WeatherDao
import com.example.top_up_weather.data.remote.api.RemoteImpl
import com.example.top_up_weather.data.remote.api.RemoteSource
import com.example.top_up_weather.data.remote.api.WeatherService
import com.example.top_up_weather.repository.WeatherRepository
import com.example.top_up_weather.utils.ApiError
import com.example.top_up_weather.utils.interceptors.NetworkConnectivityInterceptor
import com.example.top_up_weather.utils.interceptors.NetworkResponseInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(WeatherService::class.java)

    @Provides
    @Singleton
    fun provideApiHelper(apiHelper: RemoteImpl): RemoteSource = apiHelper

    @Provides
    @Singleton
    fun provideApiErrorClass(gson: Gson, @ApplicationContext context: Context): ApiError {
        return ApiError(gson, context)
    }

    @Provides
    @Singleton
    fun provideGSon(): Gson = GsonBuilder().create()

    @Singleton
    @Provides
    fun provideWeatherDb(@ApplicationContext context: Context): WeatherDatabase {
        return Room.databaseBuilder(
            context,
            WeatherDatabase::class.java,
            WeatherDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideWeatherDAO(weatherDatabase: WeatherDatabase): WeatherDao {
        return weatherDatabase.weatherDao()
    }

    @Singleton
    @Provides
    fun provideRepository(
        appCoroutineDispatcher: AppCoroutineDispatcher, localDataSource: LocalDataSource,
        remoteSource: RemoteSource
    ): WeatherRepository {
        return WeatherRepository(appCoroutineDispatcher, remoteSource, localDataSource)
    }

    @Provides
    fun provideDispatcher(): AppCoroutineDispatcher {
        return AppCoroutineDispatcher()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences("TOP_UP", Context.MODE_PRIVATE)
}