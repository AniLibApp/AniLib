package com.revolgenx.anilib.fragment.list

import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.viewmodel.DroppedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DroppedFragment : MediaListFragment() {
    override val viewModel by viewModel<DroppedViewModel>()
    override val mediaListStatus: Int = MediaListStatus.DROPPED.ordinal
}
