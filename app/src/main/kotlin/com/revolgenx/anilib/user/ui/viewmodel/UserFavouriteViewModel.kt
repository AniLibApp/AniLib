package com.revolgenx.anilib.user.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.user.data.field.UserFavouriteType

class UserFavouriteViewModel : ViewModel() {
    var favouriteType = mutableStateOf(UserFavouriteType.FAVOURITE_ANIME)
}