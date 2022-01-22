package com.revolgenx.anilib.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.databinding.ChipTagPresenterBinding


class MediaFilterFormatAdapter(private val context: Context) :
    RecyclerView.Adapter<MediaFilterFormatAdapter.MediaFilterFormatViewHolder>() {

    private val listFormats by lazy {
        context.resources.getStringArray(R.array.media_format)
    }

    var onItemRemoved: ((item: Int) -> Unit)? = null

    var currentList: MutableList<Int>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaFilterFormatViewHolder {
        return MediaFilterFormatViewHolder(
            ChipTagPresenterBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MediaFilterFormatViewHolder, position: Int) {
        val format = currentList?.get(position) ?: return
        val currentFormat = listFormats[format]

        holder.binding.chipTagView.text = currentFormat

        holder.binding.chipTagView.setOnCloseIconClickListener {
            val removed = currentList!!.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount - position);
            onItemRemoved?.invoke(removed)
        }

    }

    override fun getItemCount(): Int {
        return currentList?.size ?: 0
    }

    inner class MediaFilterFormatViewHolder(val binding: ChipTagPresenterBinding) :
        RecyclerView.ViewHolder(binding.root)
}