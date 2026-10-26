package com.fabin.volumecontrol

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.fabin.volumecontrol.ui.theme.VolumeControlTheme

class MainActivity : ComponentActivity() {

    private var showPermissionDialog by mutableStateOf(false)
    private var permissionRequestStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            VolumeControlTheme {

                if (showPermissionDialog) {

                    AlertDialog(
                        onDismissRequest = {
                            showPermissionDialog = false
                            finish()
                        },

                        title = {
                            Text("Permission Required")
                        },

                        text = {
                            Text(
                                "Volume Control needs permission to display " +
                                        "the volume controller over other apps. " +
                                        "Please allow this permission to use the app."
                            )
                        },

                        confirmButton = {
                            Button(
                                onClick = {
                                    showPermissionDialog = false
                                    permissionRequestStarted = true

                                    val intent = Intent(
                                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:$packageName")
                                    )

                                    startActivity(intent)
                                }
                            ) {
                                Text("Allow")
                            }
                        },

                        dismissButton = {
                            TextButton(
                                onClick = {
                                    showPermissionDialog = false
                                    finish()
                                }
                            ) {
                                Text("Not Now")
                            }
                        }
                    )
                }
            }
        }

        checkOverlayPermission()
    }

    private fun checkOverlayPermission() {

        if (Settings.canDrawOverlays(this)) {

            // Permission already granted.
            // Start the floating controller immediately.
            startVolumeService()

        } else {

            // Show our explanation before opening Android Settings.
            showPermissionDialog = true
        }
    }

    override fun onResume() {
        super.onResume()

        if (!permissionRequestStarted) {
            return
        }

        permissionRequestStarted = false

        if (Settings.canDrawOverlays(this)) {

            // User enabled the permission.
            startVolumeService()

        } else {

            // User returned without enabling the permission.
            finish()
        }
    }

    private fun startVolumeService() {

        startService(
            Intent(this, VolumeOverlayService::class.java)
        )

        // Close the Activity.
        // The floating controller is the actual app interface.
        finish()
    }
}