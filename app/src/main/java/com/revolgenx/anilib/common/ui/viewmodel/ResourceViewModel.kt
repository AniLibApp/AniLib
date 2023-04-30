package com.revolgenx.anilib.common.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.revolgenx.anilib.common.data.field.BaseField
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.ext.isNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

abstract class ResourceViewModel<M, F : BaseField<*>> : BaseViewModel<F>() {
    open val resource: MutableState<ResourceState<M>?> = mutableStateOf(null)
    protected abstract fun loadData(): Flow<M?>

    fun getResource() {
        if (resource.value.isNull()) {
            refresh()
        }
    }

    fun refresh() {
        resource.value = ResourceState.loading()
        loadData()
            .onEach {
                Timber.d("My current thread ${Thread.currentThread().name}")
                val data = it ?: throw ApolloException("No data available")
                resource.value = ResourceState.success(data)
            }
            .catch {
                resource.value = ResourceState.error(it)
            }.launchIn(viewModelScope)
    }

}
