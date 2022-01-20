package com.revolgenx.anilib.radio.source

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.radio.repository.room.RadioStation
import com.revolgenx.anilib.radio.repository.room.getDefaultStream
import com.revolgenx.anilib.radio.data.PlaybackState
import com.revolgenx.anilib.radio.repository.api.RadioGithubApi
import com.revolgenx.anilib.radio.repository.room.RadioStationDao
import kotlinx.coroutines.launch
import java.util.*


class RadioStationSource(radioStationDao: RadioStationDao, radioGithubApi: RadioGithubApi) :
    RadioStationStore(radioStationDao, radioGithubApi) {

    private val getAsNavigableMap get() = this as NavigableMap<Long, RadioStation>

    //use to update all list
    val radioStations = MutableLiveData<List<RadioStation>>()

    //use to update favourite list
    val favouriteRadioStations = MutableLiveData<List<RadioStation>>()

    //use to update recent list
    val recentRadioStations = MutableLiveData<List<RadioStation>>()

    //use only for info update. don't update player with this
    var currentRadioStation = MutableLiveData<RadioStation>()

    //update play/pause state with this
    val playbackState = MutableLiveData<PlaybackState>()

    var currentStationChannelChangeListener: (() -> Unit)? = null

    fun setFavourite(id: Long, favourite: Boolean) {
        val station = this[id]!!
        station.isFavourite = favourite
        serviceScope.launch {
            updateRadioStation(station)
            onFavouriteStationChanged()
        }
    }

    fun updateDefaultChannel(station: RadioStation) {
        this[station.id] = station
        serviceScope.launch {
            updateRadioStation(station)
            if (station.id == currentRadioStation.value?.id) {
                currentStationChannelChangeListener?.invoke()
            }
        }
    }

    fun setAccessedOn(radioStation: RadioStation) {
        serviceScope.launch {
            updateAccessedOn(radioStation)
        }
    }

    fun removeAccessedOn(id: Long) {
        val currentStation = this[id] ?: return
        serviceScope.launch {
            updateAccessedOn(currentStation, true)
        }
    }

    fun getRecentRadioStation(): RadioStation? {
        return recentRadioStations.value?.firstOrNull() ?: getAsNavigableMap.firstEntry()?.value
    }

    private fun onRecentStationChanged() {
        recentRadioStations.postValue(this.values.filter { it.accessedOn != null }
            .sortedByDescending { it.accessedOn!! })
    }

    private fun onFavouriteStationChanged() {
        favouriteRadioStations.postValue(this.values.filter { it.isFavourite })
    }

    private fun onRadioStationsChanged() {
        radioStations.postValue(this.values.toList())
    }

    val radioChildrenType = MutableLiveData<RadioChildrenType>().also {
        it.value = RadioChildrenType.ALL
    }

    fun setCurrentRadioStation(id: Long) {
        val currentStation = this[id] ?: return
        currentRadioStation.value = currentStation
        setAccessedOn(currentStation)
    }

    fun gotToNextStation() {
        val currentStation = currentRadioStation.value ?: return
        currentRadioStation.value =
            (getAsNavigableMap.higherEntry(currentStation.id)?.value ?: firstEntry()?.value)
                ?: return
    }

    fun goToPreviousStation() {
        val currentStation = currentRadioStation.value ?: return
        currentRadioStation.value =
            (getAsNavigableMap.lowerEntry(currentStation.id)?.value ?: lastEntry()?.value) ?: return
    }

    fun findStation(string: String?): RadioStation? {
        val stations = radioStations.value ?: return null
        return stations.firstOrNull { it.name.contains(string ?: "", true) } ?: stations.random()
    }

    fun getRadioStation(id: Long): RadioStation? {
        return this[id]
    }

    fun getMediaItems(): List<MediaBrowserCompat.MediaItem> {
        return when (radioChildrenType.value) {
            RadioChildrenType.FAVOURITE -> {
                favouriteRadioStations.value?.map { it.mapToMediaItem() } ?: emptyList()
            }
            else -> {
                values.map { it.mapToMediaItem() }
            }
        }
    }

    private fun RadioStation.mapToMediaItem(): MediaBrowserCompat.MediaItem {
        return MediaBrowserCompat.MediaItem(
            MediaDescriptionCompat.Builder()
                .setMediaId(id.toString())
                .setTitle(name)
                .setIconUri(logo?.toUri())
                .setMediaId(getDefaultStream().stream)
                .build(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
        )
    }

    override fun onRadioStationLoaded() {
        onRadioStationsChanged()
        onFavouriteStationChanged()
        onRecentStationChanged()
    }
}

enum class RadioChildrenType {
    ALL, FAVOURITE
}
