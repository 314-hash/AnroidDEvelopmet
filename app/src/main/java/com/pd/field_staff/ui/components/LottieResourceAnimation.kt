package com.pd.field_staff.ui.components

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.pd.field_staff.R
import kotlinx.coroutines.delay

@Composable
fun ConfettiAnimation(onFinish: () -> Unit) {
    val poppingComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loader_confetti_party))
    val animateState = animateLottieCompositionAsState(composition = poppingComposition, isPlaying = true)
    LottieAnimation(composition = poppingComposition, iterations = 1)
    if(animateState.isAtEnd){
        val finishUpdate by rememberUpdatedState(newValue = onFinish)
        LaunchedEffect(Unit ){
            delay(2000L)
            finishUpdate()
        }
    }
}

@Composable
fun LottieAnimationSpec(@RawRes animRes: Int) {
    val animationComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(animRes))
    LottieAnimation(animationComposition, iterations = Int.MAX_VALUE)
}
