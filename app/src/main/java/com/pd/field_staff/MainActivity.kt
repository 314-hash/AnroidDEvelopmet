package com.pd.field_staff

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.pd.field_staff.ui.theme.FieldStaffTheme
import com.pd.field_staff.ui.views.auth.LoginView
import com.pd.field_staff.ui.views.main.DashboardView
import com.pd.field_staff.ui.views.main.jobs.JobDetailsView
import com.pd.field_staff.ui.views.main.jobs.JobLocationView
import com.pd.field_staff.ui.views.main.settings.PaymentHistoryView
import com.pd.field_staff.utils.extension.animationTransition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    val sharedPreferences: SharedPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FieldStaffTheme {
                val scope = rememberCoroutineScope()
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ){
                    installSplashScreen().apply {
                        scope.launch {
                            delay(2000)
                            setKeepOnScreenCondition { false }
                        }
                    }
                    gotoNextScreen()
                }else {
                    Box(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxSize()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.splash_image),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                    SideEffect {
                        scope.launch {
                            delay(3000)
                            gotoNextScreen()
                        }
                    }

                }

            }
        }
    }

    private fun gotoNextScreen() {
        var nextView = Intent(this, LoginView::class.java)
        val _token = sharedPreferences.getString("jwtToken", null)
        if(_token != null) {
            nextView = Intent(this, DashboardView::class.java)
        }
        startActivity(nextView, animationTransition())
        finish()
    }

}
