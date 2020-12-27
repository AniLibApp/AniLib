package com.revolgenx.anilib.radio.repository.api

import com.revolgenx.anilib.BuildConfig
import kotlinx.coroutines.Dispatchers

class RadioGithubApi {
    suspend fun getAllRadioStations(): ResourceWrapper<List<RadioStation>> {
        val service = RadioGithubService.create()
        return requestApi(Dispatchers.IO) {
            if(BuildConfig.DEBUG){
                service.getDev().filter { it.isObsolete == null || it.isObsolete == false }
            }else{
                service.getRepo().filter { it.isObsolete == null || it.isObsolete == false }
            }
        }
    }
}

