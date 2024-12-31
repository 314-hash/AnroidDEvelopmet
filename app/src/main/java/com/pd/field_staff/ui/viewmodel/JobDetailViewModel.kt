package com.pd.field_staff.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.pd.field_staff.core.api.ApiUrl
import com.pd.field_staff.core.api.StaffApi
import com.pd.field_staff.ui.theme.InterFonts
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class JobDetailViewModel(
    private val api: StaffApi,
    private val jobListService: JobListService
): ViewModel() {

    var jobList: MutableStateFlow<List<JobDetail>> = MutableStateFlow(emptyList())
        private set

    var jobState: MutableStateFlow<JobState> = MutableStateFlow(JobState.Empty)
        private set

    init {
        //loadJobList()
    }

    fun loadJobList(category: Int = 0) {
        jobState.value = JobState.Loading
        viewModelScope.launch {
            val response = jobListService.getJobList()
            response.onSuccess { jobs ->
                val newJobs = jobs.map { it ->
                    it.copy(
                        id = UUID.randomUUID().toString(),
                        latitude = "14.5750826",
                        longitude = "121.0681381",
                        category = (1..3).random()
                    )
                }
                if(category == 0) {
                    jobList.value = newJobs
                }else{
                    jobList.value = newJobs.filter { it.category == category }
                }
                jobState.value = JobState.Success
            }
            .onFailure { error ->
                jobState.value = JobState.Error(error.message.toString())
            }
        }
    }


    sealed interface JobState {
        data object Empty: JobState
        data object Loading: JobState
        data object Success: JobState
        data class Error(var error: String): JobState
    }

}

data class JobDetail(
    var id: String = "",
    val name: String,
    val category: Int = 0,
    val time: String,
    val duration: String,
    val location: String,
    val distance: String,
    val latitude: String,
    val longitude: String,
    val notes: String,
    val price: String,
    val image: String,
    val client: ClientInfo
) {
    val latLang: LatLng = LatLng(latitude.toDouble(), longitude.toDouble())
}

data class ClientInfo(
    val name: String,
    val phone: String,
    val email: String
)

class JobListService(
    private val api: StaffApi
) {

    suspend fun getJobList(): Result<List<JobDetail>> {
        return api.request(HttpMethod.Get, ApiUrl.JOB_LIST.url())
    }
}