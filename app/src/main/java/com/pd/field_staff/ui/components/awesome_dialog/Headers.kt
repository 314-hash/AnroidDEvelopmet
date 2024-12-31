package com.pd.field_staff.ui.components.awesome_dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.pd.field_staff.R

@Composable
fun InfoHeader(modifier: Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.info))
    LottieAnimation(
        composition,
        modifier = modifier,
    )
}

@Composable
fun SuccessHeader(modifier: Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
    LottieAnimation(
        composition,
        modifier = modifier,
    )
}

@Composable
fun ErrorHeader(modifier: Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))
    LottieAnimation(
        composition,
        modifier = modifier
    )
}


