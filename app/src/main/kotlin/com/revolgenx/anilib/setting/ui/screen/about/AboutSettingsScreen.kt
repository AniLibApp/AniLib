package com.revolgenx.anilib.setting.ui.screen.about

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import anilib.i18n.R
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen

object AboutSettingsScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        AboutScreenContent()
    }
}


private typealias CharacterScreenPage = PagerScreen<CharacterScreenPageType>


private enum class CharacterScreenPageType {
    ABOUT,
    LICENSES,
    TRANSLATORS
}

private val pages = listOf(
    CharacterScreenPage(CharacterScreenPageType.ABOUT, R.string.about),
    CharacterScreenPage(CharacterScreenPageType.LICENSES, R.string.licenses),
    CharacterScreenPage(CharacterScreenPageType.TRANSLATORS, R.string.translators)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutScreenContent() {
    val pagerState = rememberPagerState { pages.size }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    PagerScreenScaffold(
        pages = pages,
        pagerState = pagerState,
        scrollBehavior = scrollBehavior,
        contentWindowInsets = horizontalBottomWindowInsets()
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .padding(8.dp)
        ) {
            when (pages[page].type) {
                CharacterScreenPageType.ABOUT -> AppAboutScreen()
                CharacterScreenPageType.LICENSES -> AppLibrariesScreen()
                CharacterScreenPageType.TRANSLATORS -> AppAboutScreen()
            }
        }
    }

}