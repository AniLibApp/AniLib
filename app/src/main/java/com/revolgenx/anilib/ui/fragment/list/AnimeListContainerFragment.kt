package com.revolgenx.anilib.ui.fragment.list

import androidx.navigation.fragment.navArgs
import com.revolgenx.anilib.data.meta.MediaListMeta

class AnimeListContainerFragment : MediaListContainerFragment() {
    private val animeListMetaArgs: AnimeListContainerFragmentArgs by navArgs()

    override fun mediaListMetaArgs(): MediaListMeta = animeListMetaArgs.mediaListMeta
}