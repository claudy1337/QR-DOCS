package com.autonture.qrdocs.ui.zoomfile

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.autonture.qrdocs.R
import com.autonture.qrdocs.ui.Ultis

class ZoomFileActivity: AppCompatActivity()  {
    lateinit var imageView: ImageView
    var url: String = "https://www.svgrepo.com/show/197208/waiting-room.svg"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoom_file)

        imageView = findViewById(R.id.image_result)


        Ultis().fetchSVG(this,url,imageView)
    }
}