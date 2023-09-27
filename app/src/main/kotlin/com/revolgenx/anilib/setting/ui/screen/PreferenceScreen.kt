package com.revolgenx.anilib.setting.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.theme.primary
import com.revolgenx.anilib.common.ui.theme.typography
import com.revolgenx.anilib.setting.ui.component.ListPreferenceItem
import com.revolgenx.anilib.setting.ui.model.PreferenceModel
import kotlinx.coroutines.launch

abstract class PreferenceScreen : AndroidScreen() {
    abstract val titleRes: Int

    @Composable
    abstract fun getPreferences(): List<PreferenceModel>

    @Composable
    override fun Content() {
        val preferences = getPreferences()
        PreferenceScreenContent(titleRes, preferences)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreferenceScreenContent(titleRes: Int, preferences: List<PreferenceModel>) {
    ScreenScaffold(
        title = stringResource(id = titleRes),
    ) {
        LazyColumn() {
            preferences.forEachIndexed { i, preference ->
                when (preference) {
                    is PreferenceModel.PreferenceGroup -> {
                        item {
                            PreferenceGroupHeader(title = preference.title)
                        }
                        items(items = preference.preferenceItems) { item ->
                            PreferenceItem(
                                item = item
                            )
                        }
                        item {
                            if (i < preferences.lastIndex) {
                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }

                    else -> {
                        item {
                            PreferenceItem(item = preference)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PreferenceItem(
    item: PreferenceModel,
) {
    val scope = rememberCoroutineScope()
    when (item) {
        is PreferenceModel.ListPreferenceModel<*> -> {
            val prefState = item.pref.collectAsState()
            ListPreferenceItem(
                value = prefState.value,
                title = item.title,
                entries = item.entries,
                subtitle = item.subtitleProvider(prefState.value)
            ) { newValue ->
                scope.launch {
                    item.pref.set(newValue)
                }
            }
        }

        is PreferenceModel.BasicListPreference -> {
            ListPreferenceItem(
                value = item.value,
                title = item.title,
                entries = item.entries,
                subtitle = item.subtitleProvider(item.value)
            ) { newValue ->
                scope.launch {
                    item.onValueChanged(newValue)
                }
            }
        }

        is PreferenceModel.CustomPreference -> TODO()
        is PreferenceModel.EditTextPreference -> TODO()
        is PreferenceModel.InfoPreference -> TODO()
        is PreferenceModel.MultiSelectListPreference -> TODO()
        is PreferenceModel.PreferenceGroup -> TODO()
        is PreferenceModel.SliderPreference -> TODO()
        is PreferenceModel.SwitchPreference -> TODO()
        is PreferenceModel.TextPreferenceModel -> TODO()
    }
}

@Composable
private fun PreferenceGroupHeader(title: String) {
    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp, top = 14.dp),
    ) {
        Text(
            text = title,
            color = primary,
            modifier = Modifier.padding(horizontal = PrefsHorizontalPadding),
            style = typography().bodyMedium,
        )
    }
}

val PrefsHorizontalPadding: Dp = 16.dp