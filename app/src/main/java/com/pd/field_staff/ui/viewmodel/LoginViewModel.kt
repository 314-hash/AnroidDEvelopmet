package com.pd.field_staff.ui.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pd.field_staff.core.api.ApiUrl
import com.pd.field_staff.core.api.StaffApi
import com.pd.field_staff.utils.extension.PrefKeys
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import kotlin.getOrElse

class LoginViewModel(
    private val staffApi: StaffApi,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    var loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Empty)
        private set


    fun onLogin(email: String, password: String) {
        loginState.value = LoginState.Loading
        viewModelScope.launch {
            val params = hashMapOf("email" to email, "password" to password)
           val response: Result<LoginResponse> = staffApi.request(HttpMethod.Post, ApiUrl.LOGIN.url(), params)
            response.onSuccess { result ->
                sharedPreferences.edit().apply {
                    putString("jwtToken", result.token)
                    putString(PrefKeys.USER_EMAIL, email)
                    putString(PrefKeys.USER_NAME, result.name)
                }.apply()
                loginState.value = LoginState.Success
            }
            .onFailure { error ->
                Timber.d("Result => %s", error.message)
                loginState.value = LoginState.Error(error.message)
            }
        }

    }

    fun resetPassword(email: String) {
        loginState.value = LoginState.Loading
        viewModelScope.launch {
            val response = staffApi.createPostRequest(ApiUrl.FORGOT_PASSWORD.url(), hashMapOf("email" to email))
            if (response.isSuccess){
                loginState.value = LoginState.RestPasswordSuccess
            }else{
                response.getOrElse { ex ->
                    LoginState.Error(ex.message.toString())
                }
            }
        }
    }

    sealed interface LoginState {
        data object Empty: LoginState
        data object Loading: LoginState
        data object Progress: LoginState
        data object Success: LoginState
        data object RestPasswordLoading: LoginState
        data object RestPasswordSuccess: LoginState
        data class Error(val message: String?): LoginState
    }

}

data class UserInfo(
    val id: Int,
    val first_name: String,
    val last_name: String,
    val email: String
) {
    val fullName: String
        get() = "$first_name $last_name"
}

data class LoginResponse(
    val token: String,
    val status: String,
    val message: String,
    val name: String,
    val email: String,
    val auth_role: Int,
    val on_status: Int
)