package com.pd.field_staff.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.pd.field_staff.core.api.ApiUrl
import com.pd.field_staff.core.api.StaffApi
import com.pd.field_staff.ui.models.UserProfile
import com.pd.field_staff.utils.extension.PrefKeys
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class ProfileViewModel(
    private val staffApi: StaffApi,
    private val sharedPrefs: SharedPreferences
) : ViewModel() {

    var profileState: MutableStateFlow<ProfileState> = MutableStateFlow<ProfileState>(ProfileState.Empty)
        private set

    fun getProfile() {
        profileState.value = ProfileState.Loading
        viewModelScope.launch {
            val response: Result<ProfileResponse> = staffApi.request(HttpMethod.Get, ApiUrl.PROFILE.url())
            response.onSuccess { result ->
                profileState.value = ProfileState.ProfileSuccess(result.profile)
            }
            .onFailure { error ->
                profileState.value = ProfileState.Error(error.message.toString())
            }
        }
    }

    fun updateProfile(profile: UserProfile) {
        profileState.value = ProfileState.Loading
        viewModelScope.launch {
            val response: Result<ProfileResponse> = staffApi.request(HttpMethod.Put,ApiUrl.PROFILE.url(), profile.toHashMap())
            response.onSuccess { result ->
                profileState.value = ProfileState.Success("Request sent. Pending for approval")
            }
            .onFailure { error ->
                profileState.value = ProfileState.Error(error.message.toString())
            }
        }

    }

    fun changePassword(newPassword: String, passwordConf: String) {
        profileState.value = ProfileState.Loading
        viewModelScope.launch {
            val response = staffApi.createPostRequest(ApiUrl.CHANGE_PASSWORD.url(), hashMapOf(
                "new_password" to newPassword,
                "new_password_confirmation" to passwordConf
            ))
            val result = response.getOrNull()
            if(response.isSuccess && result != null){
                val json = JSONObject(result)
                profileState.value = ProfileState.Success(json.getString("message"))
            }else{
                response.getOrElse { ex ->
                    profileState.value = ProfileState.Error(ex.message.toString())
                }
            }

        }
    }

    fun logOut() {
        try {
            clearDatabase()
            sharedPrefs.edit().clear().apply()
        }catch (_: Exception){

        }
        profileState.value = ProfileState.LoggedOut
    }

    private fun clearDatabase() {
        viewModelScope.launch {
            //realm.write {

            //}
        }
    }

    sealed interface ProfileState {
        data object Empty: ProfileState
        data object Loading: ProfileState
        data object LoggedOut: ProfileState
        data class ProfileSuccess(val user: UserProfile): ProfileState
        data class Success(val message: String): ProfileState
        data class Error(var error: String?): ProfileState
    }
}

data class ProfileResponse(
    val status: String,
    val message: String,
    val profile: UserProfile
)