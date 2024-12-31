package com.pd.field_staff.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.pd.field_staff.ui.theme.ForestGreen
import com.pd.field_staff.ui.theme.LightStyle
import com.pd.field_staff.ui.theme.RegularStyle


// https://www.youtube.com/watch?v=vCP8k_oJTkA&list=PLOqk5DrPOdLC0CbpnaIcefVQ1ZkgF4KuH&index=4
@Composable
fun CustomOutlineTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    leadingIconImage: ImageVector,
    leadingIconDesc: String = "",
    isPasswordField: Boolean = false,
    isPasswordVisible: Boolean = false,
    onVisibilityChange: (Boolean) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    showError: Boolean = false,
    errorMessage: String = "",
    readOnly: Boolean = false,
){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            shape = RoundedCornerShape(30.dp),
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            label = { Text(label, style = RegularStyle) },
            leadingIcon = {
                Icon(
                   imageVector = leadingIconImage,
                   contentDescription = leadingIconDesc,
                   tint = if (showError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
            },
            isError = showError,
            trailingIcon = {
                if(showError && !isPasswordField) Icon(imageVector = Icons.Filled.Error, contentDescription = "Error")
                if(isPasswordField) {
                    IconButton(onClick = { onVisibilityChange(!isPasswordVisible) }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Password"
                        )
                    }
                }
            },
            visualTransformation = when {
                isPasswordField && isPasswordVisible -> VisualTransformation.None
                isPasswordField -> PasswordVisualTransformation()
                else -> VisualTransformation.None
            },
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            readOnly = readOnly
        )
        if (showError){
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = LightStyle,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .offset(y = (-8).dp)
                    .fillMaxWidth(0.9f)
            )
        }
    }
}

@Composable
fun SimpleCustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String = "",
    isPasswordField: Boolean = false,
    isPasswordVisible: Boolean = false,
    onVisibilityChange: (Boolean) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    showError: Boolean = false,
    errorMessage: String = "",
    readOnly: Boolean = false,
    singleLine: Boolean = true
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceAround
    ) {

        Text(label, style = RegularStyle, modifier = Modifier.padding(vertical = 10.dp))

        OutlinedTextField(
            shape = RoundedCornerShape(15.dp),
            value = value,
            onValueChange = { onValueChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            trailingIcon = {
                if(isPasswordField) {
                    IconButton(onClick = { onVisibilityChange(!isPasswordVisible) }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "Password"
                        )
                    }
                }
            },
            visualTransformation = when {
                isPasswordField && isPasswordVisible -> VisualTransformation.None
                isPasswordField -> PasswordVisualTransformation()
                else -> VisualTransformation.None
            },
            textStyle = RegularStyle,
            isError = showError,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            readOnly = readOnly,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE5E5E5),
                unfocusedBorderColor = Color(0xFFE5E5E5)
            )
        )

        if (showError){
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = LightStyle,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .offset(y = (-8).dp)
                    .fillMaxWidth(0.9f)
            )
        }

    }

}

@Composable
fun DateTextField(
    modifier: Modifier,
    text: MutableState<String>, label: String, onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    if(isPressed){
        onClick()
    }
    OutlinedTextField(
        value = text.value,
        onValueChange = {  },
        readOnly = true, // Makes the text field non-editable
        interactionSource = interactionSource,
        modifier = modifier // Handles click events
            .focusable(false), // Prevents the text field from gaining focus
        label = { Text(label, style = RegularStyle) },
        textStyle = RegularStyle,
        enabled = true,
        colors = OutlinedTextFieldDefaults.colors(
            cursorColor = Color.Transparent, // Hides the cursor
            focusedBorderColor = Color(0xFFE5E5E5),
            unfocusedBorderColor = Color(0xFFE5E5E5)
        ),
        shape = RoundedCornerShape(5.dp),
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit
){
    var searchText by remember { mutableStateOf("") }
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        OutlinedTextField(
            shape = RoundedCornerShape(15.dp),
            value = searchText,
            placeholder = { Text(text = "Search for job today", style = RegularStyle, color = Color(0x9E95A280)) },
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                    Icon(
                        modifier = Modifier.background(color = ForestGreen, shape = RoundedCornerShape(5.dp)).padding(5.dp),
                        imageVector = Icons.Default.Search,
                        contentDescription = "search",
                        tint = Color.White
                    )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE5E5E5),
                unfocusedBorderColor = Color(0xFFE5E5E5)
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch(searchText) }
            ),
            textStyle = RegularStyle,
            singleLine = true
        )
    }
}



