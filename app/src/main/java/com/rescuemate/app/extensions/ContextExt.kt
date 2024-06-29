package com.rescuemate.app.extensions

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.ComponentActivity
import io.github.rupinderjeet.kprogresshud.KProgressHUD

tailrec fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun Context.progressBar(): KProgressHUD = KProgressHUD.create(this.getActivity())
        .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
        .setCancellable(true)
        .setAnimationSpeed(1)
        .setDimAmount(0.5f)

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context.openDialPanel(phoneNumber: String){
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
    startActivity(intent)
}

fun Context.openEmail(emailAddress: String){
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$emailAddress")
    }
    startActivity(Intent.createChooser(emailIntent, "Send feedback"))
}