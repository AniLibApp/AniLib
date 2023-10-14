package com.revolgenx.anilib.user.ui.model

import android.text.Spanned
import com.revolgenx.anilib.UserQuery
import com.revolgenx.anilib.common.ext.nullIfEmpty
import com.revolgenx.anilib.common.ui.model.BaseModel
import com.revolgenx.anilib.common.ui.model.DateModel
import com.revolgenx.anilib.fragment.UserRelation
import com.revolgenx.anilib.social.factory.markdown
import com.revolgenx.anilib.social.markdown.anilify
import com.revolgenx.anilib.user.ui.model.statistics.UserGenreStatisticModel
import com.revolgenx.anilib.user.ui.model.statistics.UserStatisticTypesModel
import com.revolgenx.anilib.user.ui.model.statistics.UserStatisticsModel
import com.revolgenx.anilib.user.ui.model.statistics.toModel
import com.revolgenx.anilib.user.ui.model.stats.UserActivityHistoryModel
import com.revolgenx.anilib.user.ui.model.stats.UserStatsModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.time.temporal.WeekFields
import java.util.Locale

data class UserModel(
    val id: Int = -1,
    val name: String? = null,
    val about: String? = null,
    val aboutSpanned: Spanned? = null,
    val avatar: UserAvatarModel? = null,
    val bannerImage: String? = null,
    val isFollowing: Boolean = false,
    val isFollower: Boolean = false,
    val isBlocked: Boolean = false,
    val options: UserOptionsModel? = null,
    val mediaListOptions: MediaListOptionModel? = null,
    val favourites: FavouritesModel? = null,
    val statistics: UserStatisticTypesModel? = null,
    val stats: UserStatsModel? = null,
    val unreadNotificationCount: Int? = null,
    val siteUrl: String? = null,

    var following: Int = 0,
    var followers: Int = 0,
) : BaseModel {
    val isMutual get() = isFollower && isFollowing
}


fun UserQuery.User.toModel(): UserModel {
    val avatar = avatar?.userAvatar?.toModel()
    val anilifiedAbout = anilify(about)

    val userActivityHistory = stats?.activityHistory?.nullIfEmpty()?.let {
        val currentDate = LocalDate.now()
        val dateWithStartOfWeek =
            currentDate.with(WeekFields.of(Locale.getDefault()).firstDayOfWeek)
        val startingDate = dateWithStartOfWeek.minusWeeks(23)
        val totalDays = ChronoUnit.DAYS.between(startingDate, currentDate)
        val activityHistories = it.map { activityHistory ->
            val newDate =
                Instant.ofEpochSecond(activityHistory!!.date!!.toLong()).atZone(ZoneOffset.UTC)
                    .toLocalDate()
            object {
                val amount = activityHistory.amount!!
                val date = newDate
                val level = activityHistory.level!!
            }
        }

        val newActivityHistories = mutableListOf<UserActivityHistoryModel>()

        for (i in 0 until totalDays) {
            val activityHistory = activityHistories.find { it.date == startingDate.plusDays(i) }
            val userActivityHistory = if (activityHistory == null) {
                UserActivityHistoryModel(
                    amount = 0,
                    date = DateModel(localDate = startingDate),
                    level = 0,
                    alpha = 0f
                )
            } else {
                UserActivityHistoryModel(
                    amount = activityHistory.amount,
                    date = DateModel(localDate = startingDate),
                    level = activityHistory.level,
                    alpha = activityHistory.level / 10f
                )
            }
            newActivityHistories.add(userActivityHistory)
        }
        newActivityHistories
    }


    return UserModel(
        id = id,
        name = name,
        avatar = avatar,
        bannerImage = bannerImage ?: avatar?.image,
        isBlocked = isBlocked ?: false,
        isFollower = isFollower ?: false,
        isFollowing = isFollowing ?: false,
        about = about ?: "",
        aboutSpanned = markdown.toMarkdown(anilifiedAbout),
        siteUrl = siteUrl,
        stats = stats?.let {
            UserStatsModel(
                activityHistory = userActivityHistory
            )
        },
        statistics = statistics?.let { stats ->
            val animeStats = stats.anime?.userMediaStatistics?.toModel()
            val mangaStats = stats.manga?.userMediaStatistics?.toModel()
            val mediaGenres = animeStats?.genres?.toMutableList()?.let { mediaGenres ->
                mangaStats?.genres?.let { m ->
                    mediaGenres.addAll(m)
                }
                mediaGenres
            }

            val mediaStats =
                mediaGenres?.fold(mutableListOf<UserGenreStatisticModel>()) { acc, userGenreStatisticModel ->
                    acc.also {
                        it.find { it.genre == userGenreStatisticModel.genre }
                            ?.let { it.count = it.count + userGenreStatisticModel.count } ?: it.add(
                            userGenreStatisticModel
                        )
                    }
                }?.sortedByDescending { it.count }?.let {
                    UserStatisticsModel(
                        genres = it
                    )
                }
            UserStatisticTypesModel(
                anime = animeStats,
                manga = mangaStats,
                media = mediaStats
            )
        }
    )
}

fun UserRelation.toModel(): UserModel {
    val avatar = avatar?.userAvatar?.toModel()
    return UserModel(
        id = id,
        name = name,
        avatar = avatar,
        isFollower = isFollower ?: false,
        isFollowing = isFollowing ?: false
    )
}