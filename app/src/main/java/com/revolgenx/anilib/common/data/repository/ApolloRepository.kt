package com.revolgenx.anilib.common.data.repository

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query
import kotlinx.coroutines.flow.Flow

interface ApolloRepository{
    fun <D : Query.Data> query(query: Query<D>): Flow<ApolloResponse<D>>
    fun <D : Mutation.Data> mutation(mutation: Mutation<D>): Flow<ApolloResponse<D>>
}