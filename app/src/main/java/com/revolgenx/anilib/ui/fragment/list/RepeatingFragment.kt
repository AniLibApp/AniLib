package com.revolgenx.anilib.ui.fragment.list

import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.ui.viewmodel.media_list.RepeatingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepeatingFragment : MediaListFragment() {
    override val viewModel by viewModel<RepeatingViewModel>()
    override val mediaListStatus: Int = MediaListStatus.REPEATING.ordinal
}
