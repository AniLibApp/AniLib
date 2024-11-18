package com.revolgenx.anilib.widget.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.components.CircleIconButton
import androidx.glance.appwidget.cornerRadius
import androidx.glance.appwidget.lazy.LazyColumn
import androidx.glance.appwidget.lazy.items
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.material3.ColorProviders
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.revolgenx.anilib.R
import com.revolgenx.anilib.airing.data.service.AiringScheduleServiceImpl
import com.revolgenx.anilib.app.ui.activity.MainActivity
import com.revolgenx.anilib.common.data.constant.dateFormat
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.data.store.theme.WidgetThemeDataStore
import com.revolgenx.anilib.common.ext.naText
import com.revolgenx.anilib.common.ui.activity.BaseMainActivity
import com.revolgenx.anilib.common.ui.component.image.coilImageLoader
import com.revolgenx.anilib.common.ui.theme.defaultDarkTheme
import com.revolgenx.anilib.common.ui.theme.defaultTheme
import com.revolgenx.anilib.media.ui.model.MediaCoverImageModel
import com.revolgenx.anilib.type.MediaType
import com.revolgenx.anilib.widget.viewmodel.AiringWidgetResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.format.TextStyle as TimeTextStyle


class AiringScheduleWidget : GlanceAppWidget(), KoinComponent {
    private val appPreferencesDataStore: AppPreferencesDataStore = get()
    private val airingWidgetResource: AiringWidgetResource = get()
    private val widgetThemeDataStore: WidgetThemeDataStore = get()

    companion object {
        private val mediaIdKey = ActionParameters.Key<Int>(
            BaseMainActivity.WIDGET_MEDIA_ID_KEY
        )
        private val mediaTypeKey = ActionParameters.Key<Int>(
            BaseMainActivity.WIDGET_MEDIA_TYPE_KEY
        )
        private val airingScheduleKey = ActionParameters.Key<Boolean>(
            BaseMainActivity.WIDGET_AIRING_SCHEDULE_SCREEN_KEY
        )
    }

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            LaunchedEffect(airingWidgetResource) {
                airingWidgetResource.watchSettings()
            }

