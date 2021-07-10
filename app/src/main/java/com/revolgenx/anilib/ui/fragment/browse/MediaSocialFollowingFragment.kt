package com.revolgenx.anilib.ui.fragment.browse

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.meta.MediaInfoMeta
import com.revolgenx.anilib.data.model.media_info.MediaSocialFollowingModel
import com.revolgenx.anilib.ui.presenter.media.MediaSocialFollowingPresenter
import com.revolgenx.anilib.ui.viewmodel.media.MediaSocialFollowingViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaSocialFollowingFragment : BasePresenterFragment<MediaSocialFollowingModel>() {
    companion object {
        private const val MEDIA_INFO_META_KEY = "MEDIA_INFO_META_KEY"
        fun newInstance(meta: MediaInfoMeta) = MediaSocialFollowingFragment().also {
            it.arguments = bundleOf(MEDIA_INFO_META_KEY to meta)
        }
    }

    private val mediaBrowserMeta
        get() = arguments?.getParcelable<MediaInfoMeta?>(MEDIA_INFO_META_KEY)


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