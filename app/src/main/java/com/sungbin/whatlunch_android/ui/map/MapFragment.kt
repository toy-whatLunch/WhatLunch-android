package com.sungbin.whatlunch_android.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.*
import com.sungbin.whatlunch_android.R
import com.sungbin.whatlunch_android.base.HiltBaseFragment
import com.sungbin.whatlunch_android.databinding.FragmentMapBinding
import com.sungbin.whatlunch_android.util.LOG_TAG
import com.sungbin.whatlunch_android.util.Util
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapFragment : HiltBaseFragment<FragmentMapBinding, MapViewModel, NavArgs>() {
    override val layoutId: Int = R.layout.fragment_map
    override val viewModel: MapViewModel by viewModels()
    override val navArgs: NavArgs by navArgs()

    private val requestPermissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    override fun initView(saveInstanceState: Bundle?) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .build()

        binding.locationBtn.setOnClickListener {
            checkLocation()
        }
    }

    override fun initDataBinding() {
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
                    val latitude = location.latitude
                    val longitude = location.longitude
                    binding.latitudeText.text = latitude.toString()
                    binding.longitudeText.text = longitude.toString()
                    removeLocation() // 위치를 한번 받은 후 remove()
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
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                val latitude = location.latitude
                val longitude = location.longitude
            }
        }
    }
}