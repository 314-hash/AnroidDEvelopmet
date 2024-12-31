package com.pd.field_staff.ui.views.auth

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.pd.field_staff.R
import com.pd.field_staff.ui.components.LottieAnimationSpec
import com.pd.field_staff.ui.components.SimpleCustomTextField
import com.pd.field_staff.ui.theme.BoldStyle
import com.pd.field_staff.ui.theme.FieldStaffTheme
import com.pd.field_staff.ui.theme.MediumStyle
import com.pd.field_staff.ui.theme.RegularStyle
import com.pd.field_staff.ui.viewmodel.LoginViewModel
import com.pd.field_staff.ui.views.dialogs.EnableLocationDialog
import com.pd.field_staff.ui.views.main.DashboardView
import com.pd.field_staff.utils.extension.animationTransition
import com.pd.field_staff.utils.extension.showToast
import org.koin.androidx.compose.koinViewModel

class LoginView :  ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            FieldStaffTheme {

                val focusManager = LocalFocusManager.current
                var email by rememberSaveable { mutableStateOf("eman.devs@premiumdesignscape.com") }
//                    var email by rememberSaveable { mutableStateOf("") }
                var password by rememberSaveable { mutableStateOf("I28iunI4")  }
//                    var password by rememberSaveable { mutableStateOf("")  }
                var validEmail by rememberSaveable { mutableStateOf(true) }
                var validPassword by rememberSaveable { mutableStateOf(true) }
                var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
                var isLoading by rememberSaveable { mutableStateOf(false) }
                var showForgotPassword by rememberSaveable { mutableStateOf(false) }
                val emailError = "Please enter email address"
                val passwordError = "Please enter password"
                val loginViewModel: LoginViewModel = koinViewModel()

                val lifecycleOwner = LocalLifecycleOwner.current
                val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                var isLocationEnabled by remember {
                    mutableStateOf(
                        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                    )
                }

                DisposableEffect(lifecycleOwner) {
                    val observer = LifecycleEventObserver { _, event ->
                        if (event == Lifecycle.Event.ON_RESUME) {
                            // Update the location status when the activity resumes
                            isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                        }
                    }
                    lifecycleOwner.lifecycle.addObserver(observer)
                    onDispose {
                        lifecycleOwner.lifecycle.removeObserver(observer)
                    }
                }

                LaunchedEffect(Unit) {
                    loginViewModel.loginState.collect { state ->
                        when (state) {
                            is LoginViewModel.LoginState.Loading -> { isLoading = true }
                            is LoginViewModel.LoginState.Success -> {
                                isLoading = false
                                gotoNextScreen()
                            }
                            is LoginViewModel.LoginState.RestPasswordSuccess -> {
                                isLoading = false
                                showForgotPassword = false
                            }
                            is LoginViewModel.LoginState.Error -> {
                                isLoading = false
                                showForgotPassword = false
                                state.message?.let { showToast(it) }
                            }
                            else -> { /*TODO*/ }
                        }
                    }
                }

                fun onSignIn(email: String, password: String){
                    validEmail = email.isNotBlank()
                    validPassword = password.isNotBlank()
                    if ( validEmail and validPassword){
                        loginViewModel.onLogin(email, password)
                    }
                }

                Box(modifier = Modifier.fillMaxSize().padding(20.dp),
                    contentAlignment = Alignment.Center){

                    Column {

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
                            horizontalArrangement = Arrangement.Center){
                            Image(
                                modifier = Modifier.fillMaxWidth(0.75f).height(100.dp),
                                painter = painterResource(R.drawable.pd_logo),
                                contentDescription = null,
                                contentScale = ContentScale.FillBounds
                            )
                        }


                        Column {

                            Text(modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 20.dp, bottom = 20.dp),
                                text = "Login to your account", style = RegularStyle, fontSize = 20.sp)

                            // email
                            SimpleCustomTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = "Email Address",
                                showError = !validEmail,
                                errorMessage = emailError,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                                )
                            )
                            // password
                            SimpleCustomTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = "Password",
                                showError = !validPassword,
                                isPasswordField = true,
                                isPasswordVisible = isPasswordVisible,
                                onVisibilityChange = { isPasswordVisible = it },
                                errorMessage = passwordError,
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = { focusManager.clearFocus() }
                                )
                            )
                            // forgot password
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp, end = 20.dp),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(modifier = Modifier.clickable { showForgotPassword = true }
                                    , textAlign = TextAlign.Right,
                                    text = "Forgot Password?", style = RegularStyle)
                            }

                            // submit button
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp),
                                onClick = { onSignIn(email, password) }
                            ) {
                                Text(
                                    "Log in",
                                    style = BoldStyle,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    if(isLoading) {
                        LottieAnimationSpec(animRes = R.raw.loader_liquid_four_dot)
                    }
                    if (showForgotPassword){
                        ForgotPasswordDialog(loginViewModel, onDismiss = { showForgotPassword = false })
                    }

                    if (!isLocationEnabled) {
                        EnableLocationDialog {
                            isLocationEnabled = true
                        }
                    }

                }
            }
        }
    }

    private fun gotoNextScreen() {
        var nextView = Intent(this, DashboardView::class.java)
        startActivity(nextView, animationTransition())
    }
}


@Composable
fun ForgotPasswordDialog(
    loginViewModel: LoginViewModel,
    onDismiss: () -> Unit
) {

    var email by remember { mutableStateOf("")}
    var validEmail by rememberSaveable { mutableStateOf(true) }
    val emailError = "Please enter email address"

    fun onForgotPassword() {
        validEmail = email.isNotBlank()
        if (validEmail){
            loginViewModel.resetPassword(email)
        }
    }


    Dialog(onDismissRequest = onDismiss) {
        Column (
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White)
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Forgot Password",
                style = BoldStyle, color = Color.Black,
                modifier = Modifier.padding(vertical = 10.dp)
            )
            // email
            SimpleCustomTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email Address",
                showError = !validEmail,
                errorMessage = emailError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { onForgotPassword() }
                )
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onForgotPassword() }
            ) {
                Text("Reset Password", style = MediumStyle)
            }
        }
    }
}