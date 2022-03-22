package com.example.top_up_weather.di

import android.content.Context
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.room.Room
import com.example.top_up_weather.BuildConfig
import com.example.top_up_weather.data.local.LocalDataSource
import com.example.top_up_weather.data.local.db.WeatherDao
import com.example.top_up_weather.data.local.db.WeatherDatabase
import com.example.top_up_weather.data.remote.api.RemoteSource
import com.example.top_up_weather.data.remote.api.RemoteImpl
import com.example.top_up_weather.data.remote.api.WeatherService
import com.example.top_up_weather.repository.Repository
import com.example.top_up_weather.repository.WeatherRepository
import com.example.top_up_weather.utils.ApiError
import com.example.top_up_weather.utils.AppCoroutineDispatchers
import com.example.top_up_weather.utils.interceptors.NetworkConnectivityInterceptor
import com.example.top_up_weather.utils.interceptors.NetworkResponseInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL


    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(NetworkConnectivityInterceptor(context))
            .addInterceptor(NetworkResponseInterceptor())
            .callTimeout(10L, TimeUnit.MINUTES).build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson,okHttpClient: OkHttpClient, BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
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
    fun provideWeatherDAO(@NonNull weatherDatabase: WeatherDatabase): WeatherDao {
        return weatherDatabase.weatherDao()
    }


    @Provides
    fun provideDispatcher(): AppCoroutineDispatchers {
        return AppCoroutineDispatchers()
    }

    @Singleton
    @Provides
    fun provideRepository(
        dispatchers: AppCoroutineDispatchers,
        localDataSource: LocalDataSource,
        remoteSource: RemoteSource
    ): WeatherRepository{
        return WeatherRepository(dispatchers,remoteSource, localDataSource)
    }

}