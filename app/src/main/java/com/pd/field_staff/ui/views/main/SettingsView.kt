package com.pd.field_staff.ui.views.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pd.field_staff.ui.theme.ForestGreen
import com.pd.field_staff.ui.theme.LightGray
import com.pd.field_staff.ui.theme.LightGreen
import com.pd.field_staff.ui.theme.MediumStyle
import com.pd.field_staff.ui.theme.RegularStyle
import com.pd.field_staff.utils.extension.EventEmit
import com.pd.field_staff.utils.extension.EventState
import com.pd.field_staff.utils.extension.showToast
import kotlinx.coroutines.launch

@Composable
fun SettingsView() {
    val currentContext = LocalContext.current
    val settingOptions = listOf(
        SettingItem(
            title = "Profile Settings",
            icon = Icons.Default.Person
        ),
        SettingItem(
            title = "Notification Settings",
            icon = Icons.Default.Notifications
        )
    )

    val settingLogout = SettingItem(
        title = "Logout",
        icon = Icons.Default.ExitToApp
    )

    var showLogoutDialog by remember { mutableStateOf(false) }
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
    ) {
        items(settingOptions) { option ->
            SettingsItem(option) {  }
            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
        }

        item {
           SettingsItem(settingLogout) {
               showLogoutDialog = true
           }
            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
        }
    }

    if(showLogoutDialog) {
        LogoutDialog {
            showLogoutDialog = false
        }
    }

}


@Composable
fun SettingsItem(
    settingItem: SettingItem,
    onItemClick: () -> Unit
) {
    var isExpanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    var iconRotation by remember { mutableStateOf(0f) }
    val animationRotation = remember {
        Animatable(0f)
    }

    fun animateIcon() {
        coroutineScope.launch {
            animationRotation.animateTo(
                targetValue = if(isExpanded) 0f else 180f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
           iconRotation = if(isExpanded) 0f else 180f
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { androidx.compose.material.MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
                onClick = {
                    isExpanded = !isExpanded
                    animateIcon()
                    onItemClick()
                }
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    modifier = Modifier.size(20.dp),
                    imageVector = settingItem.icon,
                    contentDescription = null,
                    tint = ForestGreen
                )
                Text(settingItem.title, style = MediumStyle, fontSize = 14.sp, color = Color.Black)
            }

            Icon(
                modifier = Modifier.size(20.dp).rotate(animationRotation.value),
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowDown,
                contentDescription = null,
                tint = Color.Gray
            )
        }

        AnimatedVisibility(
            visible = isExpanded,
            enter = slideInVertically(animationSpec = tween(200)) + fadeIn(),
            exit = slideOutVertically(animationSpec = tween(200)) + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                if (settingItem.title == "Notification Settings") {
                    NotificationOption()
                } else if (settingItem.title == "Profile Settings") {
                    Text("No profile changes available", style = RegularStyle, fontSize = 13.sp, color = Color.Black.copy(alpha = 0.7f))
                }
            }
        }
    }
}

data class SettingItem(
    val title: String,
    val icon: ImageVector
)

@Composable
fun LogoutDialog(onDismiss: ()-> Unit){
    val currentContext = LocalContext.current
    androidx.compose.material.AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Logout Confirmation") },
        text = { Text("Are you sure you want to logout from this application?") },
        confirmButton = {
            androidx.compose.material.TextButton(onClick = {
                EventEmit.eventState.value = EventState.LoggedOut
                onDismiss()
            }) {
                Text("Confirm", color = ForestGreen)
            }
        },
        dismissButton = {
            androidx.compose.material.TextButton(onClick = onDismiss) {
                Text("Cancel", color = ForestGreen)
            }
        }
    )
}

@Composable
fun NotificationOption() {
   var notificationEnabled by remember { mutableStateOf(true) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text("Push Notification", style = RegularStyle, fontSize = 13.sp, color = Color.Black)
        CustomSwitch(
            checked = notificationEnabled,
            onCheckedChange = { notificationEnabled = it }
        )
    }
}

@Composable
fun CustomSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val thumbColor = if (checked) ForestGreen else Color.LightGray
    val trackColor = if(checked) LightGreen else LightGray.copy(alpha = 0.5f)
    val trackWidth = 30.dp
    val thumbSize = 18.dp
    val offset by animateFloatAsState(if (checked) trackWidth - thumbSize else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "thumb switch")
    
    Surface(
        modifier = Modifier
            .size(width = trackWidth, height = 25.dp)
            .clip(RoundedCornerShape(50))
            .clickable { onCheckedChange(!checked) },
        color = trackColor
    ) {
        Box(
            contentAlignment = Alignment.CenterStart
        ) {
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .size(thumbSize)
                    .background(color = thumbColor, shape = RoundedCornerShape(50))
                    .padding(start = offset),
            ) {

            }
        }
    }
}
