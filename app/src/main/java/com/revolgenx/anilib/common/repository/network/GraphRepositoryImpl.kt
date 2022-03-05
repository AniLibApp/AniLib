package com.revolgenx.anilib.common.repository.network

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.rx2.Rx2Apollo
import io.reactivex.Single
import kotlinx.coroutines.flow.Flow

class GraphRepositoryImpl(private val apolloClient: ApolloClient) : BaseGraphRepository {

    private fun <D : Query.Data> getQuery(query: Query<D>) = apolloClient.query(query)
    private fun <D : Mutation.Data> getMutation(mutation: Mutation<D>) =
        apolloClient.mutation(mutation)

    override fun <D : Query.Data> request(query: Query<D>): Single<ApolloResponse<D>> {
        return Rx2Apollo.single(getQuery(query))
    }

    override fun <D : Mutation.Data> request(mutation: Mutation<D>): Single<ApolloResponse<D>> {
        return Rx2Apollo.single(getMutation(mutation))
    }

    override fun <D : Query.Data> query(query: Query<D>): Flow<ApolloResponse<D>> {
        return getQuery(query).toFlow()
    }

    override fun <D : Mutation.Data> mutation(mutation: Mutation<D>): Flow<ApolloResponse<D>> {
        return getMutation(mutation).toFlow()
    }

}