package com.revolgenx.anilib.common.data.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

interface IBaseDataModel<T> {
    val data: Flow<T?>
    fun get(): T?
    suspend fun set(value: Any?)
    suspend fun collect(collector: FlowCollector<T?>)

    @Composable
    fun collectAsState(): State<T?>
}


class PreferenceDataStoreModel<T>(
    private val dataStore: DataStore<Preferences>,
    private val prefKey: Preferences.Key<T>,
    private val defaultValue: T? = null,
) : IBaseDataModel<T> {

    override val data: Flow<T?> = dataStore.data.map { it[prefKey] ?: defaultValue }

    override fun get(): T? = runBlocking { data.first() }

    @Suppress("UNCHECKED_CAST")
    override suspend fun set(value: Any?) {
        if (value == null) {
            dataStore.edit { it.remove(prefKey) }
        } else {
            dataStore.edit {
                it[prefKey] = value as T
            }
        }
    }

    override suspend fun collect(collector: FlowCollector<T?>) {
        data.collect(collector)
    }

    @Composable
    override fun collectAsState(): State<T?> {
        val flow = remember(this) { data.map { it } }
        return flow.collectAsState(initial = get())
    }
}