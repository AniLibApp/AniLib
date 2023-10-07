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

interface BaseNullableDataStore<T> {
    fun getNullable(): T?
    suspend fun collectNullable(collector: FlowCollector<T?>)
    fun mapNullable(): Flow<T?>

    @Composable
    fun collectAsNullableState(): State<T?>
}

interface BaseDataStore<T> {
    fun get(): T
    suspend fun set(value: Any?)
    suspend fun update(transform: suspend (t: T) -> T)
    fun map(): Flow<T>

    suspend fun collect(collector: FlowCollector<T>)

    @Composable
    fun collectAsState(): State<T>
}


class FieldDataStore<T>(
    private val dataStore: DataStore<T>
) : BaseDataStore<T> {
    override fun get(): T = runBlocking { map().first() }

    @Suppress("UNCHECKED_CAST")
    override suspend fun set(value: Any?) {
        dataStore.updateData { value as T }
    }

    override fun map(): Flow<T> = dataStore.data

    override suspend fun update(transform: suspend (t: T) -> T) {
        dataStore.updateData(transform)
    }

    override suspend fun collect(collector: FlowCollector<T>) {
        map().collect(collector)
    }

    @Composable
    override fun collectAsState(): State<T> {
        val flow = remember(this) { map() }
        return flow.collectAsState(initial = get())
    }
}

interface BasePreferenceDataModel<T>: BaseDataStore<T>, BaseNullableDataStore<T>

class PreferenceDataModel<T>(
    private val dataStore: DataStore<Preferences>,
    private val prefKey: Preferences.Key<T>,
    private val defaultValue: T? = null,
) :  BasePreferenceDataModel<T>{
    override fun get(): T = getNullable()!!
    override fun getNullable(): T? = runBlocking { mapNullable().first() }
    override fun map(): Flow<T> = mapNullable().map { it!! }
    override fun mapNullable(): Flow<T?> = dataStore.data.map { it[prefKey] ?: defaultValue }

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

    override suspend fun update(transform: suspend (t: T) -> T) {}
    override suspend fun collect(collector: FlowCollector<T>) {
        map().collect(collector)
    }

    override suspend fun collectNullable(collector: FlowCollector<T?>) {
        mapNullable().collect(collector)
    }

    @Composable
    override fun collectAsState(): State<T> {
        val flow = remember(this) { mapNullable().map { it!! } }
        return flow.collectAsState(initial = get())
    }

    @Composable
    override fun collectAsNullableState(): State<T?> {
        val flow = remember(this) { mapNullable() }
        return flow.collectAsState(initial = getNullable())
    }

}