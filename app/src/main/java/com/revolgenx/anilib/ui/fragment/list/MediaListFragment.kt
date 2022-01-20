package com.revolgenx.anilib.ui.fragment.list

import android.content.res.Configuration
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.infrastructure.event.DisplayModeChangedEvent
import com.revolgenx.anilib.infrastructure.event.DisplayTypes
import com.revolgenx.anilib.data.field.MediaListCollectionFilterField
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.meta.MediaListMeta
import com.revolgenx.anilib.data.model.list.AlMediaListModel
import com.revolgenx.anilib.common.preference.getMediaListGridPresenter
import com.revolgenx.anilib.constant.MediaListDisplayMode
import com.revolgenx.anilib.home.discover.presenter.MediaListCollectionPresenter
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import com.revolgenx.anilib.ui.viewmodel.list.MediaListCollectionViewModel
import com.revolgenx.anilib.util.EventBusListener
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class MediaListFragment : BasePresenterFragment<AlMediaListModel>(), EventBusListener {

    override val basePresenter: Presenter<AlMediaListModel>
        get() = MediaListCollectionPresenter(requireContext(), mediaListMeta!!, viewModel)
    override val baseSource: Source<AlMediaListModel>
        get() = viewModel.source ?: createSource()

    abstract val viewModel: MediaListCollectionViewModel
    abstract val mediaListStatus: Int

    protected abstract val mediaListMeta: MediaListMeta?


    override fun createSource(): Source<AlMediaListModel> {
        return viewModel.createSource()
    }

    override fun reloadLayoutManager() {
        var span =
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2

        when (getMediaListGridPresenter()) {
            MediaListDisplayMode.COMPACT -> {

            }

            MediaListDisplayMode.NORMAL, MediaListDisplayMode.MINIMAL_LIST -> {
                span /= 2
            }

            MediaListDisplayMode.CARD -> {

            }

            MediaListDisplayMode.CLASSIC, MediaListDisplayMode.MINIMAL -> {
                span += 1
            }

        }

        layoutManager =
            GridLayoutManager(
                this.context,
                span
            ).also {
                it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adapter?.getItemViewType(position) == 0) {
                            1
                        } else {
                            span
                        }
                    }
                }
            }

        super.reloadLayoutManager()
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val meta = mediaListMeta ?: return
        viewModel.field.also { field ->
            field.userId = meta.userId
            field.userName = meta.userName
            field.mediaListStatus = mediaListStatus
            field.type = meta.type
        }

        baseSwipeRefreshLayout.setOnRefreshListener {
            viewModel.renewSource()
            createSource()
            baseSwipeRefreshLayout.isRefreshing = false
            invalidateAdapter()
        }
    }

    fun filter() {
        if (visibleToUser) {
            createSource()
            invalidateAdapter()
        }
    }

    fun saveCurrentFilter(mediaListFilterField: MediaListCollectionFilterField){
        viewModel.updateFilter(mediaListFilterField)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onDisplayModeEvent(event: DisplayModeChangedEvent) {
        when (event.whichDisplay) {
            DisplayTypes.MEDIA_LIST -> {
                changePresenterLayout()
            }
        }
    }

    private fun changePresenterLayout() {
        reloadLayoutManager()
        if (visibleToUser) {
            invalidateAdapter()
        }
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }
}
