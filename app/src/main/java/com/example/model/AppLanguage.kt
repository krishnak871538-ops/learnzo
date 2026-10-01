package com.example.model

import java.util.Locale

/**
 * Supported Languages in Learnzo
 */
data class AppLanguage(
    val code: String,
    val nativeName: String,
    val englishName: String,
    val iconEmoji: String,
    val localeTag: String,
    val confirmationText: String,
    val sampleGreeting: String
) {
    val displayLabel: String
        get() = "$iconEmoji $nativeName ($englishName)"
}

object SupportedLanguages {
    val languages = listOf(
        AppLanguage(
            code = "en",
            nativeName = "English",
            englishName = "English",
            iconEmoji = "🌐",
            localeTag = "en-US",
            confirmationText = "Language changed to English.",
            sampleGreeting = "Hello little champion! Welcome to Learnzo!"
        ),
        AppLanguage(
            code = "hi",
            nativeName = "हिन्दी",
            englishName = "Hindi",
            iconEmoji = "🇮🇳",
            localeTag = "hi-IN",
            confirmationText = "भाषा बदल दी गई है।",
            sampleGreeting = "नमस्ते नन्हें चैंपियन! Learnzo में आपका स्वागत है!"
        ),
        AppLanguage(
            code = "mr",
            nativeName = "मराठी",
            englishName = "Marathi",
            iconEmoji = "🇮🇳",
            localeTag = "mr-IN",
            confirmationText = "भाषा बदलली आहे.",
            sampleGreeting = "नमस्कार बालमित्रा! Learnzo मध्ये तुमचे स्वागत आहे!"
        ),
        AppLanguage(
            code = "bn",
            nativeName = "বাংলা",
            englishName = "Bengali",
            iconEmoji = "🇮🇳",
            localeTag = "bn-IN",
            confirmationText = "ভাষা পরিবর্তন করা হয়েছে।",
            sampleGreeting = "নমস্কার ছোট্ট বন্ধু! Learnzo-তে তোমাকে স্বাগত!"
        ),
        AppLanguage(
            code = "gu",
            nativeName = "ગુજરાતી",
            englishName = "Gujarati",
            iconEmoji = "🇮🇳",
            localeTag = "gu-IN",
            confirmationText = "ભાષા બદલાઈ ગઈ છે.",
            sampleGreeting = "નમસ્તે નાના દોસ્ત! Learnzo માં આપનું સ્વાગત છે!"
        ),
        AppLanguage(
            code = "ta",
            nativeName = "தமிழ்",
            englishName = "Tamil",
            iconEmoji = "🇮🇳",
            localeTag = "ta-IN",
            confirmationText = "மொழி மாற்றப்பட்டது.",
            sampleGreeting = "வணக்கம் செல்லமே! Learnzo-விற்கு உங்களை வரவேற்கிறோம்!"
        ),
        AppLanguage(
            code = "te",
            nativeName = "తెలుగు",
            englishName = "Telugu",
            iconEmoji = "🇮🇳",
            localeTag = "te-IN",
            confirmationText = "భాష మార్చబడింది.",
            sampleGreeting = "నమస్కారం చిన్నారి నేస్తం! Learnzo కి స్వాగతం!"
        ),
        AppLanguage(
            code = "kn",
            nativeName = "ಕನ್ನಡ",
            englishName = "Kannada",
            iconEmoji = "🇮🇳",
            localeTag = "kn-IN",
            confirmationText = "ಭಾಷೆಯನ್ನು ಬದಲಾಯಿಸಲಾಗಿದೆ.",
            sampleGreeting = "ನಮಸ್ಕಾರ ಪುಟ್ಟ ಗೆಳೆಯ! Learnzo ಗೆ ಸುಸ್ವಾಗತ!"
        ),
        AppLanguage(
            code = "ml",
            nativeName = "മലയാളം",
            englishName = "Malayalam",
            iconEmoji = "🇮🇳",
            localeTag = "ml-IN",
            confirmationText = "ഭാഷ മാറ്റി.",
            sampleGreeting = "നമസ്കാരം കൊച്ചുകൂട്ടുകാരാ! Learnzo-ലേക്ക് സ്വാഗതം!"
        ),
        AppLanguage(
            code = "pa",
            nativeName = "ਪੰਜਾਬੀ",
            englishName = "Punjabi",
            iconEmoji = "🇮🇳",
            localeTag = "pa-IN",
            confirmationText = "ਭਾਸ਼ਾ ਬਦਲ ਦਿੱਤੀ ਗਈ ਹੈ।",
            sampleGreeting = "ਸਤਿ ਸ੍ਰੀ ਅਕਾਲ ਪਿਆਰੇ ਬੱਚਿਓ! Learnzo ਵਿੱਚ ਤੁਹਾਡਾ ਸਵਾਗਤ ਹੈ!"
        ),
        AppLanguage(
            code = "as",
            nativeName = "অসমীয়া",
            englishName = "Assamese",
            iconEmoji = "🇮🇳",
            localeTag = "as-IN",
            confirmationText = "ভাষা সলনি কৰা হ'ল।",
            sampleGreeting = "নমস্কাৰ মৰমৰ বন্ধু! Learnzo লৈ তোমাক স্বাগতম!"
        ),
        AppLanguage(
            code = "or",
            nativeName = "ଓଡ଼ିଆ",
            englishName = "Odia",
            iconEmoji = "🇮🇳",
            localeTag = "or-IN",
            confirmationText = "ଭାଷା ପରିବର୍ତ୍ତିତ ହୋଇଛି।",
            sampleGreeting = "ନମସ୍କାର ଛୋଟ ସାଙ୍ଗ! Learnzo କୁ ତୁମକୁ ସ୍ୱାଗତ!"
        ),
        AppLanguage(
            code = "ur",
            nativeName = "اردو",
            englishName = "Urdu",
            iconEmoji = "🇮🇳",
            localeTag = "ur-IN",
            confirmationText = "زبان تبدیل کر دی گئی ہے۔",
            sampleGreeting = "ہیلو پیارے بچو! Learnzo میں آپ کا استقبال ہے!"
        ),
        AppLanguage(
            code = "sa",
            nativeName = "संस्कृतम्",
            englishName = "Sanskrit",
            iconEmoji = "🇮🇳",
            localeTag = "sa-IN",
            confirmationText = "भाषा परिवर्तिता।",
            sampleGreeting = "नमस्ते प्रिय बाल! Learnzo मध्ये भवतां स्वागतम्!"
        )
    )

    fun getByCode(code: String): AppLanguage {
        return languages.find { it.code.equals(code, ignoreCase = true) }
            ?: languages.find { it.code == "en" }!!
    }

    fun detectDeviceLanguage(): String {
        return try {
            val deviceLang = Locale.getDefault().language.lowercase()
            when {
                deviceLang.startsWith("hi") -> "hi"
                deviceLang.startsWith("mr") -> "mr"
                deviceLang.startsWith("bn") -> "bn"
                deviceLang.startsWith("gu") -> "gu"
                deviceLang.startsWith("ta") -> "ta"
                deviceLang.startsWith("te") -> "te"
                deviceLang.startsWith("kn") -> "kn"
                deviceLang.startsWith("ml") -> "ml"
                deviceLang.startsWith("pa") -> "pa"
                deviceLang.startsWith("as") -> "as"
                deviceLang.startsWith("or") -> "or"
                deviceLang.startsWith("ur") -> "ur"
                deviceLang.startsWith("sa") -> "sa"
                deviceLang.startsWith("en") -> "en"
                else -> "en"
            }
        } catch (e: Exception) {
            "en"
        }
    }
}
