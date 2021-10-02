package com.thomas.apps.noteapp

import android.app.Application
import android.os.Build
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.decode.VideoFrameDecoder
import coil.fetch.VideoFrameFileFetcher
import coil.fetch.VideoFrameUriFetcher
import coil.util.CoilUtils
import okhttp3.OkHttpClient
import timber.log.Timber

class NoteApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        setUpTimber()

        setUpCoil()
    }

    private fun setUpTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    //image loader
    private fun setUpCoil() {
        val imageLoader = ImageLoader.Builder(applicationContext)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(this))
                    .build()
            }
            .componentRegistry {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder(applicationContext))
                } else {
                    add(GifDecoder())
                }
                add(SvgDecoder(applicationContext))

                add(VideoFrameFileFetcher(applicationContext))
                add(VideoFrameUriFetcher(applicationContext))
                add(VideoFrameDecoder(applicationContext))

            }
            .build()
        Coil.setImageLoader(imageLoader)
    }
}