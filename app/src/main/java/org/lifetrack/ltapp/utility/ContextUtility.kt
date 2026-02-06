package org.lifetrack.ltapp.utility


import android.widget.Toast
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.net.toUri
import androidx.core.content.ContextCompat


fun Context.openEmail(email: String) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "mailto:".toUri()
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
    }
}

fun Context.openDialer(phone: String) {
    try {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = "tel:$phone".toUri()
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(this, "Unable to open dialer", Toast.LENGTH_SHORT).show()
    }
}

fun Context.openSMS(phone: String, message: String? = null) {
    try {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = "smsto:$phone".toUri()

            message?.let {
                putExtra("sms_body", it)
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    } catch (_: Exception) {
        Toast.makeText(
            this,
            "No SMS app found",
            Toast.LENGTH_SHORT
        ).show()
    }
}

fun Context.makeAutoCall(phone: String = "911") {
    val hasPermission = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CALL_PHONE
    ) == PackageManager.PERMISSION_GRANTED

    if (hasPermission) {
        try {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = "tel:$phone".toUri()
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            startActivity(intent)
        } catch (e: Exception) {
            openDialer(phone)
            println(mapOf(
                "msg" to e.message,
                "traceback" to e.stackTrace
            ))
        }
    } else {
        Toast.makeText(
            this,
            "Direct Calling Permission Required \uD83C\uDF32",
            Toast.LENGTH_SHORT).show()
        openDialer(phone)
    }
}