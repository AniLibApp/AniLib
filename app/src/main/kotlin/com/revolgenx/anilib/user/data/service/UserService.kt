package com.revolgenx.anilib.user.data.service

import com.revolgenx.anilib.common.data.model.PageModel
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.relation.data.field.UserRelationField
import com.revolgenx.anilib.user.data.field.UserFavouriteField
import com.revolgenx.anilib.user.data.field.UserField
import com.revolgenx.anilib.user.data.field.UserSettingsField
import com.revolgenx.anilib.user.data.field.UserSocialCountField
import com.revolgenx.anilib.user.data.field.UserStatsOverviewField
import com.revolgenx.anilib.user.data.field.UserStatsTypeField
import com.revolgenx.anilib.user.data.field.UserToggleFollowField
import com.revolgenx.anilib.user.ui.model.UserModel
import com.revolgenx.anilib.user.ui.model.UserSocialCountModel
import com.revolgenx.anilib.user.ui.model.statistics.BaseStatisticModel
import kotlinx.coroutines.flow.Flow

interface UserService {
    fun getUser(field: UserField): Flow<UserModel?>
    fun getUserSocialCount(field: UserSocialCountField): Flow<UserSocialCountModel>
    fun getUserRelation(field: UserRelationField): Flow<PageModel<UserModel>>
    fun getUserFavourite(field: UserFavouriteField): Flow<PageModel<BaseModel>>
    fun getUserStatsOverview(field: UserStatsOverviewField): Flow<UserModel?>
    fun getUserStats(field: UserStatsTypeField): Flow<List<BaseStatisticModel>>
    fun toggleFollow(field: UserToggleFollowField): Flow<Boolean>
    fun getUserSettings(field: UserSettingsField): Flow<UserModel?>
}