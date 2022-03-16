package com.revolgenx.anilib.ui.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.revolgenx.anilib.app.theme.dynamicSurfaceColor
import com.revolgenx.anilib.databinding.ChipTagPresenterBinding
import com.revolgenx.anilib.common.event.OpenSearchEvent
import com.revolgenx.anilib.search.data.model.filter.SearchFilterModel

class MediaGenreChipAdapter() : RecyclerView.Adapter<MediaGenreChipAdapter.GenreChipViewHolder>() {

    private var currentList: List<String>? = null

    fun submitList(list: List<String>?) {
        currentList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreChipViewHolder {
        return GenreChipViewHolder(
            ChipTagPresenterBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ).also {
                it.chipTagView.isCloseIconVisible = false
            }
        )
    }

    override fun onBindViewHolder(holder: GenreChipViewHolder, position: Int) {
        val genre = currentList?.get(position) ?: return
        holder.binding.chipTagView.text = genre
        holder.binding.root.setOnClickListener {
            OpenSearchEvent(SearchFilterModel(genre = genre)).postEvent
        }
    }

    override fun getItemCount(): Int {
        return currentList?.size ?: 0
    }

    inner class GenreChipViewHolder(val binding: ChipTagPresenterBinding) :
        RecyclerView.ViewHolder(binding.root)
}