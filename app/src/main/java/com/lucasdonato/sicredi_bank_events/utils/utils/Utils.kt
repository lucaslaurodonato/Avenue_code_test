package com.lucasdonato.sicredi_bank_events.utils.utils

import android.graphics.drawable.Drawable
import android.view.View.GONE
import android.widget.ImageView
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lucasdonato.sicredi_bank_events.R
import com.lucasdonato.sicredi_bank_events.ui.AppApplication.Companion.context

object Utils {

    fun setImageUrl(imageUrl:String?, imageView: ImageView, progressBar: ProgressBar){
        imageUrl.let { photoUrl ->
            context?.let {
                Glide.with(it).load(photoUrl).listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?, model: Any?,
                            target: Target<Drawable>?, isFirstResource: Boolean
                        ): Boolean {
                            imageView.setImageResource(R.drawable.not_found)
                            progressBar.visibility = GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = GONE
                            return false
                        }
                    }).into(imageView)
            }
        }
    }

}