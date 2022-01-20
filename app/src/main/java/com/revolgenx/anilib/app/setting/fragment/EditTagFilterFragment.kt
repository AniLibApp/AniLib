package com.revolgenx.anilib.app.setting.fragment

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary
import com.revolgenx.anilib.common.ui.fragment.BaseToolbarFragment
import com.revolgenx.anilib.app.setting.data.field.EditTagField
import com.revolgenx.anilib.app.setting.data.meta.TagFilterMetaType
import com.revolgenx.anilib.app.setting.data.meta.TagFilterSettingMeta
import com.revolgenx.anilib.databinding.EditTagFilterFragmentLayoutBinding
import com.revolgenx.anilib.databinding.EditTagHolderLayoutBinding
import com.revolgenx.anilib.ui.dialog.InputDialog
import com.revolgenx.anilib.ui.view.makeConfirmationDialog
import com.revolgenx.anilib.app.setting.data.model.EditTagFilterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditTagFilterFragment : BaseToolbarFragment<EditTagFilterFragmentLayoutBinding>() {

    override var setHomeAsUp: Boolean = true
    override val menuRes: Int = R.menu.add_remove_tag_filter_menu
    override val titleRes: Int
        get() = when (viewModel.tagFilterType) {
            TagFilterMetaType.GENRE -> R.string.edit_genre
            TagFilterMetaType.TAG -> R.string.edit_tags
            TagFilterMetaType.STREAMING_ON -> R.string.edit_streaming_on
        }

    private val viewModel by viewModel<EditTagFilterViewModel>()
    private lateinit var adapter: TagAdapter


    companion object {
        private const val TAG_SETTING_FILTER_META_KEY = "TAG_SETTING_FILTER_META_KEY"

        fun newInstance(tagFilterMeta: TagFilterSettingMeta): EditTagFilterFragment {
            return EditTagFilterFragment().also {
                it.arguments = bundleOf(TAG_SETTING_FILTER_META_KEY to tagFilterMeta)
            }
        }
    }

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): EditTagFilterFragmentLayoutBinding {
        return EditTagFilterFragmentLayoutBinding.inflate(inflater, parent, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tagFilterMeta =
            arguments?.getParcelable<TagFilterSettingMeta>(TAG_SETTING_FILTER_META_KEY)
                ?: return super.onViewCreated(view, savedInstanceState)

        viewModel.tagFilterType = tagFilterMeta.type

        if (savedInstanceState == null) {
            viewModel.updateField(requireContext())
        }


        adapter = TagAdapter()
        binding.addRemoveTagRecyclerView.adapter = adapter

    }

    override fun onToolbarInflated() {
        val baseToolbar = getBaseToolbar()
        val menu = baseToolbar.menu

        val searchView = (menu.findItem(R.id.tag_search).actionView as? SearchView)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.search(newText)
                adapter.notifyDataSetChanged()
                return true
            }
        })

        menu.findItem(R.id.tag_add).setOnMenuItemClickListener {
            InputDialog.newInstance(null, InputType.TYPE_CLASS_TEXT, null).let { input ->
                input.onInputDoneListener = { newTag ->
                    viewModel.tagFields.add(EditTagField(false, false, newTag))
                    viewModel.saveTagFields(requireContext())
                    reloadAdapter()
                }
                input.show(childFragmentManager)
            }
            true
        }

        menu.findItem(R.id.tag_delete).setOnMenuItemClickListener {
            makeConfirmationDialog(requireContext()) {
                viewModel.removeTagFields()
                viewModel.saveTagFields(requireContext())
                reloadAdapter()
            }
            true
        }

        menu.findItem(R.id.tag_reload_all).setOnMenuItemClickListener {
            makeConfirmationDialog(requireContext(), R.string.reset_to_default) {
                viewModel.reloadTagFields(requireContext())
                viewModel.saveTagFields(requireContext())
                reloadAdapter()
            }
            true
        }

        menu.findItem(R.id.tag_unselect_all).setOnMenuItemClickListener {
            adapter.deSelectAll()
            true
        }

        menu.findItem(R.id.tag_exclude).setOnMenuItemClickListener {
            viewModel.saveExcludedTags(requireContext())
            reloadAdapter()
            true
        }
        menu.findItem(R.id.tag_not_exclude).setOnMenuItemClickListener {
            viewModel.saveNotExcludedTags(requireContext())
            reloadAdapter()
            true
        }

    }

    private fun reloadAdapter() {
        viewModel.search("")
        adapter.notifyDataSetChanged()
    }


    inner class TagAdapter :
        RecyclerView.Adapter<TagAdapter.TagHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagHolder {
            return TagHolder(
                EditTagHolderLayoutBinding.inflate(LayoutInflater.from(context), parent, false)
            )
        }

        override fun getItemCount(): Int {
            return viewModel.filteredTagFields.size
        }

        override fun onBindViewHolder(holder: TagHolder, position: Int) {
            val item = viewModel.filteredTagFields[position]
            holder.bind(item)
        }

        fun deSelectAll() {
            viewModel.tagFields.forEach { it.isSelected = false }
            notifyDataSetChanged()
        }

        inner class TagHolder(val v: EditTagHolderLayoutBinding) :
            RecyclerView.ViewHolder(v.root) {
            fun bind(item: EditTagField) {
                v.apply {
                    this.tagChooserCheckBox.setOnCheckedChangeListener { _, isChecked ->
                        item.isSelected = isChecked
                    }
                    tagChooserCheckBox.setTextColor(dynamicTextColorPrimary)
                    tagChooserCheckBox.text = item.tag
                    tagChooserCheckBox.isChecked = item.isSelected
                    excludedTagIc.visibility = if(item.isExcluded) View.VISIBLE else View.GONE
                }
            }

        }
    }
}