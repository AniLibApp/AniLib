package com.revolgenx.anilib.ui.fragment.browse

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.model.DynamicMenu
import com.revolgenx.anilib.ui.view.widgets.AlCardView
import com.pranavpandey.android.dynamic.support.widget.DynamicLinearLayout
import com.pranavpandey.android.dynamic.support.widget.DynamicSpinner
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BasePresenterFragment
import com.revolgenx.anilib.data.meta.MediaInfoMeta
import com.revolgenx.anilib.data.model.media_info.MediaCharacterModel
import com.revolgenx.anilib.ui.presenter.media.MediaCharacterPresenter
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.util.dp
import com.revolgenx.anilib.ui.viewmodel.media.MediaCharacterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaCharacterFragment : BasePresenterFragment<MediaCharacterModel>() {

    override val basePresenter: Presenter<MediaCharacterModel>
        get() {
            return MediaCharacterPresenter(
                requireContext()
            )
        }

    override val baseSource: Source<MediaCharacterModel>
        get() {
            return viewModel.source ?: createSource()
        }

    private val mediaBrowserMeta get() = arguments?.getParcelable<MediaInfoMeta?>(MEDIA_INFO_META_KEY)
    private val viewModel by viewModel<MediaCharacterViewModel>()

    private lateinit var languageSpinner: DynamicSpinner


    companion object {
        private const val MEDIA_INFO_META_KEY = "MEDIA_INFO_META_KEY"
        fun newInstance(meta: MediaInfoMeta) = MediaCharacterFragment().also {
            it.arguments = bundleOf(MEDIA_INFO_META_KEY to meta)
        }
    }

    override fun createSource(): Source<MediaCharacterModel> {
        return viewModel.createSource()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)

        if (mediaBrowserMeta?.type != MediaType.MANGA.ordinal) {
            languageSpinner = DynamicSpinner(requireContext()).also {
                it.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            val languageContainer = AlCardView(requireContext()).also {
                it.layoutParams = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).also { params ->
                    params.setMargins(dp(6f), dp(10f), dp(6f), dp(10f))
                }
                it.addView(languageSpinner)
            }
            return DynamicLinearLayout(requireContext()).also {
                it.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                it.orientation = LinearLayout.VERTICAL
                it.addView(languageContainer)
                it.addView(v)
            }
        }
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mediaBrowserMeta ?: return
        val span: Int = when (mediaBrowserMeta!!.type) {
            MediaType.ANIME.ordinal -> {
                if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 4 else 2
            }
            else -> {
                if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 6 else 3
            }
        }

        layoutManager =
            GridLayoutManager(
                this.context,
                span
            ).also {
                it.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adapter?.elementAt(position)?.element!!.let {
                                it.type == MediaType.ANIME.ordinal || it.type == MediaType.MANGA.ordinal
                            }) {
                            1
                        } else {
                            span
                        }
                    }
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val spinnerItems = mutableListOf<DynamicMenu>()
        requireContext().resources.getStringArray(R.array.staff_language).forEach {
            spinnerItems.add(DynamicMenu(null, it))
        }

        viewModel.field.also {
            it.mediaId = mediaBrowserMeta!!.mediaId
            it.type = mediaBrowserMeta!!.type
        }

        if (mediaBrowserMeta?.type != MediaType.MANGA.ordinal) {
            languageSpinner.adapter = DynamicSpinnerImageAdapter(
                requireContext(),
                R.layout.ads_layout_spinner_item,
                R.id.ads_spinner_item_icon,
                R.id.ads_spinner_item_text, spinnerItems
            )

            viewModel.field.language?.let {
                languageSpinner.setSelection(it)
            }

            languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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

    }

    override fun onDestroyView() {
        if (::languageSpinner.isInitialized) {
            languageSpinner.onItemSelectedListener = null
        }
        super.onDestroyView()
    }

}

