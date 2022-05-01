package com.revolgenx.anilib.character.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.character.presenter.CharacterMediaPresenter
import com.revolgenx.anilib.character.viewmodel.CharacterMediaViewModel
import com.revolgenx.anilib.databinding.CharacterMediaFragmentLayoutBinding
import com.revolgenx.anilib.media.data.model.MediaModel
import com.revolgenx.anilib.character.data.constant.CharacterMediaSort
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter
import com.revolgenx.anilib.util.onItemSelected
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterMediaFragment : BasePresenterFragment<MediaModel>() {

    override val basePresenter: Presenter<MediaModel>
        get() {
            return CharacterMediaPresenter(
                requireContext()
            )
        }

    private var _cBinding: CharacterMediaFragmentLayoutBinding? = null
    private val cBinding: CharacterMediaFragmentLayoutBinding get() = _cBinding!!

    private val viewModel by viewModel<CharacterMediaViewModel>()
    private val field get() = viewModel.field
    override val baseSource: Source<MediaModel>
        get() = viewModel.source ?: createSource()

    private val characterId: Int? get() = arguments?.getInt(CHARACTER_ID_KEY)
    override var gridMaxSpan: Int = 6
    override var gridMinSpan: Int = 3

    companion object {
        private const val CHARACTER_ID_KEY = "CHARACTER_ID_KEY"
        fun newInstance(characterId: Int) = CharacterMediaFragment().also {
            it.arguments = bundleOf(CHARACTER_ID_KEY to characterId)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        _cBinding = CharacterMediaFragmentLayoutBinding.inflate(inflater, container, false)
        cBinding.characterMediaContainerLayout.addView(v)
        return cBinding.root
    }

    override fun createSource(): Source<MediaModel> {
        return viewModel.createSource()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.field.characterId = characterId ?: return
        cBinding.bind()
        cBinding.initListener()
    }

    private fun CharacterMediaFragmentLayoutBinding.initListener() {
        characterMediaSortSpinner.onItemSelected {
            field.sort = CharacterMediaSort.values()[it]
            renewAdapter()
        }
        characterMediaOnListCheckbox.setOnCheckedChangeListener(null)
        characterMediaOnListCheckbox.setOnCheckedChangeListener { _, isChecked ->
            field.onList = isChecked
            renewAdapter()
        }
    }

    private fun renewAdapter() {
        createSource()
        invalidateAdapter()
    }

    private fun CharacterMediaFragmentLayoutBinding.bind() {
        val statusItems = requireContext().resources.getStringArray(R.array.character_media_sort).map {
            DynamicMenu(null, it)
        }
        characterMediaSortSpinner.adapter = makeSpinnerAdapter(requireContext(), statusItems)
        characterMediaSortSpinner.setSelection(field.sort.ordinal, false)
        characterMediaOnListCheckbox.isChecked = field.onList
    }

    override fun onDestroyView() {
        _cBinding = null
        super.onDestroyView()
    }

}