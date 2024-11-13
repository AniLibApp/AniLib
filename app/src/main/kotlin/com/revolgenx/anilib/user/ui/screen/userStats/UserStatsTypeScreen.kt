package com.revolgenx.anilib.user.ui.screen.userStats

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.app.ui.viewmodel.ScrollTarget
import com.revolgenx.anilib.app.ui.viewmodel.ScrollViewModel
import com.revolgenx.anilib.common.ext.browseGenreScreen
import com.revolgenx.anilib.common.ext.browseTagScreen
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ext.staffScreen
import com.revolgenx.anilib.common.ext.studioScreen
import com.revolgenx.anilib.common.ui.component.common.HeaderText
import com.revolgenx.anilib.common.ui.component.image.ImageAsync
import com.revolgenx.anilib.common.ui.component.image.ImageOptions
import com.revolgenx.anilib.common.ui.compose.paging.LazyPagingList
import com.revolgenx.anilib.common.ui.composition.localNavigator
import com.revolgenx.anilib.common.ui.screen.state.EmptyScreen
import com.revolgenx.anilib.common.ui.screen.state.ResourceScreen
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.media.ui.model.isAnime
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.user.ui.model.statistics.BaseStatisticModel
import com.revolgenx.anilib.user.ui.model.statistics.UserGenreStatisticModel
import com.revolgenx.anilib.user.ui.model.statistics.UserStaffStatisticModel
import com.revolgenx.anilib.user.ui.model.statistics.UserStudioStatisticModel
import com.revolgenx.anilib.user.ui.model.statistics.UserTagStatisticModel
import com.revolgenx.anilib.user.ui.model.statistics.UserVoiceActorStatisticModel
import com.revolgenx.anilib.user.ui.viewmodel.userStats.UserStatsTypeViewModel
import kotlinx.coroutines.flow.collectLatest
import anilib.i18n.R as I18nR

@Composable
fun UserStatsTypeScreen(
    type: MediaType,
    viewModel: UserStatsTypeViewModel,
    scrollViewModel: ScrollViewModel
) {
    val navigator = localNavigator()
    LaunchedEffect(viewModel) {
        viewModel.getResource()
    }

    val listScrollState = rememberLazyListState()

    LaunchedEffect(scrollViewModel) {
        scrollViewModel.scrollEventFor(ScrollTarget.USER).collectLatest {
            listScrollState.animateScrollToItem(0)
        }
    }

    val isAnime = type.isAnime

    ResourceScreen(viewModel = viewModel) {
        if (it.isEmpty()) {
            EmptyScreen()
        }else{
            LazyPagingList(
                items = it,
                scrollState = listScrollState,
                onRefresh = {
                    viewModel.refresh()
                }
            ) {
                val model = it ?: return@LazyPagingList
                UserStatsTypeItem(isAnime, model){
                    when (model) {
                        is UserGenreStatisticModel -> {
                            model.genre?.let { navigator.browseGenreScreen(model.genre) }
                        }
                        is UserTagStatisticModel -> model.tag?.name?.let { navigator.browseTagScreen(it) }
                        is UserStudioStatisticModel -> model.studio?.id?.let { navigator.studioScreen(it) }
                        is UserVoiceActorStatisticModel -> model.voiceActor?.id?.let { navigator.staffScreen(it) }
                        is UserStaffStatisticModel -> model.staff?.id?.let { navigator.staffScreen(it) }
                    }
                }
            }
        }
    }
}


@Composable
private fun UserStatsTypeItem(isAnime: Boolean, model: BaseStatisticModel, onClick: OnClick) {
    val header = when (model) {
        is UserGenreStatisticModel -> model.genre
        is UserTagStatisticModel -> model.tag?.name
        is UserStudioStatisticModel -> model.studio?.name
        is UserVoiceActorStatisticModel -> model.voiceActor?.name?.full
        is UserStaffStatisticModel -> model.staff?.name?.full
        else -> null
    }

    val image = when (model) {
        is UserVoiceActorStatisticModel -> model.voiceActor?.image?.image
        is UserStaffStatisticModel -> model.staff?.image?.image
        else -> null
    }

    OutlinedCard(
        modifier = Modifier
            .padding(8.dp),
        onClick = onClick
    ) {
        Row {
            image?.let {
                ImageAsync(
                    modifier = Modifier
                        .height(110.dp)
                        .width(72.dp),
                    imageUrl = it,
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    ),
                    previewPlaceholder = R.drawable.bleach
                )
            }
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                StatsHeaderText(text = header)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StatsTypeCardItem(labelId = I18nR.string.count, text = model.count.toString())
                    Spacer(modifier = Modifier.size(2.dp))
                    StatsTypeCardItem(
                        labelId = I18nR.string.mean_score,
                        text = "${model.meanScore}%"
                    )
                    Spacer(modifier = Modifier.size(2.dp))
                    StatsTypeCardItem(
                        labelId = if (isAnime) I18nR.string.time_watched else I18nR.string.chapters_read,
                        text = if (isAnime) stringResource(id = I18nR.string.d_day_d_hour).format(
                            model.day,
                            model.hour
                        ) else model.chaptersRead.naText()
                    )
                }
            }
        }
    }
}

@Composable
fun StatsTypeCardItem(@StringRes labelId: Int, text: String) {
    Column {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.1.sp,
            maxLines = 1
        )
        Text(
            text = stringResource(id = labelId),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatsHeaderText(text: String?) {
    text ?: return
    HeaderText(text = text)
}