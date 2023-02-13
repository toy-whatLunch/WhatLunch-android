package com.sungbin.whatlunch_android

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.sungbin.whatlunch_android.base.HiltBaseActivity
import com.sungbin.whatlunch_android.databinding.ActivityMainBinding
import com.sungbin.whatlunch_android.util.Token
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : HiltBaseActivity<ActivityMainBinding, MainViewModel>() {
    override val layoutId: Int = R.layout.activity_main
    override val viewModel: MainViewModel by viewModels()

    private lateinit var navController: NavController

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onSupportNavigateUp(): Boolean {

        return !navController.navigateUp()
    }

    override fun initView(saveInstanceState: Bundle?) {
        // 뷰를 표시 할 프래그먼트 초기화
        val host = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = host.findNavController()

        firebaseAuth = FirebaseAuth.getInstance()

        initPage(false)
    }

    override fun initDataBinding() {

    }

    override fun initAfterBinding() {

    }

    private fun initPage(isLogin: Boolean){
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
//        if(FirebaseAuth.getInstance().currenUser!= null)
        Log.d("파이어베이스 유저", firebaseAuth.currentUser.toString())
        if(isLogin){
            navGraph.setStartDestination(R.id.homeFragment)
        }else{
            navGraph.setStartDestination(R.id.loginFragment)
        }
        navController.setGraph(navGraph, null)
    }

}