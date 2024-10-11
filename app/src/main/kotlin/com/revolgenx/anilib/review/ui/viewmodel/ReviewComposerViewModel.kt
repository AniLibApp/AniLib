package com.revolgenx.anilib.review.ui.viewmodel

import android.text.Spanned
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.exception.ApolloHttpException
import com.revolgenx.anilib.common.data.store.AppPreferencesDataStore
import com.revolgenx.anilib.common.ui.viewmodel.ResourceViewModel
import com.revolgenx.anilib.review.data.field.ReviewField
import com.revolgenx.anilib.review.data.field.SaveReviewField
import com.revolgenx.anilib.review.data.service.ReviewService
import com.revolgenx.anilib.review.ui.model.ReviewModel
import com.revolgenx.anilib.common.ext.markdown
import com.revolgenx.anilib.common.ext.anilify
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ReviewComposerViewModel(
    private val reviewService: ReviewService,
    appPreferencesDataStore: AppPreferencesDataStore
) : ResourceViewModel<ReviewModel, ReviewField>() {
    override val field: ReviewField = ReviewField(
        userId = appPreferencesDataStore.userId.get()
    )

    var reviewId by mutableIntStateOf(-1)
    var body by mutableStateOf(TextFieldValue())
    var summary by mutableStateOf("")
    var private by mutableStateOf(false)
    var score by mutableIntStateOf(50)
    var spannedBody by mutableStateOf<Spanned?>(null)

    var canSend = derivedStateOf {
        body.text.length > 2200 && summary.length > 20 && summary.length < 120
    }

    fun toMarkdown() {
        spannedBody = markdown.toMarkdown(anilify(body.text))
    }

    fun appendString(value: Pair<String, Int>) {
        body.let { tf ->
            val cursorPos = body.selection.end
            val newText =
                tf.text.substring(0, cursorPos) + value.first + tf.text.substring(cursorPos)
            body =
                tf.copy(text = newText, selection = TextRange(newText.length - value.second))
        }
    }

    override fun load(): Flow<ReviewModel?> {
        return reviewService.getReview(field).onEach {
            it ?: return@onEach
            reviewId = it.id
            body = TextFieldValue(text = it.body ?: "")
            summary = it.summary ?: ""
            private = it.private
            score = it.score ?: 50
        }.catch {
            if (it is ApolloHttpException && it.statusCode == 404){
                emit(ReviewModel())
            }else{
                throw it
            }
        }
    }

    override fun save() {
        if (field.mediaId == null) return
        super.save()
        val saveField = SaveReviewField(
            id = reviewId.takeIf { it != -1 },
            mediaId = field.mediaId!!,
            summary = summary,
            body = body.text,
            private = private,
            score = score
        )

        reviewService.saveReview(saveField)
            .onEach {
                reviewId = it
                saveComplete(it)
            }.catch {
                errorMsg = anilib.i18n.R.string.operation_failed
                saveFailed(it)
            }.launchIn(viewModelScope)
    }

    override fun delete() {
        if (reviewId == -1) return
        super.delete()
        reviewService.deleteReview(reviewId)
            .onEach {
                if (it) {
                    reset()
                    deleteComplete(it)
                } else {
                    throw ApolloException("Failed to delete.")
                }
            }.catch {
                errorMsg = anilib.i18n.R.string.operation_failed
                deleteFailed(it)
            }.launchIn(viewModelScope)
    }

    private fun reset() {
        resource.value = null
        reviewId = -1
        body = TextFieldValue()
        summary = ""
        private = false
        score = 50
        spannedBody = null
    }

}