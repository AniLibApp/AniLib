package com.revolgenx.anilib.user.ui.viewmodel.userStats

import androidx.compose.runtime.mutableIntStateOf
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.user.data.field.UserStatsOverviewField
import com.revolgenx.anilib.user.data.service.UserService
import com.revolgenx.anilib.user.ui.model.UserModel
import kotlinx.coroutines.flow.Flow

class UserStatsOverviewViewModel(mediaType: MediaType, private val userService: UserService) :
    ResourceViewModel<UserModel, UserStatsOverviewField>() {
    val statsScoreType = mutableIntStateOf(0)
    val statsLengthType = mutableIntStateOf(0)
    val statsReleaseYearType = mutableIntStateOf(0)
    val statsStartYearType = mutableIntStateOf(0)

    override val field: UserStatsOverviewField = UserStatsOverviewField(mediaType)
    override fun load(): Flow<UserModel?> {
        return userService.getUserStatsOverview(field)
    }
}