package com.revolgenx.anilib.radio.data

import com.revolgenx.anilib.radio.repository.room.RadioStation
import com.revolgenx.anilib.radio.repository.room.updatePlayState

sealed class PlaybackState {
    data class RadioPlayState(val station: RadioStation) : PlaybackState() {
        init {
            station.playbackState = this
            station.updatePlayState()
        }
    }

    data class RadioStopState(val station: RadioStation?) : PlaybackState() {
        init {
            station?.playbackState = this
            station?.updatePlayState()
        }
    }

    data class RadioBufferingState(val station: RadioStation) : PlaybackState() {
        init {
            station.playbackState = this
            station.updatePlayState()
        }
    }

}
