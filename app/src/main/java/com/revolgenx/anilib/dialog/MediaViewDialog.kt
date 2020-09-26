package com.revolgenx.anilib.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.pagers.PageSizePager
import com.pranavpandey.android.dynamic.support.dialog.DynamicDialog
import com.revolgenx.anilib.R
import com.revolgenx.anilib.presenter.media.MediaViewPresenter
import com.revolgenx.anilib.source.MediaSource
import com.revolgenx.anilib.viewmodel.media.MediaViewDialogViewModel
import kotlinx.android.synthetic.main.media_view_dialog_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaViewDialog : BaseDialogFragment() {
    override var viewRes: Int? = R.layout.media_view_dialog_layout

    private lateinit var adapter: Adapter

    private val loadingPresenter: Presenter<Unit> by lazy {
        Presenter.forLoadingIndicator(
            requireContext(), R.layout.loading_layout
        )
    }

    private val errorPresenter: Presenter<Unit> by lazy {
        Presenter.forErrorIndicator(requireContext(), R.layout.error_layout)
    }

    private val emptyPresenter: Presenter<Unit> by lazy {
        Presenter.forEmptyIndicator(requireContext(), R.layout.empty_layout)
    }


    private val presenter
        get() = MediaViewPresenter(requireContext())

    private val source: MediaSource
        get() = viewModel.source ?: viewModel.createSource()


    companion object {
        private const val MEDIA_IDS_IN_KEY = "MEDIA_IDS_IN_KEY"
        fun newInstance(mediaIdsIn: List<Int>): MediaViewDialog {
            return MediaViewDialog().apply {
                arguments = bundleOf(MEDIA_IDS_IN_KEY to mediaIdsIn.toIntArray())
            }
        }
    }

    private val viewModel by viewModel<MediaViewDialogViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
    }
    override fun onShowListener(alertDialog: DynamicDialog, savedInstanceState: Bundle?) {
        super.onShowListener(alertDialog, savedInstanceState)
        with(alertDialog) {
            mediaViewDone.setOnClickListener {
                dismiss()
            }
            if (savedInstanceState == null) {
                viewModel.field.mediaIdsIn =
                    arguments?.getIntArray(MEDIA_IDS_IN_KEY)?.toList() ?: return
            }

            mediaListViewRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            invalidateAdapter(mediaListViewRecyclerView)
        }
    }

    private fun invalidateAdapter(recyclerView: RecyclerView) {
        adapter = Adapter.builder(this, 10)
            .setPager(PageSizePager(10))
            .addSource(source)
            .addPresenter(presenter)
            .addPresenter(loadingPresenter)
            .addPresenter(errorPresenter)
            .addPresenter(emptyPresenter)
            .into(recyclerView)
    }

}