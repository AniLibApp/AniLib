package com.revolgenx.anilib.common.ext

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.ui.composition.LocalSnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.ocpsoft.prettytime.PrettyTime
import java.util.Date
import java.util.NavigableMap
import java.util.TreeMap

fun String?.naText() = this.takeIf { !it.isNullOrEmpty() } ?: "?"
fun String?.emptyText() = this.takeIf { !it.isNullOrEmpty() } ?: ""
fun Int?.naText() = this?.toString() ?: "?"
fun Int?.naInt() = this ?: 0

fun String?.getOrDefault() = this ?: ""
fun Int?.getOrDefault() = this ?: 0
fun Double?.getOrDefault() = this ?: 0.0

@StringRes
fun Int?.naStringRes() = this.takeIf { it != null } ?: R.string.na

@DrawableRes
fun Int?.naDrawableRes() = this.takeIf { it != null } ?: R.drawable.ic_question_mark

@Composable
fun localContext() = LocalContext.current

@Composable
fun localSnackbarHostState() = LocalSnackbarHostState.current

fun Any?.isNull() = this == null
fun Any?.isNotNull() = this != null

private val suffixes: NavigableMap<Long, String> by lazy {
    TreeMap<Long, String>().apply {
        put(1_000L, "k")
        put(1_000_000L, "M")
        put(1_000_000_000L, "G")
        put(1_000_000_000_000L, "T")
        put(1_000_000_000_000_000L, "P")
        put(1_000_000_000_000_000_000L, "E")
    }
}

fun Int.prettyNumberFormat(): String = this.toLong().prettyNumberFormat()

fun Long.prettyNumberFormat(): String {
    //Long.MIN_VALUE == -Long.MIN_VALUE so we need an adjustment here
    if (this == Long.MIN_VALUE) return (Long.MIN_VALUE + 1).prettyNumberFormat()
    if (this < 0) return "-" + this.prettyNumberFormat()
    if (this < 1000) return this.toString() //deal with easy case
    val e: Map.Entry<Long, String> = suffixes.floorEntry(this)!!
    val divideBy = e.key
    val suffix = e.value
    val truncated = this / (divideBy / 10) //the number part of the output times 10
    val hasDecimal =
        truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
    return if (hasDecimal) (truncated / 10.0).toString() + suffix else (truncated / 10).toString() + suffix
}

fun Long.prettyTime(): String {
    return PrettyTime().format(Date(this * 1000L))
}


fun CoroutineScope.launchIO(block: suspend CoroutineScope.() -> Unit): Job =
    launch(Dispatchers.IO, block = block)


fun <T> Flow<T>.onIO() = flowOn(Dispatchers.IO)

@Composable
fun emptyWindowInsets() = WindowInsets(0)

@Composable
fun horizontalWindowInsets() = WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)
@Composable
fun horizontalBottomWindowInsets() = NavigationBarDefaults.windowInsets


@OptIn(ExperimentalMaterial3Api::class)
fun CoroutineScope.hideBottomSheet(state: SheetState, openBottomSheet: MutableState<Boolean>) {
    this.launch { state.hide() }.invokeOnCompletion {
        if (!state.isVisible) {
            openBottomSheet.value = false
        }
    }
}

fun <T> List<T>?.getOrEmpty() = this ?: emptyList()