package com.revolgenx.anilib.media.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.media.data.field.MediaWatchField
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.media.data.model.MediaStreamingEpisodeModel
import com.revolgenx.anilib.media.presenter.MediaWatchPresenter
import com.revolgenx.anilib.media.viewmodel.MediaWatchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaWatchFragment : BasePresenterFragment<MediaStreamingEpisodeModel>() {

    private val viewModel by viewModel<MediaWatchViewModel>()
    private var mediaBrowserMeta: MediaInfoMeta? = null
    private val field: MediaWatchField by lazy {
        MediaWatchField().apply {
            mediaId = mediaBrowserMeta?.mediaId ?: -1
        }
    }

    override var gridMaxSpan: Int = 4
    override var gridMinSpan: Int = 4

    override val basePresenter: Presenter<MediaStreamingEpisodeModel> by lazy {
        MediaWatchPresenter(requireContext())
    }

    override val baseSource: Source<MediaStreamingEpisodeModel>
        get() {
            return if (viewModel.watchSource != null) {
                viewModel.watchSource!!
            } else {
                createSource()
            }
        }

    companion object {
        private const val MEDIA_INFO_META_KEY = "MEDIA_INFO_META_KEY"
        fun newInstance(meta: MediaInfoMeta) = MediaWatchFragment().also {
            it.arguments = bundleOf(MEDIA_INFO_META_KEY to meta)
        }
    }

    override fun createSource(): Source<MediaStreamingEpisodeModel> {
        return viewModel.createSource(field)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaBrowserMeta = arguments?.getParcelable(MEDIA_INFO_META_KEY) ?: return
    }


}
