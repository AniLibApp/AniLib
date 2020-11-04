package com.revolgenx.anilib.ui.fragment.settings


import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.otaliastudios.elements.Adapter
import com.otaliastudios.elements.Presenter
import com.otaliastudios.elements.Source
import com.revolgenx.anilib.R
import com.revolgenx.anilib.constant.MediaTagFilterTypes
import com.revolgenx.anilib.ui.dialog.TagFilterSettingDialogFragment
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.data.meta.TagFilterMetaType
import com.revolgenx.anilib.data.meta.TagFilterSettingMeta
import kotlinx.android.synthetic.main.customize_filter_adapter_fragment_layout.view.*
import kotlinx.android.synthetic.main.customize_filter_fragment_layout.*

class CustomizeFilterFragment : BaseLayoutFragment() {
    override val layoutRes: Int = R.layout.customize_filter_fragment_layout
    override var titleRes: Int? = R.string.custom_filters
    override var setHomeAsUp: Boolean = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customizeFilterRecyclerView.layoutManager = GridLayoutManager(
            this.context,
            if (requireContext().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 6 else 3
        )
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val adapter = Adapter.builder(this)
            .addSource(
                Source.fromList(
                    listOf(
                        MediaTagFilterTypes.TAGS to
                                R.drawable.ic_tag,
                        MediaTagFilterTypes.GENRES to
                                R.drawable.ic_genre,
                        MediaTagFilterTypes.STREAMING_ON to
                                R.drawable.ic_watch
                    )
                )
            ).addPresenter(
                Presenter.simple<Pair<MediaTagFilterTypes, Int>>(
                    requireContext(),
                    R.layout.customize_filter_adapter_fragment_layout,
                    0
                ) { v, p ->
                    with(v) {
                        this.filterIv.setImageResource(p.second)
                        val dialogFragment = when(p.first){
                            MediaTagFilterTypes.GENRES -> {
                                this.filterName.text = getString(R.string.genre)
                                    TagFilterSettingDialogFragment.newInstance(TagFilterSettingMeta(TagFilterMetaType.GENRE))
                            }
                            MediaTagFilterTypes.TAGS ->{
                                this.filterName.text = getString(R.string.tags)
                                    TagFilterSettingDialogFragment.newInstance(TagFilterSettingMeta(TagFilterMetaType.TAG))
                            }
                            MediaTagFilterTypes.STREAMING_ON -> {
                                this.filterName.text = getString(R.string.streaming_on)
                                    TagFilterSettingDialogFragment.newInstance(TagFilterSettingMeta(TagFilterMetaType.STREAMING_ON))
                            }
                            else->{
                                TagFilterSettingDialogFragment.newInstance(TagFilterSettingMeta(TagFilterMetaType.TAG))
                            }
                        }
                        this.setOnClickListener {
                            dialogFragment.show(childFragmentManager, TagFilterSettingDialogFragment::class.java.simpleName)
                        }
                    }
                }).build()

        customizeFilterRecyclerView.adapter = adapter;
    }
}