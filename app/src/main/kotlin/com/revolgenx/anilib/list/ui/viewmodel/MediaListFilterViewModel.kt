package com.revolgenx.anilib.list.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.list.data.filter.MediaListCollectionFilter

sealed class MediaListFilterViewModel : ViewModel() {
    lateinit var filter: MediaListCollectionFilter
}

class AnimeListFilterViewModel : MediaListFilterViewModel()
class MangaListFilterViewModel : MediaListFilterViewModel()