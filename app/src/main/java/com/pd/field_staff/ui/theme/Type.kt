package com.pd.field_staff.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.pd.field_staff.R

val InterFonts = FontFamily(
    Font(R.font.inter_28pt_bold, FontWeight.Bold),
    Font(R.font.inter_28pt_medium, FontWeight.Medium),
    Font(R.font.inter_28pt_regular, FontWeight.Normal),
    Font(R.font.inter_28pt_light, FontWeight.Light),
    Font(R.font.inter_28pt_thin, FontWeight.Thin)
)
// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = InterFonts,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)




val BoldStyle = TextStyle(
    fontFamily = InterFonts,
    fontWeight = FontWeight.Bold,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
)

val MediumStyle = TextStyle(
    fontFamily = InterFonts,
    fontWeight = FontWeight.Medium,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
)

val RegularStyle = TextStyle(
    fontFamily = InterFonts,
    fontWeight = FontWeight.Normal,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
)

val LightStyle = TextStyle(
    fontFamily = InterFonts,
    fontWeight = FontWeight.Light,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
)

val ThinStyle = TextStyle(
    fontFamily = InterFonts,
    fontWeight = FontWeight.Thin,
    lineHeight = 16.sp,
    letterSpacing = 0.5.sp
)