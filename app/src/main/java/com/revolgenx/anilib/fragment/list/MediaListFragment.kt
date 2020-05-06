package com.revolgenx.anilib.fragment.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.activity.MediaListActivity
import com.revolgenx.anilib.event.ListEditorResultEvent
import com.revolgenx.anilib.meta.MediaListMeta
import com.revolgenx.anilib.field.MediaListFilterField
import com.revolgenx.anilib.field.list.MediaListField
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.list.MediaListModel
import com.revolgenx.anilib.presenter.list.MediaListPresenter
import com.revolgenx.anilib.util.registerForEvent
import com.revolgenx.anilib.util.unRegisterForEvent
import com.revolgenx.anilib.viewmodel.MediaListViewModel
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

abstract class MediaListFragment : BasePresenterFragment<MediaListModel>() {

    override val basePresenter: Presenter<MediaListModel>
        get() = MediaListPresenter(requireContext(), mediaListMeta!!, viewModel, viewLifecycleOwner)
    override val baseSource: Source<MediaListModel>
        get() = viewModel.source ?: createSource()

    abstract val viewModel: MediaListViewModel
    abstract val mediaListStatus: Int

    protected var mediaListMeta: MediaListMeta? = null


    override fun createSource(): Source<MediaListModel> {
        return viewModel.createSource()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val span =
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
        layoutManager =
            GridLayoutManager(
                this.context,
                span
            ).also {
                it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adapter?.elementAt(position)?.element?.type == 0) {
                            1
                        } else {
                            span
                        }
                    }
                }
            }
    }

    override fun onStart() {
        super.onStart()
        registerForEvent()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        arguments?.classLoader = MediaListMeta::class.java.classLoader
        mediaListMeta = arguments?.getParcelable(MediaListActivity.MEDIA_LIST_META_KEY) ?: return
        viewModel.field.also { field ->
            field.userId = mediaListMeta?.userId
            field.userName = mediaListMeta?.userName
            field.mediaListStatus = mediaListStatus
            field.type = mediaListMeta?.type
        }
        super.onActivityCreated(savedInstanceState)

        baseSwipeRefreshLayout.setOnRefreshListener {
            viewModel.renewSource()
            createSource()
            baseSwipeRefreshLayout.isRefreshing = false
            invalidateAdapter()
        }
    }

    fun filter(mediaListFilterField: MediaListFilterField) {
        viewModel.filter = mediaListFilterField
        if (visibleToUser) {
            createSource()
            invalidateAdapter()
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onListEditorEvent(event: ListEditorResultEvent) {
        if (event.listEditorResultMeta.status != mediaListStatus) return
        event.listEditorResultMeta.let {
            if (it.progress == null) {
                viewModel.filteredList?.remove(viewModel.listMap[it.mediaId])
                viewModel.listMap.remove(it.mediaId)
                createSource()
                invalidateAdapter()
            } else {
                viewModel.listMap[it.mediaId]?.apply {
                    progress = it.progress?.toString()
                }
                adapter?.notifyDataSetChanged()
            }
        }
        EventBus.getDefault().removeStickyEvent(event)
    }

    override fun onStop() {
        super.onStop()
        unRegisterForEvent()
    }
}
