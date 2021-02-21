package com.revolgenx.anilib.ui.fragment.list

import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.ui.viewmodel.list.PausedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PausedFragment  : MediaListFragment() {
    override val viewModel by viewModel<PausedViewModel>()
    override val mediaListStatus: Int = MediaListStatus.PAUSED.ordinal
}
