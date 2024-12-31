package com.pd.field_staff.ui.views.main.settings

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pd.field_staff.ui.theme.BoldStyle
import com.pd.field_staff.ui.theme.FieldStaffTheme
import com.pd.field_staff.ui.theme.ForestGreen
import com.pd.field_staff.ui.theme.MediumStyle
import com.pd.field_staff.ui.theme.RegularStyle
import com.pd.field_staff.ui.theme.SkyBlue
import com.pd.field_staff.ui.theme.SoftSand
import com.pd.field_staff.utils.extension.animationTransition
import java.util.Date

class PaymentHistoryView: ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FieldStaffTheme {

                var initDateMillis by remember { mutableStateOf<Long?>(null) }
                val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initDateMillis)

                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = ForestGreen),
                            title = {
                                Text(text = "Time Entry History", color = Color.White, style = MediumStyle, fontSize = 14.sp)
                            }, navigationIcon = {
                                Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null, tint = Color.White,
                                    modifier = Modifier.clickable(onClick = { finish() }))

                            }
                        )
                    }
                ) {  padding ->

                    Column(
                        modifier = Modifier.padding(padding)
                    ) {
                        DatePicker(
                            state = datePickerState,
                            showModeToggle = false,
                            headline = null,
                            title = null,
                            colors = DatePickerDefaults.colors(
                                containerColor = Color.White,
                                titleContentColor = ForestGreen,
                                subheadContentColor = ForestGreen
                            )
                        )

                        datePickerState.selectedDateMillis?.let {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Rate Per Hour", style = RegularStyle, fontSize = 13.sp)
                                    Text("$7/hr", style = BoldStyle, fontSize = 13.sp)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Date", style = RegularStyle, fontSize = 13.sp)
                                    Text("25 Oct 2024", style = BoldStyle, fontSize = 13.sp)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Time In", style = RegularStyle, fontSize = 13.sp)
                                    Text("8:00 am", style = BoldStyle, fontSize = 13.sp)
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Time Out", style = RegularStyle, fontSize = 13.sp)
                                    Text("5:00 pm", style = BoldStyle, fontSize = 13.sp)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Total Work Hours", style = RegularStyle, fontSize = 13.sp)
                                    Text("8hrs", style = BoldStyle, fontSize = 13.sp)
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Total Amount", style = RegularStyle, fontSize = 13.sp)
                                    Text("$56", style = BoldStyle, fontSize = 13.sp)
                                }

                                Button(
                                    onClick = ::gotoPaymentDetails
                                ) {
                                    Text("VIEW DETAILS", style = MediumStyle)
                                }

                            }
                        }

                    }

                }
            }
        }

    }


    private fun gotoPaymentDetails(){
        val intent = Intent(this, PaymentDetailsView::class.java)
        startActivity(intent, animationTransition())
    }
}