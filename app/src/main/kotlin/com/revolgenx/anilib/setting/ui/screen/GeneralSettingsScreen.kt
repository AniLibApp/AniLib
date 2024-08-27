package com.revolgenx.anilib.setting.ui.screen

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import anilib.i18n.R
import com.revolgenx.anilib.common.data.constant.ContentOrder
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.localSnackbarHostState
import com.revolgenx.anilib.common.ui.component.dialog.ConfirmationDialog
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcUnfold
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.common.util.getDisplayName
import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel
import com.revolgenx.anilib.setting.ui.component.GroupPreferenceItem
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import com.revolgenx.anilib.setting.ui.component.ListPreferenceItem
import com.revolgenx.anilib.setting.ui.component.SwitchPreferenceItem
import com.revolgenx.anilib.setting.ui.component.TextPreferenceItem
import com.revolgenx.anilib.setting.ui.viewmodel.GeneralSettingsViewModel
import com.revolgenx.anilib.setting.ui.viewmodel.ContentOrderData
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.xmlpull.v1.XmlPullParser
import sh.calvin.reorderable.ReorderableColumn
import anilib.i18n.R as I18nR

object GeneralSettingsScreen : PreferencesScreen() {

    @Composable
    override fun PreferencesContent() {
        val context = localContext()
        val scope = rememberCoroutineScope()
        val snackbar = localSnackbarHostState()
        val viewModel: GeneralSettingsViewModel = koinViewModel()

        val appDataStore = viewModel.appPreferencesDataStore
        val langs = remember { getLangs(context) }

        val currentLanguage = remember {
            mutableStateOf(
                AppCompatDelegate.getApplicationLocales().get(0)?.toLanguageTag() ?: ""
            )
        }

        val imageQualityEntry = remember {
            coverImagePrefList(context)
        }


        LaunchedEffect(currentLanguage) {
            val locale = if (currentLanguage.value.isEmpty()) {
                LocaleListCompat.getEmptyLocaleList()
            } else {
                LocaleListCompat.forLanguageTags(currentLanguage.value)
            }
            AppCompatDelegate.setApplicationLocales(locale)
        }

        ListPreferenceItem(
            value = currentLanguage.value,
            title = stringResource(R.string.settings_general_app_language),
            entries = langs
        ) { newValue ->
            currentLanguage.value = newValue!!
        }

        val mediaCoverImageType = appDataStore.mediaCoverImageType.collectAsState()

        ListPreferenceItem(
            value = mediaCoverImageType.value,
            title = stringResource(R.string.cover_image_quality),
            entries = imageQualityEntry
        ) { newValue ->
            scope.launch {
                appDataStore.mediaCoverImageType.set(newValue)
            }
        }

        val openMediaListEntryEditorOnClick =
            appDataStore.openMediaListEntryEditorOnClick.collectAsState()
        SwitchPreferenceItem(
            title = stringResource(id = R.string.settings_list_editor_on_single_tap),
            subtitle = stringResource(id = R.string.settings_list_editor_on_single_tap_desc),
            checked = openMediaListEntryEditorOnClick.value!!
        ) {
            scope.launch {
                appDataStore.openMediaListEntryEditorOnClick.set(it)
            }
        }


        val loadGif = appDataStore.autoPlayGif.collectAsState()
        SwitchPreferenceItem(
            title = stringResource(id = R.string.settings_auto_play_gif),
            subtitle = stringResource(id = R.string.settings_play_gif_desc),
            checked = loadGif.value!!
        ) {
            scope.launch {
                appDataStore.autoPlayGif.set(it)
            }
        }

        val showUserAbout = appDataStore.showUserAbout.collectAsState()

        SwitchPreferenceItem(
            title = stringResource(id = R.string.settings_show_users_about),
            subtitle = stringResource(id = R.string.settings_show_users_about_desc),
            checked = showUserAbout.value!!
        ) {
            scope.launch {
                appDataStore.showUserAbout.set(it)
            }
        }


        val showExploreOrderDialog = remember {
            mutableStateOf(false)
        }

        val showMainPageOrderDialog = remember {
            mutableStateOf(false)
        }

        GroupPreferenceItem(title = stringResource(id = I18nR.string.settings_change_order)) {
            TextPreferenceItem(
                title = stringResource(id = R.string.settings_explore_section_order),
                subtitle = stringResource(id = R.string.settings_explore_section_order_desc)
            ) {
                viewModel.exploreSectionOrderState = viewModel.exploreSectionOrder.map { it.copy() }
                showExploreOrderDialog.value = true
            }

            ReorderDialog(
                showOrderingDialog = showExploreOrderDialog,
                list = viewModel.exploreSectionOrderState,
                title = stringResource(id = R.string.settings_explore_section_order),
                canDisable = true,
                onSettle = { from, to ->
                    viewModel.exploreSectionOrderState =
                        viewModel.exploreSectionOrderState.toMutableList().apply {
                            add(to, removeAt(from))
                        }
                },
                onConfirm = {
                    viewModel.updateExploreSectionSettings()
                    scope.launch { 
                        snackbar.showSnackbar(message = context.getString(R.string.restart_required_msg), withDismissAction = true)
                    }
                }
            )

            TextPreferenceItem(
                title = stringResource(id = R.string.settings_main_page_order),
                subtitle = stringResource(id = R.string.settings_main_page_order_desc)
            ) {
                viewModel.mainPageOrderState = viewModel.mainPageOrder.map { it.copy() }
                showMainPageOrderDialog.value = true
            }

            ReorderDialog(
                showOrderingDialog = showMainPageOrderDialog,
                list = viewModel.mainPageOrderState,
                title = stringResource(id = R.string.settings_main_page_order),
                canDisable = false,
                onSettle = { from, to ->
                    viewModel.mainPageOrderState =
                        viewModel.mainPageOrderState.toMutableList().apply {
                            add(to, removeAt(from))
                        }
                },
                onConfirm = {
                    viewModel.updateMainPageSettings()
                    scope.launch {
                        snackbar.showSnackbar(message = context.getString(R.string.restart_required_msg), withDismissAction = true)
                    }
                }
            )

        }
    }