            CompositionLocalProvider(LocalContext provides context) {
                val widgetTheme = widgetThemeDataStore.collectAsState().value
                GlanceTheme(colors = ColorProviders(widgetTheme.darkColorScheme)) {
                    Box(
                        modifier = GlanceModifier
                            .fillMaxSize()
                            .appWidgetBackground()
                            .background(GlanceTheme.colors.surface)
                    ) {
                        airingWidgetResource.getResource()
                        Column {
                            Row(
                                modifier = GlanceModifier.fillMaxWidth()
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    GlanceText(
                                        text = airingWidgetResource.startDateTime.dayOfWeek.getDisplayName(
                                            TimeTextStyle.FULL,
                                            Locale.getDefault()
                                        ),
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 18.sp
                                    )

                                    GlanceText(
                                        text = airingWidgetResource.startDateTime.format(
                                            DateTimeFormatter.ofPattern(
                                                dateFormat
                                            )
                                        ),
                                        fontSize = 10.sp,
                                        color = GlanceTheme.colors.onSurfaceVariant
                                    )
                                }
                                Spacer(modifier = GlanceModifier.defaultWeight())
                                CircleIconButton(
                                    backgroundColor = null,
                                    imageProvider = ImageProvider(R.drawable.ic_open_in_new),
                                    contentDescription = null,
                                    onClick = actionStartActivity<MainActivity>(
                                        actionParametersOf(
                                            airingScheduleKey to true,
                                        )
                                    )
                                )
                                CircleIconButton(
                                    backgroundColor = null,
                                    imageProvider = ImageProvider(R.drawable.ic_refresh),
                                    contentDescription = null,
                                    onClick = {
                                        airingWidgetResource.refresh()
                                    }
                                )
                            }



                            when (val resourceValue = airingWidgetResource.resource.value) {
                                is ResourceState.Error -> {
                                    Box(
                                        modifier = GlanceModifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Image(
                                                provider = ImageProvider(R.drawable.ic_error_anilib),
                                                contentDescription = null
                                            )

                                            GlanceText(text = context.getString(anilib.i18n.R.string.something_went_wrong))

                                            resourceValue.message?.let {
                                                GlanceText(text = it)
                                            }

                                        }
                                    }
                                }

                                is ResourceState.Loading -> {
                                    Box(
                                        modifier = GlanceModifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }

                                is ResourceState.Success -> {
                                    val data = resourceValue.stateValue ?: return@Column
                                    val items = data.data ?: return@Column

                                    if(items.isEmpty()){
                                        Box(
                                            modifier = GlanceModifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Image(
                                                    provider = ImageProvider(R.drawable.ic_sad_anilib),
                                                    contentDescription = null
                                                )

                                                GlanceText(text = context.getString(anilib.i18n.R.string.its_empty))

                                            }
                                        }
                                        return@Column
                                    }

                                    LazyColumn(
                                        modifier = GlanceModifier.padding(horizontal = 10.dp)
                                    ) {
                                        items(items = items, itemId = { it.id.toLong() }) { item ->
                                            val media = item.media ?: return@items
                                            val mediaTitleType =
                                                appPreferencesDataStore.mediaTitleType.collectAsState()

                                            Row(
                                                modifier = GlanceModifier.padding(4.dp)
                                                    .clickable(
                                                        actionStartActivity<MainActivity>(
                                                            actionParametersOf(
                                                                mediaIdKey to media.id,
                                                                mediaTypeKey to (media.type?.ordinal
                                                                    ?: MediaType.ANIME.ordinal)
                                                            )
                                                        )
                                                    )
                                                    .height(AiringWidgetHeight)
                                                    .fillMaxWidth(),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                GlanceImage(
                                                    modifier = GlanceModifier.size(ImageSize)
                                                        .cornerRadius(12.dp),
                                                    context = context,
                                                    url = media.coverImage?.image(
                                                        MediaCoverImageModel.type_large
                                                    )
                                                )

                                                Column(
                                                    modifier = GlanceModifier
                                                        .padding(vertical = 2.dp)
                                                        .padding(start = 6.dp)
                                                        .fillMaxHeight()
                                                ) {
                                                    GlanceText(
                                                        text = media.title?.title(mediaTitleType.value!!)
                                                            .naText(),
                                                        fontWeight = FontWeight.Medium,
                                                        fontSize = 16.sp
                                                    )

                                                    Spacer(modifier = GlanceModifier.defaultWeight())

                                                    if (item.timeUntilAiringModel.alreadyAired) {
                                                        val airedAtString =
                                                            context.getString(anilib.i18n.R.string.ep_s_aired_at)
                                                                .format(
                                                                    item.episode,

                                                                    )
                                                        GlanceText(
                                                            text = airedAtString,
                                                            fontSize = 11.sp
                                                        )

                                                        GlanceText(
                                                            text = item.airingAtModel.airedAt,
                                                            fontSize = 14.sp,
                                                            fontWeight = FontWeight.Medium,
                                                            color = GlanceTheme.colors.onSurfaceVariant
                                                        )
                                                    } else {
                                                        val airedAtString =
                                                            context.getString(anilib.i18n.R.string.ep_s_airing_in)
                                                                .format(item.episode)
                                                        GlanceText(
                                                            text = airedAtString,
                                                            fontSize = 11.sp
                                                        )

                                                        Row(
                                                            modifier = GlanceModifier.fillMaxWidth()
                                                        ) {
                                                            GlanceText(
                                                                text = item.timeUntilAiringModel.formatWidgetString(
                                                                    context
                                                                ),
                                                                fontSize = 14.sp,
                                                                fontWeight = FontWeight.Medium,
                                                                color = GlanceTheme.colors.onSurfaceVariant
                                                            )

                                                            Spacer(modifier = GlanceModifier.defaultWeight())

                                                            GlanceText(
                                                                text = item.airingAtModel.airingTime(
                                                                    true
                                                                ),
                                                                fontSize = 11.sp
                                                            )
                                                        }
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }

                                else -> {}
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun GlanceImage(
    modifier: GlanceModifier,
    context: Context, url: String?
) {

    val imageProvider = remember {
        mutableStateOf<ImageProvider?>(null)
    }

    LaunchedEffect(url) {
        val imageResult =
            context.coilImageLoader.execute(ImageRequest.Builder(context).data(url).build())
        imageProvider.value = when (imageResult) {
            is ErrorResult -> null
            is SuccessResult -> ImageProvider(imageResult.drawable.toBitmap())
        }
    }

    Box(
        modifier = modifier
    ) {
        val provider = imageProvider.value

        provider ?: return@Box
        Image(
            modifier = GlanceModifier.fillMaxSize(),
            provider = provider, contentDescription = null,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun GlanceText(
    modifier: GlanceModifier = GlanceModifier,
    text: String,
    fontSize: TextUnit? = null,
    fontWeight: FontWeight = FontWeight.Normal,
    color: ColorProvider = GlanceTheme.colors.onSurface,
    maxLines: Int = 1
) {
    Text(
        modifier = modifier,
        text = text,
        style = TextStyle(
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = color
        ),
        maxLines = maxLines
    )
}

private val AiringWidgetHeight = 72.dp
private val ImageSize = 60.dp