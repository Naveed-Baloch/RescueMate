package com.rescuemate.app.extensions

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import io.github.rupinderjeet.kprogresshud.KProgressHUD

fun KProgressHUD.isVisible(isLoading: Boolean) = if (isLoading) this.show() else this.dismiss()

fun Modifier.clickableWithOutRipple(onClick: () -> Unit): Modifier = composed {
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() }) {
        onClick()
    }
}