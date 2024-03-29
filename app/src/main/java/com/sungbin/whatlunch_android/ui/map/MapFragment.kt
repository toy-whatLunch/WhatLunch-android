package com.sungbin.whatlunch_android.ui.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.google.android.gms.location.*
import com.sungbin.whatlunch_android.R
import com.sungbin.whatlunch_android.base.HiltBaseFragment
import com.sungbin.whatlunch_android.databinding.FragmentMapBinding
import com.sungbin.whatlunch_android.network.data.KakaoData
import com.sungbin.whatlunch_android.util.LOG_TAG
import com.sungbin.whatlunch_android.util.UiState
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

    private val mapView by lazy { MapView(requireActivity()) }

    private var latitude = 0.0
    private var longitude = 0.0
    override fun initView(saveInstanceState: Bundle?) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .build()

        binding.mapView.addView(mapView)
        mapView.mapViewEventListener = this
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading
        mapView.setPOIItemEventListener(markerEventListener)

        binding.locationBtn.setOnClickListener {
            checkLocation()
        }

        binding.keywordEditView.setOnEditorActionListener { v, actionId, keyEvent ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH){
                mapView.removeAllPOIItems()
                viewModel.getSearchKeyword(v.text.toString(), longitude, latitude)
                true
            }else{
                false
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
                    it.data.documents.forEach{
                        setMapCenterPoint(lat = it.y.toDouble(), lon = it.x.toDouble(), name = it.place_name, it)
                    }
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

    private fun setMapCenterPoint(lat: Double, lon: Double, name: String, kakaoData: KakaoData.Document){
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lon), true)

        val marker = MapPOIItem()
        val mapPoint = MapPoint.mapPointWithGeoCoord(lat, lon)
        marker.apply {
            itemName = name
            tag = 0
            setMapPoint(mapPoint)
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
            userObject = kakaoData
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
                    latitude = location.latitude
                    longitude = location.longitude
                    binding.latitudeText.text = latitude.toString()
                    binding.longitudeText.text = longitude.toString()
                    removeLocation() // 위치를 한번 받은 후 remove()

                    mapView.removeAllPOIItems()
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

    override fun onLoadMapView() {
        getLastLocation()
    }

    private val markerEventListener = object : MapView.POIItemEventListener{
        override fun onPOIItemSelected(mapView: MapView?, marker: MapPOIItem?) {
            marker?.let {
                Log.d(LOG_TAG, it.itemName)
                val url = (it.userObject as KakaoData.Document).place_url

                Log.d(LOG_TAG, url)
            }
        }

        override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
        }

        override fun onCalloutBalloonOfPOIItemTouched(
            p0: MapView?,
            p1: MapPOIItem?,
            p2: MapPOIItem.CalloutBalloonButtonType?
        ) {

        }

        override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
        }

    }
}