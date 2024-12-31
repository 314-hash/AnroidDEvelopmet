package com.pd.field_staff.ui.views.dialogs

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.provider.Settings
import android.text.TextUtils
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Construction
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.rememberAsyncImagePainter
import com.pd.field_staff.ui.components.CustomOutlineTextField
import com.pd.field_staff.ui.components.SimpleCustomTextField
import com.pd.field_staff.ui.theme.AllCornersRounded
import com.pd.field_staff.ui.theme.BoldStyle
import com.pd.field_staff.ui.theme.ForestGreen
import com.pd.field_staff.ui.theme.LightStyle
import com.pd.field_staff.ui.theme.MediumStyle
import com.pd.field_staff.ui.theme.PDRed
import com.pd.field_staff.ui.theme.RegularStyle
import com.pd.field_staff.ui.viewmodel.JobPlace
import com.pd.field_staff.ui.viewmodel.ProfileViewModel
import com.pd.field_staff.utils.extension.showToast
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel


@Composable
fun ClockInDialog(onClockIn: () -> Unit) {

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Column (
            modifier = Modifier.fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White)
                .padding(15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text("Please clock in before starting to work", style = MediumStyle)

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onClockIn() },
                colors = ButtonDefaults.buttonColors(containerColor = PDRed, contentColor = Color.White)
            ) {
                Text("Clock In", style = MediumStyle)
            }
        }

    }

}

@Composable
fun ClockInToolsDialog(onDismissRequest: () -> Unit, onConfirm: () -> Unit) {

    val tools = listOf("Snow Shovel","Snow Plow","Snow Blower","Snow Pusher","Salt Spreader")

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.75f)
                .background(color = Color.White, shape = RoundedCornerShape(15.dp))
                .padding(15.dp)
        ) {

            Text(text = "Tools for you", style = BoldStyle,textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp), fontSize = 15.sp, color = Color.Black)

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(tools, key = { it }) { tool ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Construction,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Text(tool)
                    }
                }
            }

            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = onConfirm
            ) {
                Text(text = "Confirm Tools", style = RegularStyle, color = Color.White)
            }
        }
    }

}

@Composable
fun ClockOutToolsDialog(onDismissRequest: () -> Unit, onConfirm: () -> Unit) {

    val tools = listOf("Snow Shovel","Snow Plow","Snow Blower","Snow Pusher","Salt Spreader")

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(fraction = 0.75f)
                .background(color = Color.White, shape = RoundedCornerShape(15.dp))
                .padding(16.dp)
        ) {

            Text(text = "Tools for you", style = BoldStyle,textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp), fontSize = 15.sp, color = Color.Black)

            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(tools, key = { it }) { tool ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Construction,
                            contentDescription = null,
                            tint = Color.Black
                        )
                        Text(tool)
                    }
                }
            }


            Text(text = "Are you sure you want to clock out?", style = RegularStyle, color = Color.Black, fontSize = 15.sp)
            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = ForestGreen
                ),
                border = BorderStroke(1.dp, ForestGreen),
                onClick = onDismissRequest
            ) {
                Text("No - Get the Tools", style = MediumStyle)

            }
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onConfirm
            ) {
                Text(text = "Clock Out", style = RegularStyle, color = Color.White)
            }
        }
    }

}

@Composable
fun AddPhotoDialog(uri: Uri, onDismissRequest: () -> Unit, onConfirm: (String) -> Unit) {
    var description by remember { mutableStateOf("") }
    var validDescription by remember { mutableStateOf(true) }
    var errorMessage = "Please enter a description"
    val context = LocalContext.current

    fun onConfirmEntry() {
        if (description.isEmpty()) {
            validDescription = false
        } else {
            validDescription = true
            onConfirm(description)
        }
    }

    Dialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.White)
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Add Photo", style = BoldStyle, color = Color.Black)
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
                SimpleCustomTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = "Description",
                    showError = !validDescription,
                    errorMessage = errorMessage,
                    singleLine = false
                )
                Button(onClick = ::onConfirmEntry) {
                    Text(text = "Confirm")
                }
            }
    }

}

@Composable
fun EnableLocationDialog(onDismiss: () -> Unit) {

    val context = LocalContext.current

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                modifier = Modifier.size(40.dp),
                imageVector = Icons.Default.MyLocation,
                contentDescription = null
            )

            Text("Enable Location", style = MediumStyle, color = Color.Black, fontSize = 20.sp)

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // Open the location settings
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    context.startActivity(intent)
                }
            ) {
                Text(text = "Turn On Location Service", style = RegularStyle, fontSize = 13.sp)
            }
        }
    }
}


