package com.revolgenx.anilib.character.fragment

import android.os.Bundle
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.character.presenter.CharacterMediaPresenter
import com.revolgenx.anilib.character.viewmodel.CharacterMediaViewModel
import com.revolgenx.anilib.media.data.model.MediaModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterMediaFragment : BasePresenterFragment<MediaModel>() {

    override val basePresenter: Presenter<MediaModel>
        get() {
            return CharacterMediaPresenter(
                requireContext()
            )
        }

    private val viewModel by viewModel<CharacterMediaViewModel>()
    override val baseSource: Source<MediaModel>
        get() = viewModel.source ?: createSource()

    private val characterId: Int? get() =arguments?.getInt(CHARACTER_ID_KEY)
    override var gridMaxSpan: Int = 6
    override var gridMinSpan: Int = 3

    companion object {
        private const val CHARACTER_ID_KEY = "CHARACTER_ID_KEY"
        fun newInstance(characterId: Int) = CharacterMediaFragment().also {
            it.arguments = bundleOf(CHARACTER_ID_KEY to characterId)
        }
    }

    override fun createSource(): Source<MediaModel> {
        return viewModel.createSource()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.field.characterId = characterId ?: return
    }

}