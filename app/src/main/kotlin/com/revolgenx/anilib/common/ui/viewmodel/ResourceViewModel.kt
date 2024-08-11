package com.revolgenx.anilib.common.ui.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
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

    open var deleteResource by mutableStateOf<ResourceState<Any>?>(null)
    open var saveResource by mutableStateOf<ResourceState<Any>?>(null)
    var errorMsg by mutableStateOf<Int?>(null)

    val isSuccess get() = resource.value is ResourceState.Success


    protected abstract fun load(): Flow<M?>
    
    open fun save(){
        saveResource = ResourceState.loading()
    }
    protected fun saveComplete(data: Any?){
        saveResource = ResourceState.success(data)
    }
    protected fun saveFailed(it: Throwable){
        saveResource = ResourceState.error(it)
    }

    open fun delete(){
        deleteResource = ResourceState.loading()
    }
    protected fun deleteComplete(data: Any?){
        deleteResource = ResourceState.success(data)
    }
    protected fun deleteFailed(it: Throwable){
        deleteResource = ResourceState.error(it)
    }

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
        load()
            .onEach {
                val data = it ?: throw ApolloException("No data available")
                resource.value = ResourceState.success(data)
            }
            .onCompletion { onComplete() }
            .catch {
                resource.value = ResourceState.error(it)
            }.launchIn(viewModelScope)
    }

    fun getData() = resource.value?.stateValue
}
