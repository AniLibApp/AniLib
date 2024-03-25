package com.revolgenx.anilib.social.ui.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcBold
import com.revolgenx.anilib.common.ui.icons.appicon.IcCenter
import com.revolgenx.anilib.common.ui.icons.appicon.IcCode
import com.revolgenx.anilib.common.ui.icons.appicon.IcHeader
import com.revolgenx.anilib.common.ui.icons.appicon.IcImage
import com.revolgenx.anilib.common.ui.icons.appicon.IcItalics
import com.revolgenx.anilib.common.ui.icons.appicon.IcLink
import com.revolgenx.anilib.common.ui.icons.appicon.IcList
import com.revolgenx.anilib.common.ui.icons.appicon.IcOrderedList
import com.revolgenx.anilib.common.ui.icons.appicon.IcPaste
import com.revolgenx.anilib.common.ui.icons.appicon.IcQuote
import com.revolgenx.anilib.common.ui.icons.appicon.IcSpoiler
import com.revolgenx.anilib.common.ui.icons.appicon.IcStrikeThrough
import com.revolgenx.anilib.common.ui.icons.appicon.IcVideo
import com.revolgenx.anilib.common.ui.icons.appicon.IcYoutube
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.common.util.OnClickWithValue


@Composable
fun MarkdownEditor(
    modifier: Modifier = Modifier,
    onClick: OnClickWithValue<Pair<String, Int>>
) {
    val clipboardManager = LocalClipboardManager.current
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {
        MarkdownButton(imageVector = AppIcons.IcPaste){
            clipboardManager.getText()?.text?.let {
                onClick(it to 0)
            }
        }
        MarkdownButton(imageVector = AppIcons.IcBold) {
            onClick("____" to 2)
        }
        MarkdownButton(imageVector = AppIcons.IcItalics) {
            onClick("__" to 1)
        }
        MarkdownButton(imageVector = AppIcons.IcStrikeThrough) {
            onClick("~~~~" to 2)
        }
        MarkdownButton(imageVector = AppIcons.IcSpoiler) {
            onClick("~!!~" to 2)
        }
        MarkdownButton(imageVector = AppIcons.IcLink) {
            onClick("[link]()" to 1)
        }
        MarkdownButton(imageVector = AppIcons.IcImage) {
            onClick("img220()" to 1)
        }
        MarkdownButton(imageVector = AppIcons.IcYoutube) {
            onClick("youtube()" to 1)
        }
        MarkdownButton(imageVector = AppIcons.IcVideo) {
            onClick("webm()" to 1)
        }
        MarkdownButton(imageVector = AppIcons.IcOrderedList) {
            onClick("1. " to 0)
        }
        MarkdownButton(imageVector = AppIcons.IcList) {
            onClick("- " to 0)
        }
        MarkdownButton(imageVector = AppIcons.IcHeader) {
            onClick("# " to 0)
        }
        MarkdownButton(imageVector = AppIcons.IcCenter) {
            onClick("~~~~~~" to 3)
        }
        MarkdownButton(imageVector = AppIcons.IcQuote) {
            onClick(">" to 0)
        }
        MarkdownButton(imageVector = AppIcons.IcCode) {
            onClick("``" to 1)
        }
    }
}

@Composable
private fun MarkdownButton(imageVector: ImageVector, onClick: OnClick) {
    IconButton(onClick = onClick) {
        Icon(imageVector = imageVector, contentDescription = null)
    }
}