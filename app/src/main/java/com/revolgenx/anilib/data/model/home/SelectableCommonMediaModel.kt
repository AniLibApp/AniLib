package com.revolgenx.anilib.data.model.home

import com.revolgenx.anilib.data.model.CommonMediaModel

data class SelectableCommonMediaModel(
    var isSelected: Boolean = false,
    var selectionListener: ((selected:Boolean) -> Unit)? = null
) : CommonMediaModel() {

}