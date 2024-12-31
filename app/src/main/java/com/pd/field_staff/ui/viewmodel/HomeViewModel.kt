package com.pd.field_staff.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pd.field_staff.core.api.ApiUrl
import com.pd.field_staff.core.api.StaffApi
import com.pd.field_staff.ui.views.main.EntryType
import com.pd.field_staff.utils.extension.PrefKeys
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.Date

class HomeViewModel(
    private val api: StaffApi,
    private  val sharedPrefs: SharedPreferences
): ViewModel() {

    var homeState: MutableStateFlow<HomeState> = MutableStateFlow(HomeState.Empty)
        private set

    fun timeEntry(type: EntryType) {
        homeState.value = HomeState.Loading

        viewModelScope.launch {
            val response: Result<ClockResponse> = api.request(HttpMethod.Post, ApiUrl.TIME_ENTRY.url(), hashMapOf("type" to type.ordinal))
            response.onSuccess {  result ->
                when(type){
                    EntryType.IN ->{
                        sharedPrefs.edit().apply {
                            putBoolean(PrefKeys.NEED_CLOCK_IN, false)
                            putInt(PrefKeys.CLOCK_STATUS, 1)
                            putLong(PrefKeys.CLOCK_IN_TIME, Date().time)
                        }.apply()
                        homeState.value = HomeState.SuccessClockIn(result.message)
                    }
                    EntryType.OUT -> {
                        sharedPrefs.edit().apply {
                            putBoolean(PrefKeys.NEED_CLOCK_IN, true)
                            putInt(PrefKeys.CLOCK_STATUS, 0)
                        }.apply()
                        homeState.value = HomeState.SuccessClockOut(result.message)
                    }
                    else -> {
                        sharedPrefs.edit().apply {
                            putBoolean(PrefKeys.NEED_CLOCK_IN, false)
                            putInt(PrefKeys.CLOCK_STATUS, type.ordinal)
                        }.apply()
                        val nextType = when(type) {
                            EntryType.TAKE_LUNCH -> EntryType.RESTART_WORK
                            EntryType.RESTART_WORK -> EntryType.OUT
                            else -> EntryType.IN
                        }
                        homeState.value = HomeState.SuccessTimeEntry(result.message, nextType)
                    }
                }
            }
            .onFailure { error ->
                homeState.value = HomeState.Error(error.message.toString())
            }

        }
    }

    sealed interface HomeState {
        data object Empty: HomeState
        data object Loading: HomeState
        data class Success(val message: String): HomeState
        data class Error(val error: String): HomeState
        data class SuccessTimeEntry(val message: String, val nextEntryType: EntryType): HomeState
        data class SuccessClockIn(val message: String): HomeState
        data class SuccessClockOut(val message: String): HomeState
    }

}



data class ClockResponse(
    val status: Int,
    val message: String,
    val time_stamp: String
)