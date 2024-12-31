package com.pd.field_staff.ui.views.main

import android.content.Intent
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pd.field_staff.R
import com.pd.field_staff.ui.components.LottieAnimationSpec
import com.pd.field_staff.ui.components.SearchTextField
import com.pd.field_staff.ui.theme.ForestGreen
import com.pd.field_staff.ui.theme.LightGreen
import com.pd.field_staff.ui.theme.MediumStyle
import com.pd.field_staff.ui.theme.PDRed
import com.pd.field_staff.ui.theme.RegularStyle
import com.pd.field_staff.ui.viewmodel.HomeViewModel
import com.pd.field_staff.ui.views.dialogs.ClockInDialog
import com.pd.field_staff.ui.views.dialogs.ClockInToolsDialog
import com.pd.field_staff.ui.views.dialogs.ClockOutToolsDialog
import com.pd.field_staff.ui.views.main.settings.TimeHistoryView
import com.pd.field_staff.utils.extension.EventEmit
import com.pd.field_staff.utils.extension.EventState
import com.pd.field_staff.utils.extension.PrefKeys
import com.pd.field_staff.utils.extension.Utils
import com.pd.field_staff.utils.extension.animationTransition
import com.pd.field_staff.utils.extension.showToast
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun HomeView(
    onJobCategory: (Int) -> Unit
) {

    val currentContext = LocalContext.current
    val homeViewModel: HomeViewModel = koinViewModel()
    val sharedPrefs: SharedPreferences = koinInject()

    var currentTime = remember { mutableStateOf(LocalDateTime.now()) }
    val clockFormat = DateTimeFormatter.ofPattern("h:mm:ss a")
    val dateFormat = DateTimeFormatter.ofPattern("MMMM d")

    val clockStatus = EntryType.entries[sharedPrefs.getInt(PrefKeys.CLOCK_STATUS, 0)]
    //val needClockedIn = sharedPrefs.getBoolean(PrefKeys.NEED_CLOCK_IN, true)
    var currentEntryType by remember { mutableStateOf(clockStatus) }
    //var showClockedInDialog by remember { mutableStateOf(needClockedIn) }

    var showClockInToolsDialog by remember { mutableStateOf(false) }
    var showClockOutToolsDialog by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var timeDuration by remember { mutableStateOf("0h 0m") }
    // TODO: save the login timestamp to shared prefs
    val startTime = LocalDateTime.of(2024, 12, 17, 13, 0, 0) // 1:00 PM

    LaunchedEffect(key1 = Unit) {
        while (true) {
            currentTime.value = LocalDateTime.now()
            delay(1000)
            timeDuration = Utils.getHoursAndMinutesDifference(start = startTime, end = currentTime.value)
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.homeState.collect { state ->
            when(state) {
                is HomeViewModel.HomeState.Empty -> { }
                is HomeViewModel.HomeState.Loading -> { isLoading = true }
                is HomeViewModel.HomeState.Success -> {
                    isLoading = false
                    currentContext.showToast(state.message)
                }
                is HomeViewModel.HomeState.Error -> {
                    isLoading = false
                    currentContext.showToast(state.error)
                }
                is HomeViewModel.HomeState.SuccessClockOut -> {
                    isLoading = false
                    currentEntryType = EntryType.IN
                    //showClockedInDialog = true
                    currentContext.showToast(state.message)
                }
                is HomeViewModel.HomeState.SuccessClockIn -> {
                    isLoading = false
                    currentEntryType = EntryType.TAKE_LUNCH
                    //showClockedInDialog = false
                    showClockInToolsDialog = true
                    currentContext.showToast(state.message)
                }
                is HomeViewModel.HomeState.SuccessTimeEntry -> {
                    isLoading = false
                    currentEntryType = state.nextEntryType
                    currentContext.showToast(state.message)
                }
            }
        }
    }

    fun todayHistory() {
        val intent = Intent(currentContext, TimeHistoryView::class.java)
        currentContext.startActivity(intent, currentContext.animationTransition())
    }

    fun timeEntry(){

        if (currentEntryType == EntryType.OUT) {
            showClockOutToolsDialog = true
            return
        }
        homeViewModel.timeEntry(currentEntryType)

    }

    Box(
        modifier = Modifier.fillMaxSize().padding(20.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // user profile picture and name
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Image(
                    modifier = Modifier.size(50.dp),
                    painter = painterResource(R.drawable.user_avatar),
                    contentDescription = null
                )
                Column {
                    Text(
                        "Hi, Fieldworker",
                        style = RegularStyle,
                        fontSize = 15.sp,
                        color = Color.Black.copy(alpha = 0.5f)
                    )
                    Text("Eman Nollase", style = RegularStyle)
                }
            }

            SearchTextField { searchText -> }

            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)
                    .background(color = currentEntryType.backgroundColor)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    Box(
                        modifier = Modifier.size(100.dp)
                            .background(color = currentEntryType.outerColor, shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier.size(75.dp)
                                .background(color = currentEntryType.innerColor, shape = CircleShape)
                                .padding(10.dp).clickable(onClick = { timeEntry() }),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(currentEntryType.label.uppercase(), style = MediumStyle, color = Color.White, fontSize = 10.sp,
                                textAlign = TextAlign.Center)
                        }
                    }

                    //date and time
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Today: ${currentTime.value.format(dateFormat)}", style = RegularStyle, color = Color.Gray)
                        Text(
                            currentTime.value.format(clockFormat),
                            style = RegularStyle
                        )
                    }

                }
            }

            Text("Time History", style = RegularStyle)

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
                        Text("7:30 am", style = RegularStyle)
                        Text(currentEntryType.label, style = RegularStyle)
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(timeDuration, style = RegularStyle, color = PDRed)
                    Text("Duration", style = RegularStyle)
                }

            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    modifier = Modifier.clickable(onClick = ::todayHistory),
                    text = "View Today's History",
                    style = RegularStyle,
                    textDecoration = TextDecoration.Underline
                )
            }

            Text("Service Rules/Tools", style = RegularStyle, fontSize = 13.sp)

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier.size(75.dp).clickable(onClick = { onJobCategory(1) }),
                    painter = painterResource(R.drawable.category_plow_snow),
                    contentDescription = null
                )
                Image(
                    modifier = Modifier.size(75.dp).clickable(onClick = { onJobCategory(2) }),
                    painter = painterResource(R.drawable.category_landscape),
                    contentDescription = null
                )
                Image(
                    modifier = Modifier.size(75.dp).clickable(onClick = { onJobCategory(3) }),
                    painter = painterResource(R.drawable.category_maintenance),
                    contentDescription = null
                )
                Image(
                    modifier = Modifier.size(75.dp).clickable(onClick = { onJobCategory(0) }),
                    painter = painterResource(R.drawable.category_see_all),
                    contentDescription = null
                )
            }

        }

        if(isLoading) {
            LottieAnimationSpec(animRes = R.raw.loader_liquid_four_dot)
        }

        /*if(showClockedInDialog){
            ClockInDialog {
                homeViewModel.timeEntry(EntryType.IN)
            }
        }*/

        if(showClockInToolsDialog){
            ClockInToolsDialog(
                onDismissRequest = { showClockInToolsDialog = false },
                onConfirm = { showClockInToolsDialog = false }
            )
        }

        if(showClockOutToolsDialog){
            ClockOutToolsDialog(
                onDismissRequest = { showClockOutToolsDialog = false },
                onConfirm = {
                    showClockOutToolsDialog = false
                    homeViewModel.timeEntry(EntryType.OUT)
                }
            )
        }
    }

}


val RedBg = Color(0xFFFCEEEE)
val RedOuterCircleBg = Color(0xFFF9DDDD)
val RedInnerCircleBg = Color(0xFFE36767)

val GreenBg = Color(0xFFF1FFF3)
val GreenOuterCircleBg = Color(0xFFE7F6E9)
val GreenInnerCircleBg = Color(0xFF29B13B)

val BlueBg = Color(0xFFE6F1FF)
val BlueOuterCircleBg = Color(0xFFCCE2FF)
val BlueInnerCircleBg = Color(0xFF4D9AFE)

enum class EntryType(
    val label: String,
    val backgroundColor: Color,
    val outerColor: Color,
    val innerColor: Color
) {
    IN("Clock In", RedBg, RedOuterCircleBg, RedInnerCircleBg),
    TAKE_LUNCH("Take Lunch",GreenBg, GreenOuterCircleBg, GreenInnerCircleBg),
    RESTART_WORK("Restart Work", BlueBg, BlueOuterCircleBg, BlueInnerCircleBg),
    OUT("Clock Out", GreenBg, GreenOuterCircleBg, GreenInnerCircleBg)
}