package com.revolgenx.anilib.fragment.browse

import android.os.Bundle
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.activity.MediaBrowseActivity
import com.revolgenx.anilib.meta.MediaBrowserMeta
import com.revolgenx.anilib.field.media.MediaReviewField
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.MediaReviewModel
import com.revolgenx.anilib.presenter.MediaReviewPresenter
import com.revolgenx.anilib.viewmodel.MediaReviewViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaReviewFragment : BasePresenterFragment<MediaReviewModel>() {

    override val basePresenter: Presenter<MediaReviewModel>
        get() {
            return MediaReviewPresenter(requireContext())
        }

    override val baseSource: Source<MediaReviewModel>
        get() {
            return viewModel.reviewSource ?: createSource()
        }

    private val viewModel by viewModel<MediaReviewViewModel>()

    private var mediaBrowserMeta: MediaBrowserMeta? = null
    private val field by lazy {
        MediaReviewField().also {
            it.mediaId = mediaBrowserMeta?.mediaId ?: -1
        }
    }

    override fun createSource(): Source<MediaReviewModel> {
        return viewModel.createSource(field)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        mediaBrowserMeta =
            arguments?.getParcelable(MediaBrowseActivity.MEDIA_BROWSER_META) ?: return
        super.onActivityCreated(savedInstanceState)
    }

}