    @Composable
    private fun <T: ContentOrder> ReorderDialog(
        showOrderingDialog: MutableState<Boolean>,
        list: List<ContentOrderData<T>>,
        title: String,
        canDisable: Boolean,
        onSettle: (fromIndex: Int, toIndex: Int) -> Unit,
        onConfirm: OnClick
    ) {
        ConfirmationDialog(
            openDialog = showOrderingDialog,
            title = title,
            text = {
                ReorderableColumn(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    list = list,
                    onSettle = onSettle
                ) { _, item, _ ->
                    key(item) {
                        val interactionSource = remember { MutableInteractionSource() }
                        Card(
                            modifier = Modifier.padding(vertical = 4.dp),
                            onClick = {},
                            interactionSource = interactionSource,
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                MediumText(
                                    stringResource(id = item.value.toStringRes()),
                                    Modifier.padding(horizontal = 8.dp)
                                )
                                Spacer(modifier = Modifier.weight(1f))

                                if(canDisable){
                                    val isEnabled = remember { mutableStateOf(item.isEnabled) }
                                    Switch(checked = isEnabled.value, onCheckedChange = {
                                        isEnabled.value = it
                                        item.isEnabled = it
                                    })
                                }

                                IconButton(
                                    modifier = Modifier.draggableHandle(
                                        interactionSource = interactionSource,
                                    ),
                                    onClick = {},
                                ) {
                                    Icon(
                                        imageVector = AppIcons.IcUnfold,
                                        contentDescription = "Reorder"
                                    )
                                }
                            }

                        }
                    }
                }
            },
            onConfirm = onConfirm
        )
    }

    override val titleRes: Int = I18nR.string.settings_general

    private fun coverImagePrefList(context: Context): List<ListPreferenceEntry<Int>> {
        return listOf(
            ListPreferenceEntry(
                title = context.getString(I18nR.string.low),
                value = MediaCoverImageModel.type_medium
            ),
            ListPreferenceEntry(
                title = context.getString(I18nR.string.medium),
                value = MediaCoverImageModel.type_large
            ),
            ListPreferenceEntry(
                title = context.getString(I18nR.string.high),
                value = MediaCoverImageModel.type_extra_large
            )
        )
    }

    private fun getLangs(context: Context): MutableList<ListPreferenceEntry<String>> {
        val langs = mutableListOf<ListPreferenceEntry<String>>()
        val parser = context.resources.getXml(I18nR.xml.locales_config)
        var eventType = parser.eventType
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.name == "locale") {
                for (i in 0 until parser.attributeCount) {
                    if (parser.getAttributeName(i) == "name") {
                        val langTag = parser.getAttributeValue(i)
                        val displayName = getDisplayName(langTag)
                        if (displayName.isNotEmpty()) {
                            langs.add(ListPreferenceEntry(title = displayName, value = langTag))
                        }
                    }
                }
            }
            eventType = parser.next()
        }

        langs.sortBy { it.title }
        langs.add(
            0,
            ListPreferenceEntry(
                title = context.getString(R.string.settings_default),
                value = ""
            )
        )

        return langs
    }
}
