package com.revolgenx.anilib.common.data.store

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

abstract class BaseDataStore<T>(val source: DataStore<T>) {
    fun get() = runBlocking { source.data.first() }
    @Composable
    fun collectAsState() = source.data.collectAsState(initial = get())
}

interface BasePreferenceDataStore<T> {
    val data: Flow<T?>
    fun get(): T?
    suspend fun set(value: T?)
    suspend fun collect(collector: FlowCollector<T?>)

    @Composable
    fun collectAsState(): State<T?>
}