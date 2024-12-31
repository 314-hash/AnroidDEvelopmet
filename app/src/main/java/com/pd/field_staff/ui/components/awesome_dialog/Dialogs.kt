package com.pd.field_staff.ui.components.awesome_dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.pd.field_staff.ui.theme.MediumStyle
import com.pd.field_staff.ui.theme.RegularStyle

@Composable
fun SuccessDialog(
    title: String = "",
    desc: String = "",
    positiveBtnLabel: String = "Ok",
    onDismiss: () -> Unit,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit,
    buttonPresence: ButtonPresence = ButtonPresence.All
) {
    Dialog(onDismissRequest = onDismiss ) {
        Box(
            Modifier
                .width(300.dp)
                .height(400.dp)
        ) {
            Column(
                Modifier
                    .width(300.dp)
                    .height(300.dp)
            ) {
                Spacer(Modifier.height(36.dp))
                Box(
                    Modifier
                        .width(300.dp)
                        .height(164.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(10.dp))) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(title.uppercase(), style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(desc, style = TextStyle(fontSize = 14.sp, ))
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth()) {
                            when(buttonPresence) {
                                ButtonPresence.Cancel -> {
                                    CancelButton(
                                        Modifier.fillMaxWidth().weight(1f),
                                        onNegative = onNegativeClick
                                    )
                                }
                                ButtonPresence.Success -> {
                                    OkButton(
                                        Modifier.fillMaxWidth().weight(1f),
                                        label = positiveBtnLabel,
                                        onPositive = onPositiveClick
                                    )
                                }
                                else -> {
                                    CancelButton(
                                        Modifier.fillMaxWidth().weight(1f),
                                        onNegative = onNegativeClick
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    OkButton(
                                        Modifier.fillMaxWidth().weight(1f),
                                        label = positiveBtnLabel,
                                        onPositive = onPositiveClick
                                    )
                                }
                            }
                        }
                    }
                }
            }
            SuccessHeader(
                Modifier
                    .size(72.dp)
                    .align(Alignment.TopCenter)
                    .border(
                        border = BorderStroke(width = 5.dp, color = Color.White),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun ErrorDialog(
    title: String = "",
    desc: String = "",
    positiveBtnLabel: String = "Ok",
    onDismiss: () -> Unit,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            Modifier
                .width(300.dp)
                .height(400.dp)) {
            Column(
                Modifier
                    .width(300.dp)
                    .height(300.dp)
            ) {
                Spacer(Modifier.height(36.dp))
                Box(
                    Modifier
                        .width(300.dp)
                        .height(164.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(10.dp))) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(title.uppercase(), style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(desc, style = TextStyle(fontSize = 14.sp, ))
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth()) {
                            CancelButton(
                                Modifier.fillMaxWidth().weight(1f),
                                onNegative = onNegativeClick
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OkButton(
                                Modifier.fillMaxWidth().weight(1f),
                                label = positiveBtnLabel,
                                onPositive = onPositiveClick
                            )
                        }
                    }
                }
            }
            ErrorHeader(
                Modifier
                    .size(72.dp)
                    .align(Alignment.TopCenter)
                    .border(
                        border = BorderStroke(width = 5.dp, color = Color.White),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
fun InfoDialog(
    title: String = "",
    desc: String = "",
    height: Dp = 200.dp,
    positiveBtnLabel: String = "Ok",
    onDismiss: () -> Unit,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            Modifier
                .width(300.dp)
                .height(400.dp)) {
            Column(
                Modifier
                    .width(300.dp)
                    .height(400.dp)
            ) {
                Spacer(Modifier.height(36.dp))
                Box(
                    Modifier
                        .width(300.dp)
                        .height(height)
                        .background(color = Color.White, shape = RoundedCornerShape(10.dp))) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(title, style = MediumStyle, textAlign = TextAlign.Center, fontSize = 12.sp, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(desc, style = RegularStyle, textAlign = TextAlign.Justify, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            CancelButton(
                                Modifier.fillMaxWidth().weight(1f),
                                onNegative = onNegativeClick
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            OkButton(
                                Modifier.fillMaxWidth().weight(1f),
                                label = positiveBtnLabel,
                                onPositive = onPositiveClick
                            )
                        }
                    }
                }
            }
            InfoHeader(
                Modifier
                    .size(72.dp)
                    .align(Alignment.TopCenter)
                    .border(
                        border = BorderStroke(width = 4.dp, color = Color.White),
                        shape = CircleShape
                    )
            )
        }
    }
}
