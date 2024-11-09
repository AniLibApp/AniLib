package com.revolgenx.anilib.entry.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.common.ui.icons.AppIcons
import com.revolgenx.anilib.common.ui.icons.appicon.IcArrowDown
import com.revolgenx.anilib.common.ui.icons.appicon.IcArrowUp
import com.revolgenx.anilib.common.ui.icons.appicon.IcHappy
import com.revolgenx.anilib.common.ui.icons.appicon.IcNeutral
import com.revolgenx.anilib.common.ui.icons.appicon.IcSad
import com.revolgenx.anilib.common.ui.icons.appicon.IcStar
import com.revolgenx.anilib.common.ui.icons.appicon.IcStarOutline
import com.revolgenx.anilib.common.util.OnClick
import com.revolgenx.anilib.type.ScoreFormat
import kotlin.math.round

@Composable
fun MediaListEntryScore(
    score: Double, scoreFormat: ScoreFormat, onScoreChange: (score: Double) -> Unit
) {

    when (scoreFormat) {
        ScoreFormat.POINT_100 -> {
            CountEditor(count = score.toInt(), max = 100) { newScore ->
                onScoreChange(newScore.toDouble())
            }
        }

        ScoreFormat.POINT_10_DECIMAL -> {
            DoubleCountEditor(count = score, onScoreChange = onScoreChange)
        }

        ScoreFormat.POINT_10 -> {
            CountEditor(count = score.toInt()) { newScore ->
                onScoreChange(newScore.toDouble())
            }
        }

        ScoreFormat.POINT_5 -> {
            StarScore(score = score, onScoreChange = onScoreChange)
        }

        ScoreFormat.POINT_3 -> {
            SmileyScore(score = score, onScoreChange = onScoreChange)
        }

        ScoreFormat.UNKNOWN__ -> {}
    }
}


@Composable
fun CountEditor(
    count: Int, max: Int? = 10, onScoreChange: (count: Int) -> Unit
) {
    val min = 0
    val scoreValue = "$count"

    fun getScoreWithInRange(newScore: Int) =
        if (newScore < min) min else if (max != null && newScore > max) max else newScore

    val textFieldValueState by remember(count) {
        mutableStateOf(
            TextFieldValue(
                text = scoreValue, selection = TextRange(scoreValue.length)
            )
        )
    }


    fun increaseScore() {
        count.plus(1).let(::getScoreWithInRange).let { onScoreChange(it) }
    }

    fun decreaseScore() {
        count.minus(1).let(::getScoreWithInRange).let { onScoreChange(it) }
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        IconButton(onClick = { decreaseScore() }) {
            Icon(imageVector = AppIcons.IcArrowDown, contentDescription = null)
        }

        TextField(
            modifier = Modifier.weight(1f),
            value = textFieldValueState,
            onValueChange = { textFieldValue ->
                val newScore =
                    (textFieldValue.text.ifBlank { "0" }.toInt()).let(::getScoreWithInRange)
                onScoreChange(newScore)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        IconButton(onClick = { increaseScore() }) {
            Icon(imageVector = AppIcons.IcArrowUp, contentDescription = null)
        }
    }
}


@Composable
fun DoubleCountEditor(
    count: Double,
    max: Double = 10.0,
    incrementBy: Double = 0.5,
    onScoreChange: (count: Double) -> Unit
) {
    val min = 0.0

    val scoreValue = if (count.mod(1.0) == 0.0) {
        "${count.toInt()}"
    } else {
        "$count"
    }

    var textFieldValueState by remember(count) {
        mutableStateOf(
            TextFieldValue(
                text = scoreValue, selection = TextRange(scoreValue.length)
            )
        )
    }

    fun updateScore(newScore: Double) {
        val roundedScore = round(newScore * 100) / 100
        onScoreChange(roundedScore)
    }

    fun getScoreWithInRange(newScore: Double) =
        if (newScore < min) min else if (newScore > max) max else newScore

    fun increaseScore() {
        count.plus(incrementBy).let(::getScoreWithInRange).let { updateScore(it) }
    }

    fun decreaseScore() {
        count.minus(incrementBy).let(::getScoreWithInRange).let { updateScore(it) }
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        IconButton(onClick = { decreaseScore() }) {
            Icon(imageVector = AppIcons.IcArrowDown, contentDescription = null)
        }
        TextField(
            modifier = Modifier.weight(1f),
            value = textFieldValueState,
            onValueChange = { textFieldValue ->
                val oldTextHasPeriod = textFieldValueState.text.contains('.')
                val newTextHasPeriod = textFieldValue.text.contains('.')

                if (oldTextHasPeriod && newTextHasPeriod) {
                    val text = textFieldValue.text.trim('.').apply {
                        split('.').getOrNull(0)?.let {
                            if (it.length > 1) trimStart('0')
                        }
                    }.ifBlank { "0" }
                    val newScore = text.toDouble().let(::getScoreWithInRange)
                    if (newScore == count) {
                        textFieldValueState = textFieldValue.copy(scoreValue)
                    } else {
                        onScoreChange(newScore)
                    }
                } else if (!oldTextHasPeriod && newTextHasPeriod) {
                    val text =
                        textFieldValue.text.apply { if (length > 1) trimStart('0') }.ifBlank { "0" }
                    textFieldValueState = textFieldValue.copy(text)
                } else {
                    val text = textFieldValue.text.trimStart('0').trim('.').ifBlank { "0" }
                    val newScore = text.toDouble().let(::getScoreWithInRange)
                    onScoreChange(newScore)
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            )
        )
        IconButton(onClick = { increaseScore() }) {
            Icon(imageVector = AppIcons.IcArrowUp, contentDescription = null)
        }
    }
}


@Composable
private fun StarScore(score: Double, onScoreChange: (score: Double) -> Unit) {

    fun updateScore(newScore: Double) {
        onScoreChange(if (score == newScore) 0.0 else newScore)
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        (1..5).map {
            StarScoreButton(selected = score >= it) {
                updateScore(it.toDouble())
            }
        }
    }
}


@Composable
private fun StarScoreButton(selected: Boolean, onClick: OnClick) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = interactionSource, indication = null, onClick = onClick
            )
    ) {
        Icon(
            modifier = Modifier.align(Alignment.Center),
            imageVector = if (selected) AppIcons.IcStar else AppIcons.IcStarOutline,
            contentDescription = null,
            tint = if (selected) MaterialTheme.colorScheme.primary else LocalContentColor.current
        )
    }
}


@Composable
private fun SmileyScore(score: Double, onScoreChange: (score: Double) -> Unit) {
    val isHappy = score == 3.0
    val isNeutral = score == 2.0
    val isSad = score == 1.0

    fun updateScore(newScore: Double) {
        onScoreChange(if (score == newScore) 0.0 else newScore)
    }
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SmileyScoreButton(selected = isHappy, AppIcons.IcHappy) {
            updateScore(3.0)
        }
        SmileyScoreButton(selected = isNeutral, AppIcons.IcNeutral) {
            updateScore(2.0)
        }
        SmileyScoreButton(selected = isSad, AppIcons.IcSad) {
            updateScore(1.0)
        }
    }
}

@Composable
private fun SmileyScoreButton(selected: Boolean, icon: ImageVector, onClick: OnClick) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (selected) MaterialTheme.colorScheme.primary else LocalContentColor.current
        )
    }
}

