package com.revolgenx.anilib.fragment.list

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.activity.MediaListActivity
import com.revolgenx.anilib.event.meta.MediaListMeta
import com.revolgenx.anilib.field.MediaListFilterField
import com.revolgenx.anilib.field.list.MediaListField
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.list.MediaListModel
import com.revolgenx.anilib.presenter.list.MediaListPresenter
import com.revolgenx.anilib.type.MediaListStatus
import com.revolgenx.anilib.viewmodel.MediaListViewModel

abstract class MediaListFragment : BasePresenterFragment<MediaListModel>() {

    override val basePresenter: Presenter<MediaListModel>
        get() = MediaListPresenter(requireContext())
    override val baseSource: Source<MediaListModel>
        get() = viewModel.source ?: createSource()

    abstract val viewModel: MediaListViewModel
    abstract val mediaListStatus: Int

    protected var mediaListMeta: MediaListMeta? = null

    private val mediaListField by lazy {
        MediaListField().also { field ->
            field.userId = mediaListMeta?.userId
            field.userName = mediaListMeta?.userName
            field.mediaListStatus = mediaListStatus
            field.type = mediaListMeta?.type
        }
    }

    override fun createSource(): Source<MediaListModel> {
        return viewModel.createSource(mediaListField)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val span =
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 6 else 3
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        arguments?.classLoader = MediaListMeta::class.java.classLoader
        mediaListMeta = arguments?.getParcelable(MediaListActivity.MEDIA_LIST_META_KEY) ?: return
        super.onActivityCreated(savedInstanceState)
    }

    fun filter(mediaListFilterField: MediaListFilterField) {
        viewModel.filter = mediaListFilterField
        if(visibleToUser){
            createSource()
            invalidateAdapter()
        }
    }
}
