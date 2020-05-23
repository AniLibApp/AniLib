package com.revolgenx.anilib.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.field.TagChooserField
import com.revolgenx.anilib.field.TagField
import kotlinx.android.synthetic.main.tag_chooser_dialog_fragment_layout.*
import kotlinx.android.synthetic.main.tag_holder_layout.view.*

class TagChooserDialogFragment : DynamicDialogFragment() {
    companion object {
        const val TAG_KEY = "tag_key"
        fun newInstance(tags: TagChooserField) = TagChooserDialogFragment().apply {
            arguments = bundleOf(TAG_KEY to tags)
        }
    }

    private var doneListener: OnDoneListener? = null
    private val tagChooserField: TagChooserField by lazy { arguments?.getParcelable<TagChooserField>(TAG_KEY)!! }
    private val tagAdapter by lazy {
        TagAdapter()
    }

    override fun onCustomiseBuilder(
        dialogBuilder: DynamicDialog.Builder,
        savedInstanceState: Bundle?
    ): DynamicDialog.Builder {
        with(dialogBuilder) {
            arguments?.containsKey(TAG_KEY) ?: return super.onCustomiseBuilder(dialogBuilder, savedInstanceState)
            setTitle(tagChooserField.header)
            setView(R.layout.tag_chooser_dialog_fragment_layout)
            isAutoDismiss = false
        }

        return super.onCustomiseBuilder(dialogBuilder, savedInstanceState)
    }

    override fun onCustomiseDialog(
        alertDialog: DynamicDialog,
        savedInstanceState: Bundle?
    ): DynamicDialog {

        if (arguments?.containsKey(TAG_KEY) == true)
            alertDialog.apply {
                setOnShowListener {
                    doneTv.setOnClickListener {
                        doneListener?.onDone(
                            tag,
                            tagChooserField.tags
                        )
                        dismiss()
                    }
                    cancelTv.setOnClickListener {
                        dismiss()
                    }
                    unSelectAllTv.setOnClickListener {
                        tagAdapter.deSelectAll()
                    }
                    tagRecyclerView.layoutManager = LinearLayoutManager(context)
                    tagRecyclerView.adapter = tagAdapter
                }
            }

        return super.onCustomiseDialog(alertDialog, savedInstanceState)
    }


    fun onDoneListener(listener: OnDoneListener) {
        doneListener = listener
    }

    inner class TagAdapter : RecyclerView.Adapter<TagAdapter.TagHolder>() {
        private val textColor by lazy {
            DynamicTheme.getInstance().get().tintSurfaceColor
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagHolder {
            return TagHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.tag_holder_layout,
                    parent,
                    false
                )
            )
        }


        override fun getItemCount(): Int {
            return tagChooserField.tags.size
        }

        override fun onBindViewHolder(holder: TagHolder, position: Int) {
            val item = tagChooserField.tags[position]
            holder.bind(item)
        }


        override fun onViewRecycled(holder: TagHolder) {
            holder.unbind()
            super.onViewRecycled(holder)
        }

        fun deSelectAll() {
            tagChooserField.tags.forEach {
                it.isTagged = false
            }
            notifyDataSetChanged()
        }

        inner class TagHolder(v: View) : RecyclerView.ViewHolder(v) {
            fun bind(item: TagField) {
                itemView.apply {
                    tagCheckBox.setTextColor(textColor)
                    tagCheckBox.text = item.tag
                    tagCheckBox.isChecked = item.isTagged
                    tagCheckBox.setOnCheckedChangeListener { _, isChecked ->
                        item.isTagged = isChecked
                    }
                }
            }

            fun unbind() {
                itemView.apply {
                    tagCheckBox.setOnCheckedChangeListener(null)
                }
            }
        }
    }

    interface OnDoneListener {
        fun onDone(fragmentTag: String?, list: List<TagField>)
    }
}