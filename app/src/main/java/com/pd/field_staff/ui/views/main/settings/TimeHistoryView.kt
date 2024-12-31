package com.pd.field_staff.ui.views.main.settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pd.field_staff.ui.theme.FieldStaffTheme
import com.pd.field_staff.ui.theme.ForestGreen
import com.pd.field_staff.ui.theme.LightGreen
import com.pd.field_staff.ui.theme.MediumStyle
import com.pd.field_staff.ui.theme.PDRed
import com.pd.field_staff.ui.theme.RegularStyle

class TimeHistoryView: ComponentActivity() {

    val timeEntries = listOf(
        TimeEntryModel(timeStamp = "7:30 am", timeType = "Clock In", duration = "3h 2m"),
        TimeEntryModel(timeStamp = "11:30 am", timeType = "Take Lunch", duration = "1h 2m"),
        TimeEntryModel(timeStamp = "1:30 pm", timeType = "Restart Work", duration = "1h 2m"),
        TimeEntryModel(timeStamp = "5:30 pm", timeType = "Clock Out", duration = "8h 2m"),
    )

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FieldStaffTheme {

                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                            title = {
                                Text(text = "Time History", color = Color.Black, style = MediumStyle, fontSize = 14.sp)
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
                        ).padding(horizontal = 20.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {

                        LazyColumn {
                            itemsIndexed(timeEntries){ _, entry ->
                                TimeEntryItem(entry)
                            }
                        }

                    }

                }


            }
        }

    }

}

@Composable
fun TimeEntryItem(timeEntry: TimeEntryModel) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier.background(color = LightGreen, shape = RoundedCornerShape(5.dp)).padding(15.dp),
                imageVector = Icons.Default.AccessTime,
                contentDescription = "time",
                tint = ForestGreen
            )

            Column(
                modifier = Modifier.padding(start = 10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(timeEntry.timeStamp, style = RegularStyle)
                Text(timeEntry.timeType, style = RegularStyle)
            }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(timeEntry.duration, style = RegularStyle, color = PDRed)
            Text("Duration", style = RegularStyle)
        }

    }
}

data class TimeEntryModel(
    val timeStamp: String,
    val timeType: String,
    val duration: String
)