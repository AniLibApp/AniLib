package com.revolgenx.anilib.user.data.model

class UserProfileModel : UserPrefModel() {
    var about: String = ""
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
