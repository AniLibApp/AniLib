package com.revolgenx.anilib.list.fragment

import com.revolgenx.anilib.list.viewmodel.AnimeListCollectionStoreVM
import com.revolgenx.anilib.list.viewmodel.MediaListCollectionStoreVM
import com.revolgenx.anilib.type.MediaType
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AnimeListCollectionFragment : BaseMediaListCollectionFragment() {
    override val mediaType: MediaType = MediaType.ANIME
    override val listCollectionStoreVM: MediaListCollectionStoreVM by sharedViewModel<AnimeListCollectionStoreVM>()
}