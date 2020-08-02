package com.revolgenx.anilib.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.dialog.fragment.DynamicDialogFragment
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.BrowseActivity
import com.revolgenx.anilib.event.TagEvent
import com.revolgenx.anilib.event.TagOperationType
import com.revolgenx.anilib.field.TagChooserField
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.util.TagPrefUtil
import kotlinx.android.synthetic.main.tag_chooser_dialog_fragment_layout.*
import kotlinx.android.synthetic.main.tag_chooser_dialog_header_layout.*
import kotlinx.android.synthetic.main.tag_chooser_dialog_header_layout.view.*
import kotlinx.android.synthetic.main.tag_holder_layout.view.*

class TagChooserDialogFragment : DynamicDialogFragment() {
    companion object {
        const val TAG_KEY = "tag_key"
        const val CLICKED_STATE_KEY = "clicked_state_key"
        fun newInstance(tags: TagChooserField) = TagChooserDialogFragment().apply {
            arguments = bundleOf(TAG_KEY to tags)
        }
    }


    private var clickedState = -1
    private var tagChooserDialogCallback: TagChooserDialogCallback? = null
    private val tagChooserField: TagChooserField by lazy {
        arguments?.getParcelable<TagChooserField>(
            TAG_KEY
        )!!.also { field ->
            field.tags = field.tags.map { TagField(it.tag, it.isTagged) }
        }
    }
    private lateinit var tagAdapter: TagAdapter

