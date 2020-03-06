package com.revolgenx.anilib.repository.network

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.rx2.Rx2Apollo
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

class GraphRepositoryImpl(private val apolloClient: ApolloClient): BaseGraphRepository {
    override fun <D : Operation.Data, T, V : Operation.Variables> request(query: Query<D, T, V>): Observable<Response<T>> {
        return Rx2Apollo.from(apolloClient.query(query)).observeOn(Schedulers.io())
    }
}