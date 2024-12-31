package com.pd.field_staff.ui.models

import com.google.android.gms.maps.model.LatLng
import java.util.UUID


data class Jobs(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val image: String,
    val date: String,
    val latitude: Double,
    val longitude: Double,
    val location: LatLng
)

//val jobList = listOf<Jobs>(
//    Jobs(name = "Plow the snow", image = "image1", date = "Oct 26, 2024", latitude = 14.5840916,
//        longitude = 121.0588601,
//        location = LatLng(14.5840916, 121.0588601)),
//    Jobs(name = "Rake the leaves", image = "image1", date = "Nov 26, 2024",latitude = 14.5750826,
//        longitude = 121.0681381,
//        location = LatLng(14.5750826, 121.0681381)),
//    Jobs(name = "Mow the Lawn", image = "image1", date = "Nov 1, 2024",latitude = 14.5814516,
//        longitude = 121.0654201,
//        location = LatLng(14.5814516, 121.0654201)),
//)


data class UserProfile(
    var id: Int = 0,
    var user_type: String = "",
    var first_name: String = "",
    var last_name: String = "",
    var email: String = "",
    var address: String = "",
    var city: String = "",
    var state: String = "",
    var zip: String = "",
    var phone: String = ""
) {

    fun toHashMap(): HashMap<String, Any> {
        return hashMapOf(
            "first_name" to first_name,
            "last_name" to last_name,
            "email" to email,
            "address" to address,
            "city" to city,
            "state" to state,
            "zip" to zip,
            "phone" to phone
        )
    }
}