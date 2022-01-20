package com.revolgenx.anilib.list.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.revolgenx.anilib.common.ui.bottomsheet.BottomSheetFragment
import com.revolgenx.anilib.databinding.MediaListGroupSelectorBottomSheetBinding
import com.revolgenx.anilib.list.adapter.MediaListGroupSelectorAdapter

class MediaListGroupSelectorBottomSheet : BottomSheetFragment<MediaListGroupSelectorBottomSheetBinding>() {
    companion object {
        private const val media_list_group_selector_arg_key = "media_list_group_selector_arg_key"
        fun newInstance(groupNameList: List<Pair<Pair<String, Int>, Boolean>>) =
            MediaListGroupSelectorBottomSheet().also {
                it.arguments = bundleOf(media_list_group_selector_arg_key to groupNameList)
            }
    }

    private val groupNameList get() = arguments?.get(media_list_group_selector_arg_key) as? List<Pair<Pair<String, Int>, Boolean>>
    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): MediaListGroupSelectorBottomSheetBinding {
        return MediaListGroupSelectorBottomSheetBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val groupNameSelectorList = groupNameList ?: return
        binding.mediaListGroupRecyclerView.adapter = MediaListGroupSelectorAdapter() {
            dismiss()
        }.also {
            it.groupSelectorList = groupNameSelectorList
        }
    }
}