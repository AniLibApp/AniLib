package com.revolgenx.anilib.common.data.store

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


class AppPreferencesDataStore<T>(
    private val dataStore: DataStore<Preferences>,
    private val prefKey: Preferences.Key<T>,
    private val defaultValue: T? = null,
) : BasePreferenceDataStore<T> {

    override val data: Flow<T?> = dataStore.data.map { it[prefKey] ?: defaultValue }

    override fun get(): T? = runBlocking { data.first() }

    override suspend fun set(value: T?) {
        if (value == null) {
            dataStore.edit { it.remove(prefKey) }
        } else {
            dataStore.edit {
                it[prefKey] = value
            }
        }
    }

    override suspend fun collect(collector: FlowCollector<T?>) {
        data.collect(collector)
    }

    @Composable
    override fun collectAsState(): State<T?> {
        return data.collectAsState(initial = get())
    }
}