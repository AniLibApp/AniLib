package com.revolgenx.anilib.radio.ui.presenter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.facebook.drawee.generic.RoundingParams
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.common.presenter.BasePresenter.Companion.PRESENTER_BINDING_KEY
import com.revolgenx.anilib.databinding.AllRadioStationPresenterLayoutBinding
import com.revolgenx.anilib.radio.data.PlaybackState
import com.revolgenx.anilib.radio.data.events.FavouriteEvent
import com.revolgenx.anilib.radio.repository.room.RadioStation
import com.revolgenx.anilib.radio.ui.util.RadioPlayerHelper
import com.revolgenx.anilib.util.openLink

class AllRadioStationPresenter(context: Context) : Presenter<RadioStation>(context) {
    override val elementTypes: Collection<Int> = listOf(0)
    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return AllRadioStationPresenterLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ).let { binding ->
            Holder(binding.root).also { it[PRESENTER_BINDING_KEY] = binding }
        }
    }


    override fun onBind(page: Page, holder: Holder, element: Element<RadioStation>) {
        super.onBind(page, holder, element)
        val station = element.data ?: return

        val binding: AllRadioStationPresenterLayoutBinding =
            holder[PRESENTER_BINDING_KEY] ?: return

        binding.radioStationName.text = station.name
        binding.radioStationSiteName.text = station.site
        RoundingParams.asCircle().also {
            it.overlayColor = DynamicTheme.getInstance().get().backgroundColor
            binding.radioStationIv.hierarchy.roundingParams = it
        }
        binding.radioStationIv.setImageURI(station.logo)

        binding.radioStationFavourite.isChecked = station.isFavourite

        binding.root.setOnClickListener {
            RadioPlayerHelper.pausePlay(context, station.id)
        }

        binding.radioStationFavourite.setOnCheckedChangeListener { _, isChecked ->
            station.isFavourite = isChecked
            FavouriteEvent(station.id, station.isFavourite).postEvent
        }

        binding.radioStationMore.onPopupMenuClickListener = { _, position ->
            when (position) {
                0 -> {
                    context.openLink(station.site)
                }
            }
        }

        when (station.playbackState) {
            is PlaybackState.RadioPlayState, is PlaybackState.RadioBufferingState -> {
                binding.radioPlayingIndicator.visibility = View.VISIBLE
                binding.radioStationIv.setBorder = true
            }
            else -> {
                binding.radioPlayingIndicator.visibility = View.GONE
                binding.radioStationIv.setBorder = false
            }
        }

        station.playbackStateListener = {
            when (it) {
                is PlaybackState.RadioPlayState, is PlaybackState.RadioBufferingState -> {
                    binding.radioPlayingIndicator.visibility = View.VISIBLE
                }
                else -> {
                    binding.radioPlayingIndicator.visibility = View.GONE
                }
            }
        }

    }

}