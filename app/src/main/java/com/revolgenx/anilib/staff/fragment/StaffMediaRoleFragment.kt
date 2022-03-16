package com.revolgenx.anilib.staff.fragment

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.staff.presenter.StaffMediaRolePresenter
import com.revolgenx.anilib.staff.viewmodel.StaffMediaRoleViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StaffMediaRoleFragment : BasePresenterFragment<MediaModel>() {
    override val basePresenter: Presenter<MediaModel>
        get() = StaffMediaRolePresenter(
            requireContext()
        )
    override val baseSource: Source<MediaModel>
        get() = viewModel.source ?: createSource()

    companion object {
        private const val STAFF_ID_KEY = "STAFF_ID_KEY"
        fun newInstance(staffId: Int) = StaffMediaRoleFragment().also {
            it.arguments = bundleOf(STAFF_ID_KEY to staffId)
        }
    }

    private val staffId get() = arguments?.getInt(STAFF_ID_KEY)
    override var gridMaxSpan: Int = 6
    override var gridMinSpan: Int = 3

    override fun createSource(): Source<MediaModel> {
        return viewModel.createSource()
    }

    private val viewModel by viewModel<StaffMediaRoleViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.field.staffId = staffId ?: return
    }
}
