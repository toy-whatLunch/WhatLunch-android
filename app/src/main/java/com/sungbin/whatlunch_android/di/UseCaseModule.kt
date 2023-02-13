package com.sungbin.whatlunch_android.di

import com.sungbin.whatlunch_android.repository.UserRepository
import com.sungbin.whatlunch_android.usecase.PostSocialLoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideRequestFirebaseTokenUseCase(repository: UserRepository) =
        PostSocialLoginUseCase(repository)
}