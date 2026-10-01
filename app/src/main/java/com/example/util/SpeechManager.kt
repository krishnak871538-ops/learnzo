package com.example.util

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import com.example.model.SupportedLanguages
import java.util.Locale

class SpeechManager(context: Context) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false
    private var isMuted = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            tts?.setPitch(1.15f) // slightly higher, friendlier cartoon tone
            tts?.setSpeechRate(0.95f) // gentle pace for kids
            val hiLocale = Locale.forLanguageTag("hi-IN")
            val result = tts?.setLanguage(hiLocale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                tts?.setLanguage(Locale.US)
            }
            Log.d("SpeechManager", "TTS initialized successfully")
        } else {
            Log.e("SpeechManager", "TTS initialization failed: $status")
        }
    }

    fun speak(text: String, langCode: String = "hi", onDone: (() -> Unit)? = null) {
        if (isMuted || !isInitialized || text.isBlank()) {
            onDone?.invoke()
            return
        }

        try {
            val appLang = SupportedLanguages.getByCode(langCode)
            val targetLocale = Locale.forLanguageTag(appLang.localeTag)
            var res = tts?.setLanguage(targetLocale)
            if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                res = tts?.setLanguage(Locale.forLanguageTag(appLang.code))
                if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
                    if (appLang.code != "en") {
                        tts?.setLanguage(Locale.forLanguageTag("hi-IN"))
                    } else {
                        tts?.setLanguage(Locale.US)
                    }
                }
            }

            if (onDone != null) {
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {}
                    override fun onDone(utteranceId: String?) {
                        onDone()
                    }
                    override fun onError(utteranceId: String?) {
                        onDone()
                    }
                })
            }

            val utteranceId = "LEARNZO_${System.currentTimeMillis()}"
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        } catch (e: Exception) {
            Log.w("SpeechManager", "Error speaking text", e)
            onDone?.invoke()
        }
    }

    fun speak(text: String, isHindi: Boolean, onDone: (() -> Unit)? = null) {
        speak(text, if (isHindi) "hi" else "en", onDone)
    }

    fun stop() {
        try {
            tts?.stop()
        } catch (e: Exception) {
            Log.w("SpeechManager", "Error stopping TTS", e)
        }
    }

    fun toggleMute(): Boolean {
        isMuted = !isMuted
        if (isMuted) stop()
        return isMuted
    }

    fun isAudioMuted(): Boolean = isMuted

    fun shutdown() {
        try {
            tts?.stop()
            tts?.shutdown()
            tts = null
        } catch (e: Exception) {
            Log.w("SpeechManager", "Error shutting down TTS", e)
        }
    }
}
