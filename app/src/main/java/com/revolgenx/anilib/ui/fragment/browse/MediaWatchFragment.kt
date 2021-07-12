package com.revolgenx.anilib.ui.fragment.browse

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.data.field.media.MediaWatchField
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.meta.MediaInfoMeta
import com.revolgenx.anilib.data.model.media_info.MediaWatchModel
import com.revolgenx.anilib.ui.presenter.media.MediaWatchPresenter
import com.revolgenx.anilib.ui.viewmodel.media.MediaWatchViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaWatchFragment : BasePresenterFragment<MediaWatchModel>() {

    private val viewModel by viewModel<MediaWatchViewModel>()
    private var mediaBrowserMeta: MediaInfoMeta? = null
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

    companion object{
        private const val MEDIA_INFO_META_KEY = "MEDIA_INFO_META_KEY"
        fun newInstance(meta:MediaInfoMeta) = MediaWatchFragment().also {
            it.arguments = bundleOf(MEDIA_INFO_META_KEY to meta)
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
                        return if (adapter?.getItemViewType(position) == 0) {
                            1
                        } else {
                            span
                        }
                    }
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mediaBrowserMeta = arguments?.getParcelable(MEDIA_INFO_META_KEY) ?: return
    }


}
