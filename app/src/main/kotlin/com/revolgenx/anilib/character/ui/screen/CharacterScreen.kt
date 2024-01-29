package com.revolgenx.anilib.character.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.ui.viewmodel.CharacterAboutViewModel
import com.revolgenx.anilib.character.ui.viewmodel.CharacterActorViewModel
import com.revolgenx.anilib.character.ui.viewmodel.CharacterMediaViewModel
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.horizontalWindowInsets
import com.revolgenx.anilib.common.ui.component.action.OpenInBrowserOverflowMenu
import com.revolgenx.anilib.common.ui.component.action.OverflowMenu
import com.revolgenx.anilib.common.ui.component.action.ShareOverflowMenu
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcAbout
import com.revolgenx.anilib.common.ui.icons.appicon.IcMedia
import com.revolgenx.anilib.common.ui.icons.appicon.IcVoice
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

class CharacterScreen(val characterId: Int) : AndroidScreen() {
    @Composable
    override fun Content() {
        CharacterScreenContent(characterId)
    }
}


private typealias CharacterScreenPage = PagerScreen<CharacterScreenPageType>


private enum class CharacterScreenPageType {
    ABOUT,
    MEDIA,
    VOICE_ROLES
}

private val pages = listOf(
    CharacterScreenPage(CharacterScreenPageType.ABOUT, I18nR.string.about, AppIcons.IcAbout),
    CharacterScreenPage(CharacterScreenPageType.MEDIA, I18nR.string.media, AppIcons.IcMedia),
    CharacterScreenPage(
        CharacterScreenPageType.VOICE_ROLES,
        I18nR.string.voice_roles,
        AppIcons.IcVoice
    )
)


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun CharacterScreenContent(characterId: Int) {
    val pagerState = rememberPagerState { pages.size }
    val aboutViewModel: CharacterAboutViewModel = koinViewModel()
    aboutViewModel.field.characterId = characterId

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    PagerScreenScaffold(
        pages = pages,
        pagerState = pagerState,
        scrollBehavior = scrollBehavior,
        actions = {
            aboutViewModel.resource.value?.stateValue?.siteUrl?.let {site->
                OverflowMenu {
                    OpenInBrowserOverflowMenu(link = site)
                    ShareOverflowMenu(text = site)
                }
            }
        },
        contentWindowInsets = horizontalBottomWindowInsets()
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            when (pages[page].type) {
                CharacterScreenPageType.ABOUT -> CharacterAboutScreen(aboutViewModel)
                CharacterScreenPageType.MEDIA -> CharacterMediaScreen(characterId)
                CharacterScreenPageType.VOICE_ROLES -> CharacterActorScreen(characterId)
            }
        }
    }
}