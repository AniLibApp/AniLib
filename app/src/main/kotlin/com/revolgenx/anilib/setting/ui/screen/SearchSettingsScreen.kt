package com.revolgenx.anilib.setting.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import com.revolgenx.anilib.common.ui.component.action.ActionMenu
import com.revolgenx.anilib.common.ui.component.action.NavigationIcon
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayout
import com.revolgenx.anilib.common.ui.component.appbar.AppBarLayoutDefaults
import com.revolgenx.anilib.common.ui.component.scaffold.ScreenScaffold
import com.revolgenx.anilib.common.ui.component.search.SearchBar
import com.revolgenx.anilib.common.ui.component.search.SearchBarContainerHeight
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcCancel
import anilib.i18n.R as I18nR

object SearchSettingScreen : AndroidScreen() {

    @Composable
    override fun Content() {
        SearchSettingScreenContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchSettingScreenContent() {
    ScreenScaffold(
        topBar = {
            SearchSettingTopAppBar()
        }
    ) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSettingTopAppBar() {
    AppBarLayout(
        colors = AppBarLayoutDefaults.transparentColors(),
        containerHeight = SearchBarContainerHeight
    ) {
        Box(
            modifier = Modifier
                .height(SearchBarContainerHeight)
                .fillMaxWidth()
        ) {
            SearchBar(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(8.dp),

                query = "",
                onQueryChange = {

                },
                onSearch = {

                },
                active = false,
                onActiveChange = {

                },
                placeholder = { Text(text = stringResource(id = I18nR.string.settings_placeholder_search)) },
                leadingIcon = {
                    NavigationIcon()
                },
                trailingIcon = {
                    if ("viewModel.query".isNotEmpty()) {
                        ActionMenu(
                            icon = AppIcons.IcCancel,
                            contentDescriptionRes = I18nR.string.clear
                        ) {

                        }
                    }
                }
            )
        }
    }
}