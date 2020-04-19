package com.revolgenx.anilib.fragment.browse

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.pranavpandey.android.dynamic.support.adapter.DynamicSpinnerImageAdapter
import com.pranavpandey.android.dynamic.support.model.DynamicSpinnerItem
import com.pranavpandey.android.dynamic.support.widget.*
import com.revolgenx.anilib.R
import com.revolgenx.anilib.activity.MediaBrowseActivity
import com.revolgenx.anilib.meta.MediaBrowserMeta
import com.revolgenx.anilib.field.media.MediaCharacterField
import com.revolgenx.anilib.fragment.base.BasePresenterFragment
import com.revolgenx.anilib.model.MediaCharacterModel
import com.revolgenx.anilib.presenter.MediaCharacterPresenter
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.util.dp
import com.revolgenx.anilib.viewmodel.MediaCharacterViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MediaCharacterFragment : BasePresenterFragment<MediaCharacterModel>() {

    override val basePresenter: Presenter<MediaCharacterModel>
        get() {
            return MediaCharacterPresenter(requireContext())
        }

    override val baseSource: Source<MediaCharacterModel>
        get() {
            return viewModel.source ?: createSource()
        }

    private var mediaBrowserMeta: MediaBrowserMeta? = null
    private val viewModel by viewModel<MediaCharacterViewModel>()
    private val field by lazy {
        MediaCharacterField().also {
            it.mediaId = mediaBrowserMeta!!.mediaId
            it.type = mediaBrowserMeta!!.type
        }
    }

    private lateinit var languageSpinner: DynamicSpinner

    override fun createSource(): Source<MediaCharacterModel> {
        return viewModel.createSource(field)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = super.onCreateView(inflater, container, savedInstanceState)

        languageSpinner = DynamicSpinner(requireContext()).also {
            it.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val languageContainer = DynamicCardView(requireContext()).also {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mediaBrowserMeta =
            arguments?.getParcelable(MediaBrowseActivity.MEDIA_BROWSER_META) ?: return
        val span:Int = when (mediaBrowserMeta!!.type) {
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
        val spinnerItems = mutableListOf<DynamicSpinnerItem>()
        requireContext().resources.getStringArray(R.array.staff_language).forEach {
            spinnerItems.add(DynamicSpinnerItem(null, it))
        }

        languageSpinner.adapter = DynamicSpinnerImageAdapter(
            requireContext(),
            R.layout.ads_layout_spinner_item,
            R.id.ads_spinner_item_icon,
            R.id.ads_spinner_item_text, spinnerItems
        )

        field.language?.let {
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
                if (field.language != position) {
                    field.language = position
                    createSource()
                    invalidateAdapter()
                }
            }
        }
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroy() {
        if (::languageSpinner.isInitialized) {
            languageSpinner.onItemSelectedListener = null
        }
        super.onDestroy()
    }

}

