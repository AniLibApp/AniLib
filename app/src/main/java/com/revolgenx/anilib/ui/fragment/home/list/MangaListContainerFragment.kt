package com.revolgenx.anilib.ui.fragment.home.list

import android.os.Bundle
import com.revolgenx.anilib.common.preference.UserPreference
import com.revolgenx.anilib.common.preference.recentAnimeListStatus
import com.revolgenx.anilib.common.preference.recentMangaListStatus
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.infrastructure.event.ListEvent
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import org.greenrobot.eventbus.Subscribe

class MangaListContainerFragment : MediaListContainerFragment() {

    override fun mediaListMetaArgs(): MediaListMeta = MediaListMeta(
        UserPreference.userId,
        null,
        MediaType.MANGA.ordinal
    )

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(savedInstanceState == null){
            val listStatus = recentMangaListStatus(requireContext())
            setCurrentStatus(listStatus)
        }
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }

    @Subscribe
    fun onListEvent(event: ListEvent) {
        when (event) {
            is ListEvent.ListStatusChangedEvent -> {
                if (isMangaType(event.listType)) {
                    setCurrentStatus(event.status)
                }
            }

            is ListEvent.ListFilterChangedEvent -> {
                if (isMangaType(event.listType)) {
                    filterList(event.meta)
                }
            }

        }
    }

    private fun isMangaType(listType: Int) = listType == MediaType.MANGA.ordinal

}