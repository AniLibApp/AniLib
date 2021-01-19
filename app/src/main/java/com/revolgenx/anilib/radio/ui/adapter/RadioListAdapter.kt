package com.revolgenx.anilib.radio.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.facebook.drawee.generic.RoundingParams
import com.pranavpandey.android.dynamic.support.theme.DynamicTheme
import com.revolgenx.anilib.databinding.AllRadioStationPresenterLayoutBinding
import com.revolgenx.anilib.radio.data.PlaybackState
import com.revolgenx.anilib.radio.data.events.FavouriteEvent
import com.revolgenx.anilib.radio.repository.room.RadioStation
import com.revolgenx.anilib.radio.ui.util.RadioPlayerHelper
import com.revolgenx.anilib.util.openLink

class RadioListAdapter(private val context: Context) :
    RecyclerView.Adapter<RadioListAdapter.RadioListViewHolder>() {

    private var radioList: List<RadioStation>? = null

    inner class RadioListViewHolder(val binding: AllRadioStationPresenterLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RadioListViewHolder {
        return RadioListViewHolder(
            AllRadioStationPresenterLayoutBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RadioListViewHolder, position: Int) {
        if (radioList.isNullOrEmpty()) {
            return
        }

        val station = radioList!![position]
        val binding = holder.binding
        binding.radioStationFavourite.setOnCheckedChangeListener(null)

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

        binding.radioStationMore.onPopupMenuClickListener = { _, pos ->
            when (pos) {
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

    override fun getItemCount(): Int {
        return radioList?.size ?: 0
    }

    fun submitList(it: List<RadioStation>?) {
        radioList = it
        notifyDataSetChanged()
    }

    fun onRadioItemChanged(radioId:Long){
        if(radioList == null) return
        radioList!!.indexOfFirst { radioId == it.id }.takeIf {  it != -1 }?.let {
            notifyItemChanged(it)
        }
    }
}