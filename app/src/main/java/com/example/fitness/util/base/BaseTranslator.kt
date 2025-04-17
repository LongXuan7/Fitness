package com.example.fitness.util.base

import android.content.Context
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

class BaseTranslator(
    private val sourceLang: String = TranslateLanguage.VIETNAMESE,
    private val targetLang: String = TranslateLanguage.ENGLISH
) {
    private val translator: Translator

    init {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLang)
            .setTargetLanguage(targetLang)
            .build()
        translator = Translation.getClient(options)
        downloadModelIfNeeded()
    }

    private fun downloadModelIfNeeded(onSuccess: (() -> Unit)? = null, onFailure: ((Exception) -> Unit)? = null) {
        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener { onSuccess?.invoke() }
            .addOnFailureListener { exception -> onFailure?.invoke(exception) }
    }

    fun translate(
        text: String,
        onTranslated: (String) -> Unit,
        onError: (Exception) -> Unit = {}
    ) {
        translator.translate(text)
            .addOnSuccessListener { translatedText ->
                onTranslated(translatedText)
            }
            .addOnFailureListener { exception ->
                onError(exception)
            }
    }

    fun close() {
        translator.close()
    }
}
