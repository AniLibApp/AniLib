package com.revolgenx.anilib.ui.viewmodel.home.list

import com.revolgenx.anilib.data.field.list.MediaListCollectionField
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.ui.viewmodel.BaseViewModel
import com.revolgenx.anilib.ui.viewmodel.media_list.MediaListCollectionViewModel

class MediaListContainerViewModel(private val listStatusViewModel: Map<Int, MediaListCollectionViewModel>) :
    BaseViewModel() {

    var currentListStatus: Int = MediaListStatus.CURRENT.ordinal

    var mediaListField: MediaListCollectionField = MediaListCollectionField()
        set(value) {
            field = value
            listStatusViewModel.forEach {
                with(it.value.field) {
                    userId = field.userId
                    userName = field.userName
                    type = field.type
                }
            }
        }

    val mediaListViewModel: MediaListCollectionViewModel
        get() = listStatusViewModel[currentListStatus] ?: error("no viewmodel")

}
