package com.revolgenx.anilib.infrastructure.repository.network

import com.apollographql.apollo3.api.*
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

interface BaseGraphRepository {
    fun <D : Query.Data> request(query: Query<D>): Single<ApolloResponse<D>>
    fun <D : Mutation.Data> request(mutate: Mutation<D>): Single<ApolloResponse<D>>
}