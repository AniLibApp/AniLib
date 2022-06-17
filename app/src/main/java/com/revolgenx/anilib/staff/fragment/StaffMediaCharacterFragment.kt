package com.revolgenx.anilib.staff.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.staffMediaCharacterDisplayModePref
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.constant.StaffMediaCharacterDisplayMode
import com.revolgenx.anilib.databinding.StaffMediaCharacterFragmentLayoutBinding
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.staff.presenter.StaffMediaCharacterPresenter
import com.revolgenx.anilib.staff.viewmodel.StaffMediaCharacterViewModel
import com.revolgenx.anilib.staff.data.constant.StaffMediaCharacterSort
import com.revolgenx.anilib.staff.presenter.StaffMediaCharacterHeaderPresenter
import com.revolgenx.anilib.staff.presenter.StaffMediaCharacterSeriesPresenter
import com.revolgenx.anilib.staff.source.StaffMediaCharacterHeaderSource
import com.revolgenx.anilib.ui.view.makeArrayPopupMenu
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter
import com.revolgenx.anilib.util.isLandScape
import com.revolgenx.anilib.util.onItemSelected
import org.koin.androidx.viewmodel.ext.android.viewModel

class StaffMediaCharacterFragment : BasePresenterFragment<MediaModel>() {
    override val basePresenter: Presenter<MediaModel>
        get() = if (this.field.sortCharacter) StaffMediaCharacterSeriesPresenter(requireContext())
        else StaffMediaCharacterPresenter(requireContext())

    override val baseSource: Source<MediaModel>
        get() = viewModel.source ?: createSource()

    private val displayMode get() = staffMediaCharacterDisplayModePref

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
    private val field get() = viewModel.field

    private var _sBinding: StaffMediaCharacterFragmentLayoutBinding? = null
    private val sBinding get() = _sBinding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        _sBinding = StaffMediaCharacterFragmentLayoutBinding.inflate(inflater, container, false)
        sBinding.staffMediaContainerLayout.addView(v)
        return sBinding.root
    }

    override fun createSource(): Source<MediaModel> {
        return viewModel.createSource()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.field.staffId = staffId ?: return

        sBinding.bind()
        sBinding.initListener()
    }

    private fun StaffMediaCharacterFragmentLayoutBinding.bind() {
        val statusItems = requireContext().resources.getStringArray(R.array.staff_media_character_sort).map {
            DynamicMenu(null, it)
        }
        staffMediaSortSpinner.adapter = makeSpinnerAdapter(requireContext(), statusItems)
        staffMediaSortSpinner.setSelection(field.sort.ordinal, false)
        staffMediaOnListCheckbox.isChecked = field.onList
    }

    private fun StaffMediaCharacterFragmentLayoutBinding.initListener() {
        staffMediaSortSpinner.onItemSelected {
            field.sort = StaffMediaCharacterSort.values()[it]
            field.sortCharacter = field.sort == StaffMediaCharacterSort.CHARACTER_SORT
            staffMediaOnListCheckbox.isEnabled = !field.sortCharacter
            staffMediaCharacterPopupMenu.isEnabled = !field.sortCharacter
            loadLayoutManager()
            renewAdapter()
        }

        staffMediaOnListCheckbox.setOnCheckedChangeListener(null)
        staffMediaOnListCheckbox.setOnCheckedChangeListener { _, isChecked ->
            field.onList = isChecked
            renewAdapter()
        }

        staffMediaCharacterPopupMenu.onPopupMenuClickListener = { _, pos ->
            when (pos) {
                0 -> {
                    makeArrayPopupMenu(
                        staffMediaCharacterPopupMenu,
                        resources.getStringArray(R.array.staff_media_character_display_modes),
                        selectedPosition = staffMediaCharacterDisplayModePref
                    ) { _, _, index, _ ->
                        staffMediaCharacterDisplayModePref = index
                        loadLayoutManager()
                        invalidateAdapter()
                    }
                }
            }
        }
    }

    override fun getSpanCount(): Int {
        val isLandScape = requireContext().isLandScape
        return if (field.sortCharacter) {
            if (isLandScape) 6 else 3
        } else {
            when (StaffMediaCharacterDisplayMode.values()[displayMode]) {
                StaffMediaCharacterDisplayMode.COMPACT -> {
                    if (isLandScape) 4 else 2
                }
                StaffMediaCharacterDisplayMode.NORMAL -> {
                    if (isLandScape) 2 else 1
                }
            }
        }
    }

    override fun adapterBuilder(): Adapter.Builder {
        val builder = super.adapterBuilder()

        if (field.sortCharacter) {
            builder.addSource(StaffMediaCharacterHeaderSource())
            builder.addPresenter(StaffMediaCharacterHeaderPresenter(requireContext()))
        }

        return builder
    }

    private fun renewAdapter() {
        createSource()
        invalidateAdapter()
    }

    override fun onDestroyView() {
        _sBinding = null
        super.onDestroyView()
    }
}