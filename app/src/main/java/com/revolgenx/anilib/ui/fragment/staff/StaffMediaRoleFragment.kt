package com.revolgenx.anilib.ui.fragment.staff

import android.os.Bundle
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.model.staff.StaffMediaRoleModel
import com.revolgenx.anilib.ui.presenter.staff.StaffMediaRolePresenter
import com.revolgenx.anilib.ui.viewmodel.staff.StaffMediaRoleViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StaffMediaRoleFragment : BasePresenterFragment<StaffMediaRoleModel>() {
    override val basePresenter: Presenter<StaffMediaRoleModel>
        get() = StaffMediaRolePresenter(
            requireContext()
        )
    override val baseSource: Source<StaffMediaRoleModel>
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

    override fun createSource(): Source<StaffMediaRoleModel> {
        return viewModel.createSource()
    }

    private val viewModel by viewModel<StaffMediaRoleViewModel>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.field.staffId = staffId ?: return
    }
}
