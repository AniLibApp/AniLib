package com.revolgenx.anilib.setting.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.setting.ui.component.ListPreferenceItem
import com.revolgenx.anilib.setting.ui.component.SwitchPreferenceItem
import com.revolgenx.anilib.setting.ui.model.PreferenceModel
import kotlinx.coroutines.launch

abstract class PreferencesScreen : AndroidScreen() {
    abstract val titleRes: Int

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        ScreenScaffold(
            title = stringResource(id = titleRes),
            actions = {
                SaveAction()
            },
            contentWindowInsets = horizontalBottomWindowInsets()
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                PreferencesContent()
            }
        }
    }

    @Composable
    protected abstract fun PreferencesContent()

    @Composable
    protected open fun SaveAction() {

    }

}


@Composable
private fun PreferenceItem(
    item: PreferenceModel,
) {
    val scope = rememberCoroutineScope()
    when (item) {
        is PreferenceModel.ListPreferenceModel<*> -> {
            val prefState = item.pref?.collectAsState() ?: item.prefState
            val prefValue = prefState?.value
            ListPreferenceItem(
                value = prefValue,
                title = item.title,
                entries = item.entries,
                subtitle = item.subtitleProvider(prefValue)
            ) { newValue ->
                scope.launch {
                    if (!item.onValueChangedListener(newValue)) {
                        item.updatePref(newValue)
                    }
                }
            }
        }

        is PreferenceModel.SwitchPreference -> {
            val prefState = item.pref?.collectAsState()
            val prefValue = prefState?.value ?: (item.prefState?.value == true)
            SwitchPreferenceItem(
                title = item.title,
                subtitle = item.subtitle,
                icon = item.icon,
                checked = prefValue,
                onCheckedChanged = { newValue ->
                    scope.launch {
                        val invoked = item.onValueChanged?.invoke(newValue)
                        if (invoked == null || !invoked) {
                            item.pref?.set(newValue)
                        }
                    }
                },
            )
        }

        is PreferenceModel.CustomPreference -> {
            item.content(item)
        }

        is PreferenceModel.EditTextPreference -> TODO()
        is PreferenceModel.InfoPreference -> TODO()
        is PreferenceModel.MultiSelectListPreference -> TODO()
        is PreferenceModel.PreferenceGroup -> TODO()
        is PreferenceModel.SliderPreference -> TODO()
        is PreferenceModel.TextPreferenceModel -> TODO()
    }
}

