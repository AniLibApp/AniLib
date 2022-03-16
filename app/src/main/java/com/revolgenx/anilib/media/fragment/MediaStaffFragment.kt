package com.revolgenx.anilib.media.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.media.data.field.MediaStaffField
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.media.presenter.MediaStaffPresenter
import com.revolgenx.anilib.media.viewmodel.MediaStaffViewModel
import com.revolgenx.anilib.staff.data.model.StaffEdgeModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaStaffFragment : BasePresenterFragment<StaffEdgeModel>() {

    override val basePresenter: Presenter<StaffEdgeModel>
        get() {
            return MediaStaffPresenter(
                requireContext()
            )
        }

    override val baseSource: Source<StaffEdgeModel>
        get() {
            return viewModel.staffSource ?: createSource()
        }

    private val viewModel by viewModel<MediaStaffViewModel>()
    private var mediaBrowserMeta: MediaInfoMeta? = null
    private val field by lazy {
        MediaStaffField().also {
            it.mediaId = mediaBrowserMeta?.mediaId ?: -1
        }
    }

    companion object {
        private const val MEDIA_INFO_META_KEY = "MEDIA_INFO_META_KEY"
        fun newInstance(meta: MediaInfoMeta) = MediaStaffFragment().also {
            it.arguments = bundleOf(MEDIA_INFO_META_KEY to meta)
        }
    }

    override fun createSource(): Source<StaffEdgeModel> {
        return viewModel.createSource(field)
    }

    override val gridMaxSpan: Int = 6
    override val gridMinSpan: Int = 3

    override fun getItemSpanSize(position: Int): Int =
        if (adapter?.getItemViewType(position) == 0) {
            1
        } else {
            span
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaBrowserMeta =
            arguments?.getParcelable(MEDIA_INFO_META_KEY) ?: return
    }

}
