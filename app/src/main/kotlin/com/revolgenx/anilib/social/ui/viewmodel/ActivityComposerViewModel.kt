package com.revolgenx.anilib.social.ui.viewmodel

import android.text.Spanned
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.social.factory.markdown

class ActivityComposerViewModel : ViewModel() {
    val text = mutableStateOf("")
    val spannedText = mutableStateOf<Spanned?>(null)

    fun toMarkdown() {
        spannedText.value = markdown.toMarkdown(text.value)
    }
}