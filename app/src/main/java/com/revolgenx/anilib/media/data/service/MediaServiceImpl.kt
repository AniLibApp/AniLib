package com.revolgenx.anilib.media.data.service

import com.apollographql.apollo3.api.ApolloResponse
import com.revolgenx.anilib.MediaQuery
import com.revolgenx.anilib.common.data.operation.OperationUtil.hasUserEnabledAdult
import com.revolgenx.anilib.common.data.operation.OperationUtil.nn
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.media.data.model.MediaFilterModel

class MediaServiceImpl(private val apolloRepository: ApolloRepository) : MediaService {
    override suspend fun getMediaList(
        page: Int,
        perPage: Int,
        filter: MediaFilterModel
    ): ApolloResponse<MediaQuery.Data> {
        val query = MediaQuery(
            page = nn(page),
            perPage = nn(perPage),
            season = nn(filter.season),
            seasonYear = nn(filter.seasonYear),
            year = nn(filter.year?.let { "$it%" }),
            sort = nn(filter.sort?.let { listOf(it) }),
            format_in = nn(filter.formatsIn),
            idIn = nn(filter.idIn),
            isAdult = nn(filter.isAdult ?: hasUserEnabledAdult.takeIf { it.not() }),
            status = nn(filter.status),
            genre_in = nn(filter.genreIn),
            tag_in = nn(filter.tagIn)
        )
        return apolloRepository.query(query)
    }
}