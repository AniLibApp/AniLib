package com.revolgenx.anilib.fragment.info

import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import com.revolgenx.anilib.R
import com.revolgenx.anilib.event.meta.CharacterMeta
import com.revolgenx.anilib.field.CharacterField
import com.revolgenx.anilib.field.CharacterMediaField
import com.revolgenx.anilib.fragment.base.BaseToolbarFragment
import com.revolgenx.anilib.markwon.MarkwonImpl
import com.revolgenx.anilib.model.character.CharacterModel
import com.revolgenx.anilib.repository.util.Status.*
import com.revolgenx.anilib.viewmodel.CharacterViewModel
import kotlinx.android.synthetic.main.character_fragment_layout.*
import kotlinx.android.synthetic.main.error_layout.*
import kotlinx.android.synthetic.main.loading_layout.*
import kotlinx.android.synthetic.main.resource_status_container_layout.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CharacterFragment : BaseToolbarFragment() {

    companion object {
        const val CHARACTER_META_KEY = "character_meta_key"
    }

    private val viewModel by viewModel<CharacterViewModel>()

    private lateinit var characterMeta: CharacterMeta
    private val characterField by lazy {
        CharacterField().also {
            it.characterId = characterMeta.characterId
        }
    }

    private val characterMediaField by lazy {
        CharacterMediaField().also {
            it.characterId = characterMeta.characterId
        }
    }
    override val title: String
        get() = getString(R.string.character)

    override val contentRes: Int
        get() = R.layout.character_fragment_layout

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments?.classLoader = CharacterMeta::class.java.classLoader
        characterMeta = arguments?.getParcelable<CharacterMeta>(CHARACTER_META_KEY) ?: return

        characterIv.setImageURI(characterMeta.characterUrl)
        viewModel.characterInfoLiveData.observe(viewLifecycleOwner) { res ->
            when (res.status) {
                SUCCESS -> {
                    resourceStatusContainer.visibility = View.GONE
                    progressLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                    updateView(res.data!!)
                }
                ERROR -> {
                    resourceStatusContainer.visibility = View.VISIBLE
                    progressLayout.visibility = View.GONE
                    errorLayout.visibility = View.VISIBLE
                }
                LOADING -> {
                    resourceStatusContainer.visibility = View.VISIBLE
                    progressLayout.visibility = View.VISIBLE
                    errorLayout.visibility = View.GONE
                }
            }
        }
        if (savedInstanceState == null) {
            viewModel.getCharacterInfo(characterField)
        }
    }

    private fun updateView(item: CharacterModel) {
        characterNameTv.text = item.name?.full
        item.name?.native?.let {
            nativeNameTv.subtitle = it
        } ?: let {
            nativeNameTv.visibility = View.GONE
        }

        item.name?.alternative?.let {
            alternativeNameTv.subtitle = it.joinToString()
        }
        Timber.d("desc ${item.descrition}" + "heeloo wo \n hellowo     newline\\ new new line")

        MarkwonImpl.instance.setMarkdown(expandableTv, item.descrition ?: "")
    }
}