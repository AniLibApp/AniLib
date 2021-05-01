package com.revolgenx.anilib.ui.view.sort

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.pranavpandey.android.dynamic.support.widget.DynamicImageView
import com.pranavpandey.android.dynamic.support.widget.DynamicTextView
import com.pranavpandey.android.dynamic.theme.Theme
import com.revolgenx.anilib.R
import com.revolgenx.anilib.ui.dialog.sorting.AniLibSortingDialog
import com.revolgenx.anilib.ui.dialog.sorting.AniLibSortingModel
import com.revolgenx.anilib.ui.dialog.sorting.SortOrder
import com.revolgenx.anilib.util.dp

class AlSortLayout:LinearLayout {

    private var sortItems:List<AniLibSortingModel>? =  null
    private val sortTitleTv:DynamicTextView
    private val sortOrderIv:DynamicImageView

    var onSortItemSelected: ((item:AniLibSortingModel?)->Unit)? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, def: Int) : super(
        context,
        attributeSet,
        def
    ) {
        orientation = HORIZONTAL

        sortTitleTv = DynamicTextView(context, attributeSet, def).also {
            it.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT).also {params->
                params.gravity = Gravity.CENTER_VERTICAL
            }
            it.textSize = 13f
            it.gravity = Gravity.CENTER_VERTICAL
        }

        sortOrderIv = DynamicImageView(context, attributeSet, def).also {
            it.layoutParams = LayoutParams(dp(24f), dp(24f)).also { params->
                params.gravity = Gravity.CENTER_VERTICAL
                params.marginEnd = dp(6f)
                params.marginStart = dp(6f)
            }
            it.colorType = Theme.ColorType.TEXT_PRIMARY

            it.setPadding(3)
        }

        addView(sortTitleTv)
        addView(sortOrderIv)

        setOnClickListener {
            openSortingDialog(context)
        }
    }

    private fun openSortingDialog(context: Context){
        if(sortItems == null) return
        AniLibSortingDialog(sortItems!!).show(context){
            onButtonClickedListener = onButtonClickedListener@{ _, which ->
                if(sortItems == null || getContext() == null) return@onButtonClickedListener
                if(which == AlertDialog.BUTTON_POSITIVE){
                    val activeItem = getActiveSortItem()
                    updateActiveSortItem(activeItem)
                    onSortItemSelected?.invoke(activeItem)
                }
            }
        }
    }

    fun setSortItems(sortItems:List<AniLibSortingModel>){
        this.sortItems = sortItems
        updateActiveSortItem(getActiveSortItem())
    }

    fun setActiveSortItem(sortItem:AniLibSortingModel){
        if(sortItems == null) return
        sortItems!!.firstOrNull{ it.data == sortItem.data }?.let {
            it.order = sortItem.order
            updateActiveSortItem(sortItem)
        }
    }

    fun getActiveSortItem() = sortItems?.firstOrNull { it.order != SortOrder.NONE }

    private fun updateActiveSortItem(activeSortItem:AniLibSortingModel?){
        val sortTitle:String
        val sortIcon:Drawable?
        if(activeSortItem == null){
            sortTitle = context.getString(R.string.none)
            sortIcon = null
        }else{
            sortTitle = activeSortItem.title
            val icon = when (activeSortItem.order) {
                SortOrder.ASC -> {
                    R.drawable.ic_asc
                }
                SortOrder.DESC->{
                    R.drawable.ic_desc
                }
                else->{
                    R.drawable.ic_asc
                }
            }

            sortIcon = ContextCompat.getDrawable(context, icon)
        }

        sortTitleTv.text = sortTitle
        sortOrderIv.setImageDrawable(sortIcon)
    }
}