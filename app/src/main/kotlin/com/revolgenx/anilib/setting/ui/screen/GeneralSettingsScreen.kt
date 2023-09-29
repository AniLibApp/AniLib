package com.revolgenx.anilib.setting.ui.screen

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.core.os.LocaleListCompat
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.util.getDisplayName
import com.revolgenx.anilib.setting.ui.component.ListPreferenceEntry
import com.revolgenx.anilib.setting.ui.model.PreferenceModel
import org.xmlpull.v1.XmlPullParser
import anilib.i18n.R as I18nR

object GeneralSettingsScreen : PreferenceScreen() {

    override val titleRes: Int = I18nR.string.settings_general

    @Composable
    override fun getPreferences(): List<PreferenceModel> {
        val context = localContext()
        val langs = remember { getLangs(context) }
        val currentLanguage = remember {
            mutableStateOf(
                AppCompatDelegate.getApplicationLocales().get(0)?.toLanguageTag() ?: ""
            )
        }
        LaunchedEffect(currentLanguage) {
            val locale = if (currentLanguage.value.isEmpty()) {
                LocaleListCompat.getEmptyLocaleList()
            } else {
                LocaleListCompat.forLanguageTags(currentLanguage.value)
            }
            AppCompatDelegate.setApplicationLocales(locale)
        }
        return listOf(
            PreferenceModel.ListPreferenceModel(
                prefState = currentLanguage,
                title = stringResource(anilib.i18n.R.string.settings_general_app_language),
                entries = langs,
                onValueChanged = { newValue ->
                    currentLanguage.value = newValue!!
                    true
                },
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
