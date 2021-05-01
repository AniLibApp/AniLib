package com.revolgenx.anilib.ui.dialog.sorting

data class AniLibSortingModel(
    val data:Any,
    val title:String,
    var order:SortOrder,
    val canSort:Boolean = true,
    val allowNone:Boolean = true
)

enum class SortOrder{
    ASC, DESC, NONE, CHECK
}