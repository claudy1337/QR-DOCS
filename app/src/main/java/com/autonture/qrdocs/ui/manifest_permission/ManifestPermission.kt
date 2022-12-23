package com.autonture.qrdocs.ui.manifest_permission

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.autonture.qrdocs.R
import com.autonture.qrdocs.ui.manifest_permission.ManifestPermission.SETTINGS_PERMISSION_CODE


object ManifestPermission {

    private val TAG = ManifestPermission::class.java.getSimpleName()
    const val SETTINGS_PERMISSION_CODE = 1000




    fun showRationalDialog(activity : Activity, message : String) {
        Log.d(TAG,"showRationalDialog($activity,$message")
        val builder = activity.let { AlertDialog.Builder(it, R.style.CustomAlertDialog) }
        builder.setTitle("Manifest Permissions")
        builder.setMessage(message)
        builder.setPositiveButton("Settings") { dialog, which ->
            dialog.dismiss()
            showAppPermissionSettings(activity)
        }
        builder.setNegativeButton("Not Now") { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }

    private fun showAppPermissionSettings(activity : Activity) {
        Log.d("PermissionsResult", "showAppPermissionSettings()")
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", activity.packageName, null)
        )
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        activity.startActivityForResult(intent, SETTINGS_PERMISSION_CODE)
    }
}