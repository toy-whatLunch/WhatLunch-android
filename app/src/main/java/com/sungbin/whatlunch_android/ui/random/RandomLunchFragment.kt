package com.sungbin.whatlunch_android.ui.random

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.sungbin.whatlunch_android.R
import com.sungbin.whatlunch_android.base.HiltBaseFragment
import com.sungbin.whatlunch_android.databinding.FragmentRandomLunchBinding
import com.sungbin.whatlunch_android.network.data.KakaoData
import com.sungbin.whatlunch_android.ui.map.MapViewModel
import com.sungbin.whatlunch_android.util.LOG_TAG
import com.sungbin.whatlunch_android.util.UiState
import com.sungbin.whatlunch_android.util.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RandomLunchFragment : HiltBaseFragment<FragmentRandomLunchBinding, MapViewModel, NavArgs>() {
    override val layoutId: Int = R.layout.fragment_random_lunch
    override val viewModel: MapViewModel by viewModels()
    override val navArgs: NavArgs by navArgs()

    private var latitude = 0.0
    private var longitude = 0.0

    private val lunchList = mutableListOf<KakaoData.Document>()
    private var count = 0
    override fun initView(saveInstanceState: Bundle?) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .build()

        checkLocation()

        binding.randomBtn.setOnClickListener {
            count = 0

            for(i in 1..3) { // 카카오 검색 갯수 45개 제한 (15 * 3)
                viewModel.getSearchCategory(longitude, latitude, i)
            }
        }
    }

    override fun initDataBinding() {
        viewModel.kakaoData.asLiveData().observe(viewLifecycleOwner){
            when (it) {
                is UiState.Loading -> {
                    Log.d("UiState", "로딩")
                }
                is UiState.Empty -> {
                    Log.d("UiState", "상태 무")
                }
                is UiState.Success -> {
                    lunchList.addAll(it.data.documents)
                    count ++

                    if(count == 3){
                        binding.resultTv.text = lunchList.random().toString()
                    }

                }
                is UiState.Error -> {
                    count ++
                    Log.d("UiState", "에러 ${it.message}")
                }
            }
        }
    }

    override fun initAfterBinding() {

    }

    /**
     * 권한 확인
     */
    private fun checkLocation() {
        if (!Util.checkPermission(*requestPermissionList, context = requireActivity())) {
            requestPermissionResult.launch(requestPermissionList)
            return
        }
        updateLocation()
    }

    /**
     *  권한 결과
     */
    private val requestPermissionResult =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            // 권한 최초 승인 한 경우 위치 받음
            updateLocation()
            Log.d(LOG_TAG, "위치 권한 획득")
        }


    /**
     *  위치는 한번만 받으면 충분 (검색 시)
     */
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    removeLocation() // 위치를 한번 받은 후 remove()

                    for(i in 1..3) { // 카카오 검색 갯수 45개 제한 (15 * 3)
                        viewModel.getSearchCategory(longitude, latitude, i)
                    }
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun updateLocation() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private fun removeLocation(){
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(){
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            checkLocation()
        }
        fusedLocationProviderClient.lastLocation.addOnFailureListener {
            checkLocation()
        }
    }
}