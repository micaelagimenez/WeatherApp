package com.project.astropaychallenge.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.project.astropaychallenge.BuildConfig.BASE_API_URL
import com.project.astropaychallenge.data.services.WeatherService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @WeatherApi
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, gson: Gson) = Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    @Provides
    fun provideGson() = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    fun provideOkHttpClient() = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    @Provides
    fun provideWeatherService(@WeatherApi retrofit: Retrofit) =
        retrofit.create(WeatherService::class.java)
}