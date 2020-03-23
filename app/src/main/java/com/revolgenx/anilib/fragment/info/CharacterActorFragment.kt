package com.revolgenx.anilib.fragment.info

import android.os.Bundle
import android.view.View
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.event.meta.CharacterMeta
import com.revolgenx.anilib.field.CharacterVoiceActorField
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.VoiceActorModel
import com.revolgenx.anilib.presenter.CharacterActorPresenter
import com.revolgenx.anilib.viewmodel.CharacterActorViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterActorFragment : BasePresenterFragment<VoiceActorModel>() {

    override val basePresenter: Presenter<VoiceActorModel> by lazy {
        CharacterActorPresenter(requireContext())
    }

    override val baseSource: Source<VoiceActorModel>
        get() = viewModel.source ?: createSource()
    private val viewModel by viewModel<CharacterActorViewModel>()

    private lateinit var characterMeta: CharacterMeta
    private val field by lazy {
        CharacterVoiceActorField().also {
            it.characterId = characterMeta.characterId
        }
    }


    override fun createSource(): Source<VoiceActorModel> {
        return viewModel.createSource(field)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layoutManager = FlexboxLayoutManager(context).also { manager ->
            manager.justifyContent = JustifyContent.SPACE_EVENLY
            manager.alignItems = AlignItems.CENTER
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        arguments?.classLoader = CharacterMeta::class.java.classLoader
        characterMeta =
            arguments?.getParcelable(CharacterFragment.CHARACTER_META_KEY) ?: return
        super.onActivityCreated(savedInstanceState)
    }


    override fun onResume() {
        if (!visibleToUser)
            invalidateAdapter()
        super.onResume()
    }
}
