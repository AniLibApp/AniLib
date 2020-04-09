package com.revolgenx.anilib.fragment.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.activity.MediaListActivity
import com.revolgenx.anilib.event.meta.MediaListMeta
import com.revolgenx.anilib.field.list.MediaListField
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.list.MediaListModel
import com.revolgenx.anilib.presenter.list.MediaListPresenter
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.viewmodel.WatchingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class WatchingFragment : MediaListFragment() {
    override val viewModel by viewModel<WatchingViewModel>()
    override val mediaListStatus: Int = MediaListStatus.CURRENT.ordinal
}
