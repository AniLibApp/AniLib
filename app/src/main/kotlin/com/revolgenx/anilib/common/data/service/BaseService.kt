package com.revolgenx.anilib.common.data.service

import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Mutation
import com.apollographql.apollo3.api.Query
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.common.data.repository.ApolloRepository
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import kotlinx.coroutines.flow.Flow

abstract class BaseService(
    protected val apolloRepository: ApolloRepository,
    private val appPreferencesDataStore: AppPreferencesDataStore
) {
    protected fun <Q : Query.Data, T : Query<Q>> BaseField<T>.toQuery(): Flow<ApolloResponse<Q>> {
        this.canShowAdult = appPreferencesDataStore.displayAdultContent.get()!!
        return apolloRepository.query(this.toQueryOrMutation())
    }

    protected fun <M : Mutation.Data, T : Mutation<M>> BaseField<T>.toMutation(): Flow<ApolloResponse<M>> {
        return apolloRepository.mutation(this.toQueryOrMutation())
    }
}