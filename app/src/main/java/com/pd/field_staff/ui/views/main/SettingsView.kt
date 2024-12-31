package com.pd.field_staff.ui.views.main

import android.content.Intent
import android.content.SharedPreferences
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.PersonPin
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pd.field_staff.ui.theme.BoldStyle
import com.pd.field_staff.ui.theme.ForestGreen
import com.pd.field_staff.ui.theme.LightStyle
import com.pd.field_staff.ui.theme.MediumStyle
import com.pd.field_staff.ui.theme.RegularStyle
import com.pd.field_staff.ui.theme.ThinStyle
import com.pd.field_staff.ui.viewmodel.ProfileViewModel
import com.pd.field_staff.ui.views.dialogs.ChangePassword
import com.pd.field_staff.ui.views.main.settings.EditProfileView
import com.pd.field_staff.ui.views.main.settings.PaymentDetailsView
import com.pd.field_staff.ui.views.main.settings.PaymentHistoryView
import com.pd.field_staff.utils.extension.EventEmit
import com.pd.field_staff.utils.extension.EventState
import com.pd.field_staff.utils.extension.PrefKeys
import com.pd.field_staff.utils.extension.animationTransition
import com.pd.field_staff.R
import com.pd.field_staff.ui.views.main.settings.EquipmentRentalView
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject


private val menus = listOf<SettingMenu>(
    SettingMenu("Edit Profile", icon = Icons.Filled.PersonPin, action = MenuAction.EDIT_PROFILE),
    SettingMenu("Work Schedule", icon = Icons.Filled.AttachMoney, action = MenuAction.UNASSIGNED),
    SettingMenu("Payment History", icon = Icons.Filled.AttachMoney, action = MenuAction.PAYMENT_HISTORY),
    SettingMenu("Vacation Leave", icon = Icons.Filled.AttachMoney, action = MenuAction.UNASSIGNED),
    SettingMenu("Equipment Rental", icon = Icons.Filled.AttachMoney, action = MenuAction.EQUIPMENT_RENTAL),
    SettingMenu("Language", icon = Icons.Filled.AttachMoney, action = MenuAction.UNASSIGNED),
    SettingMenu("Privacy & Security", icon = Icons.Filled.AttachMoney, action = MenuAction.UNASSIGNED),
    SettingMenu("Change Password", icon = Icons.Filled.Security, action = MenuAction.CHANGE_PASSWORD),
    SettingMenu("Logout", icon = Icons.AutoMirrored.Filled.Logout, action = MenuAction.LOGOUT),
)


@Composable
fun SettingsView() {

    val sharedPrefs: SharedPreferences = koinInject<SharedPreferences>()
    val profileViewModel: ProfileViewModel = koinViewModel()
    val fullName = sharedPrefs.getString(PrefKeys.USER_NAME, "")!!
    val email = sharedPrefs.getString(PrefKeys.USER_EMAIL, "")!!
    val currentContext = LocalContext.current
    var showChangePassword by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        profileViewModel.profileState.collect { state ->
            when (state) {
                is ProfileViewModel.ProfileState.LoggedOut -> {
                    EventEmit.triggerEvent(EventState.LoggedOut)
                }
                else -> { }
            }
        }
    }

    fun <T : ComponentActivity> gotoView(clazz: Class<T>) {
        val intent = Intent(currentContext, clazz)
        currentContext.startActivity(intent, currentContext.animationTransition())
    }


    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

            TopAvatar(fullName, email)
            //
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(10.dp)
            ) {
                items(menus) { menu ->
                    SettingItemMenu(menu = menu) { action ->
                        when (action) {
                            MenuAction.EDIT_PROFILE -> {
                                gotoView(EditProfileView::class.java)
                            }
                            MenuAction.PAYMENT_HISTORY -> {
                               gotoView(PaymentDetailsView::class.java)
                            }
                            MenuAction.UNASSIGNED -> { }
                            MenuAction.EQUIPMENT_RENTAL -> {
                                gotoView(EquipmentRentalView::class.java)
                            }
                            MenuAction.CHANGE_PASSWORD -> { showChangePassword = true }
                            MenuAction.LOGOUT -> { profileViewModel.logOut() }
                        }
                    }
                }
            }
        }

        if(showChangePassword){
            ChangePassword(
                onConfirm = { showChangePassword = false },
                onDismissRequest = { showChangePassword = false }
            )
        }
    }
}

@Composable
fun SettingItemMenu(menu: SettingMenu, onSelect: (MenuAction) -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp)
                .clickable { onSelect(menu.action) },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = menu.name, style = RegularStyle)

            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = menu.name)
        }

        HorizontalDivider()
    }
}
data class SettingMenu(
    val name: String,
    val icon: ImageVector,
    val action: MenuAction
)

enum class MenuAction {
    EDIT_PROFILE, UNASSIGNED,EQUIPMENT_RENTAL, PAYMENT_HISTORY, CHANGE_PASSWORD, LOGOUT
}

@Composable
fun TopAvatar(fullName: String, email: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier.size(75.dp),
                painter = painterResource(R.drawable.user_avatar),
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(fullName, color = Color.Black, style = MediumStyle)
            Text(email, color = Color.Black, style = LightStyle)
        }
    }

}