package com.revolgenx.anilib.ui.fragment.home.list

import android.os.Bundle
import com.revolgenx.anilib.common.preference.recentAnimeListStatus
import com.revolgenx.anilib.common.preference.userId
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.infrastructure.event.ListEvent
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import org.greenrobot.eventbus.Subscribe

class AnimeListContainerFragment : MediaListContainerFragment(){

    override fun mediaListMetaArgs(): MediaListMeta = MediaListMeta(
        requireContext().userId(),
        null,
        MediaType.ANIME.ordinal
    )

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(savedInstanceState == null){
            val listStatus = recentAnimeListStatus(requireContext())
            setCurrentStatus(listStatus)
        }
    }

    @Subscribe
    fun onListEvent(event:ListEvent){
        when(event){
            is ListEvent.ListStatusChangedEvent -> {
                if(isAnimeType(event.listType)) {
                    setCurrentStatus(event.status)
                }
            }
            is ListEvent.ListFilterChangedEvent -> {
                if(isAnimeType(event.listType)){
                    filterList(event.meta)
                }
            }
        }
    }

    private fun isAnimeType(listType: Int) = listType == MediaType.ANIME.ordinal
}