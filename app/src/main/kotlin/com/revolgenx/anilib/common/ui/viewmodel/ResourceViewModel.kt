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
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

abstract class ResourceViewModel<M : Any, F : BaseField<*>> : BaseViewModel<F>() {
    open val resource: MutableState<ResourceState<M>?> = mutableStateOf(null)
    protected abstract fun loadData(): Flow<M?>

    fun getResource() {
        if (resource.value.isNull()) {
            onInit()
            refresh()
        }
    }

    protected open fun onInit() {}
    protected open fun onComplete() {}

    fun refresh() {
        resource.value = ResourceState.loading()
        loadData()
            .onEach {
                val data = it ?: throw ApolloException("No data available")
                resource.value = ResourceState.success(data)
            }
            .onCompletion { onComplete() }
            .catch {
                resource.value = ResourceState.error(it)
            }.launchIn(viewModelScope)
    }

    protected fun getData(): M? {
        val state = resource.value
        return if (state is ResourceState.Success) state.data else null
    }


}
