package com.sungbin.whatlunch_android.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.sungbin.whatlunch_android.R
import com.sungbin.whatlunch_android.base.HiltBaseFragment
import com.sungbin.whatlunch_android.databinding.FragmentHomeBinding
import com.sungbin.whatlunch_android.util.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@AndroidEntryPoint
class HomeFragment : HiltBaseFragment<FragmentHomeBinding, HomeViewModel, NavArgs>() {
    override val layoutId: Int = R.layout.fragment_home
    override val viewModel: HomeViewModel by viewModels()
    override val navArgs: NavArgs by navArgs()

    private lateinit var firebaseAuth: FirebaseAuth
    override fun initView(saveInstanceState: Bundle?) {
        firebaseAuth = FirebaseAuth.getInstance()

        binding.logoutBtn.setOnClickListener {
            Firebase.auth.signOut()
            val action = HomeFragmentDirections.actionHomeFragmentToLoginFragment()
            findNavController().navigate(action)
        }
        binding.mapBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToMapFragment()
            findNavController().navigate(action)
        }
    }

    override fun initDataBinding() {
        viewModel.user.asLiveData().observe(viewLifecycleOwner){
            when (it) {
                is UiState.Loading -> {
                    Log.d("UiState", "로딩")
                }
                is UiState.Empty -> {
                    Log.d("UiState", "상태 무")
                }
                is UiState.Success -> {
                    viewModel.registerFcmToken(it.data.deviceInfo?.fcmToken)
                    Log.d("UiState", "데이터받음")
                }
                is UiState.Error -> {
                    Log.d("UiState", "에러 ${it.message}")
                }
            }
        }
    }

    override fun initAfterBinding() {
        viewModel.getUser()
    }

    private lateinit var backPressedDispatcher: OnBackPressedDispatcher
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
           requireActivity().finish()
        }
    }

    override fun onDetach() {
        super.onDetach()
        callback.remove()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callback.isEnabled = true
        backPressedDispatcher = requireActivity().onBackPressedDispatcher
        backPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

}