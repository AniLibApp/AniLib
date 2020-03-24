package com.revolgenx.anilib.fragment.character

import android.os.Bundle
import android.view.View
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.event.meta.CharacterMeta
import com.revolgenx.anilib.field.CharacterMediaField
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.character.CharacterMediaModel
import com.revolgenx.anilib.presenter.CharacterMediaPresenter
import com.revolgenx.anilib.viewmodel.CharacterMediaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CharacterMediaFragment : BasePresenterFragment<CharacterMediaModel>() {

    private lateinit var characterMeta: CharacterMeta
    private val field by lazy {
        CharacterMediaField().also {
            it.characterId = characterMeta.characterId
        }
    }

    override val basePresenter: Presenter<CharacterMediaModel>
        get() {
            return CharacterMediaPresenter(requireContext())
        }

    private val viewModel by viewModel<CharacterMediaViewModel>()
    override val baseSource: Source<CharacterMediaModel>
        get() = viewModel.source ?: createSource()

    override fun createSource(): Source<CharacterMediaModel> {
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
        characterMeta = arguments?.getParcelable(CharacterFragment.CHARACTER_META_KEY) ?: return
        super.onActivityCreated(savedInstanceState)
    }


    override fun onResume() {
        if (!visibleToUser)
            invalidateAdapter()
        super.onResume()
    }
}