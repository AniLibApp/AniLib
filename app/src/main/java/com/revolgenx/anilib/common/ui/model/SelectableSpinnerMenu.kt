package com.revolgenx.anilib.common.ui.model

data class SelectableSpinnerMenu(val title: String, var isSelected: Boolean) {
    override fun toString() = title
}