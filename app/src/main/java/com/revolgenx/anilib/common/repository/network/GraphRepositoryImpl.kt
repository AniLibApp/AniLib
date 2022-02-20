package com.revolgenx.anilib.common.repository.network

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.rx2.Rx2Apollo
import io.reactivex.Single

class GraphRepositoryImpl(private val apolloClient: ApolloClient): BaseGraphRepository {
    override fun <D : Query.Data> request(query: Query<D>): Single<ApolloResponse<D>> {
        return Rx2Apollo.single(apolloClient.query(query))
    }

    override fun <D : Mutation.Data> request(mutate: Mutation<D>): Single<ApolloResponse<D>> {
        return Rx2Apollo.single(apolloClient.mutation(mutate))
    }


}