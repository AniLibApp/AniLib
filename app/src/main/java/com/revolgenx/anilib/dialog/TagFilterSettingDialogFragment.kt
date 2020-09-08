package com.revolgenx.anilib.dialog

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.meta.TagFilterMetaType
import com.revolgenx.anilib.meta.TagFilterSettingMeta
import com.revolgenx.anilib.util.TagPrefUtil
import com.revolgenx.anilib.viewmodel.setting.TagEditMode
import com.revolgenx.anilib.viewmodel.setting.TagFilterSettingDialogViewModel
import kotlinx.android.synthetic.main.tag_chooser_dialog_header_layout.view.*
import kotlinx.android.synthetic.main.tag_filter_setting_dialog_fragment_layout.*
import kotlinx.android.synthetic.main.tag_holder_layout.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TagFilterSettingDialogFragment : BaseDialogFragment() {
    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel
    override var viewRes: Int? = R.layout.tag_filter_setting_dialog_fragment_layout


    companion object {
        const val TAG_SETTING_FILTER_META_KEY = "TAG_SETTING_FILTER_META_KEY"

        fun newInstance(tagFilterMeta: TagFilterSettingMeta): TagFilterSettingDialogFragment {
            return TagFilterSettingDialogFragment().also {
                it.arguments = bundleOf(TAG_SETTING_FILTER_META_KEY to tagFilterMeta)
            }
        }
    }

    private val viewModel by viewModel<TagFilterSettingDialogViewModel>()
    private lateinit var adapter: TagAdapter

    override fun onCustomiseBuilder(
        dialogBuilder: DynamicDialog.Builder,
        savedInstanceState: Bundle?
    ): DynamicDialog.Builder {
        with(dialogBuilder) {
            val tagFilterMeta =
                arguments?.getParcelable<TagFilterSettingMeta>(TAG_SETTING_FILTER_META_KEY)
                    ?: return super.onCustomiseBuilder(dialogBuilder, savedInstanceState)

            viewModel.tagFilterType = tagFilterMeta.type
            dialogBuilder.setNeutralButton(R.string.unselect_all, null)
            if (savedInstanceState == null) {
                viewModel.updateField(context)
            }
            val v = LayoutInflater.from(context).inflate(
                R.layout.tag_chooser_dialog_header_layout,
                null
            )

            v.setHeaderTitle()
            v.setUpHeaderListener()
            setCustomTitle(v)
            isAutoDismiss = false
        }
        return super.onCustomiseBuilder(dialogBuilder, savedInstanceState)
    }


    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        super.onShowListener(alertDialog, savedInstanceState)
        with(alertDialog) {
            this.window.let { window ->
                window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }

            getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
                adapter.deSelectAll()
            }

            adapter = TagAdapter()
            tagFilterSettingRecyclerView.adapter = adapter
        }
    }

    override fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
        viewModel.saveTagFields(requireContext())
    }

    override fun onNeutralClicked(dialogInterface: DialogInterface, which: Int) {
        adapter.deSelectAll()
    }


    private fun View.setHeaderTitle() {
        tagTitleTv.text = when (viewModel.tagFilterType) {
            TagFilterMetaType.GENRE -> getString(R.string.genre)
            TagFilterMetaType.TAG -> getString(R.string.tags)
            TagFilterMetaType.STREAMING_ON -> getString(R.string.streaming_on)
        }
    }


    private fun View.setUpHeaderListener() {
        when (viewModel.editMode) {
            TagEditMode.ADD -> addTagFromLayout.visibility = View.VISIBLE
            TagEditMode.DELETE, TagEditMode.RELOAD -> areYouSureLayout.visibility = View.VISIBLE
        }
        setUpHeaderVisibility()
        initHeaderListener()
    }

    private fun View.setUpHeaderVisibility() {
        this.customTagAddIv.setOnClickListener {
            areYouSureLayout.visibility = View.GONE
            addTagFromLayout.visibility =
                if (addTagFromLayout.visibility == View.VISIBLE) {
                    viewModel.editMode = TagEditMode.DEFAULT
                    View.GONE
                } else {
                    viewModel.editMode = TagEditMode.ADD
                    View.VISIBLE
                }
        }
        deleteTagIv.setOnClickListener {
            addTagFromLayout.visibility = View.GONE
            if (viewModel.editMode == TagEditMode.DELETE) {
                viewModel.editMode = TagEditMode.DEFAULT
                areYouSureLayout.visibility = View.GONE
            } else {
                viewModel.editMode = TagEditMode.DELETE
                areYouSureLayout.visibility = View.VISIBLE
            }
        }

        refreshTagIv.setOnClickListener {
            addTagFromLayout.visibility = View.GONE
            if (viewModel.editMode == TagEditMode.RELOAD) {
                viewModel.editMode = TagEditMode.DEFAULT
                areYouSureLayout.visibility = View.GONE
            } else {
                viewModel.editMode = TagEditMode.RELOAD
                areYouSureLayout.visibility = View.VISIBLE
            }
        }

    }

    private fun View.initHeaderListener() {
        addTagFromEtIv.setOnClickListener {
            tagAddEt.text.toString().takeIf { it.isNotEmpty() }?.let { newTag ->
                viewModel.tagFields.add(TagField(newTag, false))
                tagAddEt.setText("")
                adapter.notifyDataSetChanged()
            }
        }

        reloadTagValueTv.setOnClickListener {
            when (viewModel.editMode) {
                TagEditMode.DELETE -> {
                    viewModel.removeTagFields()
                    adapter.notifyDataSetChanged()
                    areYouSureLayout.visibility = View.GONE
                }
                TagEditMode.RELOAD -> {
                    viewModel.reloadTagFields(requireContext())
                    adapter.notifyDataSetChanged()
                    areYouSureLayout.visibility = View.GONE
                }
            }
            viewModel.editMode = TagEditMode.DEFAULT
        }
    }

    //region adapter
    inner class TagAdapter :
        RecyclerView.Adapter<TagAdapter.TagHolder>() {

        private val textColor = DynamicTheme.getInstance().get().tintSurfaceColor

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
            return viewModel.tagFields.size
        }

        override fun onBindViewHolder(holder: TagHolder, position: Int) {
            val item = viewModel.tagFields[position]
            holder.bind(item)
        }

        fun deSelectAll() {
            viewModel.tagFields.forEach { it.isTagged = false }
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

    //endregion
}
