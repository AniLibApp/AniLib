package com.revolgenx.anilib.setting.ui.screen.filter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import anilib.i18n.R
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.setting.ui.component.GroupPreferenceItem
import com.revolgenx.anilib.setting.ui.component.PrefsHorizontalPadding
import com.revolgenx.anilib.setting.ui.component.TextPreferenceItem
import com.revolgenx.anilib.setting.ui.viewmodel.FilterSettingsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

object FilterSettingsScreen : AndroidScreen() {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = localNavigator()
        val viewModel: FilterSettingsViewModel = koinViewModel()
        val scope = rememberCoroutineScope()

        ScreenScaffold(
            title = stringResource(id = R.string.filter),
            contentWindowInsets = horizontalBottomWindowInsets()
        ) {
            Column(
                modifier = Modifier
                    .imePadding()
                    .verticalScroll(rememberScrollState())
            ) {

                GroupPreferenceItem(title = stringResource(id = R.string.anime)) {
                    TextPreferenceItem(
                        title = stringResource(id = R.string.settings_exclude_tags),
                        subtitle = stringResource(id = R.string.settings_exclude_tags_desc),
                        onClick = {
                            navigator.push(TagFilterSettingsScreen(isTag = true, isAnime = true))
                        }
                    )

                    TextPreferenceItem(
                        title = stringResource(id = R.string.settings_exclude_genre),
                        subtitle = stringResource(id = R.string.settings_exclude_genre_desc),
                        onClick = {
                            navigator.push(TagFilterSettingsScreen(isTag = false, isAnime = true))
                        }
                    )

                    MaximumNumberFilter(
                        title = stringResource(id = R.string.settings_max_episodes),
                        subtitle = stringResource(id = R.string.settings_max_episodes_desc),
                        value = viewModel.maxEpisodesPref.get()!!.toString(),
                        onIntValueChange = {
                            scope.launch {
                                viewModel.maxEpisodesPref.set(it)
                            }
                        }
                    )

                    MaximumNumberFilter(
                        title = stringResource(id = R.string.settings_max_duration),
                        subtitle = stringResource(id = R.string.settings_max_duration_desc),
                        value = viewModel.maxDurationsPref.get()!!.toString(),
                        onIntValueChange = {
                            scope.launch {
                                viewModel.maxDurationsPref.set(it)
                            }
                        }
                    )


                }

                GroupPreferenceItem(title = stringResource(id = R.string.manga)) {

                    TextPreferenceItem(
                        title = stringResource(id = R.string.settings_exclude_tags),
                        subtitle = stringResource(id = R.string.settings_exclude_tags_desc),
                        onClick = {
                            navigator.push(TagFilterSettingsScreen(isTag = true, isAnime = false))
                        }
                    )


                    TextPreferenceItem(
                        title = stringResource(id = R.string.settings_exclude_genre),
                        subtitle = stringResource(id = R.string.settings_exclude_genre_desc),
                        onClick = {
                            navigator.push(TagFilterSettingsScreen(isTag = false, isAnime = false))
                        }
                    )

                    MaximumNumberFilter(
                        title = stringResource(id = R.string.settings_max_chapters),
                        subtitle = stringResource(id = R.string.settings_max_chapters_desc),
                        value = viewModel.maxChaptersPref.get()!!.toString(),
                        onIntValueChange = {
                            scope.launch {
                                viewModel.maxChaptersPref.set(it)
                            }
                        }
                    )

                    MaximumNumberFilter(
                        title = stringResource(id = R.string.settings_max_volumes),
                        subtitle = stringResource(id = R.string.settings_max_volumes_desc),
                        value = viewModel.maxVolumesPref.get()!!.toString(),
                        onIntValueChange = {
                            scope.launch {
                                viewModel.maxVolumesPref.set(it)
                            }
                        }
                    )
                }
            }
        }
    }
}


@Composable
private fun MaximumNumberFilter(
    title: String,
    subtitle: String,
    value: String,
    onIntValueChange: (Int) -> Unit
) {
    val number = remember(value) {
        mutableStateOf(value)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextPreferenceItem(
            modifier = Modifier.weight(0.7f),
            title = title,
            subtitle = subtitle
        )

        TextField(
            modifier = Modifier
                .weight(0.3f)
                .padding(end = PrefsHorizontalPadding),
            value = number.value,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                number.value = it
                val newIntValue = it.toIntOrNull()
                if (it.isNotEmpty() && newIntValue != null && newIntValue > 0) {
                    onIntValueChange(newIntValue)
                }
            }
        )
    }
}