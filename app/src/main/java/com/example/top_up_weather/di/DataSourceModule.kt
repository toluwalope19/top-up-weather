package com.example.top_up_weather.di

import com.example.top_up_weather.data.remote.api.RemoteImpl

import com.example.top_up_weather.data.remote.api.RemoteSource

import dagger.Binds

import com.example.top_up_weather.repository.WeatherRepository

import com.example.top_up_weather.data.local.LocalDataSourceImpl

import com.example.top_up_weather.data.local.LocalDataSource
import com.example.top_up_weather.repository.Repository
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun provideDataSource(localDataSource: LocalDataSourceImpl?): LocalDataSource?
    @Binds
    abstract fun provideRepoImpl(repo: WeatherRepository?): Repository?

}