package com.revolgenx.anilib.ui.dialog

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
import com.revolgenx.anilib.common.ui.dialog.BaseDialogFragment
import com.revolgenx.anilib.data.field.TagField
import com.revolgenx.anilib.data.field.TagState
import com.revolgenx.anilib.data.meta.TagFilterMetaType
import com.revolgenx.anilib.data.meta.TagFilterSettingMeta
import com.revolgenx.anilib.databinding.TagChooserDialogHeaderLayoutBinding
import com.revolgenx.anilib.databinding.TagFilterSettingDialogFragmentLayoutBinding
import com.revolgenx.anilib.databinding.TagFilterSettingHolderLayoutBinding
import com.revolgenx.anilib.ui.viewmodel.setting.TagEditMode
import com.revolgenx.anilib.ui.viewmodel.setting.TagFilterSettingDialogViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TagFilterSettingDialogFragment : BaseDialogFragment<TagFilterSettingDialogFragmentLayoutBinding>() {
    override var positiveText: Int? = R.string.done
    override var negativeText: Int? = R.string.cancel

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

    override fun bindView(): TagFilterSettingDialogFragmentLayoutBinding {
        return TagFilterSettingDialogFragmentLayoutBinding.inflate(provideLayoutInflater)
    }

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

            val tagChooserBinding = TagChooserDialogHeaderLayoutBinding.inflate(LayoutInflater.from(context), null, false)
            tagChooserBinding.setHeaderTitle()
            tagChooserBinding.setUpHeaderListener()
            setCustomTitle(tagChooserBinding.root)
            isAutoDismiss = false
        }
        return super.onCustomiseBuilder(dialogBuilder, savedInstanceState)
    }


    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        super.onShowListener(alertDialog, savedInstanceState)


        alertDialog.window.let { window ->
            window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }

        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener {
            adapter.deSelectAll()
        }

        adapter = TagAdapter()
        binding.tagFilterSettingRecyclerView.adapter = adapter
    }

    override fun onPositiveClicked(dialogInterface: DialogInterface, which: Int) {
        viewModel.saveTagFields(requireContext())
    }

    override fun onNeutralClicked(dialogInterface: DialogInterface, which: Int) {
        adapter.deSelectAll()
    }


    private fun TagChooserDialogHeaderLayoutBinding.setHeaderTitle() {
        tagTitleTv.text = when (viewModel.tagFilterType) {
            TagFilterMetaType.GENRE -> getString(R.string.genre)
            TagFilterMetaType.TAG -> getString(R.string.tags)
            TagFilterMetaType.STREAMING_ON -> getString(R.string.streaming_on)
        }
    }


    private fun TagChooserDialogHeaderLayoutBinding.setUpHeaderListener() {
        when (viewModel.editMode) {
            TagEditMode.ADD -> addTagFromLayout.visibility = View.VISIBLE
            TagEditMode.DELETE, TagEditMode.RELOAD -> areYouSureLayout.visibility = View.VISIBLE
            else -> {
            }
        }
        setUpHeaderVisibility()
        initHeaderListener()
    }

    private fun TagChooserDialogHeaderLayoutBinding.setUpHeaderVisibility() {
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

    private fun TagChooserDialogHeaderLayoutBinding.initHeaderListener() {
        addTagFromEtIv.setOnClickListener {
            tagAddEt.text.toString().takeIf { it.isNotEmpty() }?.let { newTag ->
                viewModel.tagFields.add(TagField(newTag, TagState.EMPTY))
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
                else -> {
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
                TagFilterSettingHolderLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
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
            viewModel.tagFields.forEach { it.tagState = TagState.EMPTY }
            notifyDataSetChanged()
        }

        inner class TagHolder(val v: TagFilterSettingHolderLayoutBinding) : RecyclerView.ViewHolder(v.root) {
            fun bind(item: TagField) {
                v.apply {
                    this.tagChooserCheckBox.setOnCheckedChangeListener { _, isChecked ->
                        item.tagState = if (isChecked) TagState.TAGGED else TagState.EMPTY
                    }
                    tagChooserCheckBox.setTextColor(textColor)
                    tagChooserCheckBox.text = item.tag
                    tagChooserCheckBox.isChecked = item.tagState == TagState.TAGGED
                }
            }

        }
    }

    //endregion
}
