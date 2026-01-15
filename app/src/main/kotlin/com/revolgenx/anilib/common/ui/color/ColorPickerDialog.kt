package com.revolgenx.anilib.common.ui.color

import androidx.annotation.FloatRange
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.toColorInt
import anilib.i18n.R
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ui.component.text.MediumText
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcCheck
import com.revolgenx.anilib.common.ui.icons.appicon.IcChevronLeft
import com.revolgenx.anilib.common.ui.icons.appicon.IcChevronRight
import com.revolgenx.anilib.common.util.colorHex
import kotlinx.coroutines.launch
import org.koin.compose.koinInject
import kotlin.math.max
import android.graphics.Color as AndroidColor

@Composable
fun ColorPickerDialog(
    openDialog: MutableState<Boolean>,
    title: String,
    selectedColor: Int,
    colorsInt: Array<Int>,
    onColorSelected: (color: Int) -> Unit
) {
    if (openDialog.value) {
        val scope = rememberCoroutineScope()
        val appPreferencesDataStore: AppPreferencesDataStore = koinInject()

        val recentColorPresets =
            appPreferencesDataStore.recentColorPresets.collectAsState().value!!.let {
                if (it.isEmpty()) {
                    emptyArray()
                } else {
                    it.split(",")
                        .map(String::toInt)
                        .toTypedArray()
                }
            }
        var customMode by remember {
            mutableStateOf(false)
        }

        val selectedTemplateColor = remember {
            mutableIntStateOf(selectedColor)
        }

        var selectedCustomColor = remember {
            selectedColor
        }

        Dialog(onDismissRequest = {
            openDialog.value = false
        }, content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { openDialog.value = false }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .animateContentSize()
                        .padding(vertical = 24.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { }
                        ),
                    color = AlertDialogDefaults.containerColor,
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(modifier = Modifier.padding(vertical = 24.dp, horizontal = 12.dp)) {
                        Text(
                            modifier = Modifier.padding(bottom = 16.dp),
                            text = title,
                            style = MaterialTheme.typography.headlineSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Column(
                            modifier = Modifier.weight(1f, fill = false)
                        ) {
                            if (customMode) {
                                ColorPickerCustom(
                                    selectedColor = selectedCustomColor,
                                    onColorSelected = {
                                        selectedCustomColor = it
                                    }
                                )
                            } else {
                                ColorPickerTemplate(
                                    colors = colorsInt,
                                    selectedColor = selectedTemplateColor.intValue,
                                    recentPresetColors = recentColorPresets,
                                    onColorSelected = {
                                        selectedTemplateColor.intValue = it
                                    }
                                )
                            }
                        }

                        ProvideTextStyle(value = MaterialTheme.typography.labelLarge) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp)
                            ) {
                                TextButton(onClick = { customMode = !customMode }) {
                                    Text(text = stringResource(id = if (customMode) R.string.presets else R.string.custom))
                                }

                                Spacer(modifier = Modifier.weight(1f))
                                TextButton(onClick = { openDialog.value = false }) {
                                    Text(text = stringResource(id = R.string.cancel))
                                }
                                TextButton(onClick = {
                                    scope.launch {
                                        val newSelectedColor =
                                            if (customMode) selectedCustomColor else selectedTemplateColor.intValue

                                        val newRecentColorPresets =
                                            recentColorPresets.toMutableList().let {
                                                it.remove(newSelectedColor)
                                                it.add(0, newSelectedColor)
                                                it.take(8)
                                            }

                                        appPreferencesDataStore.recentColorPresets.set(
                                            newRecentColorPresets.joinToString(separator = ",")
                                        )
                                        onColorSelected(newSelectedColor)
                                        openDialog.value = false
                                    }
                                }) {
                                    Text(text = stringResource(id = R.string.pick))
                                }
                            }
                        }

                    }
                }
            }
        })
    }

}

@Composable
private fun ColorPickerTemplate(
    colors: Array<Int>,
    selectedColor: Int,
    recentPresetColors: Array<Int>,
    onColorSelected: (color: Int) -> Unit
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        ColorBoxFlowRow(colors, onColorSelected, selectedColor)
        if (recentPresetColors.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(vertical = 12.dp),
                text = stringResource(id = R.string.recent)
            )
            ColorBoxFlowRow(recentPresetColors, onColorSelected, selectedColor)
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun ColorBoxFlowRow(
    colors: Array<Int>,
    onColorSelected: (color: Int) -> Unit,
    selectedColor: Int
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        colors.forEach { color ->
            ColorBox(color, onColorSelected, selectedColor)
        }
    }
}

