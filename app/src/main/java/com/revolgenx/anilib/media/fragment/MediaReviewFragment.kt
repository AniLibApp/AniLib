package com.revolgenx.anilib.media.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.media.data.field.MediaReviewField
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.media.data.model.MediaReviewModel
import com.revolgenx.anilib.media.presenter.MediaReviewPresenter
import com.revolgenx.anilib.media.viewmodel.MediaReviewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaReviewFragment : BasePresenterFragment<MediaReviewModel>() {

    override val basePresenter: Presenter<MediaReviewModel>
        get() {
            return MediaReviewPresenter(
                requireContext()
            )
        }

    override val baseSource: Source<MediaReviewModel>
        get() {
            return viewModel.reviewSource ?: createSource()
        }

    private val viewModel by viewModel<MediaReviewViewModel>()

    private var mediaBrowserMeta: MediaInfoMeta? = null
    private val field by lazy {
        MediaReviewField().also {
            it.mediaId = mediaBrowserMeta?.mediaId ?: -1
        }
    }

    companion object {
        private const val MEDIA_INFO_META_KEY = "MEDIA_INFO_META_KEY"
        fun newInstance(meta: MediaInfoMeta) = MediaReviewFragment().also {
            it.arguments = bundleOf(MEDIA_INFO_META_KEY to meta)
        }
    }

    override fun createSource(): Source<MediaReviewModel> {
        return viewModel.createSource(field)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaBrowserMeta =
            arguments?.getParcelable(MEDIA_INFO_META_KEY) ?: return
    }

}
