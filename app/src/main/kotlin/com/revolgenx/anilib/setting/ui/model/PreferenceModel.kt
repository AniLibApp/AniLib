package com.revolgenx.anilib.setting.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.revolgenx.anilib.R
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import org.koin.core.component.KoinComponent


interface PreferenceValueChangedListener<T> {
    val onValueChanged: suspend (newValue: T) -> Boolean
}

interface PreferenceItemModel {
    val subtitle: String?
    val icon: ImageVector?
}


sealed class PreferenceModel {
    abstract val title: String
    abstract val enabled: Boolean

    data class TextPreferenceModel(
        override val title: String,
        override val subtitle: String? = null,
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true,
        override val onValueChanged: suspend (newValue: String) -> Boolean = { true },
        val onClick: (() -> Unit)? = null,
    ) : PreferenceModel(), PreferenceItemModel, PreferenceValueChangedListener<String>

    data class SwitchPreference(
        val pref: Preferences.Key<Boolean>,
        override val title: String,
        override val subtitle: String? = null,
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true,
        override val onValueChanged: suspend (newValue: Boolean) -> Boolean = { true },
    ) : PreferenceModel(), PreferenceItemModel, PreferenceValueChangedListener<Boolean>

    data class SliderPreference(
        val value: Int,
        val min: Int = 0,
        val max: Int,
        override val title: String = "",
        override val subtitle: String? = null,
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true,
        override val onValueChanged: suspend (newValue: Int) -> Boolean = { true },
    ) : PreferenceModel(), PreferenceItemModel, PreferenceValueChangedListener<Int>


    data class ListPreferenceModel<T>(
        val pref: PreferenceData<T>,
        override val title: String,
        override val subtitle: String? = "%s",
        val subtitleProvider: @Composable (value: T, entries: Map<T, String>) -> String? =
            { v, e -> subtitle?.format(e[v]) },
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true,
        override val onValueChanged: suspend (newValue: T) -> Boolean = { true },
        val entries: List<ListPreferenceEntry<out T>>,
    ) : PreferenceModel(), PreferenceItemModel, PreferenceValueChangedListener<T>


    data class BasicListPreference(
        val value: String,
        override val title: String,
        override val subtitle: String? = "%s",
        val subtitleProvider: @Composable (value: String, entries: Map<String, String>) -> String? =
            { v, e -> subtitle?.format(e[v]) },
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true,
        override val onValueChanged: suspend (newValue: String) -> Boolean = { true },

        val entries: Map<String, String>,
    ) : PreferenceModel(), PreferenceItemModel, PreferenceValueChangedListener<String>


    data class MultiSelectListPreference(
        val pref: Preferences.Key<Set<String>>,
        override val title: String,
        override val subtitle: String? = "%s",
        val subtitleProvider: @Composable (value: Set<String>, entries: Map<String, String>) -> String? = { v, e ->
            val combined = remember(v) {
                v.map { e[it] }
                    .takeIf { it.isNotEmpty() }
                    ?.joinToString()
            } ?: stringResource(R.string.none)
            subtitle?.format(combined)
        },
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true,
        override val onValueChanged: suspend (newValue: Set<String>) -> Boolean = { true },

        val entries: Map<String, String>,
    ) : PreferenceModel(), PreferenceItemModel, PreferenceValueChangedListener<Set<String>>


    data class EditTextPreference(
        val pref: Preferences.Key<String>,
        override val title: String,
        override val subtitle: String? = "%s",
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true,
        override val onValueChanged: suspend (newValue: String) -> Boolean = { true },
    ) : PreferenceModel(), PreferenceItemModel, PreferenceValueChangedListener<String>


    data class InfoPreference(
        override val title: String,
    ) : PreferenceModel(), PreferenceItemModel {
        override val enabled: Boolean = true
        override val subtitle: String? = null
        override val icon: ImageVector? = null
    }


    data class CustomPreference(
        override val title: String,
        val content: @Composable (PreferenceModel) -> Unit,
    ) : PreferenceModel(), PreferenceItemModel {
        override val enabled: Boolean = true
        override val subtitle: String? = null
        override val icon: ImageVector? = null
    }

    data class PreferenceGroup(
        override val title: String,
        override val enabled: Boolean = true,
        val preferenceItems: List<PreferenceModel>,
    ) : PreferenceModel()
}


interface PreferenceData<T> {
    fun get(): T?
    suspend fun set(value: Any?)
    fun map(): Flow<T?>

    @Composable
    fun collectAsState(): State<T?>
}

class PreferenceDataModel<T>(
    private val dataStore: DataStore<Preferences>,
    private val prefKey: Preferences.Key<T>,
    private val defaultValue: T? = null
) : PreferenceData<T> {
    override fun get(): T? = runBlocking {
        map().first()
    }

    override fun map(): Flow<T?> = dataStore.data.map { it[prefKey] ?: defaultValue }

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
    override fun collectAsState(): State<T?> {
        val flow = remember(this) { map() }
        return flow.collectAsState(initial = get())
    }
}