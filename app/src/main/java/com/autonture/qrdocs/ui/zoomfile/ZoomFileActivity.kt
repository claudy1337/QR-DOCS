package com.autonture.qrdocs.ui.zoomfile

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.recreate
import com.autonture.qrdocs.R
import com.autonture.qrdocs.ui.Ultis
import com.autonture.qrdocs.ui.link_storage.TemporaryLinkStorage
import com.autonture.qrdocs.ui.qrupload.UploadQrActivity
import com.autonture.qrdocs.ui.scan_profile.ScanProfileActivity
import kotlinx.android.synthetic.main.activity_zoom_file.*
import kotlinx.android.synthetic.main.layout_qr_result_show.*
import java.lang.Exception
import java.time.Duration

class ZoomFileActivity: AppCompatActivity()  {
    lateinit var imageView: ImageView
    var url: String? = null
    private val duration = Toast.LENGTH_SHORT
    private var status_update: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zoom_file)
        UpdateView(status_update)
        try {
            val bundle = intent.extras
            if (bundle != null){
                url = bundle.getString("link")
            }
            imageView = findViewById(R.id.image_result)
            if (url!!.contains(".svg")){
                Ultis().fetchSVG(this,url!!,imageView)
            }
            else if (url!!.contains(".png") || url!!.contains(".jpeg") || url!!.contains("jpg")){
                Ultis().fetchOtherFormat(url!!,imageView)
            }
            else{
                onBackPressed()
            }
            exit_zoom_btn.setOnClickListener {
                startActivity(Intent(this, ScanProfileActivity::class.java))
            }
            update_view_btn.setOnClickListener {
                if (it.isSelected) {
                    disableUpdate()
                } else
                    enableUpdate()
            }
        }
        catch (e:Exception){
            Toast.makeText(this, "update error", duration).show()
        }

    }
    private fun disableUpdate() {
        update_view_btn.isSelected = false
        UpdateView(false)
        Toast.makeText(this, "update off", duration)
    }
    private fun enableUpdate(){
        update_view_btn.isSelected = true
        UpdateView(true)
        Toast.makeText(this, "update on", duration)
    }
    private fun UpdateView(status:Boolean){
        if (status){
            val handler = Handler()
            val runnable = object : Runnable {
                override fun run() {
                    try {
                        recreate()
                    } catch (e: Exception) {}
                    handler.postDelayed(this, 10000)
                }
            }
            handler.postDelayed(runnable, 10000)

        }

    }

}

//"https://www.svgrepo.com/show/197208/waiting-room.svg" test link