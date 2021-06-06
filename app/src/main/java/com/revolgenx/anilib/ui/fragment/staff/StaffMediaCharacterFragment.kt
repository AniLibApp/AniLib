package com.revolgenx.anilib.ui.fragment.staff

import android.os.Bundle
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.model.staff.StaffMediaCharacterModel
import com.revolgenx.anilib.ui.presenter.staff.StaffMediaCharacterPresenter
import com.revolgenx.anilib.ui.viewmodel.staff.StaffMediaCharacterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StaffMediaCharacterFragment : BasePresenterFragment<StaffMediaCharacterModel>() {
    override val basePresenter: Presenter<StaffMediaCharacterModel>
        get() = StaffMediaCharacterPresenter(
            requireContext()
        )

    override val baseSource: Source<StaffMediaCharacterModel>
        get() = viewModel.source ?: createSource()

    companion object {
        private const val STAFF_ID_KEY = "STAFF_ID_KEY"
        fun newInstance(staffId: Int) = StaffMediaCharacterFragment().also {
            it.arguments = bundleOf(STAFF_ID_KEY to staffId)
        }
    }

    private val staffId get() = arguments?.getInt(STAFF_ID_KEY)

    override var gridMaxSpan: Int = 4
    override var gridMinSpan: Int = 2


    private val viewModel by viewModel<StaffMediaCharacterViewModel>()
    override fun createSource(): Source<StaffMediaCharacterModel> {
        return viewModel.createSource()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.field.staffId = staffId ?: return
    }
}