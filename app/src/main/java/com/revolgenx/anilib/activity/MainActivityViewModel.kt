package com.revolgenx.anilib.activity

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.common.data.state.UserState
import com.revolgenx.anilib.common.data.store.AppDataStore
import com.revolgenx.anilib.common.data.store.userId
import com.revolgenx.anilib.common.ext.launch

class MainActivityViewModel(appDataStore: AppDataStore) : ViewModel() {
    var userState by mutableStateOf(UserState())
    init {
        launch {
            appDataStore.userId().collect{
                userState = userState.copy(userId = it)
            }
        }
    }
}