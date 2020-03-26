package com.revolgenx.anilib.fragment.browse

import android.os.Bundle
import android.view.View
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.activity.MediaBrowserActivity
import com.revolgenx.anilib.event.meta.MediaBrowserMeta
import com.revolgenx.anilib.field.overview.MediaStaffField
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.MediaStaffModel
import com.revolgenx.anilib.presenter.MediaStaffPresenter
import com.revolgenx.anilib.viewmodel.MediaStaffViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaStaffFragment : BasePresenterFragment<MediaStaffModel>() {

    override val basePresenter: Presenter<MediaStaffModel>
        get() {
            return MediaStaffPresenter(requireContext())
        }

    override val baseSource: Source<MediaStaffModel>
        get() {
            return viewModel.staffSource ?: createSource()
        }

    private val viewModel by viewModel<MediaStaffViewModel>()
    private var mediaBrowserMeta: MediaBrowserMeta? = null
    private val field by lazy {
        MediaStaffField().also {
            it.mediaId = mediaBrowserMeta?.mediaId ?: -1
        }
    }

    override fun createSource(): Source<MediaStaffModel> {
        return viewModel.createSource(field)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layoutManager = FlexboxLayoutManager(context).also { manager ->
            manager.justifyContent = JustifyContent.SPACE_EVENLY
            manager.alignItems = AlignItems.CENTER
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        mediaBrowserMeta =
            arguments?.getParcelable(MediaBrowserActivity.MEDIA_BROWSER_META) ?: return
        super.onActivityCreated(savedInstanceState)
    }


}
