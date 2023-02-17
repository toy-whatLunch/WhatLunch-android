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
import net.daum.android.map.MapViewEventListener
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import net.daum.mf.map.api.MapView.POIItemEventListener

@AndroidEntryPoint
class MapFragment : HiltBaseFragment<FragmentMapBinding, MapViewModel, NavArgs>(), MapViewEventListener {
    override val layoutId: Int = R.layout.fragment_map
    override val viewModel: MapViewModel by viewModels()
    override val navArgs: NavArgs by navArgs()

    private val requestPermissionList = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private val mapView by lazy { MapView(requireActivity()) }
    override fun initView(saveInstanceState: Bundle?) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .build()

        binding.mapView.addView(mapView)
        mapView.mapViewEventListener = this
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading

        getLastLocation()

        binding.locationBtn.setOnClickListener {
            checkLocation()
        }
    }

    override fun initDataBinding() {
    }

    override fun initAfterBinding() {

    }

    private fun setMapCenterPoint(lat: Double, lon: Double){
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lon), true)

        val marker = MapPOIItem()
        val mapPoint = MapPoint.mapPointWithGeoCoord(lat, lon)
        marker.apply {
            itemName = "현재 위치"
            tag = 0
            setMapPoint(mapPoint)
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
        }
        mapView.addPOIItem(marker)
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

                    viewModel.getSearchCategory(longitude, latitude)
                    setMapCenterPoint(lat = latitude, lon = longitude)
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
                setMapCenterPoint(lat = latitude, lon = longitude)
            }
        }
        fusedLocationProviderClient.lastLocation.addOnFailureListener {
            checkLocation()
        }
    }

    override fun onLoadMapView() {
       Log.d(LOG_TAG, "맵뷰등장")
    }
}