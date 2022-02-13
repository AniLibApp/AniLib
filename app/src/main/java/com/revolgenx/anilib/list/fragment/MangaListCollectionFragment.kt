package com.revolgenx.anilib.list.fragment

import com.revolgenx.anilib.list.viewmodel.MangaListCollectionStoreVM
import com.revolgenx.anilib.list.viewmodel.MediaListCollectionStoreVM
import com.revolgenx.anilib.type.MediaType
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MangaListCollectionFragment : BaseMediaListCollectionFragment() {
    override val mediaType: MediaType = MediaType.MANGA
    override val listCollectionStoreVM: MediaListCollectionStoreVM by sharedViewModel<MangaListCollectionStoreVM>()
}