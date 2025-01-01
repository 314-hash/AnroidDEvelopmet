package com.pd.field_staff.ui.views.main

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pd.field_staff.R
import com.pd.field_staff.ui.components.LottieAnimationSpec
import com.pd.field_staff.ui.theme.MediumStyle
import kotlinx.coroutines.delay

@Composable
fun MessageView() {
    val message = remember { mutableStateOf("") }
    val pulseScale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        pulseScale.animateTo(
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimationSpec(
            animRes = R.raw.message_loading,
            modifier = Modifier.size(200.dp).alpha(0.7f)
        )
        androidx.compose.animation.AnimatedVisibility(
            visible = true,
            enter = fadeIn() + scaleIn(),
            exit = scaleOut(),
        ) {
            Text(
                text = "Coming Soon...",
                style = MediumStyle,
                fontSize = 20.sp
            )
        }
    }
}
