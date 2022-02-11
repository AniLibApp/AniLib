package com.revolgenx.anilib.search.presenter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.theme.dynamicAccentColor
import com.revolgenx.anilib.app.theme.dynamicBackgroundColor
import com.revolgenx.anilib.app.theme.dynamicTintBackgroundColor
import com.revolgenx.anilib.common.presenter.BasePresenter
import com.revolgenx.anilib.common.data.field.TagField
import com.revolgenx.anilib.common.data.meta.TagState
import com.revolgenx.anilib.databinding.SearchTagPresenterLayoutBinding

class SearchTagPresenter(context: Context) :
    BasePresenter<SearchTagPresenterLayoutBinding, TagField>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    private var tagRemovedListener: ((elements: String) -> Unit)? = null

    private val redTextColor = DynamicColorUtils.getContrastColor(
        ContextCompat.getColor(context, R.color.red),
        dynamicBackgroundColor
    )

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): SearchTagPresenterLayoutBinding {
        return SearchTagPresenterLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<TagField>) {
        super.onBind(page, holder, element)
        val data = element.data ?: return

        holder.getBinding()?.apply {
            val textColor = when (data.tagState) {
                TagState.UNTAGGED -> {
                    redTextColor
                }
                else -> {
                    dynamicTintBackgroundColor
                }
            }
            ColorStateList.valueOf(textColor).also {
                tagChip.chipStrokeColor = it
                tagChip.closeIconTint = it
            }
            tagChip.chipStrokeWidth = 2f
            tagChip.isCloseIconVisible = true
            tagChip.chipBackgroundColor = ColorStateList.valueOf(dynamicBackgroundColor)
            tagChip.setTextColor(textColor)
            tagChip.text = data.tag
            root.setOnClickListener {
                page.removeElement(element)
                tagRemovedListener?.invoke(data.tag)
            }
        }
    }

    fun tagRemoved(listener: (element: String) -> Unit) {
        tagRemovedListener = listener
    }

    fun removeListener() {
        tagRemovedListener = null
    }
}