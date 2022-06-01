package com.voltero

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.mapbox.api.directions.v5.models.Bearing
import com.mapbox.api.directions.v5.models.DirectionsRoute
import com.mapbox.api.directions.v5.models.RouteOptions
import com.mapbox.bindgen.Expected
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.EdgeInsets
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.LocationPuck2D
import com.mapbox.maps.plugin.animation.MapAnimationOptions
import com.mapbox.maps.plugin.animation.camera
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.navigation.base.TimeFormat
import com.mapbox.navigation.base.extensions.applyDefaultNavigationOptions
import com.mapbox.navigation.base.extensions.applyLanguageAndVoiceUnitOptions
import com.mapbox.navigation.base.options.NavigationOptions
import com.mapbox.navigation.base.route.RouterCallback
import com.mapbox.navigation.base.route.RouterFailure
import com.mapbox.navigation.base.route.RouterOrigin
import com.mapbox.navigation.core.MapboxNavigationProvider
import com.mapbox.navigation.core.directions.session.RoutesObserver
import com.mapbox.navigation.core.formatter.MapboxDistanceFormatter
import com.mapbox.navigation.core.replay.MapboxReplayer
import com.mapbox.navigation.core.replay.ReplayLocationEngine
import com.mapbox.navigation.core.replay.route.ReplayProgressObserver
import com.mapbox.navigation.core.replay.route.ReplayRouteMapper
import com.mapbox.navigation.core.trip.session.LocationMatcherResult
import com.mapbox.navigation.core.trip.session.LocationObserver
import com.mapbox.navigation.core.trip.session.RouteProgressObserver
import com.mapbox.navigation.core.trip.session.VoiceInstructionsObserver
import com.mapbox.navigation.ui.base.util.MapboxNavigationConsumer
import com.mapbox.navigation.ui.maneuver.api.MapboxManeuverApi
import com.mapbox.navigation.ui.maps.camera.NavigationCamera
import com.mapbox.navigation.ui.maps.camera.data.MapboxNavigationViewportDataSource
import com.mapbox.navigation.ui.maps.camera.lifecycle.NavigationBasicGesturesHandler
import com.mapbox.navigation.ui.maps.camera.state.NavigationCameraState
import com.mapbox.navigation.ui.maps.camera.transition.NavigationCameraTransitionOptions
import com.mapbox.navigation.ui.maps.location.NavigationLocationProvider
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowApi
import com.mapbox.navigation.ui.maps.route.arrow.api.MapboxRouteArrowView
import com.mapbox.navigation.ui.maps.route.arrow.model.RouteArrowOptions
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineApi
import com.mapbox.navigation.ui.maps.route.line.api.MapboxRouteLineView
import com.mapbox.navigation.ui.maps.route.line.model.MapboxRouteLineOptions
import com.mapbox.navigation.ui.maps.route.line.model.RouteLine
import com.mapbox.navigation.ui.tripprogress.api.MapboxTripProgressApi
import com.mapbox.navigation.ui.tripprogress.model.*
import com.mapbox.navigation.ui.voice.api.MapboxSpeechApi
import com.mapbox.navigation.ui.voice.api.MapboxVoiceInstructionsPlayer
import com.mapbox.navigation.ui.voice.model.SpeechAnnouncement
import com.mapbox.navigation.ui.voice.model.SpeechError
import com.mapbox.navigation.ui.voice.model.SpeechValue
import com.mapbox.navigation.ui.voice.model.SpeechVolume
import com.voltero.databinding.ActivityNavigationLocationBinding
import java.util.*
import kotlin.math.roundToInt

class NavigationToLocation : AppCompatActivity() {

    private companion object {
        private const val BUTTON_ANIMATION_DURATION = 1500L
    }

    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0
    private var destinationLatitude: Double = 0.0
    private var destinationLongitude: Double = 0.0

    var bottomNav: MeowBottomNavigation? = null

