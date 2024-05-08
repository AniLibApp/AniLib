package com.revolgenx.anilib.setting.ui.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.datastore.preferences.core.Preferences
import com.revolgenx.anilib.common.data.store.BasePreferenceDataStore
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import anilib.i18n.R as I18nR


interface PreferenceValueChangeListener<T> {
    val onValueChanged: (suspend (newValue: T) -> Boolean)?
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
        override val onValueChanged: (suspend (newValue: String) -> Boolean)?,
        val onClick: (() -> Unit)? = null,
    ) : PreferenceModel(), PreferenceItemModel, PreferenceValueChangeListener<String>

    data class SwitchPreference(
        val pref: BasePreferenceDataStore<Boolean>? = null,
        val prefState: State<Boolean>? = null,
        override val title: String,
        override val subtitle: String? = null,
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true,
        override val onValueChanged: (suspend (newValue: Boolean) -> Boolean)? = null,
    ) : PreferenceModel(), PreferenceItemModel, PreferenceValueChangeListener<Boolean>

    data class SliderPreference(
        val value: Int,
        val min: Int = 0,
        val max: Int,
        override val title: String = "",
        override val subtitle: String? = null,
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true,
        override val onValueChanged: (suspend (newValue: Int) -> Boolean)?,
    ) : PreferenceModel(), PreferenceItemModel, PreferenceValueChangeListener<Int>


    data class ListPreferenceModel<T>(
        val pref: BasePreferenceDataStore<T>? = null,
        val prefState: State<T>? = null,
        override val title: String,
        override val subtitle: String = "%s",
        val entries: List<ListPreferenceEntry<out T>>,
        val subtitleProvider: (value: Any?) -> String? = { v ->
            subtitle.format(entries.first { it.value == v }.title)
        },
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true,
        override val onValueChanged: (suspend (newValue: T?) -> Boolean)? = null,
    ) : PreferenceModel(), PreferenceItemModel, PreferenceValueChangeListener<T> {
        val onValueChangedListener: (suspend (newValue: Any?) -> Boolean) = {
            onValueChanged?.invoke(it as? T) == true
        }

        suspend fun updatePref(value: Any?) {
            pref?.set(value as? T)
        }
    }

    data class MultiSelectListPreference(
        val pref: Preferences.Key<Set<String>>,
        override val title: String,
        override val subtitle: String? = "%s",
        val subtitleProvider: @Composable (value: Set<String>, entries: Map<String, String>) -> String? = { v, e ->
            val combined = remember(v) {
                v.map { e[it] }
                    .takeIf { it.isNotEmpty() }
                    ?.joinToString()
            } ?: stringResource(I18nR.string.none)
            subtitle?.format(combined)
        },
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true,
        override val onValueChanged: (suspend (newValue: Set<String>) -> Boolean)?,
        val entries: Map<String, String>,
    ) : PreferenceModel(), PreferenceItemModel, PreferenceValueChangeListener<Set<String>>


    data class EditTextPreference(
        val pref: Preferences.Key<String>,
        override val title: String,
        override val subtitle: String? = "%s",
        override val icon: ImageVector? = null,
        override val enabled: Boolean = true,
        override val onValueChanged: (suspend (newValue: String) -> Boolean)?,
    ) : PreferenceModel(), PreferenceItemModel, PreferenceValueChangeListener<String>


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