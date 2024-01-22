package com.revolgenx.anilib.common.ext

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.Window
import androidx.activity.ComponentActivity
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
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogWindowProvider
import androidx.datastore.core.DataStore
import com.revolgenx.anilib.common.ui.composition.LocalSnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.ocpsoft.prettytime.PrettyTime
import java.util.Date
import java.util.NavigableMap
import java.util.TreeMap
import anilib.i18n.R as I18nR

fun String?.naText() = this.takeIf { !it.isNullOrEmpty() } ?: "?"
fun Int?.naText() = this?.toString() ?: "?"
fun Int?.orZero() = this ?: 0
fun Int?.orZeroString() = this.orZero().toString()
fun Double?.orZero() = this ?: 0.0

@Composable
fun naString() = stringResource(id = I18nR.string.na)
@StringRes
fun Int?.orNa() = this ?: I18nR.string.na

@Composable
fun localSnackbarHostState() = LocalSnackbarHostState.current!!

@Composable
fun maybeLocalSnackbarHostState() = LocalSnackbarHostState.current

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

fun <T> List<T>.nullIfEmpty() = this.takeIf { it.isNotEmpty() }

internal fun Context.componentActivity(): ComponentActivity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is ComponentActivity) return context
        context = context.baseContext
    }
    return null
}

@Composable
fun window(): Window? =
    (LocalView.current.parent as? DialogWindowProvider)?.window
        ?: LocalView.current.context.findWindow()

private tailrec fun Context.findWindow(): Window? =
    when (this) {
        is Activity -> window
        is ContextWrapper -> baseContext.findWindow()
        else -> null
    }

@Composable
fun componentActivity() = localContext().componentActivity()

fun <T> Flow<T>.get() = runBlocking { first() }
fun <T> DataStore<T>.get() = data.get()

suspend fun <T> Flow<T>.collectIfNew(old: T, collector: FlowCollector<T>){
    collect{
        if(it != old){
            collector.emit(it)
        }
    }
}