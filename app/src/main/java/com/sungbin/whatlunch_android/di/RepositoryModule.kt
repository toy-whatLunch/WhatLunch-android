package com.sungbin.whatlunch_android.di

import com.sungbin.whatlunch_android.network.api.NetworkService
import com.sungbin.whatlunch_android.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(networkService: NetworkService) = UserRepository(networkService)
}