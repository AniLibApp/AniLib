package com.revolgenx.anilib.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary
import com.revolgenx.anilib.common.data.field.TagField
import com.revolgenx.anilib.common.data.meta.TagState
import com.revolgenx.anilib.databinding.TagHolderLayoutBinding
import com.revolgenx.anilib.ui.view.TriStateCheckState
import com.revolgenx.anilib.ui.view.TriStateMode

class SelectorAdapter(private val tagMode: TriStateMode) : RecyclerView.Adapter<SelectorAdapter.TagHolder>() {
    private var tags: List<TagField> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagHolder {
        return TagHolder(
            TagHolderLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    fun submitList(tags: List<TagField>) {
        this.tags = tags
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return tags.size
    }

    override fun onBindViewHolder(holder: TagHolder, position: Int) {
        val item = tags[position]
        holder.bind(item)
    }

    fun deSelectAll() {
        tags.forEach {
            it.tagState = TagState.EMPTY
        }
        notifyDataSetChanged()
    }

    inner class TagHolder(private val v: TagHolderLayoutBinding) :
        RecyclerView.ViewHolder(v.root) {
        fun bind(item: TagField) {
            v.apply {
                tagCheckBox.triStateMode = tagMode
                tagCheckBox.setStateChangeListener { state ->
                    item.tagState = TagState.values()[state.ordinal]
                }
                tagCheckBox.setTextColor(dynamicTextColorPrimary)
                tagCheckBox.text = item.tag
                tagCheckBox.checkedState = TriStateCheckState.values()[item.tagState.ordinal]
            }
        }
    }
}
