package com.revolgenx.anilib.common.data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query
import kotlinx.coroutines.flow.Flow

class ApolloRepositoryImpl(private val apolloClient: ApolloClient) : ApolloRepository {
    override fun <D : Query.Data> query(query: Query<D>): Flow<ApolloResponse<D>> {
        return apolloClient.query(query).toFlow()
    }

    override fun <D : Mutation.Data> mutation(mutation: Mutation<D>): Flow<ApolloResponse<D>> {
        return apolloClient.mutation(mutation).toFlow()
    }
}