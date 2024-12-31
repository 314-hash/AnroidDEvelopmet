package com.pd.field_staff.ui.views.main.jobs

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.pd.field_staff.ui.models.Jobs
import com.pd.field_staff.ui.theme.BoldStyle
import com.pd.field_staff.ui.theme.FieldStaffTheme
import com.pd.field_staff.ui.theme.ForestGreen
import com.pd.field_staff.ui.theme.MediumStyle
import com.pd.field_staff.ui.theme.RegularStyle
import com.pd.field_staff.ui.viewmodel.MapViewModel
import com.pd.field_staff.ui.viewmodel.JobPlace
import com.pd.field_staff.utils.LocationHelper
import com.pd.field_staff.utils.NavigateInfoDialog
import com.pd.field_staff.utils.NavigationApp
import com.pd.field_staff.utils.createGoogleMapsIntent
import com.pd.field_staff.utils.createWazeIntent
import com.pd.field_staff.utils.extension.CacheUtils
import com.pd.field_staff.utils.extension.animationTransition
import com.pd.field_staff.utils.isAppInstalled
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

class JobLocationView : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            FieldStaffTheme {
                val context = LocalContext.current
                val scope = rememberCoroutineScope()
                // Initialize the LocationHelper
                val locationHelper = remember { LocationHelper(context) }
                val mapViewModel: MapViewModel = koinViewModel()
                var showDrivingNav by remember { mutableStateOf(false) }
                // Permissions
                val locationPermissionsState = rememberMultiplePermissionsState(
                    listOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
                // Request permissions on first launch
                LaunchedEffect(Unit) {
                    locationPermissionsState.launchMultiplePermissionRequest()
                }
                // Fetch the current location when permissions are granted
                LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
                    if (locationPermissionsState.allPermissionsGranted) {
                        locationHelper.getCurrentLocation { latLng ->
                            mapViewModel.updateCurrentLocation(latLng)
                            Timber.d("Current location => lat: %s, long: %s", latLng.latitude.toString(), latLng.longitude.toString())
                        }
                    }
                }
                // Observe lifecycle to start/stop location updates
                val lifecycleOwner = LocalLifecycleOwner.current
                DisposableEffect(lifecycleOwner, locationPermissionsState.allPermissionsGranted) {
                    val lifecycleObserver = LifecycleEventObserver { _, event ->
                        if (locationPermissionsState.allPermissionsGranted) {
                            when (event) {
                                Lifecycle.Event.ON_START -> {
                                    locationHelper.startLocationUpdates { latLng ->
                                        mapViewModel.updateCurrentLocation(latLng)
                                    }
                                }
                                Lifecycle.Event.ON_STOP -> {
                                    locationHelper.stopLocationUpdates()
                                }
                                else -> {}
                            }
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
                        locationHelper.stopLocationUpdates()
                    }
                }
                // Map camera position state
                val cameraPositionState = rememberCameraPositionState()
                // Update the camera position when current location is available
                LaunchedEffect(mapViewModel.currentLocation) {
                    mapViewModel.currentLocation?.let { location ->
                        cameraPositionState.animate(
                            CameraUpdateFactory.newLatLngZoom(location, 15f),
                            1_000
                        )
                    }
                }

                //driving dialog
                if(showDrivingNav) {
                    CacheUtils.selectedJob?.let { place ->
                        NavigateInfoDialog(
                            place = place,
                            onDismiss = { showDrivingNav = false },
                            onNavigate = { selectedPlace, navigationApp ->
                                val intent = when (navigationApp) {
                                    NavigationApp.GoogleMaps -> {
                                        if (isAppInstalled(context, "com.google.android.apps.maps")) {
                                            createGoogleMapsIntent(selectedPlace.latLang, selectedPlace.name)
                                        } else {
                                            Toast.makeText(context, "Google Maps is not installed", Toast.LENGTH_SHORT).show()
                                            null
                                        }
                                    }
                                    NavigationApp.Waze -> {
                                        if (isAppInstalled(context, "com.waze")) {
                                            createWazeIntent(selectedPlace.latLang)
                                        } else {
                                            Toast.makeText(context, "Waze is not installed", Toast.LENGTH_SHORT).show()
                                            null
                                        }
                                    }
                                }
                                intent?.let {
                                    showDrivingNav = false
                                    context.startActivity(it)
                                }
                            }
                        )
                    }
                }


                    Box(modifier = Modifier.fillMaxSize()) {

                        if (locationPermissionsState.allPermissionsGranted) {
                            GoogleMap(
                                modifier = Modifier.fillMaxSize(),
                                cameraPositionState = cameraPositionState,
                                uiSettings = MapUiSettings(zoomControlsEnabled = true),
                                properties = MapProperties(isMyLocationEnabled = true)
                            ) {
                                var markerStyle = BitmapDescriptorFactory.HUE_RED
                                Marker(
                                    state = MarkerState(position = CacheUtils.selectedJob!!.latLang),
                                    title = CacheUtils.selectedJob!!.name,
                                    icon = BitmapDescriptorFactory.defaultMarker(markerStyle),
                                    snippet = CacheUtils.selectedJob!!.location,
                                    /*onClick = {
                                        selectedJobPlace = place
                                        //scope.launch { modalSheetState.show() }
                                        true // Consume the event to prevent default info window
                                    }*/
                                )
                                /*jobList.forEach { place ->
                                    var markerStyle = BitmapDescriptorFactory.HUE_RED
                                    if (place.id == CacheUtils.selectedJob?.id){
                                        //selectedJobPlace = place
                                        markerStyle =  BitmapDescriptorFactory.HUE_GREEN
                                     }

                                }*/
                                // Optionally, add a marker at the current location
                                mapViewModel.currentLocation?.let { location ->
                                    Marker(
                                        state = MarkerState(position = location),
                                        title = "You are here",
                                        icon = BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_AZURE
                                        )
                                    )
                                }
                            }

                        } else {
                            RequestLocationPermission(permissionState = locationPermissionsState)
                        }

                        Box(
                            modifier = Modifier.fillMaxWidth().padding(20.dp),
                        ){
                            Icon(
                                imageVector = Icons.Default.ChevronLeft,
                                contentDescription = null, tint = Color.White,
                                modifier = Modifier
                                    .background(color = ForestGreen, shape = CircleShape)
                                    .padding(10.dp)
                                    .clickable(onClick = { finish() })
                            )
                        }

                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.BottomCenter
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                        .background(
                                            color = Color.White,
                                            shape = RoundedCornerShape(
                                                topStart = 20.dp,
                                                topEnd = 20.dp
                                            )
                                        )
                                ) {

                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                        ) {
                                            Text(
                                                text = "25 min",
                                                style = BoldStyle,
                                                fontSize = 20.sp
                                            )
                                            Text(text = "2:34 17 miles", style = RegularStyle)
                                        }
                                        Button(
                                            onClick = { showDrivingNav = true }
                                        ) {
                                            Icon(
                                                modifier = Modifier.rotate(45f),
                                                imageVector = Icons.Default.Navigation,
                                                contentDescription = null, tint = Color.White,
                                            )
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Text(
                                                text = "Start",
                                                style = BoldStyle,
                                                fontSize = 15.sp
                                            )
                                        }
                                    }

                                }
                            }
                    }
            }
        }
    }

    private fun gotoJobDetails() {
        val intent = Intent(applicationContext, JobDetailsView::class.java)
        intent.putExtra("show_driving", true)
        startActivity(intent, animationTransition())
        finish()
    }
}



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    permissionState: MultiplePermissionsState,
    rationale: String = "Location permission is needed to show your current location on the map."
) {
    val allPermissionsRevoked = permissionState.permissions.all { !it.status.isGranted }
    val permanentlyDenied = permissionState.permissions.any {
        !it.status.isGranted && !it.status.shouldShowRationale
    }

    when {
        allPermissionsRevoked && !permanentlyDenied -> {
            // Permissions are denied or not yet requested
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    rationale,
                    style = RegularStyle
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { permissionState.launchMultiplePermissionRequest() }) {
                    Text("Grant Permission")
                }
            }
        }
        permanentlyDenied -> {
            // Permissions are permanently denied
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    "Location permission is permanently denied. Please enable it in the app settings.",
                    style = BoldStyle
                )
            }
        }
    }
}
