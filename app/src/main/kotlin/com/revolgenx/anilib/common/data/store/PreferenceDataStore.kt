package com.revolgenx.anilib.common.data.store

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

typealias PreferencesDataStore = DataStore<Preferences>

val Context.preferencesDataStore: PreferencesDataStore by preferencesDataStore(name = "preferences_data_store.json")

interface PreferenceData<T> {
    fun get(): T
    fun getNullable(): T?
    suspend fun set(value: Any?)
    fun map(): Flow<T?>

    suspend fun collect(collector: FlowCollector<T?>)

    @Composable
    fun collectAsState(): State<T>

    @Composable
    fun collectAsNullableState(): State<T?>
}

class PreferenceDataModel<T>(
    private val dataStore: DataStore<Preferences>,
    private val prefKey: Preferences.Key<T>,
    private val defaultValue: T? = null,
) : PreferenceData<T> {
    override fun get(): T = getNullable()!!

    override fun getNullable(): T? = runBlocking { map().first() }

    override fun map(): Flow<T?> = dataStore.data.map { it[prefKey] ?: defaultValue }

    override suspend fun collect(collector: FlowCollector<T?>) {
        map().collect(collector)
    }

    override suspend fun set(value: Any?) {
        if (value == null) {
            dataStore.edit { it.remove(prefKey) }
        } else {
            dataStore.edit {
                it[prefKey] = value as T
            }
        }
    }


    @Composable
    override fun collectAsState(): State<T> {
        val flow = remember(this) { map().map { it!! } }
        return flow.collectAsState(initial = get())
    }

    @Composable
    override fun collectAsNullableState(): State<T?> {
        val flow = remember(this) { map() }
        return flow.collectAsState(initial = getNullable())
    }

}