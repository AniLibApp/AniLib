package com.revolgenx.anilib.list.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MediaListContainerSharedVM : ViewModel() {
    var userId: Int? = null
    var userName: String? = null

    val hasUserData get()= (userId ?: userName) != null

    var currentGroupNameWithCount = MutableLiveData<Pair<String, Int>?>()
    var mediaListContainerCallback = MutableLiveData<Pair<MediaListCollectionContainerCallback, Int>>()

    var animeListNavigateToTop: (()->Unit)? = null
    var mangaListNavigateToTop: (()->Unit)? = null
}

enum class MediaListCollectionContainerCallback{
    SEARCH, GROUP, CURRENT_TAB, FILTER, DISPLAY
}