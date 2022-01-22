package com.revolgenx.anilib.list.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.bottomsheet.BottomSheetFragment
import com.revolgenx.anilib.databinding.SelectorBottomSheetBinding
import com.revolgenx.anilib.databinding.SelectorItemLayoutBinding

class MediaListDisplaySelectorBottomSheet : BottomSheetFragment<SelectorBottomSheetBinding>() {

    private var callback: ((selected: Int) -> Unit)? = null

    companion object {
        private const val selector_arg_key = "selector_arg_key"
        fun newInstance(selectedIndex: Int, callback:(selected: Int) -> Unit) = MediaListDisplaySelectorBottomSheet().also {
            it.arguments = bundleOf(selector_arg_key to selectedIndex)
            it.callback = callback
        }
    }

    private val selectedIndex get() = arguments?.getInt(selector_arg_key)
    override fun bindView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): SelectorBottomSheetBinding {
        return SelectorBottomSheetBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val selectedIndex = selectedIndex ?: return

        val displayModes = requireContext().resources.getStringArray(R.array.list_display_modes)
            .mapIndexed { index, s -> s to index }

        Adapter.builder(viewLifecycleOwner)
            .addPresenter(
                Presenter.simple<Pair<String, Int>>(
                    requireContext(),
                    R.layout.selector_item_layout,
                    0
                ) { v, item ->
                    SelectorItemLayoutBinding.bind(v).apply {
                        selectorItemCb.text = item.first
                        selectorItemCb.isChecked = item.second == selectedIndex
                        selectorItemCb.setOnCheckedChangeListener { _, _ ->
                            callback?.invoke(item.second)
                            dismiss()
                        }
                    }
                })
            .addSource(Source.fromList(displayModes))
            .into(binding.selectorRecyclerView)

    }
}