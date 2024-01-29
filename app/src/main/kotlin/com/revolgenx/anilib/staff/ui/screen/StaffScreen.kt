package com.revolgenx.anilib.staff.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.revolgenx.anilib.common.ui.screen.voyager.AndroidScreen
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ext.horizontalBottomWindowInsets
import com.revolgenx.anilib.common.ext.horizontalWindowInsets
import com.revolgenx.anilib.common.ui.component.action.OpenInBrowserOverflowMenu
import com.revolgenx.anilib.common.ui.component.action.OverflowMenu
import com.revolgenx.anilib.common.ui.component.action.ShareOverflowMenu
import com.revolgenx.anilib.common.ui.component.scaffold.PagerScreenScaffold
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcAbout
import com.revolgenx.anilib.common.ui.icons.appicon.IcGroup
import com.revolgenx.anilib.common.ui.icons.appicon.IcMedia
import com.revolgenx.anilib.common.ui.screen.pager.PagerScreen
import com.revolgenx.anilib.staff.ui.viewmodel.StaffAboutViewModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffMediaCharacterViewModel
import com.revolgenx.anilib.staff.ui.viewmodel.StaffMediaRoleViewModel
import org.koin.androidx.compose.koinViewModel
import anilib.i18n.R as I18nR

class StaffScreen(val staffId: Int) : AndroidScreen() {
    @Composable
    override fun Content() {
        StaffScreenContent(staffId)
    }
}


private typealias StaffScreenPage = PagerScreen<StaffScreenPageType>

enum class StaffScreenPageType {
    ABOUT,
    VOICE_ROLES,
    STAFF_ROLES
}

private val pages = listOf(
    StaffScreenPage(StaffScreenPageType.ABOUT, I18nR.string.about, AppIcons.IcAbout),
    StaffScreenPage(StaffScreenPageType.VOICE_ROLES, I18nR.string.voice_roles, AppIcons.IcMedia),
    StaffScreenPage(
        StaffScreenPageType.STAFF_ROLES,
        I18nR.string.staff_roles,
        AppIcons.IcGroup
    )
)

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun StaffScreenContent(staffId: Int) {
    val pagerState = rememberPagerState { pages.size }

    val aboutViewModel: StaffAboutViewModel = koinViewModel()
    val mediaCharacterViewModel: StaffMediaCharacterViewModel = koinViewModel()
    val mediaRoleViewModel: StaffMediaRoleViewModel = koinViewModel()

    aboutViewModel.field.staffId = staffId
    mediaCharacterViewModel.field.staffId = staffId
    mediaRoleViewModel.field.staffId = staffId

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
                StaffScreenPageType.ABOUT -> StaffAboutScreen(aboutViewModel)
                StaffScreenPageType.VOICE_ROLES -> StaffMediaCharacterScreen(mediaCharacterViewModel)
                StaffScreenPageType.STAFF_ROLES -> StaffMediaRoleScreen(mediaRoleViewModel)
            }
        }
    }
}

