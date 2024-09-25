package com.revolgenx.anilib.social.ui.viewmodel

import android.text.Spanned
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revolgenx.anilib.common.data.state.ResourceState
import com.revolgenx.anilib.social.data.field.SaveActivityReplyField
import com.revolgenx.anilib.social.data.field.SaveMessageActivityField
import com.revolgenx.anilib.social.data.field.SaveTextActivityField
import com.revolgenx.anilib.social.data.service.ActivityUnionService
import com.revolgenx.anilib.social.factory.markdown
import com.revolgenx.anilib.social.markdown.anilify
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

enum class ActivityType {
    TEXT, REPLY, MESSAGE
}

abstract class BaseActivityComposerViewModel(
    private val activityUnionService: ActivityUnionService
) : ViewModel() {
    abstract val activityType: ActivityType

    private var activityId: Int? = null
    private var recipientId = -1
    private var replyId: Int? = null

    val canShowPrivateToggle get() = activityType == ActivityType.MESSAGE && activityId == null
    var private = false

    var textFieldValue by mutableStateOf(TextFieldValue())
    var spannedText by mutableStateOf<Spanned?>(null)

    var hasErrors by mutableStateOf(false)

    var saveResource by mutableStateOf<ResourceState<Int>?>(null)

    fun forMessage(recipientId: Int, activityId: Int?, message: String?, private: Boolean) {
        this.recipientId = recipientId
        this.activityId = activityId
        this.textFieldValue = this.textFieldValue.copy(text = message ?: "")
        this.private = private
    }

    fun forReply(activityId: Int, replyId: Int?, text: String?) {
        this.activityId = activityId
        this.replyId = replyId
        this.textFieldValue = this.textFieldValue.copy(text = text ?: "")
    }

    fun forText(activityId: Int?, text: String?) {
        this.activityId = activityId
        this.textFieldValue = this.textFieldValue.copy(text = text ?: "")
    }

    fun toMarkdown() {
        spannedText = markdown.toMarkdown(anilify(textFieldValue.text))
    }

    fun appendString(value: Pair<String, Int>) {
        textFieldValue.let { tf ->
            val cursorPos = textFieldValue.selection.end
            val newText =
                tf.text.substring(0, cursorPos) + value.first + tf.text.substring(cursorPos)
            textFieldValue =
                tf.copy(text = newText, selection = TextRange(newText.length - value.second))
        }
    }

    fun save() {
        if (textFieldValue.text.isBlank()) {
            hasErrors = true
            return
        }
        saveResource = ResourceState.loading()
        val saveActivity = when (activityType) {
            ActivityType.TEXT -> {
                val saveField = SaveTextActivityField(
                    activityId = activityId,
                    text = textFieldValue.text
                )
                activityUnionService.saveTextActivity(saveField)
            }

            ActivityType.MESSAGE -> {
                val saveField = SaveMessageActivityField(
                    activityId = activityId,
                    message = textFieldValue.text,
                    recipientId = recipientId,
                    private = private
                )
                activityUnionService.saveMessageActivity(saveField)
            }

            ActivityType.REPLY -> {
                val saveField = SaveActivityReplyField(
                    activityId = activityId,
                    replyId = replyId,
                    text = textFieldValue.text
                )
                activityUnionService.saveActivityReply(saveField)
            }
        }

        saveActivity.onEach {
            if (activityType == ActivityType.REPLY) {
                replyId = it
            } else {
                activityId = it
            }
            saveResource = ResourceState.success(it)
        }.catch {
            saveResource = ResourceState.error(it)
        }.launchIn(viewModelScope)
    }
}