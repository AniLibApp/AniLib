package com.revolgenx.anilib.radio.ui.presenter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import com.otaliastudios.elements.Element
import com.otaliastudios.elements.Page
import com.otaliastudios.elements.Presenter
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.databinding.FavouriteRadioStationPresenterBinding
import com.revolgenx.anilib.radio.data.PlaybackState
import com.revolgenx.anilib.radio.data.events.FavouriteEvent
import com.revolgenx.anilib.radio.repository.room.RadioStation
import com.revolgenx.anilib.radio.ui.util.RadioPlayerHelper
import com.revolgenx.anilib.ui.presenter.Constant

class FavouriteRadioStationPresenter(context: Context) : Presenter<RadioStation>(context) {
    override val elementTypes: Collection<Int> = listOf(0)

    private val backgroundColor = DynamicTheme.getInstance().get().backgroundColor

    override fun onCreate(parent: ViewGroup, elementType: Int): Holder {
        return FavouriteRadioStationPresenterBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        ).let { binding ->
            Holder(binding.root).also { it[Constant.PRESENTER_BINDING_KEY] = binding }
        }
    }

    override fun onBind(page: Page, holder: Holder, element: Element<RadioStation>) {
        super.onBind(page, holder, element)
        val station = element.data ?: return
        val binding =
            holder.get<FavouriteRadioStationPresenterBinding>(Constant.PRESENTER_BINDING_KEY)

        binding.radioStationNameTv.text = station.name
        binding.radioStationIv.setImageURI(station.logo)

        val cardBackgroundColor = station.backgroundColor?.let {
            Color.parseColor(
                it
            )
        } ?: backgroundColor

        binding.favouriteStationCardView.setBackgroundColor(cardBackgroundColor)
        binding.radioStationNameTv.contrastWithColor = cardBackgroundColor
        binding.favouriteMoreIv.contrastWithColor = cardBackgroundColor


        binding.root.setOnClickListener {
            RadioPlayerHelper.pausePlay(context, station.id)
        }

        binding.favouriteMoreIv.onPopupMenuClickListener = { _, post ->
            if (post == 0) {
                station.isFavourite = false
                FavouriteEvent(station.id, station.isFavourite).postEvent
            }
        }


        when (station.playbackState) {
            is PlaybackState.RadioPlayState -> {

            }
            else -> {

            }
        }
    }

}