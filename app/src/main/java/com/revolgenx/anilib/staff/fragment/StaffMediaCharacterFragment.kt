package com.revolgenx.anilib.staff.fragment

import android.os.Bundle
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.character.data.model.CharacterModel
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.staff.presenter.StaffMediaCharacterPresenter
import com.revolgenx.anilib.staff.viewmodel.StaffMediaCharacterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StaffMediaCharacterFragment : BasePresenterFragment<MediaModel>() {
    override val basePresenter: Presenter<MediaModel>
        get() = StaffMediaCharacterPresenter(
            requireContext()
        )

    override val baseSource: Source<MediaModel>
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
    override fun createSource(): Source<MediaModel> {
        return viewModel.createSource()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.field.staffId = staffId ?: return
    }
}