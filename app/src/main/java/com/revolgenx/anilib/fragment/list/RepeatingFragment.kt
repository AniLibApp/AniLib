package com.revolgenx.anilib.fragment.list

import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.viewmodel.RepeatingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RepeatingFragment : MediaListFragment() {
    override val viewModel by viewModel<RepeatingViewModel>()
    override val mediaListStatus: Int = MediaListStatus.REPEATING.ordinal
}
