package com.revolgenx.anilib.social.ui.viewmodel

import android.text.Spanned
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.revolgenx.anilib.social.data.service.ActivityUnionService
import com.revolgenx.anilib.social.factory.markdown
import com.revolgenx.anilib.social.markdown.anilify

class ActivityComposerViewModel(
    activityUnionService: ActivityUnionService
) : ViewModel() {
    var activityId by mutableIntStateOf(-1)
    var recipientId by mutableIntStateOf(-1)
    var private by mutableStateOf(false)

    val textFieldValue = mutableStateOf(TextFieldValue())
    val spannedText = mutableStateOf<Spanned?>(null)

    fun toMarkdown() {
        spannedText.value = markdown.toMarkdown(anilify(textFieldValue.value.text))
    }

    fun appendString(value: Pair<String, Int>) {
        textFieldValue.value.let { tf ->
            val cursorPos = textFieldValue.value.selection.end
            val newText = tf.text.substring(0, cursorPos) + value.first + tf.text.substring(cursorPos)
            textFieldValue.value =
                tf.copy(text = newText, selection = TextRange(newText.length - value.second))
        }
    }

    fun save(){

    }
}