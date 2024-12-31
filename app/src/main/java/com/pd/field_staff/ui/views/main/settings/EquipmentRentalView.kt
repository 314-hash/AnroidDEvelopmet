package com.pd.field_staff.ui.views.main.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pd.field_staff.ui.components.SimpleCustomTextField
import com.pd.field_staff.ui.theme.FieldStaffTheme
import com.pd.field_staff.ui.theme.ForestGreen
import com.pd.field_staff.ui.theme.MediumStyle
import com.pd.field_staff.ui.theme.RegularStyle


class EquipmentRentalView : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FieldStaffTheme {

                var equipment by remember { mutableStateOf("") }
                var qrCode by remember { mutableStateOf("") }
                var equipRate by remember { mutableStateOf("") }
                var workHours by remember { mutableStateOf("") }
                var note by remember { mutableStateOf("") }

                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                            title = {
                                Text(text = "Equipment Rental", color = Color.Black, style = MediumStyle, fontSize = 14.sp)
                            }, navigationIcon = {
                                Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null, tint = Color.Black,
                                    modifier = Modifier.clickable(onClick = { finish() }))

                            }
                        )
                    }
                ) { padding ->

                    Box(
                        modifier = Modifier.fillMaxSize().padding(
                            top = padding.calculateTopPadding(),
                            bottom = padding.calculateBottomPadding()
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(horizontal = 20.dp)
                        ) {

                            SimpleCustomTextField(
                                value = equipment,
                                label = "Equipment",
                                onValueChange = { equipment = it }
                            )

                            SimpleCustomTextField(
                                value = qrCode,
                                label = "Scan QR Code",
                                onValueChange = { qrCode = it }
                            )

                            SimpleCustomTextField(
                                value = equipRate,
                                label = "Rate",
                                onValueChange = { equipRate = it }
                            )

                            SimpleCustomTextField(
                                value = workHours,
                                label = "Work Hours (HH:MM)",
                                onValueChange = { workHours = it }
                            )

                            SimpleCustomTextField(
                                value = note,
                                label = "Notes",
                                onValueChange = { note = it }
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth().padding(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PhotoCamera,
                                        contentDescription = null,
                                        tint = ForestGreen,
                                        modifier = Modifier.padding(end = 10.dp)
                                    )
                                    Text(text = "Take Photo", style = RegularStyle, color = ForestGreen)
                                }
                            }



                        }
                    }
                }
            }
        }
    }
}
