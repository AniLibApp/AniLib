package com.revolgenx.anilib.ui.view.tags

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.otaliastudios.elements.Adapter
import com.pranavpandey.android.dynamic.support.widget.DynamicRecyclerView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.model.entry.AdvancedScore
import com.revolgenx.anilib.databinding.ChipTagPresenterBinding
import com.revolgenx.anilib.ui.dialog.InputDialog

class MediaListAdvanceScoreChipRecyclerView : DynamicRecyclerView {
    var chipTagList = mutableListOf<AdvancedScore>()

    private var fragmentManager: FragmentManager? = null

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

    fun submitTags(chipTags: MutableList<AdvancedScore>) {
        chipTagList = chipTags
        adapter!!.notifyDataSetChanged()
    }

    fun addTag(chipTag: AdvancedScore) {
        chipTagList.add(chipTag)
        adapter!!.notifyDataSetChanged()
    }

    fun setFragmentManager(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
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
            val chipTag = chipTagList[position]
            holder.binding.chipTagView.text = chipTag.scoreType
            holder.binding.chipTagView.setOnClickListener {
                val inputDialog = InputDialog.newInstance(null, InputType.TYPE_CLASS_TEXT, chipTag.scoreType, showPasteButton = false)
                inputDialog.onInputDoneListener = {
                    chipTagList[position].scoreType = it
                    notifyDataSetChanged()
                }
                inputDialog.show(fragmentManager!!, "chip_input_dialog")
            }

            holder.binding.chipTagView.setOnCloseIconClickListener {
                chipTagList.removeAt(position)
                notifyDataSetChanged()
            }
        }

        override fun getItemCount(): Int {
            return chipTagList.size
        }
    }

    class ChipTagViewHolder(val binding: ChipTagPresenterBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }
}