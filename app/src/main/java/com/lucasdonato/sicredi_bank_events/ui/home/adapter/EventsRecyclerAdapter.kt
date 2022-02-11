package com.lucasdonato.sicredi_bank_events.ui.home.adapter

import Events
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.lucasdonato.sicredi_bank_events.R
import com.lucasdonato.sicredi_bank_events.databinding.CardHomeRecyclerViewBinding
import com.lucasdonato.sicredi_bank_events.mechanism.utils.Utils
import com.lucasdonato.sicredi_bank_events.ui.base.adapter.BaseRecyclerAdapter

class EventsRecyclerAdapter : BaseRecyclerAdapter<Events, EventsRecyclerAdapter.ViewHolder>() {

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(mData[position], position)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.context),
                R.layout.card_home_recycler_view,
                viewGroup,
                false
            )
        )
    }

    inner class ViewHolder(binding: CardHomeRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root) {
        private val title = binding.homeTitle
        private val container = binding.container
        private val imageEvent = binding.homeImage
        private val imageProgress = binding.imageProgress

        fun bind(events: Events, position: Int) {
            title.text = events.title
            container.setOnClickListener { onItemClickListener?.invoke(events) }
            Utils.setImageUrl(events.image, imageEvent, imageProgress)
        }
    }

}