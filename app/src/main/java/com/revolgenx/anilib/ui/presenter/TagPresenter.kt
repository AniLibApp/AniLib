package com.revolgenx.anilib.ui.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.data.field.TagField
import com.revolgenx.anilib.data.field.TagState
import com.revolgenx.anilib.databinding.TagPresenterLayoutBinding

class TagPresenter(context: Context) : BasePresenter<TagPresenterLayoutBinding, TagField>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    private var tagRemovedListener: ((elements: String) -> Unit)? = null

    private val normalTextColor = DynamicTheme.getInstance().get().tintSurfaceColor
    private val redTextColor = ContextCompat.getColor(context, R.color.red)

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        elementType: Int
    ): TagPresenterLayoutBinding {
        return TagPresenterLayoutBinding.inflate(inflater, parent, false)
    }

    override fun onBind(page: Page, holder: Holder, element: Element<TagField>) {
        super.onBind(page, holder, element)
        val data = element.data ?: return

        holder.getBinding()?.apply {
            val textColor = when(data.tagState){
                TagState.UNTAGGED ->{
                    redTextColor
                }
                else -> {
                    normalTextColor
                }
            }

            tagName.setTextColor(textColor)

            tagName.text = data.tag
            root.setOnClickListener {
                page.removeElement(element)
                tagRemovedListener?.invoke(data.tag)
            }
        }
    }

    fun tagRemoved(listener: (element: String) -> Unit) {
        tagRemovedListener = listener
    }

    fun removeListener(){
        tagRemovedListener = null
    }
}