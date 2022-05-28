package com.voltero

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.voltero.databinding.ActivityMainBinding
import com.mapbox.maps.MapboxMap
import com.voltero.databinding.ActivityMapMainKotlinBinding

class MapMainKotlin : AppCompatActivity() {
    /* private val navigationLocationProvider = NavigationLocationProvider()
     private val locationRequest = LocationRequest.create()

     private var REQUEST_CODE_LOCATION_PERMISSION = 1

     private val locationObserver = object : LocationObserver {

         override fun onNewRawLocation(rawLocation: Location) {
         }

         override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
             val enhancedLocation = locationMatcherResult.enhancedLocation
             navigationLocationProvider.changePosition(
                 enhancedLocation,
                 locationMatcherResult.keyPoints,
             )
             updateCamera(enhancedLocation)
         }
     }*/

    private lateinit var mapboxMap: MapboxMap

    //private lateinit var mapboxNavigation: MapboxNavigation

    private lateinit var binding: ActivityMapMainKotlinBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapMainKotlinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mapboxMap = binding.mapView.getMapboxMap()

/*        binding.mapView.location.apply {
            this.locationPuck = LocationPuck2D(
                bearingImage = ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.ic_navigation_arrow
                )
            )
            setLocationProvider(navigationLocationProvider)
            enabled = true
        }
        binding.currentLocation.setOnClickListener {

            locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            locationRequest.interval = 5000
            locationRequest.fastestInterval = 2000
            val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
            builder.setAlwaysShow(true)
            val result: Task<LocationSettingsResponse> = LocationServices.getSettingsClient(applicationContext).checkLocationSettings(builder.build())
            result.addOnCompleteListener { task ->
                try {
                    val response = task.getResult(ApiException::class.java)
                    Toast.makeText(
                        this@MainActivity,
                        "GPS is already turned on",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } catch (e: ApiException) {
                    when (e.statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                            val resolvableApiException = e as ResolvableApiException
                            resolvableApiException.startResolutionForResult(
                                this@MainActivity,
                                REQUEST_CODE_LOCATION_PERMISSION
                            )
                        } catch (ex: SendIntentException) {
                            ex.printStackTrace()
                        }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {}
                    }
                }
            }

        }*/

        binding.navigationLocation.setOnClickListener {
            val navigationIntent = Intent(this, NavigationViewActivity::class.java)
            startActivity(navigationIntent)
        }

        /*   initStyle()
           requestPermission()*/
    }

/*
    @SuppressLint("MissingPermission")
    private fun initNavigation() {
        mapboxNavigation = MapboxNavigation(
            NavigationOptions.Builder(this)
                .accessToken(getString(R.string.mapbox_access_token))
                .build()
        ).apply {
            startTripSession()
            registerLocationObserver(locationObserver)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initNavigation()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                REQUEST_CODE_LOCATION_PERMISSION
            )
        } else {
            initNavigation()
        }

    }

    private fun initStyle() {
        mapboxMap.loadStyleUri(Style.MAPBOX_STREETS)
    }

    private fun updateCamera(location: Location) {
        val mapAnimationOptions = MapAnimationOptions.Builder().duration(1500L).build()
        binding.mapView.camera.easeTo(
            CameraOptions.Builder()
                .center(Point.fromLngLat(location.longitude, location.latitude))
                .zoom(12.0)
                .padding(EdgeInsets(500.0, 0.0, 0.0, 0.0))
                .build(),
            mapAnimationOptions
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode === REQUEST_CODE_LOCATION_PERMISSION) {
            when (resultCode) {
                RESULT_OK -> {
                    Toast.makeText(this, "GPS is turned on", Toast.LENGTH_SHORT).show()
                }
                RESULT_CANCELED -> Toast.makeText(
                    this,
                    "GPS required to be turned on",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @SuppressLint("Lifecycle")
    override fun onPause() {
        super.onPause()
    }

    @SuppressLint("Lifecycle")
    override fun onResume() {
        super.onResume()
    }

    @SuppressLint("Lifecycle")
    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    @SuppressLint("Lifecycle")
    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
        mapboxNavigation.stopTripSession()
        mapboxNavigation.unregisterLocationObserver(locationObserver)

    }

    @SuppressLint("Lifecycle")
    override fun onDestroy() {

        super.onDestroy()
        binding.mapView.onDestroy()
        mapboxNavigation.stopTripSession()
        mapboxNavigation.unregisterLocationObserver(locationObserver)
    }*/
}
