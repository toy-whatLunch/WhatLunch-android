package com.sungbin.whatlunch_android.di

import com.sungbin.whatlunch_android.BuildConfig
import com.sungbin.whatlunch_android.network.api.KakaoLocalService
import com.sungbin.whatlunch_android.network.api.NetworkService
import com.sungbin.whatlunch_android.network.interceptor.NetworkInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Qualifier
    annotation class WhatLunch

    @Qualifier
    annotation class Kakao

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor{
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    @WhatLunch
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient{
        return OkHttpClient.Builder()
            .addNetworkInterceptor(httpLoggingInterceptor)
            .addInterceptor(NetworkInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideGsonConverterFactory(): GsonConverterFactory{
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    @WhatLunch
    fun provideRetrofit(
        @WhatLunch okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Provides
    @Singleton
    fun provideNetworkService(@WhatLunch retrofit: Retrofit): NetworkService{
        return retrofit.create(NetworkService::class.java)
    }

    //kakao
    @Provides
    @Singleton
    @Kakao
    fun provideKakaoOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient{
        return OkHttpClient.Builder()
            .addNetworkInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    @Kakao
    fun provideKakaoRetrofit(
        @Kakao kakaoOkHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.KAKAO_BASE_URL)
            .client(kakaoOkHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()

    @Provides
    @Singleton
    fun provideKakaoService(@Kakao kakaoRetrofit: Retrofit): KakaoLocalService{
        return kakaoRetrofit.create(KakaoLocalService::class.java)
    }

}