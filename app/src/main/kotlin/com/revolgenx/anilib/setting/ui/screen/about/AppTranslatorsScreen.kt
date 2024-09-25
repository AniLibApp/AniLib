package com.revolgenx.anilib.setting.ui.screen.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.localContext
import com.revolgenx.anilib.common.ext.openUri
import com.revolgenx.anilib.common.ui.component.text.LightText
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcTranslate

private data class Translator(val language: String, val authors: String, val flag: Int)

private val translators = listOf(
    Translator(language = "ITALIANO", authors = "Yoshimitsu", flag = R.drawable.ic_flag_it),
    Translator(language = "हिंदी", authors = "Manoj Chetry (KcMj)", flag = R.drawable.ic_flag_hi),
    Translator(language = "Português", authors = "Satoru", flag = R.drawable.ic_flag_pt),
    Translator(
        language = "Español",
        authors = "ricardoric_03 | MrJako2001",
        flag = R.drawable.ic_flag_es
    ),
    Translator(language = "عربى", authors = "Sakugaky", flag = R.drawable.ic_flag_ar),
    Translator(language = "русский", authors = "Natalie", flag = R.drawable.ic_flag_ru),
    Translator(
        language = "Deutsch",
        authors = "André Niebuhr (Epr0m)",
        flag = R.drawable.ic_flag_de
    ),
    Translator(language = "Français", authors = "natsuthelight", flag = R.drawable.ic_flag_fr),
    Translator(language = "Türkçe", authors = "kyoya", flag = R.drawable.ic_flag_tr)
)


@Composable
fun AppTranslatorScreen() {
    val context = localContext()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    context.openUri("https://poeditor.com/join/project?hash=d9NRHxgZSb")
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = AppIcons.IcTranslate,
                contentDescription = null,
            )

            Column {
                MediumText(text = stringResource(id = anilib.i18n.R.string.translate)
                , fontSize = 16.sp)
                LightText(
                    text = stringResource(id = anilib.i18n.R.string.help_to_translate),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }


        translators.forEach {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(id = it.flag),
                    contentDescription = null,
                    tint = Color.Unspecified
                )

                Column {
                    MediumText(text = it.language, fontSize = 16.sp)
                    LightText(text = it.authors, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 14.sp)
                }
            }
        }
    }
}