package com.autonture.qrdocs.ui.zoomfile

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.autonture.qrdocs.R
import com.autonture.qrdocs.ui.Ultis
import com.autonture.qrdocs.ui.scan_profile.ScanProfileActivity
import kotlinx.android.synthetic.main.activity_zoom_file.*

class ZoomFileActivity: AppCompatActivity()  {
    lateinit var imageView: ImageView
    var url: String = "https://www.svgrepo.com/show/197208/waiting-room.svg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoom_file)
        imageView = findViewById(R.id.image_result)
        Ultis().fetchSVG(this,url,imageView)
        exit_zoom_btn.setOnClickListener {
            startActivity(Intent(this, ScanProfileActivity::class.java))
        }

    }
}