package com.revolgenx.anilib.data.store

import android.content.Context
import androidx.lifecycle.*
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.revolgenx.anilib.R
import com.revolgenx.anilib.common.preference.inUseMlLanguageModel
import com.revolgenx.anilib.ui.view.makeToast
import org.jsoup.Jsoup

class TranslationStore{

    private val translatedString = MutableLiveData<String>(null);
    fun translate(context:Context?, from:String): LiveData<String> {
        if(translatedString.value == null && context != null){
            val storedLangCode = inUseMlLanguageModel(context)
            if(storedLangCode.isBlank().not()){
                val option = TranslatorOptions.Builder()
                    .setSourceLanguage(TranslateLanguage.ENGLISH)
                    .setTargetLanguage(storedLangCode)
                    .build()

                val translator = Translation.getClient(option)

                val conditions = DownloadConditions.Builder()
                    .build()

                translator.downloadModelIfNeeded(conditions)
                    .addOnSuccessListener {
                        if(context == null) return@addOnSuccessListener
                        val plainText = Jsoup.parse(from).text()
                        translator.translate(plainText)
                            .addOnSuccessListener r@{
                                if(context == null) return@r
                                translatedString.postValue(it)
                            }
                            .addOnFailureListener {
                                context.makeToast(R.string.failed_to_translate)
                            }
                    }.addOnFailureListener {
                        context.makeToast(R.string.failed_to_download_language_model)
                    }
            }
        }
        return translatedString
    }
}
