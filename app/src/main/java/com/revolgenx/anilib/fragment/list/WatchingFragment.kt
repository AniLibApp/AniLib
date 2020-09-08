package com.revolgenx.anilib.fragment.list

import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.viewmodel.media_list.WatchingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WatchingFragment : MediaListFragment() {
    override val viewModel by viewModel<WatchingViewModel>()
    override val mediaListStatus: Int = MediaListStatus.CURRENT.ordinal
}
