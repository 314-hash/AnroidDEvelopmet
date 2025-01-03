package com.pd.field_staff.ui.views.main

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.pd.field_staff.ui.theme.FieldStaffTheme
import com.pd.field_staff.ui.theme.ForestGreen
import com.pd.field_staff.ui.theme.MediumStyle
import com.pd.field_staff.ui.theme.RegularStyle
import com.pd.field_staff.ui.views.auth.LoginView
import com.pd.field_staff.utils.extension.EventEmit
import com.pd.field_staff.utils.extension.EventState
import com.pd.field_staff.utils.extension.animationTransition
import org.koin.compose.koinInject
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.animation.core.*
import androidx.compose.ui.draw.scale
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

class DashboardView:ComponentActivity() {

    private lateinit var appUpdateManager: AppUpdateManager
    private val updateType = AppUpdateType.IMMEDIATE

    private val navItems = arrayListOf<BottomNavItem>(
        BottomNavItem(name="Home",route = "home", icon = Icons.Default.Home),
        BottomNavItem(name="Job List",route = "jobs/0", icon = Icons.AutoMirrored.Filled.List),
        BottomNavItem(name="Message",route = "messages", icon = Icons.AutoMirrored.Filled.Chat),
        BottomNavItem(name="Settings",route = "settings", icon = Icons.Default.Settings)
    )

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appUpdateManager = AppUpdateManagerFactory.create(applicationContext)
        checkForAppUpdates()

        setContent {

            FieldStaffTheme {

                val sharedPrefs: SharedPreferences = koinInject()
                val navController =  rememberNavController()
                var currentScreen by remember { mutableStateOf("Home") }
                val coroutineScope = rememberCoroutineScope()
                var topBarTextSize by remember { mutableStateOf(14.sp) }

                LaunchedEffect(Unit) {
                    EventEmit.eventState.collect { eventState ->
                        when(eventState) {
                            EventState.LoggedOut -> {
                                toSigninView()
                            }
                            else -> { }
                        }
                    }
                }

                Scaffold (
                    topBar = {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White),
                            title = {
                                AnimatedContent(targetState = currentScreen,
                                    transitionSpec = {
                                        fadeIn(animationSpec = tween(200)) togetherWith fadeOut(animationSpec = tween(200))
                                    }
                                ) { targetScreen ->
                                    Text(text = targetScreen, style = MediumStyle, fontSize = topBarTextSize)
                                }
                            }
                        )
                    },
                    bottomBar = {
                        BottomNavigationBar(
                            items = navItems,
                            navController = navController,
                            onItemClick = {
                                coroutineScope.launch {
                                    topBarTextSize = 20.sp
                                    kotlinx.coroutines.delay(200)
                                    topBarTextSize = 14.sp
                                }

                                currentScreen = it.name
                                navController.navigate(it.route)
                            }
                        )
                    }

                ) { paddingValue ->
                    Box(modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = paddingValue.calculateTopPadding(),
                            bottom = paddingValue.calculateBottomPadding()
                        ),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        AnimatedContent(targetState = navController.currentBackStackEntryAsState().value?.destination?.route,
                            transitionSpec = {
                                if(initialState != null) {
                                    slideInHorizontally {
                                        -it
                                    } + fadeIn(animationSpec = tween(200)) togetherWith
                                            slideOutHorizontally{
                                                it
                                            } + fadeOut(animationSpec = tween(200))
                                } else {
                                    fadeIn(animationSpec = tween(200))
                                }
                            }, label = "nav animation") {
                            DashboardNavigation(navController = navController)
                        }

                    }
                }
            }
        }
    }

    private fun checkForAppUpdates() {
        appUpdateManager.appUpdateInfo.addOnSuccessListener {  info ->
            val isUpdateAvailable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            if( isUpdateAvailable ) {
                val options = AppUpdateOptions.newBuilder(updateType).build()
                appUpdateManager.startUpdateFlowForResult(info, this, options, 1001)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            if(info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                val options = AppUpdateOptions.newBuilder(updateType).build()
                appUpdateManager.startUpdateFlowForResult(info, this, options, 1001)
            }
        }
    }

    private fun toSigninView() {
        val nextView = Intent(applicationContext, LoginView::class.java)
        startActivity(nextView, animationTransition())
        finish()
    }

}

@Composable
fun DashboardNavigation(navController: NavHostController){
    NavHost(navController = navController, startDestination = "home") {
        composable("home"){
            HomeView(
                onJobCategory = { category ->
                    navController.navigate("jobs/$category")
                }
            )
        }
        composable("settings"){
            SettingsView()
        }
        composable(
            route = "jobs/{category}",
            arguments = listOf(navArgument("category") { type= NavType.IntType })
            ) { backStackEntry ->
            val category = backStackEntry.arguments?.getInt("category")
            JobListView(category = category)
        }
        composable("messages") {
            MessageView()
        }
    }
}

data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
    val badgeCount: Int = 0
)

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val backStackEntry = navController.currentBackStackEntryAsState()

    NavigationBar(
        modifier = modifier.height(55.dp),
        containerColor = Color.White,
        tonalElevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            var iconScale by remember { mutableStateOf(1f) }
            val animationScale = remember {
                Animatable(1f)
            }

            NavigationBarItem(
                selected = selected,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.White,
                    unselectedIconColor = Color.LightGray,
                    unselectedTextColor = Color.LightGray,
                    selectedIconColor = ForestGreen,
                    selectedTextColor = ForestGreen
                ),
                onClick = {
                    onItemClick(item)
                    iconScale = 1.2f
                     animationScale.animateTo(
                            targetValue = 1f,
                            animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow
                            )
                    )
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = null,
                        modifier = Modifier.scale(animationScale.value)
                    )
                },
                label = { Text(item.name, style = RegularStyle)}
            )
        }
    }
}