    private val mapboxReplayer = MapboxReplayer()
    private val replayLocationEngine = ReplayLocationEngine(mapboxReplayer)
    private val replayProgressObserver = ReplayProgressObserver(mapboxReplayer)
    private lateinit var binding: ActivityNavigationLocationBinding
    private lateinit var mapboxMap: MapboxMap
    private lateinit var navigationCamera: NavigationCamera
    private lateinit var viewportDataSource: MapboxNavigationViewportDataSource

    private var get = 0
    private val pixelDensity = Resources.getSystem().displayMetrics.density
    private val overviewPadding: EdgeInsets by lazy {
        EdgeInsets(
            140.0 * pixelDensity,
            40.0 * pixelDensity,
            120.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private val landscapeOverviewPadding: EdgeInsets by lazy {
        EdgeInsets(
            30.0 * pixelDensity,
            380.0 * pixelDensity,
            110.0 * pixelDensity,
            20.0 * pixelDensity
        )
    }
    private val followingPadding: EdgeInsets by lazy {
        EdgeInsets(
            180.0 * pixelDensity,
            40.0 * pixelDensity,
            150.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }
    private val landscapeFollowingPadding: EdgeInsets by lazy {
        EdgeInsets(
            30.0 * pixelDensity,
            380.0 * pixelDensity,
            110.0 * pixelDensity,
            40.0 * pixelDensity
        )
    }

    private lateinit var maneuverApi: MapboxManeuverApi
    private lateinit var tripProgressApi: MapboxTripProgressApi
    private lateinit var routeLineApi: MapboxRouteLineApi
    private lateinit var routeLineView: MapboxRouteLineView
    private val routeArrowApi: MapboxRouteArrowApi = MapboxRouteArrowApi()
    private lateinit var routeArrowView: MapboxRouteArrowView

    private var isVoiceInstructionsMuted = false
        set(value) {
            field = value
            if (value) {
                binding.soundButton.muteAndExtend(BUTTON_ANIMATION_DURATION)
                voiceInstructionsPlayer.volume(SpeechVolume(0f))
            } else {
                binding.soundButton.unmuteAndExtend(BUTTON_ANIMATION_DURATION)
                voiceInstructionsPlayer.volume(SpeechVolume(1f))
            }
        }

    private lateinit var speechApi: MapboxSpeechApi
    private lateinit var voiceInstructionsPlayer: MapboxVoiceInstructionsPlayer
    private val voiceInstructionsObserver = VoiceInstructionsObserver { voiceInstructions ->
        speechApi.generate(voiceInstructions, speechCallback)
    }

    private val speechCallback =
        MapboxNavigationConsumer<Expected<SpeechError, SpeechValue>> { expected ->
            expected.fold(
                { error ->
                    voiceInstructionsPlayer.play(
                        error.fallback,
                        voiceInstructionsPlayerCallback
                    )
                },
                { value ->
                    voiceInstructionsPlayer.play(
                        value.announcement,
                        voiceInstructionsPlayerCallback
                    )
                }
            )
        }

    private val voiceInstructionsPlayerCallback =
        MapboxNavigationConsumer<SpeechAnnouncement> { value ->
            speechApi.clean(value)
        }

    private val navigationLocationProvider = NavigationLocationProvider()

    private val locationObserver = object : LocationObserver {
        var firstLocationUpdateReceived = false

        override fun onNewRawLocation(rawLocation: Location) {
            // Do nothing
        }

        override fun onNewLocationMatcherResult(locationMatcherResult: LocationMatcherResult) {
            val enhancedLocation = locationMatcherResult.enhancedLocation
            navigationLocationProvider.changePosition(
                location = enhancedLocation,
                keyPoints = locationMatcherResult.keyPoints,
            )

            viewportDataSource.onLocationChanged(enhancedLocation)
            viewportDataSource.evaluate()

            val dSpeed = enhancedLocation.speed
            val a = 3.6 * dSpeed
            val kmhSpeed = a.roundToInt()
            binding.speedView.text = kmhSpeed.toString()

            if (get < 0) {
                updateCamera(
                    Point.fromLngLat(
                        enhancedLocation.longitude,
                        enhancedLocation.latitude
                    )
                )

            }
            if (!firstLocationUpdateReceived) {
                firstLocationUpdateReceived = true
                navigationCamera.requestNavigationCameraToOverview(
                    stateTransitionOptions = NavigationCameraTransitionOptions.Builder()
                        .maxDuration(0)
                        .build()
                )
            }
        }
    }

    private val routeProgressObserver = RouteProgressObserver { routeProgress ->
        viewportDataSource.onRouteProgressChanged(routeProgress)
        viewportDataSource.evaluate()

        val style = mapboxMap.getStyle()
        if (style != null) {
            val maneuverArrowResult = routeArrowApi.addUpcomingManeuverArrow(routeProgress)
            routeArrowView.renderManeuverUpdate(style, maneuverArrowResult)
        }

        val maneuvers = maneuverApi.getManeuvers(routeProgress)
        maneuvers.fold(
            { error ->
                Toast.makeText(
                    this@NavigationToLocation,
                    error.errorMessage,
                    Toast.LENGTH_SHORT
                ).show()
            },
            {
                binding.maneuverView.visibility = View.VISIBLE
                binding.maneuverView.renderManeuvers(maneuvers)
            }
        )

        binding.tripProgressView.render(
            tripProgressApi.getTripProgress(routeProgress)
        )
    }

    private fun updateCamera(point: Point) {
        if (get == 0) {
            val cameraOptions = CameraOptions.Builder()
                .center(point)
                .zoom(13.0)
                .build()
            mapboxMap.setCamera(cameraOptions)
        } else {
            val mapAnimationOptions = MapAnimationOptions.Builder().duration(1500L).build()
            binding.mapView.camera.easeTo(
                CameraOptions.Builder()
                    .center(point)
                    .zoom(3.0)
                    .build(),
                mapAnimationOptions
            )
        }
    }

    private val routesObserver = RoutesObserver { routeUpdateResult ->
        if (routeUpdateResult.routes.isNotEmpty()) {
            val routeLines = routeUpdateResult.routes.map { RouteLine(it, null) }

            routeLineApi.setRoutes(
                routeLines
            ) { value ->
                mapboxMap.getStyle()?.apply {
                    routeLineView.renderRouteDrawData(this, value)
                }
            }

            viewportDataSource.onRouteChanged(routeUpdateResult.routes.first())
            viewportDataSource.evaluate()
        } else {
            val style = mapboxMap.getStyle()
            if (style != null) {
                routeLineApi.clearRouteLine { value ->
                    routeLineView.renderClearRouteLineValue(
                        style,
                        value
                    )
                }
                routeArrowView.render(style, routeArrowApi.clearArrows())
            }

            viewportDataSource.clearRouteData()
            viewportDataSource.evaluate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigationLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNav = findViewById(R.id.bottomNav)

        //add menu items to bottom nav
//        bottomNav?.add(MeowBottomNavigation.Model(1, R.drawable.ic_home))
//        bottomNav?.add(MeowBottomNavigation.Model(2, R.drawable.ic_category))
//        bottomNav?.add(MeowBottomNavigation.Model(3, R.drawable.ic_search))
//        bottomNav?.add(MeowBottomNavigation.Model(4, R.drawable.ic_cart))
//        bottomNav?.add(MeowBottomNavigation.Model(5, R.drawable.ic_map))
//        bottomNav?.add(MeowBottomNavigation.Model(6, R.drawable.ic_chat))
//        bottomNav?.add(MeowBottomNavigation.Model(7, R.drawable.ic_profile))

        //add menu items to bottom nav
        bottomNav?.add(MeowBottomNavigation.Model(1, R.drawable.ic_home))
        bottomNav?.add(MeowBottomNavigation.Model(2, R.drawable.ic_history))
        bottomNav?.add(MeowBottomNavigation.Model(3, R.drawable.ic_map))
        bottomNav?.add(MeowBottomNavigation.Model(4, R.drawable.ic_chat))
        bottomNav?.add(MeowBottomNavigation.Model(5, R.drawable.ic_profile))

        //set bottom nav on show listener
        bottomNav?.setOnShowListener(MeowBottomNavigation.ShowListener { item -> //define a fragment
            // Show toast message when item is selected
        })


        //set the initial fragment to show
        bottomNav?.show(3, true)

        //set menu item on click listener

        //set menu item on click listener
        bottomNav?.setOnClickMenuListener(MeowBottomNavigation.ClickListener {
            when (it.id) {
                1 -> {
                    HomeVolunteer.currentTab = 1;
                    val intent = Intent(this, HomeVolunteer::class.java)
                    startActivity(intent)
                }
                2 -> {
                    HomeVolunteer.currentTab = 2;
                    val intent = Intent(this, HomeVolunteer::class.java)
                    startActivity(intent)
                }
                3 -> {
                    HomeVolunteer.currentTab = 3;
                    val intent = Intent(this, HomeVolunteer::class.java)
                    startActivity(intent)
                }
                4 -> {
                    HomeVolunteer.currentTab = 4;
                    val intent = Intent(this, HomeVolunteer::class.java)
                    startActivity(intent)
                }
                5 -> {
                    HomeVolunteer.currentTab = 5;
                    val intent = Intent(this, HomeVolunteer::class.java)
                    startActivity(intent)
                }
            }
        })

        //set on reselect listener

        //set on reselect listener
        bottomNav?.setOnReselectListener(MeowBottomNavigation.ReselectListener {
            //display a toast
            //Toast.makeText(getApplicationContext()," You reselected "+ item.getId(), Toast.LENGTH_SHORT).show();
        })

        currentLatitude = intent.getDoubleExtra("currentLatitude", 0.0)
        currentLongitude = intent.getDoubleExtra("currentLongitude", 0.0)
        destinationLatitude = intent.getDoubleExtra("destinationLatitude", 0.0)
        destinationLongitude = intent.getDoubleExtra("destinationLongitude", 0.0)
        mapboxMap = binding.mapView.getMapboxMap()
        if (NavigationViewActivity.mapboxNavigation!!.getRoutes().isEmpty()) {
            updateCamera(Point.fromLngLat(currentLongitude, currentLatitude))

            get = 1
        }



        binding.mapView.location.apply {
            this.locationPuck = LocationPuck2D(
                bearingImage = ContextCompat.getDrawable(
                    this@NavigationToLocation,
                    R.drawable.ic_car_black
                )
            )
            setLocationProvider(navigationLocationProvider)
            enabled = true
        }

        if (NavigationViewActivity.mapboxNavigation!!.getRoutes().isEmpty()) {
            mapboxReplayer.pushEvents(
                listOf(
                    ReplayRouteMapper.mapToUpdateLocation(
                        eventTimestamp = 0.0,
                        point = Point.fromLngLat(currentLongitude, currentLatitude)
                    )
                )
            )
            mapboxReplayer.playFirstLocation()
        }

        NavigationViewActivity.mapboxNavigation = if (MapboxNavigationProvider.isCreated()) {
            MapboxNavigationProvider.retrieve()
        } else {
            MapboxNavigationProvider.create(
                NavigationOptions.Builder(this.applicationContext)
                    .accessToken(getString(R.string.mapbox_access_token))
                    .locationEngine(replayLocationEngine)
                    .build()
            )
        }

        viewportDataSource = MapboxNavigationViewportDataSource(mapboxMap)
        navigationCamera = NavigationCamera(
            mapboxMap,
            binding.mapView.camera,
            viewportDataSource
        )

        binding.mapView.camera.addCameraAnimationsLifecycleListener(
            NavigationBasicGesturesHandler(navigationCamera)
        )
        navigationCamera.registerNavigationCameraStateChangeObserver { navigationCameraState ->
            when (navigationCameraState) {
                NavigationCameraState.TRANSITION_TO_FOLLOWING,
                NavigationCameraState.FOLLOWING -> binding.recenter.visibility = View.INVISIBLE
                NavigationCameraState.TRANSITION_TO_OVERVIEW,
                NavigationCameraState.OVERVIEW,
                NavigationCameraState.IDLE -> binding.recenter.visibility = View.VISIBLE
            }
        }

        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewportDataSource.overviewPadding = landscapeOverviewPadding
        } else {
            viewportDataSource.overviewPadding = overviewPadding
        }
        if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            viewportDataSource.followingPadding = landscapeFollowingPadding
        } else {
            viewportDataSource.followingPadding = followingPadding
        }

        val distanceFormatterOptions =
            NavigationViewActivity.mapboxNavigation!!.navigationOptions.distanceFormatterOptions

        maneuverApi = MapboxManeuverApi(
            MapboxDistanceFormatter(distanceFormatterOptions)
        )

        tripProgressApi = MapboxTripProgressApi(
            TripProgressUpdateFormatter.Builder(this)
                .distanceRemainingFormatter(
                    DistanceRemainingFormatter(distanceFormatterOptions)
                )
                .timeRemainingFormatter(
                    TimeRemainingFormatter(this)
                )
                .percentRouteTraveledFormatter(
                    PercentDistanceTraveledFormatter()
                )
                .estimatedTimeToArrivalFormatter(
                    EstimatedTimeToArrivalFormatter(this, TimeFormat.NONE_SPECIFIED)
                )
                .build()
        )

        speechApi = MapboxSpeechApi(
            this,
            getString(R.string.mapbox_access_token),
            Locale.US.language
        )
        voiceInstructionsPlayer = MapboxVoiceInstructionsPlayer(
            this,
            getString(R.string.mapbox_access_token),
            Locale.US.language
        )

        val mapboxRouteLineOptions = MapboxRouteLineOptions.Builder(this)
            .withRouteLineBelowLayerId("road-label")
            .build()
        routeLineApi = MapboxRouteLineApi(mapboxRouteLineOptions)
        routeLineView = MapboxRouteLineView(mapboxRouteLineOptions)

        val routeArrowOptions = RouteArrowOptions.Builder(this).build()
        routeArrowView = MapboxRouteArrowView(routeArrowOptions)

        mapboxMap.loadStyleUri(
            Style.MAPBOX_STREETS
        ) {
            findRoute(Point.fromLngLat(destinationLongitude, destinationLatitude))
        }

        binding.stop.setOnClickListener {
            finish()
            clearRouteAndStopNavigation()
        }
        binding.recenter.setOnClickListener {
            navigationCamera.requestNavigationCameraToFollowing()
            binding.routeOverview.showTextAndExtend(BUTTON_ANIMATION_DURATION)

        }
        binding.routeOverview.setOnClickListener {
            navigationCamera.requestNavigationCameraToOverview()
            binding.recenter.showTextAndExtend(BUTTON_ANIMATION_DURATION)
        }
        binding.soundButton.setOnClickListener {
            isVoiceInstructionsMuted = !isVoiceInstructionsMuted
        }

        binding.soundButton.unmute()

        binding.startNavigationButton.setOnClickListener {

            binding.soundButton.visibility = View.VISIBLE
            binding.routeOverview.visibility = View.VISIBLE
            binding.tripProgressCard.visibility = View.VISIBLE
            binding.speedcard.visibility = View.VISIBLE


            navigationCamera.requestNavigationCameraToFollowing()

            binding.startNavigationButton.visibility = View.GONE

            NavigationViewActivity.mapboxNavigation!!.registerRouteProgressObserver(
                routeProgressObserver
            )
            NavigationViewActivity.mapboxNavigation!!.registerVoiceInstructionsObserver(
                voiceInstructionsObserver
            )
            NavigationViewActivity.mapboxNavigation!!.registerRouteProgressObserver(replayProgressObserver)
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            NavigationViewActivity.mapboxNavigation!!.startTripSession()
        }

    }

    override fun onStart() {
        get = 0
        super.onStart()
        NavigationViewActivity.mapboxNavigation!!.registerRoutesObserver(routesObserver)
        NavigationViewActivity.mapboxNavigation!!.registerLocationObserver(locationObserver)
    }

    override fun onStop() {
        super.onStop()
        get = 0
        NavigationViewActivity.mapboxNavigation!!.unregisterRoutesObserver(routesObserver)
        NavigationViewActivity.mapboxNavigation!!.unregisterRouteProgressObserver(
            routeProgressObserver
        )
        NavigationViewActivity.mapboxNavigation!!.unregisterLocationObserver(locationObserver)
        NavigationViewActivity.mapboxNavigation!!.unregisterVoiceInstructionsObserver(
            voiceInstructionsObserver
        )
        NavigationViewActivity.mapboxNavigation!!.unregisterRouteProgressObserver(replayProgressObserver)
    }


    override fun onDestroy() {
        super.onDestroy()
        MapboxNavigationProvider.destroy()
        mapboxReplayer.finish()
        maneuverApi.cancel()
        routeLineApi.cancel()
        routeLineView.cancel()
        speechApi.cancel()
        voiceInstructionsPlayer.shutdown()
        get = 0
    }

    private fun findRoute(destination: Point) {
        val originLocation = navigationLocationProvider.lastLocation
        val originPoint = originLocation?.let {
            Point.fromLngLat(it.longitude, it.latitude)
        } ?: return
        NavigationViewActivity.mapboxNavigation!!.requestRoutes(
            RouteOptions.builder()
                .applyDefaultNavigationOptions()
                .applyLanguageAndVoiceUnitOptions(this)
                .coordinatesList(listOf(originPoint, destination))
                .bearingsList(
                    listOf(
                        Bearing.builder()
                            .angle(originLocation.bearing.toDouble())
                            .degrees(45.0)
                            .build(),
                        null
                    )
                )
                .layersList(listOf(NavigationViewActivity.mapboxNavigation!!.getZLevel(), null))
                .build(),
            object : RouterCallback {
                override fun onRoutesReady(
                    routes: List<DirectionsRoute>,
                    routerOrigin: RouterOrigin
                ) {
                    setRouteAndStartNavigation(routes)
                }

                override fun onFailure(
                    reasons: List<RouterFailure>,
                    routeOptions: RouteOptions
                ) {
                }

                override fun onCanceled(routeOptions: RouteOptions, routerOrigin: RouterOrigin) {}
            }
        )
    }

    private fun setRouteAndStartNavigation(routes: List<DirectionsRoute>) {
        NavigationViewActivity.mapboxNavigation!!.setRoutes(routes)

        startSimulation(routes.first())
    }

    private fun clearRouteAndStopNavigation() {
        NavigationViewActivity.mapboxNavigation!!.setRoutes(listOf())
        binding.soundButton.visibility = View.INVISIBLE
        binding.maneuverView.visibility = View.INVISIBLE
        binding.routeOverview.visibility = View.INVISIBLE
        binding.tripProgressCard.visibility = View.INVISIBLE
        binding.speedcard.visibility = View.INVISIBLE

        binding.startNavigationButton.visibility = View.VISIBLE
    }

    private fun startSimulation(route: DirectionsRoute) {
        mapboxReplayer.run {
            stop()
            clearEvents()
            val replayEvents = ReplayRouteMapper().mapDirectionsRouteGeometry(route)
            pushEvents(replayEvents)
            seekTo(replayEvents.first())
            play()
        }
    }
}