package com.revolgenx.anilib.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.media.data.model.MediaTagModel
import com.revolgenx.anilib.databinding.ChipTagPresenterBinding
import com.revolgenx.anilib.common.event.OpenSearchEvent
import com.revolgenx.anilib.search.data.model.SearchFilterEventModel

class MediaTagChipAdapter(val onTagInfoClicked: (MediaTagModel) -> Unit) :
    RecyclerView.Adapter<MediaTagChipAdapter.TagChipViewHolder>() {

    private var currentList: List<MediaTagModel>? = null
    private var originalList: List<MediaTagModel>? = null
    private var spoilerShown = false
    private var hasSpoilerTags = false

    fun submitList(list: List<MediaTagModel>?) {
        originalList = list
        currentList = originalList?.filter { !it.isMediaSpoilerTag }
        hasSpoilerTags = originalList?.any { it.isMediaSpoilerTag } == true
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagChipViewHolder {
        return TagChipViewHolder(
            ChipTagPresenterBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ).also {
                it.chipTagView.setCloseIconResource(R.drawable.ic_info)
                it.chipTagView.setChipIconResource(R.drawable.ic_hide)
            }
        )
    }

    override fun onBindViewHolder(holder: TagChipViewHolder, position: Int) {
        holder.binding.apply {

            if (!hasSpoilerTags || position < itemCount - 1) {
                val tags = currentList?.get(position) ?: return
                chipTagView.text =
                    root.context.getString(R.string.tag_name_percent)
                        .format(tags.name, tags.rank ?: 0)
                chipTagView.isChipIconVisible = tags.isMediaSpoilerTag
                chipTagView.isCloseIconVisible = true

                root.setOnClickListener {
                    OpenSearchEvent(SearchFilterEventModel(tag = tags.name)).postEvent

                }

                chipTagView.setOnCloseIconClickListener {
                    onTagInfoClicked(tags)
                }
            } else {
                if (hasSpoilerTags) {
                    chipTagView.isCloseIconVisible = false
                    chipTagView.setText(if (spoilerShown) R.string.hide_spoiler else R.string.show_spoiler)
                    chipTagView.isChipIconVisible = true
                    root.setOnClickListener {
                        showHideSpoilerTags()
                    }
                }
            }
        }

    }


    override fun getItemCount(): Int {
        return currentList?.size?.let { if (hasSpoilerTags) it.plus(1) else it } ?: 0
    }

    private fun showHideSpoilerTags() {
        spoilerShown = if (spoilerShown) {
            currentList = originalList?.filter { !it.isMediaSpoilerTag }
            false
        } else {
            currentList = originalList
            true
        }

        notifyDataSetChanged()
    }

    inner class TagChipViewHolder(val binding: ChipTagPresenterBinding) :
        RecyclerView.ViewHolder(binding.root)
}