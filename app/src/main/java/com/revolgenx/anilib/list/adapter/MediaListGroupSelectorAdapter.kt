package com.revolgenx.anilib.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.revolgenx.anilib.databinding.MediaListGroupSelectorVhBinding

class MediaListGroupSelectorAdapter(private val callback: (groupName: String) -> Unit) :
    RecyclerView.Adapter<MediaListGroupSelectorAdapter.MediaListGroupSelectorVH>() {

    var groupSelectorList = listOf<Pair<Pair<String, Int>, Boolean>>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaListGroupSelectorVH {
        return MediaListGroupSelectorVH(
            MediaListGroupSelectorVhBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MediaListGroupSelectorVH, position: Int) {
        val item = groupSelectorList[position]
        val groupNameWithCount = item.first
        val isSelected = item.second

        with(holder.binding) {
            listGroupName.text = "%s %d".format(groupNameWithCount.first, groupNameWithCount.second)
            listGroupName.isChecked = isSelected
            listGroupName.setOnCheckedChangeListener { _, _ ->
                callback.invoke(groupNameWithCount.first)
            }
        }
    }

    override fun getItemCount(): Int = groupSelectorList.count()

    inner class MediaListGroupSelectorVH(val binding: MediaListGroupSelectorVhBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

}