package com.revolgenx.anilib.fragment.list

import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.viewmodel.CompletedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompletedFragment : MediaListFragment() {
    override val viewModel by viewModel<CompletedViewModel>()
    override val mediaListStatus: Int = MediaListStatus.COMPLETED.ordinal
}
