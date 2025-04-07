package com.revolgenx.anilib.common.ui.bottomsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.revolgenx.anilib.common.ui.component.text.SemiBoldText
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcStar


@Composable
fun WhatsNewBottomSheet(
    bottomSheetState: BottomSheetState
) {
    BottomSheet(
        state = bottomSheetState,
        peekHeight = PeekHeight.fraction(0.8f),
        behaviors = BottomSheetDefaults.dialogSheetBehaviors(navigationBarColor = BottomSheetDefaults.backgroundColor)
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
            .verticalScroll(rememberScrollState())
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

        SemiBoldText(
            modifier = Modifier.padding(vertical = 8.dp).padding(bottom = 4.dp),
            text = stringResource(id = R.string.no_watching_feature_desc),
            fontSize = 12.sp,
            maxLines = Int.MAX_VALUE
        )

        MarkdownText(
            text = stringResource(id = com.revolgenx.anilib.R.string.release_info),
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WhatsNewContentPreview() {
    WhatsNewContent()
}