package com.revolgenx.anilib.common.ui.bottomsheet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import anilib.i18n.R
import com.dokar.sheets.BottomSheetState
import com.dokar.sheets.PeekHeight
import com.dokar.sheets.m3.BottomSheet
import com.dokar.sheets.m3.BottomSheetDefaults
import com.revolgenx.anilib.common.ui.component.text.MarkdownText
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcStar


@Composable
fun WhatsNewBottomSheet(
    bottomSheetState: BottomSheetState
) {
    BottomSheet(
        state = bottomSheetState,
        peekHeight = PeekHeight.fraction(0.7f),
        behaviors = BottomSheetDefaults.dialogSheetBehaviors(lightNavigationBar = true)
    ) {
        WhatsNewContent()
    }
}

@Composable
private fun WhatsNewContent() {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = AppIcons.IcStar, contentDescription = null)
            Icon(imageVector = AppIcons.IcStar, contentDescription = null)
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                text = stringResource(id = R.string.whats_new),
                style = MaterialTheme.typography.headlineMedium
            )
            Icon(imageVector = AppIcons.IcStar, contentDescription = null)
            Icon(imageVector = AppIcons.IcStar, contentDescription = null)
        }

        Text(
            modifier = Modifier.padding(vertical = 8.dp),
            text = stringResource(id = R.string.no_watching_feature_desc),
            color = Color(0xFFEED202),
            fontSize = 12.sp
        )

        MarkdownText(
            text = stringResource(id = com.revolgenx.anilib.R.string.release_info)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WhatsNewContentPreview() {
    WhatsNewContent()
}