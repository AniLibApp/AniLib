package com.revolgenx.anilib.media.data.service

import com.apollographql.apollo3.api.ApolloResponse
import com.revolgenx.anilib.MediaQuery
import com.revolgenx.anilib.media.data.model.MediaFilterModel

interface MediaService {
    suspend fun getMediaList(
        page: Int,
        perPage: Int,
        filter: MediaFilterModel
    ): ApolloResponse<MediaQuery.Data>
}