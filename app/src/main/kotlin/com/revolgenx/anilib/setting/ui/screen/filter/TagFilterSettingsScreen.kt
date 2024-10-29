package com.revolgenx.anilib.setting.ui.screen.filter

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import anilib.i18n.R
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcCancel
import com.revolgenx.anilib.common.ui.icons.appicon.IcCheck
import com.revolgenx.anilib.common.ui.icons.appicon.IcSave
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.setting.ui.component.PrefsHorizontalPadding
import com.revolgenx.anilib.setting.ui.model.MediaTagFilterModel
import com.revolgenx.anilib.setting.ui.viewmodel.GenreFilterSettingsViewModel
import com.revolgenx.anilib.setting.ui.viewmodel.TagFilterSettingsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class TagFilterSettingsScreen(
    private val isAnime: Boolean,
    private val isTag: Boolean
) : AndroidScreen() {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val viewModel =
            if (isTag) koinViewModel<TagFilterSettingsViewModel>() else koinViewModel<GenreFilterSettingsViewModel>()
        val scope = rememberCoroutineScope()
        val context = localContext()

        ScreenScaffold(
            title = if (isTag) stringResource(id = R.string.tags) else stringResource(id = R.string.genre),
            actions = {
                val snackbar = localSnackbarHostState()
                IconButton(onClick = {
                    viewModel.saveExcludedTags(isAnime)
                    scope.launch {
                        snackbar.showSnackbar(
                            context.getString(R.string.saved_successfully),
                            withDismissAction = true
                        )
                    }
                }) {
                    Icon(
                        imageVector = AppIcons.IcSave,
                        contentDescription = stringResource(id = R.string.save)
                    )
                }
            },
            contentWindowInsets = horizontalBottomWindowInsets()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = PrefsHorizontalPadding),
            ) {
                val label =
                    if (isTag) {
                        stringResource(id = R.string.settings_exclude_tags)
                    } else {
                        stringResource(id = R.string.settings_exclude_genre)
                    }

                val tagCollection =
                    if (isAnime) viewModel.excludedAnimeTagCollection else viewModel.excludedMangaTagCollection
                FilterTags(tagCollection = tagCollection,
                    label = label,
                    placeHolder = stringResource(id = R.string.settings_exclude_tags_placeholder),
                    onTagAdd = {
                        viewModel.addExcludedTag(it, isAnime)
                    },
                    onTagRemove = {
                        viewModel.removeExcludedTag(it, isAnime)
                    })

                FlowRowFilterTags(tagCollection = if (isAnime) viewModel.excludedAnimeTags.value else viewModel.excludedMangaTags.value) {
                    viewModel.removeExcludedTag(it, isAnime)
                }
            }

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterTags(
    tagCollection: List<MediaTagFilterModel>,
    label: String,
    placeHolder: String,
    onTagAdd: (tag: MediaTagFilterModel) -> Unit,
    onTagRemove: (tag: MediaTagFilterModel) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var queryTag by remember {
        mutableStateOf("")
    }
    val tags by remember {
        derivedStateOf {
            tagCollection.filter {
                it.name.contains(
                    queryTag, ignoreCase = true
                )
            }
        }
    }

    ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
        val focusManager = LocalFocusManager.current
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .menuAnchor(MenuAnchorType.PrimaryEditable),
                value = queryTag,
                onValueChange = {
                    queryTag = it
                },
                singleLine = true,
                label = {
                    Text(text = label)
                },
                placeholder = {
                    Text(text = placeHolder)
                },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    expanded = false
                    focusManager.clearFocus()
                })
            )

        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            if (tags.isEmpty()) {
                DropdownMenuItem(
                    text = {
                        Text(
                            stringResource(id = R.string.empty),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    onClick = {},
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
            tags.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = option.name, style = MaterialTheme.typography.bodyLarge
                            )
                            if (option.isSelected.value) {
                                Icon(
                                    modifier = Modifier.padding(horizontal = 3.dp),
                                    imageVector = AppIcons.IcCheck,
                                    contentDescription = null
                                )
                            }
                        }
                    },
                    onClick = {
                        if (option.isSelected.value) {
                            onTagRemove(option)
                        } else {
                            onTagAdd(option)
                        }
                        option.isSelected.value = !option.isSelected.value
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }

    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FlowRowFilterTags(
    tagCollection: List<MediaTagFilterModel>,
    onRemoveTag: (tag: MediaTagFilterModel) -> Unit,
) {
    FlowRow(modifier = Modifier.fillMaxWidth()) {
        tagCollection.forEach {
            Box(
                modifier = Modifier
                    .padding(end = 8.dp, top = 8.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        shape = CircleShape
                    )
                    .clickable(
                        interactionSource = remember {
                            MutableInteractionSource()
                        }, indication = null
                    ) {
                        it.isSelected.value = false
                        onRemoveTag(it)
                    },
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = it.name,
                        style = MaterialTheme.typography.titleSmall,
                    )
                    Icon(
                        modifier = Modifier.size(14.dp),
                        imageVector = AppIcons.IcCancel,
                        contentDescription = null
                    )
                }
            }
        }
    }
}