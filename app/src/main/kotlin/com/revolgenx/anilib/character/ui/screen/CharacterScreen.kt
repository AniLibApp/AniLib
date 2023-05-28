package com.revolgenx.anilib.character.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.character.ui.viewmodel.CharacterAboutViewModel
import com.revolgenx.anilib.character.ui.viewmodel.CharacterMediaViewModel
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.screen.PagerScreen
import org.koin.androidx.compose.koinViewModel

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
    CharacterScreenPage(CharacterScreenPageType.ABOUT, R.string.about, R.drawable.ic_about),
    CharacterScreenPage(CharacterScreenPageType.MEDIA, R.string.media, R.drawable.ic_media_outline),
    CharacterScreenPage(
        CharacterScreenPageType.VOICE_ROLES,
        R.string.voices_roles,
        R.drawable.ic_voice
    )
)


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun CharacterScreenContent(characterId: Int) {
    val pagerState = rememberPagerState { pages.size }
    val aboutViewModel = koinViewModel<CharacterAboutViewModel>()
    val mediaViewModel = koinViewModel<CharacterMediaViewModel>()

    LaunchedEffect(aboutViewModel, mediaViewModel){
        aboutViewModel.field.characterId = characterId
        mediaViewModel.field.characterId = characterId
    }

    PagerScreenScaffold(
        pages = pages,
        pagerState = pagerState
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (pages[page].type) {
                CharacterScreenPageType.ABOUT -> CharacterAboutScreen(aboutViewModel)
                CharacterScreenPageType.MEDIA -> CharacterMediaScreen(mediaViewModel)
                CharacterScreenPageType.VOICE_ROLES -> CharacterActorScreen()
            }
        }
    }
}