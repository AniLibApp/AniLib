package com.revolgenx.anilib.ui.fragment.browse

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.meta.MediaInfoMeta
import com.revolgenx.anilib.data.meta.type.AlActivityType
import com.revolgenx.anilib.social.data.field.ActivityUnionField
import com.revolgenx.anilib.social.data.model.ActivityUnionModel
import com.revolgenx.anilib.social.ui.presenter.ActivityUnionPresenter
import com.revolgenx.anilib.social.ui.viewmodel.ActivityInfoViewModel
import com.revolgenx.anilib.social.ui.viewmodel.ActivityUnionViewModel
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityMessageComposerViewModel
import com.revolgenx.anilib.social.ui.viewmodel.activity_composer.ActivityTextComposerViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaSocialRecentActivityFragment : BasePresenterFragment<ActivityUnionModel>() {
    companion object {
        private const val MEDIA_INFO_META_KEY = "MEDIA_INFO_META_KEY"
        fun newInstance(meta: MediaInfoMeta) = MediaSocialRecentActivityFragment().also {
            it.arguments = bundleOf(MEDIA_INFO_META_KEY to meta)
        }
    }

    override val basePresenter: Presenter<ActivityUnionModel>
        get() = ActivityUnionPresenter(
            requireContext(),
            viewModel,
            activityInfoViewModel,
            textComposerViewModel,
            messageComposerViewModel
        )

    override val baseSource: Source<ActivityUnionModel>
        get() = viewModel.source ?: createSource()

    private val mediaBrowserMeta get() = arguments?.getParcelable<MediaInfoMeta?>(MEDIA_INFO_META_KEY)

    private val viewModel by viewModel<ActivityUnionViewModel>()
    private val textComposerViewModel by sharedViewModel<ActivityTextComposerViewModel>()
    private val messageComposerViewModel by sharedViewModel<ActivityMessageComposerViewModel>()
    private val activityInfoViewModel by sharedViewModel<ActivityInfoViewModel>()

    override fun createSource(): Source<ActivityUnionModel> {
        return viewModel.createSource()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.field = ActivityUnionField().also {
            it.type = AlActivityType.LIST
            it.mediaId = mediaBrowserMeta?.mediaId
        }
    }

}