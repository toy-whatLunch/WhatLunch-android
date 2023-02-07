package com.sungbin.whatlunch_android

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.sungbin.whatlunch_android.base.HiltBaseActivity
import com.sungbin.whatlunch_android.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : HiltBaseActivity<ActivityMainBinding, MainViewModel>() {
    override val layoutId: Int = R.layout.activity_main
    override val viewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController

    override fun onSupportNavigateUp(): Boolean {

        return !navController.navigateUp()
    }

    override fun initView(saveInstanceState: Bundle?) {
        // 뷰를 표시 할 프래그먼트 초기화
        val host = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        navController = host!!.findNavController()
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }
}