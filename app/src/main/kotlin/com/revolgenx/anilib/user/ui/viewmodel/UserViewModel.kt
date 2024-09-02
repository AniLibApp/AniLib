package com.revolgenx.anilib.user.ui.viewmodel

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.user.data.field.UserField
import com.revolgenx.anilib.user.data.field.UserToggleFollowField
import com.revolgenx.anilib.user.data.service.UserService
import com.revolgenx.anilib.user.ui.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import anilib.i18n.R as I18nR


private typealias UserScreenPage = PagerScreen<UserScreenPageType>


enum class UserScreenPageType {
    OVERVIEW,
    ACTIVITY,
    FAVOURITES,
    ANIME_STATS,
    MANGA_STATS
}

class UserViewModel(
    private val userService: UserService,
    val appPreferencesDataStore: AppPreferencesDataStore
) :
    ResourceViewModel<UserModel, UserField>() {
    override val field: UserField = UserField()

    var showToggleUserFollowErrorMsg by mutableStateOf(false)

    private var loggedInUserId = appPreferencesDataStore.userId.get()
    val showAbout = appPreferencesDataStore.showUserAbout.get()!!

    val isLoggedInUser = derivedStateOf {
        userId.value == loggedInUserId
    }

    val userId = mutableStateOf<Int?>(null)

    val pages = listOf(
        UserScreenPage(UserScreenPageType.OVERVIEW, I18nR.string.overview),
        UserScreenPage(
            UserScreenPageType.ACTIVITY, I18nR.string.activity,
            isVisible = mutableStateOf(false)
        ),
        UserScreenPage(
            UserScreenPageType.FAVOURITES,
            I18nR.string.favourites,
            isVisible = mutableStateOf(false)
        ),
        UserScreenPage(
            UserScreenPageType.ANIME_STATS,
            I18nR.string.anime_stats,
            isVisible = mutableStateOf(false)
        ),
        UserScreenPage(
            UserScreenPageType.MANGA_STATS,
            I18nR.string.manga_stats,
            isVisible = mutableStateOf(false)
        )
    )

    fun showAllPages() {
        pages.forEach { it.isVisible.value = true }
    }

    override fun load(): Flow<UserModel?> = userService.getUser(field)

    override fun onComplete() {
        userId.value = getData()?.id
    }

    fun toggleFollow(userModel: UserModel) {
        val isFollowing = userModel.isFollowing
        userModel.isFollowing = !isFollowing
        userModel.isFollowingState.value = !isFollowing
        val toggleFollowField = UserToggleFollowField(userModel.id)
        userService.toggleFollow(toggleFollowField)
            .onEach {success->
                if(!success){
                    showToggleUserFollowErrorMsg = true
                    userModel.isFollowing = isFollowing
                    userModel.isFollowingState.value = isFollowing
                }
            }.launchIn(viewModelScope)
    }

}