    override fun onCustomiseBuilder(
        dialogBuilder: DynamicDialog.Builder,
        savedInstanceState: Bundle?
    ): DynamicDialog.Builder {
        with(dialogBuilder) {
            arguments?.containsKey(TAG_KEY) ?: return super.onCustomiseBuilder(
                dialogBuilder,
                savedInstanceState
            )
            if (savedInstanceState != null) {
                clickedState = savedInstanceState.getInt(CLICKED_STATE_KEY)
            }
            val v = LayoutInflater.from(context).inflate(
                R.layout.tag_chooser_dialog_header_layout,
                null
            )
            v.tagTitleTv.text = tagChooserField.header
            setCustomTitle(v)
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
                    alertDialog.window.let { window ->
                        window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
                    }
                    customizeHeader(this)
                    doneTv.setOnClickListener {
                        tagChooserDialogCallback?.onTagChooserDone(
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
                    tagAdapter = TagAdapter()
                    tagRecyclerView.layoutManager = LinearLayoutManager(context)
                    tagRecyclerView.adapter = tagAdapter
                }
            }

        return super.onCustomiseDialog(alertDialog, savedInstanceState)
    }

    private fun customizeHeader(alertDialog: DynamicDialog) {
        alertDialog.apply {
            when (clickedState) {
                1 -> addTagFromLayout.visibility = View.VISIBLE
                2, 3 -> areYouSureLayout.visibility = View.VISIBLE
            }
            setUpHeaderVisibility(this)
            this.initHeaderListener()
        }
    }

    private fun DynamicDialog.initHeaderListener() {
        addTagFromEtIv.setOnClickListener {
            tagAddEt.text.toString().takeIf { it.isNotEmpty() }?.let { newTag ->
                tagChooserField.tags = tagChooserField.tags.toMutableList().also {
                    it.add(TagField(newTag, false))
                }
                when (tag) {
                    BrowseActivity.TAG_CHOOSER_DIALOG_TAG -> {
                        TagPrefUtil.saveTagPref(context, tagChooserField.tags.map { it.tag })
                        TagPrefUtil.invalidateAll(context)
                        TagEvent(TagOperationType.ADD_TAG, tag, tagChooserField.tags).postEvent
                    }
                    BrowseActivity.GENRE_CHOOSER_DIALOG_TAG -> {
                        TagPrefUtil.saveGenrePref(context, tagChooserField.tags.map { it.tag })
                        TagPrefUtil.invalidateAll(context)
                        TagEvent(TagOperationType.ADD_GENRE, tag, tagChooserField.tags).postEvent
                    }
                    BrowseActivity.STREAM_CHOOSER_DIALOG_TAG -> {
                        TagPrefUtil.saveStreamingOnPref(
                            context,
                            tagChooserField.tags.map { it.tag })
                        TagEvent(TagOperationType.ADD_STREAM, tag, tagChooserField.tags).postEvent
                    }
                }
                tagAddEt.setText("")
                tagAdapter.notifyDataSetChanged()
            }
        }

        reloadTagValueTv.setOnClickListener {
            when (clickedState) {
                2 -> {
                    tagChooserField.tags.filter { !it.isTagged }.let { newTags ->
                        val remainingTags = tagChooserField.tags.minus(newTags)
                        tagChooserField.tags = newTags

                        when (tag) {
                            BrowseActivity.TAG_CHOOSER_DIALOG_TAG -> {
                                TagPrefUtil.saveTagPref(context, newTags.map { it.tag })
                                TagPrefUtil.invalidateAll(context)
                                TagEvent(TagOperationType.DELETE_TAG, tag, remainingTags).postEvent
                            }
                            BrowseActivity.GENRE_CHOOSER_DIALOG_TAG -> {
                                TagPrefUtil.saveGenrePref(context, newTags.map { it.tag })
                                TagPrefUtil.invalidateAll(context)
                                TagEvent(TagOperationType.DELETE_GENRE, tag, remainingTags).postEvent
                            }
                            BrowseActivity.STREAM_CHOOSER_DIALOG_TAG -> {
                                TagPrefUtil.saveStreamingOnPref(context, newTags.map { it.tag })
                                TagPrefUtil.invalidateAll(context)
                                TagEvent(TagOperationType.DELETE_STREAM, tag, remainingTags).postEvent
                            }
                        }
                        tagAdapter.notifyDataSetChanged()
                    }
                    areYouSureLayout.visibility = View.GONE
                }
                3 -> {
                    when (tag) {
                        BrowseActivity.TAG_CHOOSER_DIALOG_TAG -> {
                            tagChooserField.tags = TagPrefUtil.reloadTagPref(context)
                            TagEvent(TagOperationType.ADD_TAG, tag, tagChooserField.tags).postEvent
                        }
                        BrowseActivity.GENRE_CHOOSER_DIALOG_TAG -> {
                            tagChooserField.tags = TagPrefUtil.reloadGenrePref(context)
                            TagEvent(
                                TagOperationType.ADD_GENRE,
                                tag,
                                tagChooserField.tags
                            ).postEvent
                        }
                        BrowseActivity.STREAM_CHOOSER_DIALOG_TAG -> {
                            tagChooserField.tags = TagPrefUtil.reloadStreamingPref(context)
                            TagEvent(
                                TagOperationType.ADD_STREAM,
                                tag,
                                tagChooserField.tags
                            ).postEvent
                        }
                    }
                    tagAdapter.notifyDataSetChanged()

                    TagPrefUtil.invalidateAll(context)
                    areYouSureLayout.visibility = View.GONE
                }
            }
        }
    }

    private fun setUpHeaderVisibility(dynamicDialog: DynamicDialog) {
        dynamicDialog.apply {
            customTagAddIv.setOnClickListener {
                areYouSureLayout.visibility = View.GONE
                addTagFromLayout.visibility =
                    if (addTagFromLayout.visibility == View.VISIBLE) {
                        clickedState = -1
                        View.GONE
                    } else {
                        clickedState = 1
                        View.VISIBLE
                    }
            }
            deleteTagIv.setOnClickListener {
                addTagFromLayout.visibility = View.GONE
                if (clickedState == 2) {
                    clickedState = -1
                    areYouSureLayout.visibility = View.GONE
                } else {
                    clickedState = 2
                    areYouSureLayout.visibility = View.VISIBLE
                }
            }

            refreshTagIv.setOnClickListener {
                addTagFromLayout.visibility = View.GONE
                if (clickedState == 3) {
                    clickedState = -1
                    areYouSureLayout.visibility = View.GONE
                } else {
                    clickedState = 3
                    areYouSureLayout.visibility = View.VISIBLE
                }
            }
        }

    }


    fun onDoneListener(listener: TagChooserDialogCallback) {
        tagChooserDialogCallback = listener
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CLICKED_STATE_KEY, clickedState)
        super.onSaveInstanceState(outState)
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

    interface TagChooserDialogCallback {
        fun onTagChooserDone(fragmentTag: String?, list: List<TagField>)
    }
}