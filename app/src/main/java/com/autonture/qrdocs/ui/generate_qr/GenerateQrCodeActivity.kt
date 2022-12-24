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
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.lang.Exception

class GenerateQrCodeActivity : AppCompatActivity() {

    private var imageUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_qr_code)

        download_qr_btn.setOnClickListener {
                saveImageToStorage()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    downloadImage(imageUrl!!)
                } else {


                }
                return
            }
            else -> {
            }
        }
    }


    private var msg: String? = ""
    private var lastMsg = ""

    @TargetApi(Build.VERSION_CODES.M)
    fun askPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            ) {
                AlertDialog.Builder(this, R.style.CustomAlertDialog)
                    .setTitle("Permission required")
                    .setMessage("Permission required to save photos from the Web.")
                    .setPositiveButton("Allow") { dialog, id ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                        )
                        finish()
                    }
                    .setNegativeButton("Deny") { dialog, id -> dialog.cancel() }
                    .show()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE
                )
            }
        } else {
            //saveImageToStorage(imageUrl!!)
        }
    }



    private fun generateQrCode(text: String){
        val qrGenerator = QRGEncoder(text, null, QRGContents.Type.TEXT, 500)
        try {
            val bMap = qrGenerator.encodeAsBitmap()
            qr_image?.setImageBitmap(bMap)

            imageUrl = text // convert to string
        } catch (e: WriterException){

        }
    }

    fun saveImageToStorage(){
        val externalStorage = Environment.getExternalStorageDirectory()
        if (externalStorage.equals(Environment.MEDIA_MOUNTED)){
            val storageDirectory = Environment.getExternalStorageDirectory().toString()
            val file = File(storageDirectory,"test.jpg")
            try {
                val stream:OutputStream = FileOutputStream(file)
                var drawable = ContextCompat.getDrawable(applicationContext, R.drawable.noresult)
                var bitmap = (drawable as BitmapDrawable).bitmap
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                stream.flush()
                stream.close()
                qr_image.setImageURI((Uri.parse(file.absolutePath)))
                Toast.makeText(this, "Image saved successfully  ${Uri.parse((file.absolutePath))}",
                Toast.LENGTH_SHORT).show()
            }
            catch (e: Exception){
                e.printStackTrace()
            }
        }
        else{

        }
    }

    private fun statusMessage(url: String, directory: File, status: Int): String? {
        var msg = ""
        msg = when (status) {
            DownloadManager.STATUS_FAILED -> "Download has been failed, please try again"
            DownloadManager.STATUS_PAUSED -> "Paused"
            DownloadManager.STATUS_PENDING -> "Pending"
            DownloadManager.STATUS_RUNNING -> "Downloading..."
            DownloadManager.STATUS_SUCCESSFUL -> "Image downloaded successfully in $directory" + File.separator + url.substring(
                url.lastIndexOf("/") + 1
            )
            else -> "There's nothing to download"
        }
        return msg
    }
    @SuppressLint("Range")
    private fun downloadImage(url: String) {
        val directory = File(Environment.DIRECTORY_PICTURES)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val downloadManager = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadUri = Uri.parse(url)

        val request = DownloadManager.Request(downloadUri).apply {
            setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle(url.substring(url.lastIndexOf("/") + 1))
                .setDescription("")
                .setDestinationInExternalPublicDir(
                    directory.toString(),
                    url.substring(url.lastIndexOf("/") + 1)
                )
        }

        val downloadId = downloadManager.enqueue(request)
        val query = DownloadManager.Query().setFilterById(downloadId)
        Thread(Runnable {
            var downloading = true
            while (downloading) {
                val cursor: Cursor = downloadManager.query(query)
                cursor.moveToFirst()
                if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                }
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                msg = statusMessage(url, directory, status)
                if (msg != lastMsg) {
                    this.runOnUiThread {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
                    }
                    lastMsg = msg ?: ""
                }
                cursor.close()
            }
        }).start()
    }

    companion object {
        private const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1
    }
}