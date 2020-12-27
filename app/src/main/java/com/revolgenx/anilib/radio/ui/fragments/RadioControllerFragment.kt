package com.revolgenx.anilib.radio.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.fragment.BaseLayoutFragment
import com.revolgenx.anilib.databinding.RadioControllerFragmentBinding
import com.revolgenx.anilib.radio.data.PlaybackState
import com.revolgenx.anilib.radio.source.RadioStationSource
import com.revolgenx.anilib.radio.ui.util.RadioPlayerHelper
import org.koin.android.ext.android.inject

class RadioControllerFragment : BaseLayoutFragment<RadioControllerFragmentBinding>() {

    private val radioSource by inject<RadioStationSource>()
    private val currentRadioStation get() = radioSource.currentRadioStation
    private val recentRadioStations get() = radioSource.recentRadioStations
    private val playbackState get() = radioSource.playbackState

    override fun bindView(
        inflater: LayoutInflater,
        parent: ViewGroup?
    ): RadioControllerFragmentBinding {
        return RadioControllerFragmentBinding.inflate(inflater, parent, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.stationPlayStateLayout.setOnClickListener {
            val radioStationId =
                currentRadioStation.value?.id ?: return@setOnClickListener

            RadioPlayerHelper.pausePlay(requireContext(), radioStationId)
        }

        binding.stationPlayStateIv.setOnClickListener {
            val radioStationId =
                currentRadioStation.value?.id ?: return@setOnClickListener

            RadioPlayerHelper.pausePlay(requireContext(), radioStationId)
        }

        playbackState.observe(viewLifecycleOwner) {
            when (it) {
                is PlaybackState.RadioPlayState -> {
                    binding.stationPlayStateIv.setFinish()
                    binding.radioStationDescriptionTv.text = it.station.streamTitle
                }
                is PlaybackState.RadioStopState -> {
                    binding.stationPlayStateIv.setIdle()
                    binding.radioStationDescriptionTv.text = getString(R.string.stopped)
                }
                is PlaybackState.RadioBufferingState -> {
                    binding.stationPlayStateIv.setIndeterminate()
                    binding.radioStationDescriptionTv.text = getString(R.string.buffering)
                }
            }
        }


        currentRadioStation.observe(viewLifecycleOwner) {
            binding.radioStationNameTv.text = it.name
            binding.radioStationIv.setImageURI(it.logo)

            if (playbackState.value is PlaybackState.RadioBufferingState) {
                binding.radioStationDescriptionTv.text = getString(R.string.buffering)
            } else {
                binding.radioStationDescriptionTv.text = it.streamTitle ?: ""
            }
        }

        recentRadioStations.observe(viewLifecycleOwner) {
            if (currentRadioStation.value == null) {
                radioSource.getRecentRadioStation()?.let {
                    radioSource.setCurrentRadioStation(it.id)
                }
            }
        }
    }

}