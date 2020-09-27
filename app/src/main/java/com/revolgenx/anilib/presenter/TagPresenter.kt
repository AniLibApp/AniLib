package com.revolgenx.anilib.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.pranavpandey.android.dynamic.utils.DynamicColorUtils
import com.revolgenx.anilib.R
import com.revolgenx.anilib.field.TagField
import com.revolgenx.anilib.field.TagState
import kotlinx.android.synthetic.main.tag_presenter_layout.view.*

class TagPresenter(context: Context) : Presenter<TagField>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    private lateinit var elements: MutableList<TagField>

    private var tagRemovedListener: ((elements: String) -> Unit)? = null

    private val normalTextColor = DynamicTheme.getInstance().get().tintSurfaceColor
    private val redTextColor = ContextCompat.getColor(context, R.color.red)

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.tag_presenter_layout,
                parent,
                false
            )
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<TagField>) {
        super.onBind(page, holder, element)
        val data = element.data ?: return
        if (page.isFirstPage()) {
            elements = page.elements().map { it.data as TagField}.toMutableList()
        }

        holder.itemView.apply {
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
            setOnClickListener {
                page.removeElement(element)
                elements.remove(data)
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