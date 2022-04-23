package com.revolgenx.anilib.studio.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.bottomsheet.DynamicBottomSheetFragment
import com.revolgenx.anilib.databinding.StudioFilterBottomSheetBinding
import com.revolgenx.anilib.studio.data.constant.StudioSort
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter

class StudioFilterBottomSheet : DynamicBottomSheetFragment<StudioFilterBottomSheetBinding>() {

    var onPositionClicked: ((onList: Boolean, sort: Int) -> Unit)? = null

    companion object {
        private const val STUDIO_ON_LIST_KEY = "STUDIO_ON_LIST_KEY"
        private const val STUDIO_SORT_KEY = "STUDIO_SORT_KEY"
        fun newInstance(onList: Boolean, sort: Int) = StudioFilterBottomSheet().also {
            it.arguments = bundleOf(STUDIO_ON_LIST_KEY to onList, STUDIO_SORT_KEY to sort)
        }
    }

    private val onList get() = arguments?.getBoolean(STUDIO_ON_LIST_KEY)
    private val sort get() = arguments?.getInt(STUDIO_SORT_KEY)

    override fun bindView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): StudioFilterBottomSheetBinding {
        return StudioFilterBottomSheetBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.initListener()
        binding.bindView()
    }

    private fun StudioFilterBottomSheetBinding.bindView() {
        val statusItems = requireContext().resources.getStringArray(R.array.studio_sort).map {
            DynamicMenu(null, it)
        }

        val statusSpinner = studioFilterSortSpinnerLayout.spinnerView
        statusSpinner.adapter = makeSpinnerAdapter(requireContext(), statusItems)
        statusSpinner.setSelection(sort ?: StudioSort.START_DATE_DESC.ordinal)
        studioOnListCheckbox.isChecked = onList ?: false
    }


    private fun StudioFilterBottomSheetBinding.initListener() {
        onPositiveClicked = {
            onPositionClicked?.invoke(studioOnListCheckbox.isChecked, studioFilterSortSpinnerLayout.spinnerView.selectedItemPosition)
        }
    }


}