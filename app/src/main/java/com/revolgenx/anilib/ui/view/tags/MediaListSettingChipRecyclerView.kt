package com.revolgenx.anilib.ui.view.tags

import android.annotation.SuppressLint
import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.pranavpandey.android.dynamic.support.widget.DynamicRecyclerView
import com.revolgenx.anilib.databinding.ChipTagPresenterBinding
import com.revolgenx.anilib.ui.dialog.InputDialog

class MediaListSettingChipRecyclerView : DynamicRecyclerView {
    var chipTagList = mutableListOf<String>()

    var onItemRemoved: ((item: String, position: Int) -> Unit)? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(
        context,
        attributeSet,
        def
    ) {
        layoutManager = FlexboxLayoutManager(context)
        adapter = ChipTagAdapter()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitTags(chipTags: MutableList<String>) {
        chipTagList = chipTags
        adapter?.notifyDataSetChanged()
    }

    fun onTagAdded(){
        adapter?.notifyItemInserted(chipTagList.size - 1)
    }

    inner class ChipTagAdapter : RecyclerView.Adapter<ChipTagViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipTagViewHolder {
            return ChipTagViewHolder(
                ChipTagPresenterBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }

        override fun onBindViewHolder(holder: ChipTagViewHolder, position: Int) {
            holder.binding.apply {
                val item = chipTagList[position]
                chipTagView.text = item
                chipTagView.setOnCloseIconClickListener {
                    chipTagList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, chipTagList.size)
                    onItemRemoved?.invoke(item, position)
                }
            }
        }

        override fun getItemCount() = chipTagList.size
    }

    override fun applyWindowInsets() {
    }
    class ChipTagViewHolder(val binding: ChipTagPresenterBinding) :
        RecyclerView.ViewHolder(binding.root)
}