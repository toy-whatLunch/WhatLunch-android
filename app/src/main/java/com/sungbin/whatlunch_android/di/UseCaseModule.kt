package com.sungbin.whatlunch_android.di

import com.sungbin.whatlunch_android.repository.SearchRepository
import com.sungbin.whatlunch_android.repository.UserRepository
import com.sungbin.whatlunch_android.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideRequestFirebaseTokenUseCase(repo: UserRepository) = PostSocialLoginUseCase(repo)

    @Provides
    fun provideUpdateFcmTokenUseCase(repo: UserRepository) = PutFcmTokenUseCase(repo)

    @Provides
    fun provideGetUserUseCase(repo: UserRepository) = GetUserUseCase(repo)
    // login
    @Provides
    fun provideIsKakaoTalkUseCase(repo: UserRepository) = IsKakaoTalkUseCase(repo)

    @Provides
    fun provideRunKakaoTalkLoginUseCase(repo: UserRepository) = RunKakaoTalkLoginUseCase(repo)

    @Provides
    fun provideRunKakaoWebLoginUseCase(repo: UserRepository) = RunKakaoWebLoginUseCase(repo)

    @Provides
    fun provideGetKakaoUserProfileUseCase(repo: UserRepository) = GetKakaoUserProfileUseCase(repo)

    // kakao
    @Provides
    fun provideGetSearchCategoryUseCase(repo: SearchRepository) = GetSearchCategoryUseCase(repo)
}