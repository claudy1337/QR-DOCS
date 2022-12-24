package com.autonture.qrdocs.ui.qrupload

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.autonture.qrdocs.R
import com.autonture.qrdocs.ui.generate_qr.GenerateQrCodeActivity
import com.autonture.qrdocs.ui.scan_profile.ScanProfileActivity
import kotlinx.android.synthetic.main.activity_upload_file.*

class UploadQrActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_file)
        exit_from_upload_activity_btn.setOnClickListener {
            startActivity(Intent(this, ScanProfileActivity::class.java))
        }
        generate_qr_btn.setOnClickListener {
            startActivity(Intent(this, GenerateQrCodeActivity::class.java))
        }
        upload_btn.setOnClickListener {

        }

    }
}