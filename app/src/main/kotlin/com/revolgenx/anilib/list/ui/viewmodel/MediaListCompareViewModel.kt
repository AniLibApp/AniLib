package com.revolgenx.anilib.list.ui.viewmodel

import com.revolgenx.anilib.common.ui.viewmodel.PagingViewModel
import com.revolgenx.anilib.list.data.field.MediaListCompareField
import com.revolgenx.anilib.list.data.service.MediaListService
import com.revolgenx.anilib.list.data.source.MediaListComparePagingSource
import com.revolgenx.anilib.list.ui.model.MediaListModel

class MediaListCompareViewModel(private val mediaListService: MediaListService) :
    PagingViewModel<MediaListModel, MediaListCompareField, MediaListComparePagingSource>() {

    override val field: MediaListCompareField = MediaListCompareField()

    override val pagingSource: MediaListComparePagingSource
        get() = MediaListComparePagingSource(this.field, service = mediaListService)
}