package com.revolgenx.anilib.common.repository.network

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.api.*
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

interface BaseGraphRepository {
    fun <D : Query.Data> request(query: Query<D>): Single<ApolloResponse<D>>
    fun <D : Mutation.Data> request(mutation: Mutation<D>): Single<ApolloResponse<D>>
    fun <D : Query.Data> query(query: Query<D>): Flow<ApolloResponse<D>>
    fun <D : Mutation.Data> mutation(mutation: Mutation<D>): Flow<ApolloResponse<D>>
}