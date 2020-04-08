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
import kotlinx.android.synthetic.main.browse_filter_navigation_view.*
import kotlinx.android.synthetic.main.tag_chooser_dialog_fragment_layout.*
import kotlinx.android.synthetic.main.tag_chooser_dialog_fragment_layout.tagRecyclerView
import kotlinx.android.synthetic.main.tag_holder_layout.*
import kotlinx.android.synthetic.main.tag_holder_layout.view.*

class TagChooserDialogFragment : DynamicDialogFragment() {
    companion   object {
        const val TAG_KEY = "tag_key"
        fun newInstance(tags: TagChooserField) = TagChooserDialogFragment().apply {
            arguments = bundleOf(TAG_KEY to tags)
        }
    }

    private var doneListener: OnDoneListener? = null
    private var tagChooserField: TagChooserField? = null
    private val tagAdapter by lazy {
        TagAdapter()
    }

    override fun onCustomiseBuilder(
        dialogBuilder: DynamicDialog.Builder,
        savedInstanceState: Bundle?
    ): DynamicDialog.Builder {
        tagChooserField =
            arguments?.getParcelable(TAG_KEY) ?: return super.onCustomiseBuilder(
                dialogBuilder,
                savedInstanceState
            )
        with(dialogBuilder) {
            setTitle(tagChooserField!!.header)
            setView(R.layout.tag_chooser_dialog_fragment_layout)
            isAutoDismiss = false
        }

        return super.onCustomiseBuilder(dialogBuilder, savedInstanceState)
    }

    override fun onCustomiseDialog(
        alertDialog: DynamicDialog,
        savedInstanceState: Bundle?
    ): DynamicDialog {

        if (tagChooserField != null)
            alertDialog.apply {
                setOnShowListener {
                    doneTv.setOnClickListener {
                        doneListener?.onDone(
                            tag,
                            tagAdapter.items
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
                    tagAdapter.submitList(tagChooserField!!.tags)
                }
            }

        return super.onCustomiseDialog(alertDialog, savedInstanceState)
    }


    fun onDoneListener(listener: OnDoneListener) {
        doneListener = listener
    }

    internal class TagAdapter : RecyclerView.Adapter<TagAdapter.TagHolder>() {
        private val textColor by lazy {
            DynamicTheme.getInstance().get().tintSurfaceColor
        }

        val items: MutableList<TagField> = mutableListOf()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagHolder {
            return TagHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.tag_holder_layout,
                    parent,
                    false
                )
            )
        }

        fun submitList(list: List<TagField>) {
            items.clear()
            items.addAll(list.map { TagField(it.tag, it.isTagged) })
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: TagHolder, position: Int) {
            val item = items[position]
            holder.bind(item)
        }


        override fun onViewRecycled(holder: TagHolder) {
            holder.unbind()
            super.onViewRecycled(holder)
        }

        fun deSelectAll() {
            items.forEach {
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
                    tagCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
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