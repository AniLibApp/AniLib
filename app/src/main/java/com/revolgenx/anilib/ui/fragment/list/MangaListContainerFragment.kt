package com.revolgenx.anilib.ui.fragment.list

import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.type.MediaType

class MangaListContainerFragment : MediaListContainerFragment() {

    override fun mediaListMetaArgs(): MediaListMeta = MediaListMeta(
        requireContext().userId(),
        null,
        MediaType.MANGA.ordinal
    )

    private val mangaListStatus by lazy {
        requireContext().resources.getStringArray(R.array.manga_list_status)
    }

    override fun getStatusName(): String {
        return mangaListStatus[getCurrentStatus()]
    }

    override fun getStatus(): Array<out String>{
        return mangaListStatus
    }
}