package com.revolgenx.anilib.setting.ui.screen

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.common.util.getDisplayName
import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import com.revolgenx.anilib.setting.ui.component.ListPreferenceItem
import com.revolgenx.anilib.setting.ui.model.PreferenceModel
import com.revolgenx.anilib.setting.ui.viewmodel.GeneralSettingsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.xmlpull.v1.XmlPullParser
import anilib.i18n.R as I18nR

object GeneralSettingsScreen : PreferencesScreen() {

    @Composable
    override fun PreferencesContent() {
        val context = localContext()
        val scope = rememberCoroutineScope()
        val viewModel: GeneralSettingsViewModel = koinViewModel()

        val appDataStore = viewModel.generalPreferencesDataStore
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

        ListPreferenceItem(value = currentLanguage.value, title = stringResource(anilib.i18n.R.string.settings_general_app_language), entries = langs) { newValue->
            currentLanguage.value = newValue!!
        }

        val mediaCoverImageType = appDataStore.mediaCoverImageType.collectAsState()

        ListPreferenceItem(
            value = mediaCoverImageType.value,
            title = stringResource(anilib.i18n.R.string.cover_image_quality),
            entries = imageQualityEntry
        ) { newValue->
            scope.launch{
                appDataStore.mediaCoverImageType.set(newValue)
            }
        }

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
                title = context.getString(anilib.i18n.R.string.settings_default),
                value = ""
            )
        )

        return langs
    }
}
