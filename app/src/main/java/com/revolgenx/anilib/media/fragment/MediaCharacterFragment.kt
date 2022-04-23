package com.revolgenx.anilib.media.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.os.bundleOf
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.data.model.CharacterEdgeModel
import com.revolgenx.anilib.common.preference.mediaCharacterDisplayModePref
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.constant.MediaCharacterDisplayMode
import com.revolgenx.anilib.databinding.MediaCharacterFragmentLayoutBinding
import com.revolgenx.anilib.media.data.meta.MediaInfoMeta
import com.revolgenx.anilib.media.presenter.MediaCharacterPresenter
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.media.viewmodel.MediaCharacterVM
import com.revolgenx.anilib.ui.view.makeArrayPopupMenu
import com.revolgenx.anilib.ui.view.makeSpinnerAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaCharacterFragment : BasePresenterFragment<CharacterEdgeModel>() {

    override val basePresenter: Presenter<CharacterEdgeModel>
        get() = MediaCharacterPresenter(requireContext())

    override val baseSource: Source<CharacterEdgeModel>
        get() = viewModel.source ?: createSource()

    private val mediaBrowserMeta
        get() = arguments?.getParcelable<MediaInfoMeta?>(MEDIA_INFO_META_KEY)

    private val isAnime get() = mediaBrowserMeta?.type == MediaType.ANIME.ordinal
    private val displayMode get() = mediaCharacterDisplayModePref

    private val viewModel by viewModel<MediaCharacterVM>()

    private var _sBinding: MediaCharacterFragmentLayoutBinding? = null
    private val sBinding: MediaCharacterFragmentLayoutBinding get() = _sBinding!!


    companion object {
        private const val MEDIA_INFO_META_KEY = "MEDIA_INFO_META_KEY"
        fun newInstance(meta: MediaInfoMeta) = MediaCharacterFragment().also {
            it.arguments = bundleOf(MEDIA_INFO_META_KEY to meta)
        }
    }

    override fun createSource(): Source<CharacterEdgeModel> {
        return viewModel.createSource()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        _sBinding = MediaCharacterFragmentLayoutBinding.inflate(inflater, container, false)
        if (mediaBrowserMeta?.type == MediaType.MANGA.ordinal) {
            sBinding.mediaCharacterLanguageSpinnerLayout.visibility = View.GONE
        }
        sBinding.root.addView(v)
        return sBinding.root
    }

    override fun getSpanCount(): Int {
        val isLandScape = requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        return when (MediaCharacterDisplayMode.values()[displayMode]) {
            MediaCharacterDisplayMode.COMPACT -> {
                if (isAnime) {
                    if (isLandScape) 4 else 2
                } else {
                    if (isLandScape) 6 else 3
                }
            }
            MediaCharacterDisplayMode.NORMAL -> {
                if (isAnime) {
                    if (isLandScape) 2 else 1
                } else {
                    if (isLandScape) 4 else 2
                }
            }
        }
    }

    override fun getItemSpanSize(position: Int): Int {
        return if (adapter?.elementAt(position)?.element!!.let {
                it.type == MediaType.ANIME.ordinal || it.type == MediaType.MANGA.ordinal
            }) {
            1
        } else {
            span
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mediaBrowserMeta ?: return

        val spinnerItems = mutableListOf<DynamicMenu>()
        requireContext().resources.getStringArray(R.array.staff_language).forEach {
            spinnerItems.add(DynamicMenu(null, it))
        }

        viewModel.field.also {
            it.mediaId = mediaBrowserMeta!!.mediaId
            it.type = mediaBrowserMeta!!.type
        }

        if (mediaBrowserMeta?.type != MediaType.MANGA.ordinal) {
            sBinding.mediaCharacterLanguageSpinner.adapter = makeSpinnerAdapter(requireContext(), spinnerItems)

            viewModel.field.language?.let {
                sBinding.mediaCharacterLanguageSpinner.setSelection(it)
            }

            sBinding.mediaCharacterLanguageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if ((viewModel.field.language != position) && visibleToUser) {
                        viewModel.field.language = position
                        createSource()
                        invalidateAdapter()
                    }
                }
            }
        }

        sBinding.mediaCharacterPopupMenu.onPopupMenuClickListener = { _, position ->
            when (position) {
                0 -> {
                    makeArrayPopupMenu(
                        sBinding.mediaCharacterPopupMenu,
                        resources.getStringArray(R.array.media_character_display_modes),
                        selectedPosition = mediaCharacterDisplayModePref
                    ) { _, _, index, _ ->
                        mediaCharacterDisplayModePref = index
                        loadLayoutManager()
                        invalidateAdapter()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        sBinding.mediaCharacterLanguageSpinner.onItemSelectedListener = null
        _sBinding = null
        super.onDestroyView()
    }

}

