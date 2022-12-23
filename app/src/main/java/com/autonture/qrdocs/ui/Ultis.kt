package com.autonture.qrdocs.ui

import android.content.Context
import android.widget.ImageView
import com.autonture.qrdocs.R
import com.pixplicity.sharp.Sharp
import okhttp3.*
import java.io.IOException
import java.io.InputStream

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
}