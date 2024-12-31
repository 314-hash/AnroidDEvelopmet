package com.pd.field_staff.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class MapViewModel: ViewModel() {

    // Mutable state to hold the user's current location
    var currentLocation by mutableStateOf<LatLng?>(null)
        private set

    fun updateCurrentLocation(location: LatLng) {
        currentLocation = location
    }

    // List of places to display as markers
    /*val jobPlaces = listOf(
        JobPlace(
            name = "Sydney Opera House",
            location = LatLng(-33.857, 151.215),
            description = "An iconic performing arts center."
        ),
        JobPlace(
            name = "Harbour Bridge",
            location = LatLng(-33.852, 151.211),
            description = "A heritage-listed steel arch bridge."
        ),
        JobPlace(
            name = "Bondi Beach",
            location = LatLng(-33.891, 151.276),
            description = "A popular beach and the name of the surrounding suburb."
        )
        // Add more places as needed
    )*/

    val jobPlaces = listOf(
        JobPlace(
            name = "Sydney Opera House",
            latitude = 14.5840916,
            longitude = 121.0588601,
            location = LatLng(14.5840916, 121.0588601),
            description = "An iconic performing arts center."
        ),
        JobPlace(
            name = "Harbour Bridge",
            latitude = 14.5750826,
            longitude = 121.0681381,
            location = LatLng(14.5750826, 121.0681381),
            description = "A heritage-listed steel arch bridge."
        ),
        JobPlace(
            name = "Bondi Beach",
            latitude = 14.5814516,
            longitude = 121.0654201,
            location = LatLng(14.5814516, 121.0654201),
            description = "A popular beach and the name of the surrounding suburb."
        )
        // Add more places as needed
    )

    // Initial camera position
    //val initialPosition = LatLng(-33.852, 151.211) // Sydney, Australia

}

// Data class representing a place
data class JobPlace(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val location: LatLng,
    val description: String
)
