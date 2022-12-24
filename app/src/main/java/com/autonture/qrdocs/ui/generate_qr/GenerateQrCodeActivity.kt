package com.autonture.qrdocs.ui.generate_qr

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.text.TextUtils
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.autonture.qrdocs.R
import com.google.zxing.WriterException
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_generate_qr_code.*
import kotlinx.android.synthetic.main.activity_generate_qr_code.view.*
import org.jetbrains.anko.toast
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception

class GenerateQrCodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_qr_code)

        download_qr_btn.setOnClickListener {
             //   saveImageToStorage()
        }
        create_qr_code_btn?.setOnClickListener {
            if (TextUtils.isEmpty(data_value.text.toString())) {
                generateQrCode("autonture create")
            }
            else
            {
                generateQrCode(data_value.text.toString())
            }
        }
    }
    private fun generateQrCode(text: String){
        val qrGenerator = QRGEncoder(text, null, QRGContents.Type.TEXT, 500)
        try {
            val bMap = qrGenerator.encodeAsBitmap()
            qr_image?.setImageBitmap(bMap)
        } catch (e: WriterException){
            Toast.makeText(this, "text exception", Toast.LENGTH_SHORT)
        }
    }
}