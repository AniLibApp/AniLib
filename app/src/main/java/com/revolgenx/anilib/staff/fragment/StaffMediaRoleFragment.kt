package com.revolgenx.anilib.staff.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.otaliastudios.elements.extensions.HeaderSource
import com.otaliastudios.elements.extensions.SimplePresenter
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.revolgenx.anilib.R
import com.revolgenx.anilib.airing.data.model.AiringScheduleModel
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.databinding.StaffMediaRoleFragmentLayoutBinding
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.staff.StaffMediaRoleHeaderSource
import com.revolgenx.anilib.staff.data.constant.StaffMediaCharacterSort
import com.revolgenx.anilib.staff.data.constant.StaffMediaRoleSort
import com.revolgenx.anilib.staff.presenter.StaffMediaRolePresenter
import com.revolgenx.anilib.staff.viewmodel.StaffMediaRoleViewModel
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter
import com.revolgenx.anilib.util.onItemSelected
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
    private val viewModel by viewModel<StaffMediaRoleViewModel>()
    private val field get() = viewModel.field

    private var _sBinding: StaffMediaRoleFragmentLayoutBinding? = null
    private val sBinding get()= _sBinding!!

    override fun createSource(): Source<MediaModel> {
        return viewModel.createSource()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        _sBinding = StaffMediaRoleFragmentLayoutBinding.inflate(inflater, container, false)
        sBinding.staffMediaRoleContainerLayout.addView(v)
        return sBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.field.staffId = staffId ?: return
        sBinding.bind()
        sBinding.initListener()
    }

    private fun StaffMediaRoleFragmentLayoutBinding.bind(){
        val statusItems = requireContext().resources.getStringArray(R.array.staff_media_role_sort).map {
            DynamicMenu(null, it)
        }
        staffMediaRoleSortSpinner.adapter = makeSpinnerAdapter(requireContext(), statusItems)
        staffMediaRoleSortSpinner.setSelection(field.sort.ordinal, false)
        staffMediaRoleOnListCheckbox.isChecked = field.onList
    }

    private fun StaffMediaRoleFragmentLayoutBinding.initListener(){
        staffMediaRoleSortSpinner.onItemSelected {
            field.sort = StaffMediaRoleSort.values()[it]
            renewAdapter()
        }
        staffMediaRoleOnListCheckbox.setOnCheckedChangeListener(null)
        staffMediaRoleOnListCheckbox.setOnCheckedChangeListener { _, isChecked ->
            field.onList = isChecked
            renewAdapter()
        }
    }

    private fun renewAdapter(){
        createSource()
        invalidateAdapter()
    }

    override fun adapterBuilder(): Adapter.Builder {
        return super.adapterBuilder().also { b->
            b.addSource(StaffMediaRoleHeaderSource(requireContext()))
            b.addPresenter(SimplePresenter<HeaderSource.Data<AiringScheduleModel, String>>(
                requireContext(),
                R.layout.header_presenter_layout,
                HeaderSource.ELEMENT_TYPE
            ) { v, header ->
                (v as TextView).text = header.header
            })
        }
    }

    override fun onDestroyView() {
        _sBinding = null
        super.onDestroyView()
    }
}