@Composable
fun ChangePassword(onDismissRequest: () -> Unit, onConfirm: () -> Unit) {

    Dialog(onDismissRequest = onDismissRequest) {

        val focusManager = LocalFocusManager.current
        val currentContext = LocalContext.current
        val profileViewModel: ProfileViewModel = koinViewModel()
        var newPassword by remember { mutableStateOf("")  }
        var confirmPassword by remember { mutableStateOf("")  }
        var newPasswordError  by remember { mutableStateOf("Please enter new password") }
        var confirmPasswordError by remember { mutableStateOf("Please enter confirm password") }
        var validPassword by remember { mutableStateOf(true) }
        var isPasswordVisible by remember { mutableStateOf(false) }

        var validConfPassword by remember { mutableStateOf(true) }
        var isPasswordConfVisible by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            profileViewModel.profileState.collect { state ->
                when(state) {
                    is ProfileViewModel.ProfileState.Empty -> { }
                    is ProfileViewModel.ProfileState.Error -> {
                        currentContext.showToast(state.error.toString())
                    }
                    is ProfileViewModel.ProfileState.Loading -> { }
                    is ProfileViewModel.ProfileState.Success -> {
                        currentContext.showToast(state.message)
                        onConfirm()
                    }
                    else -> { }
                }
            }
        }

        fun onChangePassword() {
            validPassword = newPassword.isNotBlank()
            validConfPassword = confirmPassword.isNotBlank()

            if (newPassword.isNotEmpty()){
                val passwordRegex = "^(?=.*[!@#\$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?])(?=.*\\d)(?=.*[A-Z]).{8,}$".toRegex()
                validPassword = newPassword.matches(passwordRegex)
                if(!validPassword) {
                    newPasswordError = "Minimum of 8 characters, 1 uppercase letter, 1 number and 1 special character"
                }
            }

            if(confirmPassword.isNotEmpty()) {
                validConfPassword = TextUtils.equals(confirmPassword, newPassword)
                if(!validConfPassword){
                    confirmPasswordError = "Password does not match!"
                }
            }

            if(validPassword and validConfPassword) {
               profileViewModel.changePassword(newPassword, confirmPassword)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Change Password", style = BoldStyle, color = Color.Black)

            // new password
            SimpleCustomTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = "New Password",
                showError = !validPassword,
                isPasswordField = true,
                isPasswordVisible = isPasswordVisible,
                onVisibilityChange = { isPasswordVisible = it },
                errorMessage = newPasswordError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) }
                )
            )

            // confirm password
            SimpleCustomTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Confirm New Password",
                showError = !validConfPassword,
                isPasswordField = true,
                isPasswordVisible = isPasswordConfVisible,
                onVisibilityChange = { isPasswordConfVisible = it },
                errorMessage = confirmPasswordError,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { focusManager.clearFocus() }
                )
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { onChangePassword() }
            ) {
                Text("SUBMIT", style = MediumStyle)
            }

        }

    }

}


@Composable
fun ShowSkipForm() {
    // Content inside the sheet
    var reason by remember { mutableStateOf("") }
    var validReason by rememberSaveable { mutableStateOf(true) }
    val reasonError = "Please enter reason for skipping the job"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)

    ) {
        Text(
            text = "Reason for Skip",
            style = RegularStyle
        )

        SimpleCustomTextField(
            value = reason,
            onValueChange = { reason = it },
            label = "Reason",
            showError = !validReason,
            errorMessage = reasonError,
            singleLine = false
        )

        Button(
            onClick = {

            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Submit", style = RegularStyle)
        }
    }
}

@Composable
fun MessageClientForm(onMessage: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth().height(250.dp)
            .padding(30.dp)

    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { }
        ) {
            Text("Call", style = RegularStyle, color = Color.White)
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onMessage
        ) {
            Text("SMS", style = RegularStyle, color = Color.White)
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onMessage
        ) {
            Text("Email", style = RegularStyle, color = Color.White)
        }


    }
}

@Composable
fun ShowSMSAndEmailDialog(
    onDismissRequest: () -> Unit
) {

    var messageContent by remember { mutableStateOf("") }
    var validMessage by remember { mutableStateOf(true) }
    var messageError = "Please enter a message"

    Dialog(onDismissRequest = onDismissRequest) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(15.dp))
                .background(Color.White)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Send Email", style = BoldStyle, color = Color.Black)

            SimpleCustomTextField(
                value = messageContent,
                onValueChange = { messageContent = it },
                label = "Message",
                showError = !validMessage,
                errorMessage = messageError,
                singleLine = false
            )

            Button(
                onClick = { },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Submit", style = RegularStyle)
            }
        }

    }
}

@Composable
fun LocationCheckAndPrompt() {
    val context = LocalContext.current
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    var isLocationEnabled by remember {
        mutableStateOf(
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        )
    }

    LaunchedEffect(Unit) {
        // Update the location status when the composable is first launched
        isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    if (!isLocationEnabled) {
        // Display a message and a button to prompt the user
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            Text("Location services are disabled.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                // Open the location settings
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            }) {
                Text("Enable Location")
            }
        }
    } else {
        // Your main app content goes here
        Text("Location is enabled.")
    }
}