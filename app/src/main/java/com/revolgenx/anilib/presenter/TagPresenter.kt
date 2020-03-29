package com.revolgenx.anilib.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.revolgenx.anilib.R
import kotlinx.android.synthetic.main.tag_presenter_layout.view.*

class TagPresenter(context: Context) : Presenter<String>(context) {
    override val elementTypes: Collection<Int>
        get() = listOf(0)

    var elements: MutableList<String> = mutableListOf()

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return Holder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.tag_presenter_layout,
                parent,
                false
            )
        )
    }

    override fun onBind(page: Page, holder: Holder, element: Element<String>) {
        super.onBind(page, holder, element)
        val data = element.data ?: return
        if (page.isFirstPage()) {
            elements.addAll(page.elements().map { it.data as String })
        }

        holder.itemView.apply {
            tagName.text = data
            setOnClickListener {
                page.removeElement(element)
                elements.remove(data)
            }
        }
    }
}