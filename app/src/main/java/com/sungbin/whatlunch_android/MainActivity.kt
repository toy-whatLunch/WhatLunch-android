package com.sungbin.whatlunch_android

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.messaging.FirebaseMessaging
import com.sungbin.whatlunch_android.base.HiltBaseActivity
import com.sungbin.whatlunch_android.databinding.ActivityMainBinding
import com.sungbin.whatlunch_android.util.Token
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : HiltBaseActivity<ActivityMainBinding, MainViewModel>(){
    override val layoutId: Int = R.layout.activity_main
    override val viewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onSupportNavigateUp(): Boolean {

        return !navController.navigateUp()
    }

    override fun initView(saveInstanceState: Bundle?) {

        setBottomNav()
        firebaseAuth = FirebaseAuth.getInstance()

        setPush()
        initPage(firebaseAuth.currentUser)

    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }

    /**
     * push 초기 설정
     */
    private fun setPush() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
        Token.register()
    }

    /**
     * 파이어베이스 유저 정보 있으면 곧 바로 로그인
     */
    private fun initPage(firebaseUser: FirebaseUser?){
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        if(firebaseUser != null){
            navGraph.setStartDestination(R.id.homeFragment)
        }else{
            navGraph.setStartDestination(R.id.loginFragment)
        }
        navController.setGraph(navGraph, null)
    }

    private fun setBottomNav(){
        // 뷰를 표시 할 프래그먼트 초기화
        val host = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = host.findNavController()

        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.setOnItemSelectedListener {
            if (it.itemId != binding.bottomNav.selectedItemId) {
                when (it.itemId) {
                    R.id.homeFragment -> {}

                    R.id.mapFragment -> {}

                    R.id.randomLunchFragment -> {}

                }

                NavigationUI.onNavDestinationSelected(it, navController)
            }

            return@setOnItemSelectedListener true
        }
        navController.addOnDestinationChangedListener(CustomDestinationChangedListener())
    }

    /**
     * 바텀 네비게이션 프래그먼트 이동 리스너 - 툴바 좌측/우측 버튼 표시 설정
     */
    private inner class CustomDestinationChangedListener :
        NavController.OnDestinationChangedListener {
        override fun onDestinationChanged(
            controller: NavController,
            destination: NavDestination,
            arguments: Bundle?,
        ) {
            // 뷰 바텀 레이아웃 visibility 설정
            setBottomLayoutVisibility(destination.id)
        }
    }

    /**
     * 뷰 바텀 레이아웃 visibility 설정
     */
    private fun setBottomLayoutVisibility(destinationId: Int) {
        when (destinationId) {
            R.id.loginFragment -> {
                binding.bottomNav.visibility = View.GONE
            }
            else -> {
                binding.bottomNav.visibility = View.VISIBLE
            }
        }
    }

}