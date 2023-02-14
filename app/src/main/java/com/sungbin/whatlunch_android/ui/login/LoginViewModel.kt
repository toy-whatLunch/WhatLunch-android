package com.sungbin.whatlunch_android.ui.login

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.model.User
import com.sungbin.whatlunch_android.base.HiltBaseViewModel
import com.sungbin.whatlunch_android.network.data.LoginData
import com.sungbin.whatlunch_android.usecase.*
import com.sungbin.whatlunch_android.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val postSocialLoginUseCase: PostSocialLoginUseCase,
    private val isKakaoTalkUseCase: IsKakaoTalkUseCase,
    private val runKakaoTalkLoginUseCase: RunKakaoTalkLoginUseCase,
    private val runKakaoWebLoginUseCase: RunKakaoWebLoginUseCase,
    private val getKakaoUserProfileUseCase: GetKakaoUserProfileUseCase
): HiltBaseViewModel(){

    private val _token = MutableStateFlow<UiState<String>>(UiState.Loading)
    val token = _token.asStateFlow()

    // 1. 카카오 로그인 시도
    fun kakaoTalkLogin(context: Context, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            // 카카오톡이 깔려 있는지 ?
            val isKakaoTalk = isKakaoTalkUseCase.invoke(context)

            if (isKakaoTalk) {
                runKakaoTalkLoginUseCase.invoke(context) { token, error ->
                    if (error != null) {
                        onError()
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@invoke
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오 웹으로 로그인 시도
                        kakaoWebLogin(context, onSuccess, onError)
                    } else if (token != null) {
                        onSuccess()
                    }
                }
            } else {
                // 카카오 웹
                kakaoWebLogin(context, onSuccess, onError)
            }
        }
    }

    // 2. 카카오가 깔려있지 않으면 카카오 웹으로 로그인 시도
    private fun kakaoWebLogin(context: Context, onSuccess: () -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            runKakaoWebLoginUseCase.invoke(context) { token, error ->
                if (error != null) {
                    onError()
                } else if (token != null) {
                    onSuccess()
                }
            }
        }
    }

    // 3. 카카오 로그인 성공 후 사용자 정보 얻어 옴(이메일)
    fun getKakaoUserProfile(onSuccess: (User) -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            getKakaoUserProfileUseCase.invoke { user, error ->
                if (error != null) {
                    onError()
                } else if (user != null) {
                    onSuccess(user)
                }
            }
        }
    }

    // 4. 카카오 로그인 성공 후 서버에 커스텀 토큰 요청
    fun socialLogin(loginData: LoginData) = viewModelScope.launch {
        val result = postSocialLoginUseCase.invoke(loginData)

        _token.value = UiState.Loading
        result?.let {
            if(it.success){
                _token.value = UiState.Success(it.data as String)
            }else{
                _token.value = UiState.Error(it.msg)
            }
        }
        _token.value = UiState.Empty
    }
}