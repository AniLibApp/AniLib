package com.revolgenx.anilib.entry.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.revolgenx.anilib.R
import com.revolgenx.anilib.type.ScoreFormat
import kotlin.math.round

@Composable
fun MediaListEntryScore(
    score: Double,
    scoreFormat: ScoreFormat,
    onScoreChange: (score: Double) -> Unit
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
    count: Int,
    max: Int? = 10,
    onScoreChange: (count: Int) -> Unit
) {
    val min = 0
    val scoreValue = "$count"

    fun getScoreWithInRange(newScore: Int) =
        if (newScore < min) min else if (max != null && newScore > max) max else newScore

    var textFieldValueState by remember(count) {
        mutableStateOf(
            TextFieldValue(
                text = scoreValue,
                selection = TextRange(scoreValue.length)
            )
        )
    }


    fun increaseScore() {
        count.plus(1)
            .let(::getScoreWithInRange)
            .let { onScoreChange(it) }
    }

    fun decreaseScore() {
        count.minus(1)
            .let(::getScoreWithInRange)
            .let { onScoreChange(it) }
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        IconButton(onClick = { decreaseScore() }) {
            Icon(painter = painterResource(id = R.drawable.ic_arrow_down), contentDescription = null)
        }
        BasicTextField(
            modifier = Modifier.weight(1f),
            value = textFieldValueState,
            onValueChange = {textFieldValue->
                val newScore = (textFieldValue.text.ifBlank { "0" }.toInt()).let(::getScoreWithInRange)
                onScoreChange(newScore)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
        )
        IconButton(onClick = { increaseScore() }) {
            Icon(painter = painterResource(id = R.drawable.ic_arrow_up), contentDescription = null)
        }
    }
}


@Composable
fun DoubleCountEditor(
    count: Double,
    max: Double = 10.0,
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
                text = scoreValue,
                selection = TextRange(scoreValue.length)
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
        count.plus(0.5).let(::getScoreWithInRange)
            .let { updateScore(it) }
    }

    fun decreaseScore() {
        count.minus(0.5).let(::getScoreWithInRange)
            .let { updateScore(it) }
    }

    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        IconButton(onClick = { decreaseScore() }) {
            Icon(painter = painterResource(id = R.drawable.ic_arrow_down), contentDescription = null)
        }
        BasicTextField(
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
                    val text = textFieldValue.text.apply { if (length > 1) trimStart('0') }
                        .ifBlank { "0" }
                    textFieldValueState = textFieldValue.copy(text)
                } else {
                    val text = textFieldValue.text.trimStart('0').trim('.').ifBlank { "0" }
                    val newScore = text.toDouble().let(::getScoreWithInRange)
                    onScoreChange(newScore)
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
        )
        IconButton(onClick = { increaseScore() }) {
            Icon(painter = painterResource(id = R.drawable.ic_arrow_up), contentDescription = null)
        }
    }
}

@Preview
@Composable
fun ScoreEditorPreview() {
    DoubleCountEditor(
        count = 3.0,
    ) {

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
private fun StarScoreButton(selected: Boolean, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
    ) {
        Icon(
            painter = painterResource(id = if (selected) R.drawable.ic_star else R.drawable.ic_star_outline),
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SmileyScoreButton(selected = isHappy, R.drawable.ic_happy) {
            updateScore(3.0)
        }
        SmileyScoreButton(selected = isNeutral, R.drawable.ic_neutral) {
            updateScore(2.0)
        }
        SmileyScoreButton(selected = isSad, R.drawable.ic_sad) {
            updateScore(1.0)
        }
    }
}

@Composable
private fun SmileyScoreButton(selected: Boolean, icon: Int, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = null,
            tint = if (selected) MaterialTheme.colorScheme.primary else LocalContentColor.current
        )
    }
}

