package com.revolgenx.anilib.fragment.browse

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.activity.MediaBrowseActivity
import com.revolgenx.anilib.field.media.MediaWatchField
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.meta.MediaBrowserMeta
import com.revolgenx.anilib.model.MediaWatchModel
import com.revolgenx.anilib.presenter.MediaWatchPresenter
import com.revolgenx.anilib.viewmodel.MediaWatchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaWatchFragment : BasePresenterFragment<MediaWatchModel>() {

    private val viewModel by viewModel<MediaWatchViewModel>()
    private var mediaBrowserMeta: MediaBrowserMeta? = null
    private val field: MediaWatchField by lazy {
        MediaWatchField().apply {
            mediaId = mediaBrowserMeta?.mediaId ?: -1
        }
    }

    override val basePresenter: Presenter<MediaWatchModel> by lazy {
        MediaWatchPresenter(requireContext())
    }

    override val baseSource: Source<MediaWatchModel>
        get() {
            return if (viewModel.watchSource != null) {
                viewModel.watchSource!!
            } else {
                createSource()
            }
        }

    override fun createSource(): Source<MediaWatchModel> {
        return viewModel.createSource(field)
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        mediaBrowserMeta =
            arguments?.getParcelable(MediaBrowseActivity.MEDIA_BROWSER_META) ?: return
        super.onActivityCreated(savedInstanceState)
    }


}
