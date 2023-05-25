package com.revolgenx.anilib.staff.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.screen.PagerScreen

class StaffScreen(val staffId: Int) : AndroidScreen() {
    @Composable
    override fun Content() {
        StaffScreenContent(staffId)
    }
}


private typealias StaffScreenPage = PagerScreen<StaffScreenPageType>

enum class StaffScreenPageType {
    ABOUT,
    MEDIA,
    VOICE_ROLES
}

private val pages = listOf(
    StaffScreenPage(StaffScreenPageType.ABOUT, R.string.about, R.drawable.ic_about),
    StaffScreenPage(StaffScreenPageType.MEDIA, R.string.media, R.drawable.ic_media_outline),
    StaffScreenPage(
        StaffScreenPageType.VOICE_ROLES,
        R.string.voices_roles,
        R.drawable.ic_voice
    )
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun StaffScreenContent(staffId: Int) {
    PagerScreenScaffold(
        pages = pages
    ) { page ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            when (pages[page].type) {
                StaffScreenPageType.ABOUT -> StaffAboutScreen(staffId)
                StaffScreenPageType.MEDIA -> StaffMediaCharacterScreen()
                StaffScreenPageType.VOICE_ROLES -> StaffMediaRoleScreen()
            }
        }
    }
}

