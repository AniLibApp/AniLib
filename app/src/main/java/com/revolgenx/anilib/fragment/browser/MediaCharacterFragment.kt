package com.revolgenx.anilib.fragment.browser

import android.content.res.Configuration
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.activity.MediaBrowserActivity
import com.revolgenx.anilib.event.meta.MediaBrowserMeta
import com.revolgenx.anilib.field.overview.MediaCharacterField
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.MediaCharacterModel
import com.revolgenx.anilib.presenter.MediaCharacterPresenter
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.viewmodel.MediaCharacterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaCharacterFragment : BasePresenterFragment<MediaCharacterModel>() {

    override val basePresenter: Presenter<MediaCharacterModel>
        get() {
            return MediaCharacterPresenter(context!!)
        }

    override val baseSource: Source<MediaCharacterModel>
        get() {
            return viewModel.characterSource ?: createSource()
        }

    private var mediaBrowserMeta: MediaBrowserMeta? = null
    private val viewModel by viewModel<MediaCharacterViewModel>()
    private val field by lazy {
        MediaCharacterField().also {
            it.mediaId = mediaBrowserMeta?.mediaId ?: -1
            it.type - mediaBrowserMeta!!.type
        }
    }

    override fun createSource(): Source<MediaCharacterModel> {
        return viewModel.createSource(field)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        mediaBrowserMeta =
            arguments?.getParcelable(MediaBrowserActivity.MEDIA_BROWSER_META) ?: return

        if (mediaBrowserMeta!!.type == MediaType.MANGA.ordinal) {
            layoutManager = GridLayoutManager(
                this.context,
                if (context!!.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
            )
        }

        super.onActivityCreated(savedInstanceState)
    }

    override fun onResume() {
        if (!visibleToUser)
            invalidateAdapter()
        super.onResume()
    }

}

