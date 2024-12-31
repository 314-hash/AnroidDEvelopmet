package com.pd.field_staff.ui.components.awesome_dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pd.field_staff.ui.theme.ForestGreen
import com.pd.field_staff.ui.theme.LightStyle

val ButtonShapes = Shapes(
    small = RoundedCornerShape(4.dp),
    medium = RoundedCornerShape(4.dp),
    large = RoundedCornerShape(10.dp)
)

@Composable
fun OkButton(modifier: Modifier = Modifier, label: String, onPositive: () -> Unit) {
    Button(
        onClick = onPositive,
        shape = ButtonShapes.large,
        colors = ButtonDefaults.buttonColors(containerColor = ForestGreen),
        modifier = modifier
    ) {
        Text(label, style = LightStyle, color = Color.White)
    }
}

@Composable
fun CancelButton(modifier: Modifier = Modifier, onNegative: () ->Unit) {
    Button(
        onClick = onNegative,
        shape = ButtonShapes.large,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = ForestGreen),
        border = BorderStroke(1.dp, ForestGreen),
        modifier = modifier
    ) {
        Text("Cancel",  style = LightStyle, color = ForestGreen)
    }
}

enum class ButtonPresence {
    Success,
    Cancel,
    All
}
