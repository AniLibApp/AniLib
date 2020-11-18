package com.revolgenx.anilib.ui.fragment.list

import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.ui.viewmodel.media_list.DroppedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DroppedFragment : MediaListFragment() {
    override val viewModel by viewModel<DroppedViewModel>()
    override val mediaListStatus: Int = MediaListStatus.DROPPED.ordinal
}
