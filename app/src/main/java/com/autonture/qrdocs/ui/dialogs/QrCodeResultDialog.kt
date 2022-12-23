package com.autonture.qrdocs.ui.dialogs

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings.Global.getString
import android.text.ClipboardManager
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.DialogFragment
import com.autonture.qrdocs.R
import com.autonture.qrdocs.db.DbHelper
import com.autonture.qrdocs.db.DbHelperI
import com.autonture.qrdocs.db.database.QrResultDataBase
import com.autonture.qrdocs.db.entities.QrResult
import com.autonture.qrdocs.ui.mainactivity.MainActivity
import com.autonture.qrdocs.ui.zoomfile.ZoomFileActivity
import com.autonture.qrdocs.utils.ContentCheckUtil
import com.autonture.qrdocs.utils.ContentCheckUtil.isWebUrl
import com.autonture.qrdocs.utils.toFormattedDisplay
import kotlinx.android.synthetic.main.layout_qr_result_show.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.json.JSONObject
import java.net.URL
import android.content.Intent as Intent


class QrCodeResultDialog (var context: Context) {

    private lateinit var dialog: Dialog

    private lateinit var dbHelperI: DbHelperI
    private lateinit var Main: MainActivity

    private var qrResult: QrResult? = null

    private var onDismissListener: OnDismissListener? = null

    init {
        init()
        initDialog()
    }


    private fun init() {
        dbHelperI = DbHelper(QrResultDataBase.getAppDatabase(context)!!)
    }

    private fun initDialog() {
        dialog = Dialog(context)
        dialog.setContentView(R.layout.layout_qr_result_show)
        dialog.setCancelable(false)
        onClicks()
    }

    private fun onClicks() {

        dialog.favouriteIcon.setOnClickListener {
            if (it.isSelected) {
                removeFromFavourite()
            } else
                addToFavourite()
        }
        dialog.saveIcon.setOnClickListener {
            OpenZoomActivity()
        }

        dialog.copyResult.setOnClickListener {
            copyResultToClipBoard()
        }

        dialog.shareResult.setOnClickListener {
            shareResult()
        }

        dialog.cancelDialog.setOnClickListener {
            dialog.dismiss()
            onDismissListener?.onDismiss()
        }

        dialog.scannedText.setOnClickListener {
            checkContentAndPerformAction(dialog.scannedText.text.toString())
        }
    }


    private fun addToFavourite() {
        dialog.favouriteIcon.isSelected = true
        dbHelperI.addToFavourite(qrResult?.id!!)
    }

    private fun removeFromFavourite() {
        dialog.favouriteIcon.isSelected = false
        dbHelperI.removeFromFavourite(qrResult?.id!!)
    }


    fun show(recentQrResult: QrResult) {
        this.qrResult = recentQrResult
        dialog.scannedDate.text = qrResult?.calendar?.toFormattedDisplay()
        dialog.scannedText.text = "QR code in ${"QR-DOCS"} app: " + qrResult!!.result
        dialog.favouriteIcon.isSelected = qrResult!!.favourite
        dialog.show()


        val url = "https://qr-scanner-api.herokuapp.com/api/user/" + qrResult!!.result
        dialog.userInfo1.text = ""
        dialog.userInfo2.text = context.getString(R.string.loading);
        dialog.userInfo3.text = ""
        dialog.userInfo4.text = ""


        doAsync{
            val json = URL(url).readText()
            uiThread {
                val obj = JSONObject(json)

                try {

                    if (obj.getString("status") == "success"){

                        dialog.userInfo1.text = "Name: \n" + obj.getJSONObject("data").getString("name")
                        dialog.userInfo2.text = "Email: \n" + obj.getJSONObject("data").getString("email")
                        dialog.userInfo3.text = "Field: \n" + obj.getJSONObject("data").getString("field")
                        dialog.userInfo4.text = "Website: \n" + obj.getJSONObject("data").getString("website")
                    }
                    else if (obj.getString("status") == "fail"){

                        dialog.userInfo1.text = ""
                        dialog.userInfo2.text = obj.getString("message")
                        dialog.userInfo3.text = ""
                        dialog.userInfo4.text = ""
                    }
                }
                catch (e: NumberFormatException) {

                    Log.e("Tag", e.toString())
                }
            }
        }

    }

    fun setOnDismissListener(dismissListener: OnDismissListener) {
        this.onDismissListener = dismissListener
    }

    private fun copyResultToClipBoard() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("QrCodeScannedResult", dialog.scannedText.text)
        clipboard.text = clip.getItemAt(0).text.toString()
        Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
    }
    private fun OpenZoomActivity(){

    }

    private fun shareResult() {
        val txtIntent = Intent(Intent.ACTION_SEND)
        txtIntent.type = "text/plain"
        txtIntent.putExtra(
            Intent.EXTRA_TEXT,
            dialog.scannedText.text.toString()
        )
        context.startActivity(Intent.createChooser(txtIntent, "Share QR Result"))
    }

    interface OnDismissListener {
        fun onDismiss()
    }


    private fun checkContentAndPerformAction(scannedText: String) {
        when {

            // if it is web url
            isWebUrl(scannedText) -> {

                // opening web url.
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(scannedText)
                context.startActivity(i)
            }
        }
    }
}