package com.lucasdonato.avenue_code_test.presentation.home.adapter

import Events
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.lucasdonato.avenue_code_test.R
import com.lucasdonato.avenue_code_test.mechanism.extensions.gone
import com.lucasdonato.avenue_code_test.presentation.base.adapter.BaseRecyclerAdapter
import kotlinx.android.synthetic.main.card_home_recycler_view.view.*

class EventsRecyclerAdapter : BaseRecyclerAdapter<Events, EventsRecyclerAdapter.ViewHolder>() {

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(mData[position], position)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(viewGroup.context).inflate(
            (R.layout.card_home_recycler_view), viewGroup,
            false
        )
    )

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(events: Events, position: Int) {
            itemView.apply {
                home_title.text = events.title
                setupImageWithGlide(events.image, context)
                container.setOnClickListener { onItemClickListener?.invoke(events) }
            }
        }

        private fun setupImageWithGlide(imageUrl: String?, context: Context) {

            itemView.apply {
                imageUrl?.let { photoUrl ->
                    Glide.with(context).load(photoUrl)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?, model: Any?,
                                target: Target<Drawable>?, isFirstResource: Boolean
                            ): Boolean {
                                home_image.setBackgroundResource(R.drawable.not_found)
                                image_progress.gone()
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                image_progress.gone()
                                return false
                            }
                        }).into(home_image)
                }
            }
        }
    }

}