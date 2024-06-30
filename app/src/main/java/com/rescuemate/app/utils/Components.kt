package com.rescuemate.app.utils

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bkcoding.garagegurufyp_user.utils.isValidEmail
import com.rescuemate.app.dto.UserType
import com.rescuemate.app.extensions.clickableWithOutRipple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomEditText(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    isError: Boolean,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
){
    TextField(
        value = value,
        onValueChange = { onValueChange(it) },
        visualTransformation = visualTransformation,
        isError = isError,
        placeholder = {
            Text(
                text = label,
                fontWeight = FontWeight.Normal
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = Color.Black,
            focusedIndicatorColor = Color.White,
            unfocusedIndicatorColor = Color.White
        ),
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = singleLine,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .fillMaxWidth()
    )
}

@Composable
fun UserTypeDropdown(
    modifier: Modifier = Modifier,
    onUserTypeSelected: (UserType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var dropDownText by remember { mutableStateOf("Select user type") }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.Black.copy(alpha = 0.1f))
            .padding(vertical = 15.dp, horizontal = 10.dp)
            .clickable { expanded = true }) {
        Text(
            text = dropDownText,
            modifier = Modifier.align(Alignment.CenterStart)
        )
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterEnd)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                onUserTypeSelected(UserType.Patient)
                dropDownText = UserType.Patient.name
            }, text = {
                    Text(text = UserType.Patient.getText()) }
            )

            DropdownMenuItem(onClick = {
                expanded = false
                onUserTypeSelected(UserType.Donor)
                dropDownText = UserType.Donor.name
            }, text = {
                Text(text = UserType.Donor.getText()) }
            )

            DropdownMenuItem(onClick = {
                expanded = false
                onUserTypeSelected(UserType.AmbulanceOwner)
                dropDownText = UserType.AmbulanceOwner.name
            }, text = {
                Text(text = UserType.AmbulanceOwner.getText()) }
            )

            DropdownMenuItem(onClick = {
                expanded = false
                onUserTypeSelected(UserType.AmbulanceOwner)
                dropDownText = UserType.LaboratoryOwner.name
            }, text = {
                Text(text = UserType.LaboratoryOwner.getText()) }
            )

        }
    }
}

@Composable
fun ActionButton(
    @DrawableRes imageId: Int,
    description: String,
    modifier: Modifier = Modifier,
    imageModifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.Black.copy(alpha = 1f),
                shape = RoundedCornerShape(5.dp)
            )
            .clip(RoundedCornerShape(5.dp))
            .background(Color.Black.copy(alpha = 0.06f))
            .size(150.dp)
            .clickableWithOutRipple { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageId),
            contentDescription = "",
            modifier = imageModifier
        )
        Text(
            text = description.uppercase(),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
                .widthIn(max = 200.dp),
            overflow = TextOverflow.Ellipsis
        )
    }
}