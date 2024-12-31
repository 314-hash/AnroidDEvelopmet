package com.pd.field_staff.ui.views.main.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pd.field_staff.ui.components.DateTextField
import com.pd.field_staff.ui.theme.BoldStyle
import com.pd.field_staff.ui.theme.FieldStaffTheme
import com.pd.field_staff.ui.theme.ForestGreen
import com.pd.field_staff.ui.theme.MediumStyle
import com.pd.field_staff.ui.theme.RegularStyle
import com.pd.field_staff.utils.extension.Utils
import com.pd.field_staff.utils.extension.formatDate
import java.util.Date

class PaymentDetailsView: ComponentActivity() {

    val paymentDatas = listOf(
        PaymentData(date = "Nov 6, 2023", hours = "8hrs", amount = "$100", status = "Pending", action = "View"),
        PaymentData(date = "Nov 7, 2023", hours = "10hrs", amount = "$100", status = "Paid", action = "View"),
        PaymentData(date = "Nov 8, 2023", hours = "8hrs", amount = "$100", status = "Paid", action = "View"),
        PaymentData(date = "Nov 9, 2023", hours = "8hrs", amount = "$100", status = "Pending", action = "View"),
        PaymentData(date = "Nov 10, 2023", hours = "8hrs", amount = "$100", status = "Pending", action = "View"),
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FieldStaffTheme {

                val startDate = remember { mutableStateOf("") }
                val endDate = remember { mutableStateOf("") }
                var fromDate by remember { mutableStateOf<Date?>(null) }
                var toDate by remember { mutableStateOf<Date?>(null) }
                var isDateFromSelected by remember { mutableStateOf(true) }
                var initDateMillis by remember { mutableStateOf<Long?>(null) }
                var showDatePicker by remember { mutableStateOf(false) }
                val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initDateMillis)
                val confirmEnabled by remember { derivedStateOf { datePickerState.selectedDateMillis != null } }

                // Table headers
                val headers = listOf("Date", "Hours", "Amount", "Status","")

                if(showDatePicker) {
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    showDatePicker = false
                                    datePickerState.selectedDateMillis?.let {
                                        if(isDateFromSelected) {
                                            fromDate = Date(it)
                                            startDate.value = Utils.formatDate(it)

                                        }else{
                                            toDate = Date(it)
                                            endDate.value = Utils.formatDate(it)
                                        }
                                    }
                                },
                                enabled = confirmEnabled
                            ) {
                                Text("OK")
                            }
                        },
                        dismissButton = {
                            TextButton( onClick = { showDatePicker = false } ) {
                                Text("Cancel")
                            }
                        }
                    ) {
                        DatePicker(
                            state = datePickerState,
                            colors = DatePickerDefaults.colors(
                                containerColor = Color.White,
                                titleContentColor = ForestGreen,
                                subheadContentColor = ForestGreen
                            )
                        )
                    }
                }

                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                            title = {
                                Text(text = "Payment History", color = Color.Black, style = MediumStyle, fontSize = 14.sp)
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
                        ),
                        contentAlignment = Alignment.TopCenter
                    ) {

                        Column(
                            modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)
                        ) {

                            Column {
                                Text("Enter date range", style = RegularStyle)

                                Row {
                                    DateTextField(
                                        modifier = Modifier.weight(1f),
                                        text = startDate,
                                        label = "Start Date",
                                        onClick = {
                                            showDatePicker = true
                                            isDateFromSelected = true
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    DateTextField(
                                        modifier = Modifier.weight(1f),
                                        text = endDate,
                                        label = "End Date",
                                        onClick = {
                                            showDatePicker = true
                                            isDateFromSelected = false
                                        }
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.End
                                ) {
                                    Button(
                                        onClick = { }
                                    ) {
                                        Text("Search", style = RegularStyle)
                                    }
                                }

                            }
                            Spacer(modifier = Modifier.height(20.dp))

                            LazyColumn {
                                item {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().border(1.dp, Color.Gray)
                                    ) {
                                        headers.forEach { header ->
                                            Text(
                                                text = header,
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .padding(8.dp),
                                                style = MediumStyle,
                                                color = ForestGreen
                                            )

                                        }
                                    }
                                }
                                itemsIndexed(paymentDatas){ index, payment ->
                                    PaymentItem(payment, index)
                                }
                            }

                        }

                    }

                }

            }
        }
    }
}

// Data class to represent table rows
data class PaymentData(
    val date: String,
    val hours: String,
    val amount: String,
    val status: String,
    val action: String
)


@Composable
private fun PaymentItem(paymentData: PaymentData, index: Int) {
    val bgColor = if(index % 2 == 0) Color(0xFFF5F5F5) else Color.White
    Row(
        modifier = Modifier.fillMaxWidth().border(1.dp, Color.LightGray).background(color = bgColor)
    ) {
        Text(
            text = paymentData.date,
            modifier = Modifier.weight(1f).padding(8.dp),
            style = RegularStyle
        )
        Text(
            text = paymentData.hours,
            modifier = Modifier.weight(1f).padding(8.dp),
            style = RegularStyle
        )
        Text(
            text = paymentData.amount,
            modifier = Modifier.weight(1f).padding(8.dp),
            style = RegularStyle
        )
        Text(
            text = paymentData.status,
            modifier = Modifier.weight(1f).padding(8.dp),
            style = RegularStyle
        )
        Text(
            text = paymentData.action,
            modifier = Modifier.weight(1f).padding(8.dp),
            style = RegularStyle,
            color = ForestGreen
        )

    }
}