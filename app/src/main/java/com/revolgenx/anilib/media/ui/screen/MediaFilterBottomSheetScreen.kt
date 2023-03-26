package com.revolgenx.anilib.media.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.androidx.AndroidScreen
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.component.dropdown.SelectableDropDownMenu
import org.koin.androidx.compose.koinViewModel
import java.util.Calendar

class MediaFilterBottomSheetScreen : AndroidScreen() {
    @Composable
    override fun Content() {
        val viewModel = koinViewModel<MediaFilterBottomSheetViewModel>()
        val windowInsets = WindowInsets.systemBars
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(windowInsets)
        ) {
            MediaFilterBottomSheetContent()
        }
    }
}


private val yearLesser = Calendar.getInstance().get(Calendar.YEAR) + 1
private val yearList by lazy {
    (yearLesser downTo 1940).map { it.toString() }
}


@Composable
private fun MediaFilterBottomSheetContent() {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(PaddingValues(horizontal = 8.dp)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val bottomSheetNavigator = LocalBottomSheetNavigator.current
            OutlinedButton(onClick = {
                bottomSheetNavigator.hide()
            }) {
                Text(stringResource(id = R.string.cancel))
            }
            Button(onClick = {
                bottomSheetNavigator.hide()
            }) {
                Text(stringResource(id = R.string.filter))
            }
        }
        LazyColumn() {
            item {
                SelectableDropDownMenu(
                    labelRes = R.string.status,
                    items = stringArrayResource(id = R.array.media_status).toList(),
                    selectedItemPosition = 0
                ) { selectedItem ->

                }
            }
            item {
                SelectableDropDownMenu(
                    labelRes = R.string.season,
                    items = stringArrayResource(id = R.array.media_season).toList(),
                    selectedItemPosition = 0
                ) { selectedItem ->

                }
            }
            item {
                SelectableDropDownMenu(
                    labelRes = R.string.sort,
                    items = stringArrayResource(id = R.array.media_sort).toList(),
                    selectedItemPosition = 0
                ) { selectedItem ->

                }
            }
            item {
                SelectableDropDownMenu(
                    labelRes = R.string.year,
                    items = yearList,
                    selectedItemPosition = 0
                ) { selectedItem ->

                }
            }
        }
    }
}


