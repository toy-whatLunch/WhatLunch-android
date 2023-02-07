package com.sungbin.whatlunch_android.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class HiltBaseActivity<T : ViewDataBinding, VM : HiltBaseViewModel> : AppCompatActivity() {
    lateinit var binding: T
    abstract val layoutId: Int
    abstract val viewModel: VM

    abstract fun initView(saveInstanceState: Bundle?)
    abstract fun initDataBinding()
    abstract fun initAfterBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this@HiltBaseActivity

        initView(savedInstanceState)
        initDataBinding()
        initAfterBinding()
    }
}