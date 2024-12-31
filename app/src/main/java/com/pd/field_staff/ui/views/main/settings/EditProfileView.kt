package com.pd.field_staff.ui.views.main.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.pd.field_staff.ui.components.LottieAnimationSpec
import com.pd.field_staff.ui.components.SimpleCustomTextField
import com.pd.field_staff.ui.models.UserProfile
import com.pd.field_staff.ui.theme.FieldStaffTheme
import com.pd.field_staff.ui.theme.ForestGreen
import com.pd.field_staff.ui.theme.MediumStyle
import com.pd.field_staff.ui.viewmodel.ProfileViewModel
import com.pd.field_staff.utils.extension.PrefKeys
import com.pd.field_staff.utils.extension.showToast
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel
import com.pd.field_staff.R

class EditProfileView: ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FieldStaffTheme {

                var userProfile by remember { mutableStateOf<UserProfile>(UserProfile()) }
                var isLoading by remember { mutableStateOf(false) }
                val profileViewModel: ProfileViewModel = koinViewModel()
                val scrollState = rememberScrollState()

                LaunchedEffect(Unit) {
                    profileViewModel.getProfile()
                    profileViewModel.profileState.collect { state ->
                        when(state) {
                            is ProfileViewModel.ProfileState.Loading -> { isLoading = true }
                            is ProfileViewModel.ProfileState.Error -> {
                                showToast(state.error.toString())
                                isLoading = false
                            }
                            is ProfileViewModel.ProfileState.Success -> {
                                showToast(state.message)
                                isLoading = false
                            }
                            is ProfileViewModel.ProfileState.ProfileSuccess -> {
                                userProfile = state.user
                                isLoading = false
                            }
                            else -> { }
                        }
                    }
                }

                fun onUpdate() {
                    profileViewModel.updateProfile(userProfile)
                }

                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                            title = {
                                Text(text = "Edit Profile", color = Color.Black, style = MediumStyle, fontSize = 14.sp)
                            }, navigationIcon = {
                                Icon(imageVector = Icons.Default.ChevronLeft, contentDescription = null, tint = Color.Black,
                                    modifier = Modifier.clickable(onClick = { finish() }))

                            }
                        )
                    },
                    bottomBar = {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = { onUpdate() }
                            ) {
                                Text("Update", style = MediumStyle, color = Color.White)
                            }
                        }

                    }
                ) { padding ->

                    Box(
                        modifier = Modifier.fillMaxSize().padding(
                            top = padding.calculateTopPadding(),
                            bottom = padding.calculateBottomPadding()
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
                            Column(
                                modifier = Modifier.verticalScroll(scrollState).weight(1f),
                                verticalArrangement = Arrangement.spacedBy(5.dp),
                            ) {

                                Text(
                                    "Please enter your details",
                                    style = MediumStyle
                                )

                                SimpleCustomTextField(
                                    value = userProfile.first_name,
                                    onValueChange = { userProfile.first_name = it },
                                    label = "First Name",
                                )

                                SimpleCustomTextField(
                                    value = userProfile.last_name,
                                    onValueChange = { userProfile.last_name = it },
                                    label = "Last Name",
                                )

                                SimpleCustomTextField(
                                    value = userProfile.phone,
                                    onValueChange = { userProfile.phone = it },
                                    label = "Contact Number",
                                )

                                SimpleCustomTextField(
                                    value = userProfile.email,
                                    onValueChange = { userProfile.email = it },
                                    label = "Email",
                                )

                                SimpleCustomTextField(
                                    value = userProfile.address,
                                    onValueChange = { userProfile.address = it },
                                    label = "Address",
                                )

                                SimpleCustomTextField(
                                    value = userProfile.city,
                                    onValueChange = { userProfile.city = it },
                                    label = "City",
                                )

                                SimpleCustomTextField(
                                    value = userProfile.state,
                                    onValueChange = { userProfile.state = it },
                                    label = "State",
                                )

                                SimpleCustomTextField(
                                    value = userProfile.zip,
                                    onValueChange = { userProfile.zip = it },
                                    label = "Zip",
                                )

                            }

                        }

                        if (isLoading){
                            LottieAnimationSpec(R.raw.loader_liquid_four_dot)
                        }

                    }

                }

            }
        }
    }
}