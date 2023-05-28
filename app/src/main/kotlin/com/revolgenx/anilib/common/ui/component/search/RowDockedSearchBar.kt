package com.revolgenx.anilib.common.ui.component.search

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.revolgenx.anilib.common.ext.colorScheme
import kotlinx.coroutines.delay

@ExperimentalMaterial3Api
@Composable
fun RowDockedSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    shape: Shape = SearchBarDefaults.dockedShape,
    colors: SearchBarColors = SearchBarDefaults.colors(),
    fontSize: TextUnit = 16.sp,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable RowScope.() -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Surface(
        shape = shape,
        color = colors.containerColor,
        contentColor = contentColorFor(colors.containerColor),
        tonalElevation = 1.dp,
        modifier = modifier
            .zIndex(1f)
            .width(SearchBarMinWidth)
    ) {
        Column {
            SearchBarInputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                active = active,
                height = height,
                onActiveChange = onActiveChange,
                enabled = enabled,
                placeholder = placeholder,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                colors = colors.inputFieldColors,
                fontSize = fontSize,
                interactionSource = interactionSource,
            )
            AnimatedVisibility(
                visible = active,
                enter = DockedEnterTransition,
                exit = DockedExitTransition,
            ) {
                Column {
                    Divider(color = colors.dividerColor)
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        content()
                    }
                }
            }
        }
    }

    LaunchedEffect(active) {
        if (!active) {
            // Not strictly needed according to the motion spec, but since the animation already has
            // a delay, this works around b/261632544.
            delay(AnimationDelayMillis.toLong())
            focusManager.clearFocus()
        }
    }

    BackHandler(enabled = active) {
        onActiveChange(false)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBarInputField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    height: Dp? = null,
    placeholder: @Composable (() -> Unit)? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = SearchBarDefaults.inputFieldColors(),
    fontSize: TextUnit = TextUnit.Unspecified,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
) {
    val focusRequester = remember { FocusRequester() }
    val textColor = colorScheme().onSurface

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier
            .height(height ?: SearchBarDefaults.InputFieldHeight)
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .onFocusChanged { if (it.isFocused) onActiveChange(true) }
            .semantics {
                onClick {
                    focusRequester.requestFocus()
                    true
                }
            },
        enabled = enabled,
        singleLine = true,
        textStyle = LocalTextStyle.current.merge(TextStyle(color = textColor, fontSize = fontSize, fontWeight = FontWeight.Normal)),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { onSearch(query) }),
        interactionSource = interactionSource,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.DecorationBox(
                value = query,
                innerTextField = innerTextField,
                enabled = enabled,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                interactionSource = interactionSource,
                placeholder = placeholder,
                leadingIcon = leadingIcon?.let { leading ->
                    {
                        Box(Modifier.offset(x = SearchBarIconOffsetX)) { leading() }
                    }
                },
                trailingIcon = trailingIcon?.let { trailing ->
                    {
                        Box(Modifier.offset(x = -SearchBarIconOffsetX)) { trailing() }
                    }
                },
                shape = SearchBarDefaults.inputFieldShape,
                colors = colors,
                contentPadding = TextFieldDefaults.contentPaddingWithoutLabel(
                    top = 8.dp,
                    bottom = 8.dp
                ),
                container = {},
            )
        }
    )
}

private val SearchBarIconOffsetX: Dp = 4.dp
internal val SearchBarMinWidth: Dp = 360.dp

// Animation specs
private const val AnimationEnterDurationMillis: Int = 500
private const val AnimationExitDurationMillis: Int = 300
private const val AnimationDelayMillis: Int = 50
private val AnimationEnterEasing = CubicBezierEasing(0.05f, 0.7f, 0.1f, 1.0f)
private val AnimationExitEasing = CubicBezierEasing(0.0f, 1.0f, 0.0f, 1.0f)
private val AnimationEnterFloatSpec: FiniteAnimationSpec<Float> = tween(
    durationMillis = AnimationEnterDurationMillis,
    delayMillis = AnimationDelayMillis,
    easing = AnimationEnterEasing,
)
private val AnimationExitFloatSpec: FiniteAnimationSpec<Float> = tween(
    durationMillis = AnimationExitDurationMillis,
    delayMillis = AnimationDelayMillis,
    easing = AnimationExitEasing,
)
private val AnimationEnterSizeSpec: FiniteAnimationSpec<IntSize> = tween(
    durationMillis = AnimationEnterDurationMillis,
    delayMillis = AnimationDelayMillis,
    easing = AnimationEnterEasing,
)
private val AnimationExitSizeSpec: FiniteAnimationSpec<IntSize> = tween(
    durationMillis = AnimationExitDurationMillis,
    delayMillis = AnimationDelayMillis,
    easing = AnimationExitEasing,
)
private val DockedEnterTransition: EnterTransition =
    fadeIn(AnimationEnterFloatSpec) + expandVertically(AnimationEnterSizeSpec)
private val DockedExitTransition: ExitTransition =
    fadeOut(AnimationExitFloatSpec) + shrinkVertically(AnimationExitSizeSpec)
