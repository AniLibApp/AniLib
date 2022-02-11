package com.revolgenx.anilib.common.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.model.SelectableSpinnerMenu

class SelectableSpinnerAdapter(context: Context, private val items: List<SelectableSpinnerMenu>) :
    ArrayAdapter<SelectableSpinnerMenu>(
        context,
        R.layout.selectable_adapter_item_layout,
        R.id.spinner_selector_tv,
        items
    ) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getView(position, convertView, parent).also {
            it.findViewById<View>(R.id.spinner_selector_checkbox).visibility = View.GONE
        }
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getDropDownView(position, convertView, parent).also {
            val item = getItem(position) ?: return@also
            it.findViewById<View>(R.id.spinner_selector_checkbox).visibility =
                if (item.isSelected) View.VISIBLE else View.GONE
        }
    }
}

