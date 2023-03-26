package com.revolgenx.anilib.common.data.repository

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query

class ApolloRepositoryImpl(private val apolloClient: ApolloClient): ApolloRepository {
    override suspend fun <D : Query.Data> query(query: Query<D>): ApolloResponse<D> {
        return apolloClient.query(query).execute()
    }

    override suspend fun <D : Mutation.Data> mutation(mutation: Mutation<D>): ApolloResponse<D> {
        return apolloClient.mutation(mutation).execute()
    }
}