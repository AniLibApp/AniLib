package com.revolgenx.anilib.user.ui.viewmodel.userStats

import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.user.data.field.UserStatsTypeField
import com.revolgenx.anilib.user.data.service.UserService
import com.revolgenx.anilib.user.ui.model.statistics.BaseStatisticModel
import kotlinx.coroutines.flow.Flow

abstract class UserStatsTypeViewModel(
    mediaType: MediaType,
    val userStatsType: UserStatsType,
    private val userService: UserService
) : ResourceViewModel<List<BaseStatisticModel>, UserStatsTypeField>() {
    override val field: UserStatsTypeField = UserStatsTypeField(mediaType, userStatsType)
    override fun load(): Flow<List<BaseStatisticModel>?> {
        return userService.getUserStats(field)
    }
}
class UserStatsGenreViewModel(mediaType: MediaType, userService: UserService) :
    UserStatsTypeViewModel(mediaType, UserStatsType.GENRE, userService)
class UserStatsTagsViewModel(mediaType: MediaType, userService: UserService) :
    UserStatsTypeViewModel(mediaType, UserStatsType.TAGS, userService)
class UserStatsVoiceActorsViewModel(userService: UserService) :
    UserStatsTypeViewModel(MediaType.ANIME, UserStatsType.VOICE_ACTORS, userService)
class UserStatsStudioViewModel(userService: UserService) :
    UserStatsTypeViewModel(MediaType.ANIME, UserStatsType.STUDIO, userService)
class UserStatsStaffViewModel(mediaType: MediaType, userService: UserService) :
    UserStatsTypeViewModel(mediaType, UserStatsType.STAFF, userService)
