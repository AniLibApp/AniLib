package com.revolgenx.anilib.ui.fragment.list

import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.ui.viewmodel.media_list.PlanningViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlanningFragment : MediaListFragment() {
    override val viewModel by viewModel<PlanningViewModel>()
    override val mediaListStatus: Int = MediaListStatus.PLANNING.ordinal
}

