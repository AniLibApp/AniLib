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

class WatchingFragment : BasePresenterFragment<MediaListModel>() {
    override val basePresenter: Presenter<MediaListModel>
        get() = MediaListPresenter(requireContext())
    override val baseSource: Source<MediaListModel>
        get() = viewModel.source ?: createSource()

    private val mediaListField by lazy {
        MediaListField().also { field ->
            field.userId = mediaListMeta?.userId
            field.userName = mediaListMeta?.userName
            field.mediaListStatus = MediaListStatus.CURRENT.ordinal
            field.type = mediaListMeta?.type
        }
    }

    private var mediaListMeta: MediaListMeta? = null

    override fun createSource(): Source<MediaListModel> {
        return viewModel.createSource(mediaListField)
    }

    private val viewModel by viewModel<WatchingViewModel>()

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

}
