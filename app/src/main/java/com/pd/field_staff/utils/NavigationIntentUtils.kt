package com.pd.field_staff.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.android.gms.maps.model.LatLng
import com.pd.field_staff.ui.models.Jobs
import com.pd.field_staff.ui.theme.MediumStyle
import com.pd.field_staff.R
import com.pd.field_staff.ui.theme.ForestGreen
import com.pd.field_staff.ui.theme.RegularStyle
import com.pd.field_staff.ui.viewmodel.JobDetail

fun createGoogleMapsIntent(location: LatLng, label: String? = null): Intent {
    val uriString = if (label != null) {
        "geo:${location.latitude},${location.longitude}?q=${location.latitude},${location.longitude}($label)"
    } else {
        "geo:${location.latitude},${location.longitude}"
    }
    val uri = Uri.parse(uriString)
    return Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.google.android.apps.maps")
    }
}

fun createWazeIntent(location: LatLng): Intent {
    val uriString = "https://waze.com/ul?ll=${location.latitude},${location.longitude}&navigate=yes"
    val uri = Uri.parse(uriString)
    return Intent(Intent.ACTION_VIEW, uri).apply {
        setPackage("com.waze")
    }
}

fun createGenericMapIntent(location: LatLng, label: String? = null): Intent {
    val uriString = if (label != null) {
        "geo:${location.latitude},${location.longitude}?q=${location.latitude},${location.longitude}($label)"
    } else {
        "geo:${location.latitude},${location.longitude}"
    }
    val uri = Uri.parse(uriString)
    return Intent(Intent.ACTION_VIEW, uri)
}

fun isAppInstalled(context: Context, packageName: String): Boolean {
    return try {
        context.packageManager.getPackageInfo(packageName, 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}

enum class NavigationApp {
    GoogleMaps,
    Waze
}

@Composable
fun NavigateInfoDialog(
    place: JobDetail,
    onDismiss: () -> Unit,
    onNavigate: (JobDetail, NavigationApp) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text( modifier = Modifier.padding(bottom = 20.dp),
                text = place.name, style = RegularStyle, fontSize = 20.sp)

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onNavigate(place, NavigationApp.GoogleMaps) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, ForestGreen),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(50.dp),
                        painter = painterResource(R.drawable.ic_google_map),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                    Text(text = "Google Maps", style = MediumStyle, fontSize = 15.sp, color = Color.Black)
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onNavigate(place, NavigationApp.Waze) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                border = BorderStroke(1.dp, ForestGreen),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        modifier = Modifier.size(50.dp),
                        painter = painterResource(R.drawable.ic_waze_map),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                    Text(text = "Waze", style = MediumStyle, fontSize = 15.sp, color = Color.Black)
                }
            }

        }
    }

}