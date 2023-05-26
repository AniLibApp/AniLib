package com.revolgenx.anilib.social.ui.viewmodel

import android.text.Spanned
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.social.factory.markwon

class ActivityComposerViewModel : ViewModel() {
    val text = mutableStateOf("")
    val spannedText = mutableStateOf<Spanned?>(null)

    fun toMarkdown() {
        spannedText.value = markwon.toMarkdown(text.value)
    }
}