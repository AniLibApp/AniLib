package com.revolgenx.anilib.repository.network

import com.apollographql.apollo.api.Operation
import com.apollographql.apollo.api.Query
import com.apollographql.apollo.api.Response
import io.reactivex.Observable

interface BaseGraphRepository {
    fun <D : Operation.Data, T, V : Operation.Variables> request(query: Query<D, T, V>): Observable<Response<T>>
}