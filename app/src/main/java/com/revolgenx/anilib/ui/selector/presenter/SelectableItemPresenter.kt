package com.revolgenx.anilib.ui.selector.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.contrastAccentWithBg
import com.revolgenx.anilib.app.theme.dynamicAccentColor
import com.revolgenx.anilib.app.theme.dynamicTextColorPrimary
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.data.tuples.MutablePair
import com.revolgenx.anilib.databinding.SelectableItemPresenterBinding
import com.revolgenx.anilib.ui.selector.constant.SelectedState

class SelectableItemPresenter(context: Context, private val hasIntermediateMode: Boolean = false) :
    BasePresenter<SelectableItemPresenterBinding, MutablePair<String, SelectedState>>(context) {
    override val elementTypes: Collection<Int> = listOf(0)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): SelectableItemPresenterBinding {
        return SelectableItemPresenterBinding.inflate(inflater, parent, false)
    }

    override fun onBind(
        page: Page,
        holder: Holder,
        element: Element<MutablePair<String, SelectedState>>
    ) {
        super.onBind(page, holder, element)
        val item = element.data ?: return
        val binding = holder.getBinding() ?: return
        binding.apply {
            selectorTv.text = item.first
            setStateViews(item.second)
            root.setOnClickListener {
                updateState(item)
                setStateViews(item.second)
            }
        }
    }

    private fun updateState(item: MutablePair<String, SelectedState>) {
        item.second = when (item.second) {
            SelectedState.SELECTED -> if (hasIntermediateMode) SelectedState.INTERMEDIATE else SelectedState.NONE
            SelectedState.INTERMEDIATE -> SelectedState.NONE
            SelectedState.NONE -> SelectedState.SELECTED
        }
    }

    private fun SelectableItemPresenterBinding.setStateViews(state: SelectedState) {
        var color: Int = dynamicTextColorPrimary
        val drawable = when (state) {
            SelectedState.SELECTED -> {
                color = contrastAccentWithBg
                selectorIv.color = dynamicAccentColor
                R.drawable.ic_check_circle
            }
            SelectedState.INTERMEDIATE -> {
                color = ContextCompat.getColor(context, R.color.red)
                selectorIv.color = color
                R.drawable.ic_intermediate_circle
            }
            SelectedState.NONE -> {
                selectorIv.color = dynamicAccentColor
                R.drawable.ic_uncheck_circle
            }
        }
        selectorTv.color = color
        selectorIv.setImageResource(drawable)
    }

}