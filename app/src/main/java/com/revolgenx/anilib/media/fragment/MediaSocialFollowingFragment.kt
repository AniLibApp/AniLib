package com.revolgenx.anilib.media.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.media.data.model.MediaSocialFollowingModel
import com.revolgenx.anilib.media.presenter.MediaSocialFollowingPresenter
import com.revolgenx.anilib.media.viewmodel.MediaSocialFollowingViewModel
import com.revolgenx.anilib.util.getParcelableCompat
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaSocialFollowingFragment : BasePresenterFragment<MediaSocialFollowingModel>() {
    companion object {
        private const val MEDIA_INFO_META_KEY = "MEDIA_INFO_META_KEY"
        fun newInstance(meta: MediaInfoMeta) = MediaSocialFollowingFragment().also {
            it.arguments = bundleOf(MEDIA_INFO_META_KEY to meta)
        }
    }

    private val mediaBrowserMeta
        get() = arguments?.getParcelableCompat<MediaInfoMeta?>(MEDIA_INFO_META_KEY)


    override val basePresenter: Presenter<MediaSocialFollowingModel>
        get() = MediaSocialFollowingPresenter(requireContext())
    override val baseSource: Source<MediaSocialFollowingModel>
        get() = viewModel.source ?: createSource()

    private val viewModel by viewModel<MediaSocialFollowingViewModel>()


    override fun createSource(): Source<MediaSocialFollowingModel> {
        return viewModel.createSource()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.field.mediaId = mediaBrowserMeta?.mediaId ?: return
    }

}