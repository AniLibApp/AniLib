package com.revolgenx.anilib.ui.fragment.list

import androidx.navigation.fragment.navArgs
import com.revolgenx.anilib.data.meta.MediaListMeta

class MangaListContainerFragment : MediaListContainerFragment() {
    private val mangaListMetaArgs: AnimeListContainerFragmentArgs by navArgs()
    override fun mediaListMetaArgs(): MediaListMeta = mangaListMetaArgs.mediaListMeta
}