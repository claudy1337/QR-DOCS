package com.autonture.qrdocs.ui.scan_profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.autonture.qrdocs.R
import com.autonture.qrdocs.ui.mainactivity.MainActivity
import com.autonture.qrdocs.ui.manifest_permission.ManifestPermission
import com.autonture.qrdocs.ui.qrupload.UploadQrActivity
import com.autonture.qrdocs.ui.zoomfile.ZoomFileActivity
import kotlinx.android.synthetic.main.activity_scan_profile.*

class ScanProfileActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_profile)

        upload_file_btn.setOnClickListener {
            startActivity(Intent(this, UploadQrActivity::class.java))
            finish()
        }
        place_camera_btn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        permission_start_btn.setOnClickListener {
            ManifestPermission.showRationalDialog(this,"Go To Setting App")
        }

        // zoom_file_btn.setOnClickListener {
        //startActivity(Intent(this, ZoomFileActivity::class.java))
        //  finish()
        //}
    }
}