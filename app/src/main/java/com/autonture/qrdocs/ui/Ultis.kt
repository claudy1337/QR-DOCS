package com.autonture.qrdocs.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import com.autonture.qrdocs.R
import com.pixplicity.sharp.Sharp
import okhttp3.*
import java.io.IOException
import java.io.InputStream
import java.util.concurrent.Executors

class Ultis {
    private var httpClient: OkHttpClient? = null
    fun fetchSVG(context: Context, url: String, target: ImageView) {
        if (httpClient == null) {
            httpClient =
                OkHttpClient.Builder().cache(Cache(context.cacheDir, 5 * 1024 * 1014) as Cache)
                    .build() as OkHttpClient
        }
        var request: Request = Request.Builder().url(url).build()
        httpClient!!.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                target.setImageResource(R.drawable.noresult_black)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call?, response: Response) {
                val stream: InputStream = response.body()!!.byteStream()
                Sharp.loadInputStream(stream).into(target)
                stream.close()
            }
        })
    }
    fun fetchOtherFormat(url: String, target: ImageView){
        val executor = Executors.newSingleThreadExecutor()
        val handler = Handler(Looper.getMainLooper())
        var image: Bitmap? = null
        executor.execute {
            val imageURL = url
            try {
                val `in` = java.net.URL(imageURL).openStream()
                image = BitmapFactory.decodeStream(`in`)
                handler.post {
                    target.setImageBitmap(image)
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}