@Composable
private fun ColorBox(
    color: Int,
    onColorSelected: (color: Int) -> Unit,
    selectedColor: Int
) {
    OutlinedCard(modifier = Modifier
        .size(48.dp)
        .fillMaxSize(),
        colors = CardDefaults.outlinedCardColors(containerColor = Color(color)),
        onClick = {
            onColorSelected(color)
        }
    ) {
        if (selectedColor == color) {
            Box(modifier = Modifier.fillMaxSize()) {
                FilledIconButton(modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.Center),
                    onClick = { }) {
                    Icon(
                        imageVector = AppIcons.IcCheck,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}


@Composable
private fun ColorPickerCustom(selectedColor: Int, onColorSelected: (color: Int) -> Unit) {
    var previewColor by remember {
        mutableStateOf(Color(selectedColor))
    }

    val updateAllColorSpace = remember {
        mutableStateOf(false)
    }

    val colorHex = remember {
        mutableStateOf(selectedColor.colorHex)
    }

    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .height(42.dp)
                .sizeIn(minWidth = 60.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    shape = CircleShape
                )
                .background(color = Color(selectedColor))
        )

        Box(
            modifier = Modifier
                .height(42.dp)
                .width(60.dp)
                .clip(CircleShape)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    shape = CircleShape
                )
                .background(color = previewColor)
        )

        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier
                .height(42.dp)
                .clip(CircleShape)
                .width(110.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                    shape = CircleShape
                )
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .align(Alignment.Center),
                value = colorHex.value,
                maxLines = 1,
                singleLine = true,
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                onValueChange = {
                    colorHex.value = if (!it.startsWith("#")) {
                            "#$it"
                        } else it
                    try {
                        previewColor = Color(it.toColorInt())
                        updateAllColorSpace.value = true
                    } catch (_: Exception) {
                    }
                },
                textStyle = LocalTextStyle.current.merge(
                    TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                )
            )
        }
    }

    var selectedTab by remember {
        mutableIntStateOf(1)
    }

    ScrollableTabRow(
        modifier = Modifier
            .padding(vertical = 8.dp),
        selectedTabIndex = selectedTab,
        edgePadding = 0.dp,
        containerColor = AlertDialogDefaults.containerColor
    ) {
        Tab(
            selected = selectedTab == 0,
            onClick = { selectedTab = 0 },
            text = {
                Text(text = stringResource(R.string.all), fontSize = 13.sp)
            }
        )

        Tab(
            selected = selectedTab == 1,
            onClick = { selectedTab = 1 },
            text = {
                Text(text = stringResource(R.string.hsv), fontSize = 13.sp)
            }
        )
        Tab(
            selected = selectedTab == 2,
            onClick = { selectedTab = 2 },
            text = {
                Text(text = stringResource(R.string.rgb), fontSize = 13.sp)
            }
        )
        Tab(
            selected = selectedTab == 3,
            onClick = { selectedTab = 3 },
            text = {
                Text(text = stringResource(R.string.cmyk), fontSize = 13.sp)
            }
        )

    }

    val isHSV by remember {
        derivedStateOf { selectedTab == 0 || selectedTab == 1 }
    }
    val isRGB by remember {
        derivedStateOf { selectedTab == 0 || selectedTab == 2 }
    }
    val isCMYK by remember {
        derivedStateOf { selectedTab == 0 || selectedTab == 3 }
    }

    Column(modifier = Modifier.verticalScroll(state = rememberScrollState())) {

        val previewColorHSV = remember {
            val hsv = FloatArray(3)
            AndroidColor.colorToHSV(previewColor.toArgb(), hsv)
            hsv
        }

        val previewColorCMYK = remember {
            colorToCMYK(previewColor)
        }

        var hValue by remember {
            mutableFloatStateOf(previewColorHSV[0])
        }

        var sValue by remember {
            mutableFloatStateOf(previewColorHSV[1])
        }

        var vValue by remember {
            mutableFloatStateOf(previewColorHSV[2])
        }

        var rValue by remember {
            mutableFloatStateOf(previewColor.red)
        }

        var gValue by remember {
            mutableFloatStateOf(previewColor.green)
        }

        var bValue by remember {
            mutableFloatStateOf(previewColor.blue)
        }

        var cValue by remember {
            mutableFloatStateOf(previewColorCMYK[0])
        }
        var mValue by remember {
            mutableFloatStateOf(previewColorCMYK[1])
        }
        var yValue by remember {
            mutableFloatStateOf(previewColorCMYK[2])
        }
        var kValue by remember {
            mutableFloatStateOf(previewColorCMYK[3])
        }


        val updateHSVColors: () -> Unit = remember {
            {
                val hsv = FloatArray(3)
                AndroidColor.colorToHSV(previewColor.toArgb(), hsv)
                hValue = hsv[0]
                sValue = hsv[1]
                vValue = hsv[2]
            }
        }

        val updateRGBColors: () -> Unit = remember {
            {
                rValue = previewColor.red
                gValue = previewColor.green
                bValue = previewColor.blue
            }
        }

        val updateCMYKColors: () -> Unit = remember {
            {
                val cmyk = colorToCMYK(previewColor)
                cValue = cmyk[0]
                mValue = cmyk[1]
                yValue = cmyk[2]
                kValue = cmyk[3]
            }
        }

        LaunchedEffect(updateAllColorSpace.value) {
            if (updateAllColorSpace.value) {
                updateHSVColors()
                updateRGBColors()
                updateCMYKColors()
                updateAllColorSpace.value = false
            }

        }


        val updateHSVPreviewColor: () -> Unit = remember {
            {
                previewColor = Color.hsv(hValue, sValue, vValue)
                updateRGBColors()
                updateCMYKColors()
                val newColor = previewColor.toArgb()
                colorHex.value = newColor.colorHex
                onColorSelected(newColor)
            }
        }

        val updateRGBPreviewColor: () -> Unit = remember {
            {
                previewColor = Color(rValue, gValue, bValue)
                updateHSVColors()
                updateCMYKColors()
                val newColor = previewColor.toArgb()
                colorHex.value = newColor.colorHex
                onColorSelected(newColor)
            }
        }

        val updateCMYKPreviewColor: () -> Unit = remember {
            {
                previewColor = CMYKToColor(cValue, mValue, yValue, kValue)
                updateHSVColors()
                updateRGBColors()
                val newColor = previewColor.toArgb()
                colorHex.value = newColor.colorHex
                onColorSelected(newColor)
            }
        }


        if (isHSV) {
            ColorSpaceSlider(
                startText = "H",
                start = 0f,
                end = 360f,
                position = hValue,
                hueSlider = true,
                onColorSpaceChange = {
                    hValue = it
                    updateHSVPreviewColor()
                }
            )


            ColorSpaceSlider(
                startText = "S",
                start = 0f,
                end = 100f,
                position = sValue * 100,
                onColorSpaceChange = {
                    sValue = it / 100
                    updateHSVPreviewColor()

                }
            )

            ColorSpaceSlider(
                startText = "V",
                start = 0f,
                end = 100f,
                position = vValue * 100,
                onColorSpaceChange = {
                    vValue = it / 100
                    updateHSVPreviewColor()
                }
            )
        }

        if (isRGB) {
            ColorSpaceSlider(
                startText = "R",
                start = 0f,
                end = 100f,
                position = rValue * 100,
                onColorSpaceChange = {
                    rValue = it / 100
                    updateRGBPreviewColor()
                }
            )

            ColorSpaceSlider(
                startText = "G",
                start = 0f,
                end = 100f,
                position = gValue * 100,
                onColorSpaceChange = {
                    gValue = it / 100
                    updateRGBPreviewColor()
                }
            )
            ColorSpaceSlider(
                startText = "B",
                start = 0f,
                end = 100f,
                position = bValue * 100,
                onColorSpaceChange = {
                    bValue = it / 100
                    updateRGBPreviewColor()
                }
            )
        }

        if (isCMYK) {
            ColorSpaceSlider(startText = "C",
                start = 0f,
                end = 100f,
                position = cValue,
                onColorSpaceChange = {
                    cValue = it
                    updateCMYKPreviewColor()
                }
            )

            ColorSpaceSlider(
                startText = "M",
                start = 0f,
                end = 100f,
                position = mValue,
                onColorSpaceChange = {
                    mValue = it
                    updateCMYKPreviewColor()
                }
            )

            ColorSpaceSlider(
                startText = "Y",
                start = 0f,
                end = 100f,
                position = yValue,
                onColorSpaceChange = {
                    yValue = it
                    updateCMYKPreviewColor()
                }
            )

            ColorSpaceSlider(
                startText = "K",
                start = 0f,
                end = 100f,
                position = kValue,
                onColorSpaceChange = {
                    kValue = it
                    updateCMYKPreviewColor()
                }
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColorSpaceSlider(
    startText: String,
    start: Float,
    end: Float,
    position: Float,
    hueSlider: Boolean = false,
    thumbColor: Color = Color.Unspecified,
    onColorSpaceChange: (Float) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MediumText(
            modifier = Modifier.padding(start = 8.dp),
            text = startText,
            fontSize = 13.sp
        )

        Icon(
            modifier = Modifier
                .padding(4.dp)
                .clickable(indication = null, interactionSource = remember {
                    MutableInteractionSource()
                }) {
                    if (position > start) {
                        onColorSpaceChange(position - 1)
                    }
                },
            imageVector = AppIcons.IcChevronLeft,
            contentDescription = null
        )

        if (hueSlider) {
            HueSlider(
                modifier = Modifier.weight(1f),
                value = position,
                start = start,
                end = end,
                onValueChange = { onColorSpaceChange(it) }
            )
        } else {
            Slider(
                modifier = Modifier.weight(1f),
                value = position,
                valueRange = start..end,
                steps = end.toInt() - start.toInt() - 1,
                colors = SliderDefaults.colors(thumbColor = thumbColor),
                onValueChange = { onColorSpaceChange(it) },
                thumb = {
                    SliderDefaults.Thumb(
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        thumbSize = DpSize(14.dp, 40.dp)
                    )
                }
            )
        }


        Icon(
            modifier = Modifier
                .size(24.dp)
                .clickable(indication = null, interactionSource = remember {
                    MutableInteractionSource()
                }) {

                    if (position < end) {
                        onColorSpaceChange(position + 1)
                    }
                },
            imageVector = AppIcons.IcChevronRight,
            contentDescription = null
        )

        MediumText(
            modifier = Modifier
                .sizeIn(minWidth = 38.dp)
                .padding(end = 8.dp),
            text = position.toInt().toString().padStart(3, ' '),
            fontSize = 13.sp,
            color = thumbColor
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HueSlider(
    modifier: Modifier = Modifier,
    value: Float,
    start: Float,
    end: Float,
    onValueChange: (Float) -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape)
        ) {
            val gradientBrush = Brush.horizontalGradient(
                colors = hueColors,
                startX = 0f,
                endX = size.width,
                tileMode = TileMode.Clamp
            )
            drawRect(
                brush = gradientBrush,
                size = Size(size.width, size.height)
            )
        }

        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = value,
            onValueChange = onValueChange,
            valueRange = start..end,
            steps = end.toInt() - start.toInt() - 1,
            colors = SliderDefaults.colors(
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
                activeTrackColor = Color.Transparent,
                inactiveTrackColor = Color.Transparent
            ),
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = remember {
                        MutableInteractionSource()
                    },
                    thumbSize = DpSize(16.dp, 40.dp)
                )
            }
        )
    }

}

