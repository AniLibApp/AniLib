package com.revolgenx.anilib.ui.fragment.home.list

import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.type.MediaType

class AnimeListContainerFragment : MediaListContainerFragment(){

    override fun mediaListMetaArgs(): MediaListMeta = MediaListMeta(
        requireContext().userId(),
        null,
        MediaType.ANIME.ordinal
    )


    private val animeListStatus by lazy {
        requireContext().resources.getStringArray(R.array.anime_list_status)
    }

    override fun getStatusName(): String {
        return animeListStatus[getCurrentStatus()]
    }

    override fun getStatus(): Array<out String> {
        return animeListStatus
    }

}