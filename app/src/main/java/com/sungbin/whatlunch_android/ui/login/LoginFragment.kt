package com.sungbin.whatlunch_android.ui.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.sungbin.whatlunch_android.R
import com.sungbin.whatlunch_android.base.HiltBaseFragment
import com.sungbin.whatlunch_android.databinding.FragmentLoginBinding
import com.sungbin.whatlunch_android.network.data.LoginData
import com.sungbin.whatlunch_android.util.LOG_TAG
import com.sungbin.whatlunch_android.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class LoginFragment : HiltBaseFragment<FragmentLoginBinding, LoginViewModel, NavArgs>() {
    override val layoutId: Int = R.layout.fragment_login
    override val viewModel: LoginViewModel by viewModels()
    override val navArgs: NavArgs by navArgs()

    private lateinit var firebaseAuth: FirebaseAuth

    override fun initView(saveInstanceState: Bundle?) {
        firebaseAuth = FirebaseAuth.getInstance()

        binding.kakaoLoginBtn.setOnClickListener {
            kakaoLogin()
        }

        binding.logoutBtn.setOnClickListener {
            firebaseAuth.currentUser?.delete()
        }

    }

    override fun initDataBinding() {
        viewModel.token.asLiveData().observe(viewLifecycleOwner){
            when (it) {
                is UiState.Loading -> {
                    Log.d("UiState", "로딩")
                }
                is UiState.Empty -> {
                    Log.d("UiState", "상태 무")
                }
                is UiState.Success -> {
                    registerTokenToFirebase(it.data)
                    Log.d("UiState", "데이터받음")
                }
                is UiState.Error -> {
                    Log.d("UiState", "에러 ${it.message}")
                }
            }
        }
    }

    override fun initAfterBinding() {

    }

    /**
     * 카카오 로그인
     */
    private fun kakaoLogin() {
        viewModel.kakaoTalkLogin(requireActivity(), {
            viewModel.getKakaoUserProfile({ user ->
                // 이메일과 닉네임은 필수 동의
                Log.d(LOG_TAG,"이메일 : ${user.kakaoAccount?.email!!}")
                Log.d(LOG_TAG,"닉네임 : ${user.kakaoAccount!!.profile?.nickname!!}")
                Log.d(LOG_TAG,"프로바이더  : kakao")
                Log.d(LOG_TAG, "uid : ${user.id}")
                val data = LoginData(user.kakaoAccount?.email!!, user.kakaoAccount!!.profile?.nickname!!, "kakao", "${user.id}")
                viewModel.socialLogin(data)
            },
                {Log.e(LOG_TAG, "카카오 로그인 실패")})
        },
            {Log.e(LOG_TAG, "카카오 로그인 실패")})
    }

    /**
     * firebase 에 네이버/카카오 토큰 등록
     */
    private fun registerTokenToFirebase(customToken: String) {
        firebaseAuth.signInWithCustomToken(customToken)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("파이어베이스", "성공")
//                    updateUI(firebaseAuth.currentUser)
                    CoroutineScope(Dispatchers.IO).launch {
                        val token: String? = firebaseAuth.currentUser?.getIdToken(false)?.await()?.token
                        Log.d("파이어베이스 토큰", token.toString())
                    }
                } else {
//                    updateUI()
                }
            }
    }

}