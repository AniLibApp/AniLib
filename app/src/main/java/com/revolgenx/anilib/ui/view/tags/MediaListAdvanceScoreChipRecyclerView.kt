package com.revolgenx.anilib.ui.view.tags

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import com.pranavpandey.android.dynamic.support.widget.DynamicRecyclerView
import com.revolgenx.anilib.data.tuples.MutablePair
import com.revolgenx.anilib.entry.data.model.AdvancedScoreModel
import com.revolgenx.anilib.databinding.ChipTagPresenterBinding
import com.revolgenx.anilib.ui.dialog.InputDialog

class MediaListAdvanceScoreChipRecyclerView : DynamicRecyclerView {
    var chipTagList = mutableListOf<String>()

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

    fun submitTags(chipTags: MutableList<String>) {
        chipTagList = chipTags
        adapter!!.notifyDataSetChanged()
    }

    fun addTag(chipTag: String) {
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
            holder.binding.chipTagView.text = chipTag
            holder.binding.chipTagView.setOnClickListener {
                val inputDialog = InputDialog.newInstance(
                    null,
                    InputType.TYPE_CLASS_TEXT,
                    chipTag,
                    showPasteButton = false
                )
                inputDialog.onInputDoneListener = {
                    chipTagList[position] = it
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