package com.revolgenx.anilib.user.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.data.store.AuthDataStore
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.user.data.field.UserField
import com.revolgenx.anilib.user.data.service.UserService
import com.revolgenx.anilib.user.ui.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
    val authDataStore: AuthDataStore
) :
    ResourceViewModel<UserModel, UserField>() {
    override val field: UserField = UserField()

    val isLoggedInUser = authDataStore.userId.map().map { it == field.userId }

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
}