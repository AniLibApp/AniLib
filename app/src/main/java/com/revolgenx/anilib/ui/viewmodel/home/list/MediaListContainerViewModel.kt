package com.revolgenx.anilib.ui.viewmodel.home.list

import com.revolgenx.anilib.data.field.list.MediaListCollectionField
import com.revolgenx.anilib.data.meta.type.MediaListStatus
import com.revolgenx.anilib.ui.viewmodel.BaseViewModel
import com.revolgenx.anilib.ui.viewmodel.list.MediaListCollectionViewModel

class MediaListContainerViewModel(val listStatusViewModel: Map<Int, MediaListCollectionViewModel>) :
    BaseViewModel() {

    var currentListStatus: Int = MediaListStatus.CURRENT.status

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
