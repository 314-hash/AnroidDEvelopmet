package com.pd.field_staff.utils.extension

import android.app.ActivityOptions
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.core.app.ActivityOptionsCompat
import com.pd.field_staff.ui.models.Jobs
import com.pd.field_staff.ui.viewmodel.JobDetail
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import java.util.Date
import java.util.Locale


fun Context.animationTransition(): Bundle {
    val options = ActivityOptions.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out)
    return options.toBundle()
}

fun Context.animationOptions(): ActivityOptionsCompat {
    return ActivityOptionsCompat.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out)
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Date.formatDate(pattern: String = "MMM d, yyyy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(this)
}

@Composable
fun pxToSp(px: Int): Float {
    val density = LocalDensity.current.density
    val fontScale = LocalContext.current.resources.configuration.fontScale
    return px / (density * fontScale)
}

@Composable
fun pxToDp(px: Float): Dp {
    val density = LocalDensity.current
    return with(density) { px.toDp() }
}

fun String.initials(): String {
    return this.split(" ")
        .mapNotNull { it.firstOrNull()?.toString() }
        .reduce { acc, s -> acc + s }
}

sealed interface EventState {
    data object ClockedIn: EventState
    data object ClockedOut: EventState
    data object LoggedOut: EventState
}

object EventEmit {
    private val _eventState = MutableSharedFlow<EventState>()
    val eventState = _eventState.asSharedFlow()

    suspend fun triggerEvent(eventState: EventState){
        _eventState.emit(eventState)
    }

}

object Utils {

    fun formatDate(millis: Long, pattern: String = "MMM d, yyyy"): String {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate().format(formatter)
    }

    /**
     * Calculates the difference in hours and minutes between two timestamps.
     *
     * @param from Start time in milliseconds.
     * @param to End time in milliseconds.
     * @return Pair of hours and minutes difference.
     */
    fun getHoursAndMinutesDifference(from: Long, to: Long): Pair<Long, Long> {
        val diffInMillis = to - from
        val hours = TimeUnit.MILLISECONDS.toHours(diffInMillis)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis) % 60
        return Pair(hours, minutes)
    }

    fun getHoursAndMinutesDifference(start: LocalDateTime, end: LocalDateTime): String {
        val duration = Duration.between(start, end)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        return "${hours}h ${minutes}m"
    }

}

object PrefKeys {
    const val USER_EMAIL = "user_email"
    const val USER_NAME = "user_name"
    const val USER_PROFILE = "user_profile"
    const val NEED_CLOCK_IN = "need_clock_in"
    const val CLOCK_STATUS = "clock_status"
    const val USER_COMPANY = "user_company"
    const val CLOCK_IN_TIME = "clock_in_time"
    const val USER_AUTH_KEY = "user_auth_key"

    const val TOTAL_CASH_ADVANCE = "total_cash_advance"
    const val REMAIN_CASH_ADVANCE = "remain_cash_advance"

    const val FILTER_TYPE = "filter_type"
    const val CAMERA_FLASH_SETTINGS = "camera_flash_settings"
}

data object CacheUtils {

    var selectedJob: JobDetail? = null

}