val hueColors: List<Color> by lazy {
    listOf(
        Color(0xFFFF0000), Color(0xFFFFFF00), Color(0xFF00FF00),
        Color(0xFF00FFFF), Color(0xFF0000FF), Color(0xFFFF00FF), Color(0xFFFF0000)
    )
}

private fun CMYKToColor(
    @FloatRange(from = 0.0, to = 100.0) cyan: Float,
    magenta: Float,
    yellow: Float,
    black: Float
): Color {
    val red = 255 * (1f - (cyan / 100f)) * (1f - (black / 100f))
    val green = 255 * (1f - (magenta / 100f)) * (1f - (black / 100f))
    val blue = 255 * (1f - (yellow / 100f)) * (1f - (black / 100f))

    return Color(Math.round(red), Math.round(green), Math.round(blue))
}

fun colorToCMYK(color: Color): FloatArray {
    val red = color.red
    val green = color.green
    val blue = color.blue

    val cmyk = FloatArray(4)
    cmyk[3] = (1f - max(max(red.toDouble(), green.toDouble()), blue.toDouble())).toFloat()
    cmyk[0] = (1f - red - cmyk[3]) / (1f - cmyk[3]) * 100
    cmyk[1] = (1f - green - cmyk[3]) / (1f - cmyk[3]) * 100
    cmyk[2] = (1f - blue - cmyk[3]) / (1f - cmyk[3]) * 100
    cmyk[3] = cmyk[3] * 100
    return cmyk
}


@Preview
@Composable
private fun HueSliderPreview() {
    HueSlider(value = 50f, onValueChange = {}, start = 0f, end = 360f)
}

@Preview(showBackground = true)
@Composable
private fun ColorSliderPreview() {
    ColorSpaceSlider(
        startText = "H",
        start = 0f,
        end = 100f,
        position = 60f,
        onColorSpaceChange = {})
}