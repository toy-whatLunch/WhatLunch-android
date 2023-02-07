package com.sungbin.whatlunch_android.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.NavArgs
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

abstract class HiltBaseFragment<T : ViewDataBinding, VM : HiltBaseViewModel, NA : NavArgs> :
    Fragment() {
    var navController: NavController? = null

    private var _binding: T? = null
    val binding get() = _binding!!

    abstract val layoutId: Int

    abstract val viewModel: VM
    abstract val navArgs: NavArgs

    abstract fun initView(saveInstanceState: Bundle?)
    abstract fun initDataBinding()
    abstract fun initAfterBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        try {
            navController = findNavController()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        initView(savedInstanceState)

        initDataBinding()
        initAfterBinding()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}