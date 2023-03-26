package com.revolgenx.anilib.common.data.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query

interface ApolloRepository{
    suspend fun <D : Query.Data> query(query: Query<D>): ApolloResponse<D>
    suspend fun <D : Mutation.Data> mutation(mutation: Mutation<D>): ApolloResponse<D>
}