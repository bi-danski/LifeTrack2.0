package org.lifetrack.ltapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.core.net.toUri

fun Context.openEmail(email: String) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            setData("mailto:".toUri())
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        }
        startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
    }
}

fun Context.openDialer(phone: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            setData("tel:$phone".toUri())
        }
        startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(this, "Unable to make call", Toast.LENGTH_SHORT).show()
    }
}

fun Context.openSMS(phone: String) {
    try{
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            setData("smsto:$phone".toUri())
//            putExtra(Intent.ACTION_SENDTO, Uri.parse("smsto:".toUri()))
        }
        startActivity(intent)
    }catch (_: Exception) {
        Toast.makeText(this, "Unable to process your sms", Toast.LENGTH_SHORT).show()
    }
}