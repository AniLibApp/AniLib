package com.revolgenx.anilib.ui.fragment.character

import android.os.Bundle
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.model.VoiceActorModel
import com.revolgenx.anilib.ui.presenter.character.CharacterActorPresenter
import com.revolgenx.anilib.ui.viewmodel.character.CharacterActorViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterActorFragment : BasePresenterFragment<VoiceActorModel>() {

    override val basePresenter: Presenter<VoiceActorModel> by lazy {
        CharacterActorPresenter(
            requireContext()
        )
    }

    override val baseSource: Source<VoiceActorModel>
        get() = viewModel.source ?: createSource()
    private val viewModel by viewModel<CharacterActorViewModel>()

    private val characterId: Int? get() = arguments?.getInt(CHARACTER_ID_KEY)

    override var gridMaxSpan: Int = 6
    override var gridMinSpan: Int = 3

    companion object {
        private const val CHARACTER_ID_KEY = "CHARACTER_ID_KEY"
        fun newInstance(characterId: Int) = CharacterActorFragment().also {
            it.arguments = bundleOf(CHARACTER_ID_KEY to characterId)
        }
    }

    override fun createSource(): Source<VoiceActorModel> {
        return viewModel.createSource()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.field.characterId = characterId ?: return
    }


}
