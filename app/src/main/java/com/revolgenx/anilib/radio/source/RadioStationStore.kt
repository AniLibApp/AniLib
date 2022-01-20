package com.revolgenx.anilib.radio.source

import androidx.lifecycle.MutableLiveData
import com.revolgenx.anilib.radio.repository.api.ErrorResponse
import com.revolgenx.anilib.radio.repository.api.RadioGithubApi
import com.revolgenx.anilib.radio.repository.api.ResourceWrapper
import com.revolgenx.anilib.radio.repository.room.RadioChannel
import com.revolgenx.anilib.radio.repository.room.RadioStation
import com.revolgenx.anilib.radio.repository.room.RadioStationDao
import kotlinx.coroutines.*
import java.util.*

abstract class RadioStationStore(
    private val radioStationRepo: RadioStationDao,
    private val radioGithubApi: RadioGithubApi
) :
    TreeMap<Long, RadioStation>() {
    protected val serviceJob = SupervisorJob()
    protected val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    var storeStateCallback: MutableLiveData<ResourceWrapper<List<RadioStation>>> = MutableLiveData()

    protected abstract fun onRadioStationLoaded()

    protected suspend fun updateRadioStation(station: RadioStation) {
        withContext(Dispatchers.IO) {
            radioStationRepo.update(station)
        }
    }

    protected suspend fun updateAccessedOn(
        station: RadioStation,
        removeAccessedOn: Boolean = false
    ) {
        withContext(Dispatchers.IO) {
            station.accessedOn = if (removeAccessedOn) null else Date()
            radioStationRepo.update(station)
        }
    }


    open fun loadAllStation(update: Boolean = false) {
        storeStateCallback.value = ResourceWrapper.Loading
        serviceScope.launch {

            val radioStore = this@RadioStationStore
            try {
                withContext(Dispatchers.IO) {
                    val storedStation: List<RadioStation> = radioStationRepo.get()

                    if (storedStation.isNotEmpty() && !update) {
                        storedStation.forEach { radioStore[it.id] = it }
                        onRadioStationLoaded()
                    }

                    if (update || storedStation.isEmpty()) {
                        loadFromApi()
                    }
                }
                storeStateCallback.value = ResourceWrapper.Success(radioStore.values.toList())
            } catch (ex: Exception) {
                storeStateCallback.value = ResourceWrapper.UnknownError(ErrorResponse(ex.message))
            }
        }
    }

    private suspend fun loadFromApi() {
        when (val allRadioStations = radioGithubApi.getAllRadioStations()) {
            is ResourceWrapper.Success -> {
                val radioStore = this@RadioStationStore
                val currentFavouriteStation =
                    radioStore.values.filter { it.isFavourite }.map { it.id }

                val toStoreStation = allRadioStations.value.map { radioStation ->
                    RadioStation(
                        radioStation.id,
                        radioStation.name,
                        radioStation.site,
                        radioStation.logo,
                        isFavourite = currentFavouriteStation.contains(radioStation.id),
                        backgroundColor = null,
                        radioStation.channel.map {
                            RadioChannel(
                                stream = it.stream,
                                bitrate = it.bitrate,
                                default = it.default
                            )
                        }
                    )
                }

                if (toStoreStation.isNotEmpty()) {
                    radioStationRepo.delete()
                    radioStationRepo.add(toStoreStation)
                    radioStore.clear()
                    toStoreStation.forEach { radioStore[it.id] = it }
                    onRadioStationLoaded()
                }
            }
            else -> {
                throw Exception("Error Loading from Api")
            }
        }
    }


}