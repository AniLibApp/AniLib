package com.revolgenx.anilib.model.user

import com.revolgenx.anilib.model.BasicUserModel

class UserProfileModel : BasicUserModel() {
    var about: String? = null
    var isFollowing: Boolean? = null
    var isFollower: Boolean? = null
    var isBlocked: Boolean? = null
    var totalAnime: Int? = null
    var totalManga: Int? = null
    var chaptersRead: Int? = null
    var daysWatched: Double? = null
    var animeMeanScore: Double? = null
    var mangaMeanScore: Double? = null
    var genreOverView: MutableMap<String, Int> = mutableMapOf()
    var siteUrl: String? = null
}
