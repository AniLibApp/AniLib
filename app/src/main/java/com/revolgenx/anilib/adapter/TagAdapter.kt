package com.revolgenx.anilib.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.field.TagField
import kotlinx.android.synthetic.main.tag_holder_layout.view.*

class TagAdapter : RecyclerView.Adapter<TagAdapter.TagHolder>() {
    private val textColor =
        DynamicTheme.getInstance().get().tintSurfaceColor

    private var tags: List<TagField> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagHolder {
        return TagHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.tag_holder_layout,
                parent,
                false
            )
        )
    }

    fun submitList(tags:List<TagField>){
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
            it.isTagged = false
        }
        notifyDataSetChanged()
    }

    inner class TagHolder(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(item: TagField) {
            itemView.apply {
                tagCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    item.isTagged = isChecked
                }
                tagCheckBox.setTextColor(textColor)
                tagCheckBox.text = item.tag
                tagCheckBox.isChecked = item.isTagged
            }
        }
    }
}
