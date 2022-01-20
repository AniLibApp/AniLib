package com.revolgenx.anilib.media.data.model

import com.revolgenx.anilib.airing.data.model.AiringScheduleConnectionModel
import com.revolgenx.anilib.airing.data.model.AiringScheduleModel
import com.revolgenx.anilib.character.data.model.CharacterConnectionModel
import com.revolgenx.anilib.common.data.model.BaseModel
import com.revolgenx.anilib.common.data.model.FuzzyDateModel
import com.revolgenx.anilib.constant.*
import com.revolgenx.anilib.home.recommendation.data.model.RecommendationConnectionModel
import com.revolgenx.anilib.list.data.model.MediaListModel
import com.revolgenx.anilib.review.data.model.ReviewConnection
import com.revolgenx.anilib.staff.data.model.StaffConnectionModel
import com.revolgenx.anilib.studio.data.model.StudioConnectionModel
import com.revolgenx.anilib.user.data.model.stats.MediaRankModel

open class MediaModel:BaseModel() {
    var airingSchedule: AiringScheduleConnectionModel? = null
    var averageScore: Int? = null
    var bannerImage: String? = null
    var chapters: Int? = null
    var characters: CharacterConnectionModel? = null
    var countryOfOrigin: String? = null
    var coverImage: MediaCoverImageModel? = null
    var description:String? = null
    var duration: Int? = null
    var endDate: FuzzyDateModel? = null
    var episodes: Int? = null
    var externalLinks: List<MediaExternalLinkModel>? = null
    var favourites: Int? = null
    var format: Int? = null
    var genres: List<String>? = null
    var hashtag: String? = null
    var idMal: Int = -1
    var isAdult: Boolean = false
    var isFavourite: Boolean = false
    var meanScore: Int? = null
    var mediaListEntry: MediaListModel? = null
    var nextAiringEpisode: AiringScheduleModel? = null
    var popularity: Int? = null
    var rankings: List<MediaRankModel>? = null
    var recommendations: RecommendationConnectionModel? = null
    var relations: MediaConnectionModel? = null
    var reviews: ReviewConnection? = null
    var season: Int? = null
    var seasonInt: Int? = null
    var seasonYear: Int? = null
    var siteUrl: String? = null
    var source: AlMediaSource? = null
    var staff: StaffConnectionModel? = null
    var startDate: FuzzyDateModel? = null
    var stats: MediaStatsModel? = null
    var status: Int? = null
    var streamingEpisodes: List<MediaStreamingEpisodeModel>? = null
    var studios: StudioConnectionModel? = null
    var synonyms: List<String>? = null
    var tags: List<MediaTagModel>? = null
    var title: MediaTitleModel? = null
    var trailer: MediaTrailerModel? = null
    var trending: Int? = null
    var trends: MediaTrendConnectionModel? = null
    var type: Int? = null
    var updatedAt: Int? = null
    var volumes: Int? = null


    //optional entry
    var isSelected: Boolean = false
    var onClickListener: ((selected:Boolean) -> Unit)? = null
}

