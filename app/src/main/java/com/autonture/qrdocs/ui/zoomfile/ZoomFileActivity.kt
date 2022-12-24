package com.autonture.qrdocs.ui.zoomfile

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.autonture.qrdocs.R
import com.autonture.qrdocs.ui.Ultis
import com.autonture.qrdocs.ui.link_storage.TemporaryLinkStorage
import com.autonture.qrdocs.ui.scan_profile.ScanProfileActivity
import kotlinx.android.synthetic.main.activity_zoom_file.*
import java.lang.Exception

class ZoomFileActivity: AppCompatActivity()  {
    lateinit var imageView: ImageView
    var url: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoom_file)
        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                try {
                    recreate()
                } catch (e: Exception) {}
                handler.postDelayed(this, 50000)
            }
        }
        handler.postDelayed(runnable, 50000)

        try {
            val bundle = intent.extras
            if (bundle != null){
                url = bundle.getString("link")
            }
            imageView = findViewById(R.id.image_result)
            Ultis().fetchSVG(this,url!!,imageView)
            exit_zoom_btn.setOnClickListener {
                startActivity(Intent(this, ScanProfileActivity::class.java))
            }

        }
        catch (e:Exception){
            Toast.makeText(this, "update", Toast.LENGTH_LONG).show()
        }

    }

}

//"https://www.svgrepo.com/show/197208/waiting-room.svg" test link