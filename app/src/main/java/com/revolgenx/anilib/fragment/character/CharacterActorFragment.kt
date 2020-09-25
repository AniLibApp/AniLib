package com.revolgenx.anilib.fragment.character

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.meta.CharacterMeta
import com.revolgenx.anilib.model.VoiceActorModel
import com.revolgenx.anilib.presenter.character.CharacterActorPresenter
import com.revolgenx.anilib.viewmodel.character.CharacterActorViewModel
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

    private lateinit var characterMeta: CharacterMeta
    private val field by lazy {
    }


    override fun createSource(): Source<VoiceActorModel> {
        return viewModel.createSource()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val span =
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 6 else 3
        layoutManager =
            GridLayoutManager(
                this.context,
                span
            ).also {
                it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adapter?.getItemViewType(position) == 0) {
                            1
                        } else {
                            span
                        }
                    }
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        arguments?.classLoader = CharacterMeta::class.java.classLoader
        characterMeta =
            arguments?.getParcelable(CharacterFragment.CHARACTER_META_KEY) ?: return
        viewModel.field.characterId = characterMeta.characterId
        super.onActivityCreated(savedInstanceState)
    }


}
