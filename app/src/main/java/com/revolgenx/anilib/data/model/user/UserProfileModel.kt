package com.revolgenx.anilib.data.model.user

import com.revolgenx.anilib.data.model.UserPrefModel
import com.revolgenx.anilib.data.model.markwon.MarkdownModel

class UserProfileModel : UserPrefModel() {
    var about: MarkdownModel? = null
    var isFollowing: Boolean? = null
    var isFollower: Boolean? = null
    var isBlocked: Boolean? = null
    var totalAnime: Int? = null
    var totalManga: Int? = null
    var episodesWatched: Int? = null
    var volumeRead: Int? = null
    var chaptersRead: Int? = null
    var daysWatched: Double? = null
    var animeMeanScore: Double? = null
    var mangaMeanScore: Double? = null
    var genreOverView: MutableMap<String, Int> = mutableMapOf()
    var siteUrl: String? = null
}
