package com.example.model

data class Subject(
    val id: String,
    val nameHi: String,
    val nameEn: String,
    val teacherName: String,
    val teacherTitle: String,
    val teacherEmoji: String,
    val teacherGender: String, // "MALE" or "FEMALE"
    val primaryColorHex: Long,
    val accentColorHex: Long,
    val iconEmoji: String,
    val welcomeGreetingHi: String,
    val welcomeGreetingEn: String,
    val encouragePraiseHi: List<String>,
    val encouragePraiseEn: List<String>,
    val gentleHelpHi: String,
    val gentleHelpEn: String
) {
    fun getLocalizedName(lang: String): String {
        return when (id) {
            "maths" -> when (lang.lowercase()) {
                "en" -> "Mathematics"
                "mr" -> "गणित (Maths)"
                "bn" -> "গণিত (Maths)"
                "gu" -> "ગણિત (Maths)"
                "ta" -> "கணிதம் (Maths)"
                "te" -> "గణితం (Maths)"
                "kn" -> "ಗಣಿತ (Maths)"
                "ml" -> "ഗണിതം (Maths)"
                "pa" -> "ਹਿਸਾਬ / ਗਣਿਤ"
                "as" -> "গণিত (Maths)"
                "or" -> "ଗଣିତ (Maths)"
                "ur" -> "ریاضی (Maths)"
                "sa" -> "गणितम् (Maths)"
                else -> nameHi
            }
            "hindi" -> when (lang.lowercase()) {
                "en" -> "Hindi Language"
                "mr" -> "हिंदी भाषा"
                "bn" -> "হিন্দি ভাষা"
                "gu" -> "હિન્દી ભાષા"
                "ta" -> "இந்தி மொழி"
                "te" -> "హిందీ భాష"
                "kn" -> "ಹಿಂದಿ ಭಾಷೆ"
                "ml" -> "ഹിന്ദി ഭാഷ"
                "pa" -> "ਹਿੰਦੀ ਭਾਸ਼ਾ"
                "as" -> "হিন্দী ভাষা"
                "or" -> "ହିନ୍ଦୀ ଭାଷା"
                "ur" -> "ہندی زبان"
                "sa" -> "हिन्दीभाषा"
                else -> nameHi
            }
            "english" -> when (lang.lowercase()) {
                "en" -> "English Phonics & Fun"
                "mr" -> "इंग्रजी (English)"
                "bn" -> "ইংরেজি (English)"
                "gu" -> "અંગ્રેજી (English)"
                "ta" -> "ஆங்கிலம் (English)"
                "te" -> "ఆంగ్లం (English)"
                "kn" -> "ಇಂಗ್ಲಿಷ್ (English)"
                "ml" -> "ഇംഗ്ലീഷ് (English)"
                "pa" -> "ਅੰਗਰੇਜ਼ੀ (English)"
                "as" -> "ইংৰাজী (English)"
                "or" -> "ଇଂରାଜୀ (English)"
                "ur" -> "انگریزی (English)"
                "sa" -> "आङ्ग्लभाषा (English)"
                else -> nameHi
            }
            "gk" -> when (lang.lowercase()) {
                "en" -> "General Knowledge (GK)"
                "mr" -> "सामान्य ज्ञान (GK)"
                "bn" -> "সাধারণ জ্ঞান (GK)"
                "gu" -> "સામાન્ય જ્ઞાન (GK)"
                "ta" -> "பொது அறிவு (GK)"
                "te" -> "సాధారణ జ్ఞానం (GK)"
                "kn" -> "ಸಾಮಾನ್ಯ ಜ್ಞಾನ (GK)"
                "ml" -> "പൊതുവിജ്ഞാനം (GK)"
                "pa" -> "ਆਮ ਗਿਆਨ (GK)"
                "as" -> "সাধাৰণ জ্ঞান (GK)"
                "or" -> "ସାଧାରଣ ଜ୍ଞାନ (GK)"
                "ur" -> "معلومات عامہ (GK)"
                "sa" -> "सामान्यज्ञानम् (GK)"
                else -> nameHi
            }
            "science" -> when (lang.lowercase()) {
                "en" -> "Science & Nature"
                "mr" -> "विज्ञान (Science)"
                "bn" -> "বিজ্ঞান (Science)"
                "gu" -> "વિજ્ઞાન (Science)"
                "ta" -> "அறிவியல் (Science)"
                "te" -> "విజ్ఞాన శాస్త్రం (Science)"
                "kn" -> "ವಿಜ್ಞಾನ (Science)"
                "ml" -> "ശാസ്ത്രം (Science)"
                "pa" -> "ਵਿਗਿਆਨ (Science)"
                "as" -> "বিজ্ঞান (Science)"
                "or" -> "ବିଜ୍ଞାନ (Science)"
                "ur" -> "سائنس (Science)"
                "sa" -> "विज्ञानम् (Science)"
                else -> nameHi
            }
            "computer" -> when (lang.lowercase()) {
                "en" -> "Computer & Coding"
                "mr" -> "संगणक (Computer)"
                "bn" -> "কম্পিউটার ও কোডিং"
                "gu" -> "કમ્પ્યુટર અને કોડિંગ"
                "ta" -> "கணினி & கோடிங்"
                "te" -> "కంప్యూటర్ & కోడింగ్"
                "kn" -> "ಕಂಪ್ಯೂಟರ್ & ಕೋಡಿಂಗ್"
                "ml" -> "കമ്പ്യൂട്ടറും കോഡിംഗും"
                "pa" -> "ਕੰਪਿਊਟਰ ਅਤੇ ਕੋਡਿੰਗ"
                "as" -> "কম্পিউটাৰ আৰু ক'ডিং"
                "or" -> "କମ୍ପ୍ୟୁଟର ଓ କୋଡିଂ"
                "ur" -> "کمپیوٹر اور کوڈنگ"
                "sa" -> "सङ्गणकम् (Computer)"
                else -> nameHi
            }
            else -> if (lang == "en") nameEn else nameHi
        }
    }

    fun getLocalizedGreeting(lang: String): String {
        return if (lang == "en") welcomeGreetingEn else welcomeGreetingHi
    }

    fun getLocalizedPraise(lang: String): List<String> {
        return if (lang == "en") encouragePraiseEn else encouragePraiseHi
    }

    fun getLocalizedHelp(lang: String): String {
        return if (lang == "en") gentleHelpEn else gentleHelpHi
    }
}

object SubjectCatalog {
    val subjects = listOf(
        Subject(
            id = "maths",
            nameHi = "गणित (Maths)",
            nameEn = "Mathematics",
            teacherName = "Prof. Raman",
            teacherTitle = "Maths Guru 👨‍🏫",
            teacherEmoji = "👨‍🏫",
            teacherGender = "MALE",
            primaryColorHex = 0xFFFF6D00,
            accentColorHex = 0xFFFFD180,
            iconEmoji = "🔢",
            welcomeGreetingHi = "नमस्ते! मैं हूँ आपका Maths Guru Prof. Raman! चलो संख्याओं के साथ जादू करते हैं!",
            welcomeGreetingEn = "Hello champion! I am Prof. Raman, your Maths Guru! Let's play with magic numbers!",
            encouragePraiseHi = listOf("वाह! बहुत बढ़िया!", "शाबाश! आपने सही हल किया!", "कमाल कर दिया! 🌟"),
            encouragePraiseEn = listOf("Outstanding math work!", "Spot on! You are a genius!", "Brilliant calculation! 🌟"),
            gentleHelpHi = "कोई बात नहीं! गलतियों से ही तो सीखते हैं। चलो मैं आसान तरीके से समझाता हूँ।",
            gentleHelpEn = "Don't worry at all! Mistakes help our brain grow. Let's solve it step by step."
        ),
        Subject(
            id = "hindi",
            nameHi = "हिंदी (Hindi)",
            nameEn = "Hindi Language",
            teacherName = "Shruti Mam",
            teacherTitle = "Hindi Shikshika 👩‍🏫",
            teacherEmoji = "👩‍🏫",
            teacherGender = "FEMALE",
            primaryColorHex = 0xFFE91E63,
            accentColorHex = 0xFFF8BBD0,
            iconEmoji = "🇮🇳",
            welcomeGreetingHi = "नमस्ते बच्चों! मैं श्रुति मैम हूँ। आज हम सुंदर हिंदी वर्ण और शब्द सीखेंगे!",
            welcomeGreetingEn = "Namaste dear learner! I am Shruti Mam. Today we will explore beautiful Hindi words!",
            encouragePraiseHi = listOf("अति उत्तम! बहुत सुंदर जवाब!", "शाबाश मेरे होनहार बच्चे!", "सुपरब! आप बहुत होशियार हो!"),
            encouragePraiseEn = listOf("Superb Hindi comprehension!", "Terrific pronunciation and answer!", "Wonderful job!"),
            gentleHelpHi = "प्यारे बच्चे, परेशान मत होइए। शब्दों को प्यार से जोड़ना सीखिए।",
            gentleHelpEn = "Take a breath, dear! Let's break the word down and learn it easily together."
        ),
        Subject(
            id = "english",
            nameHi = "अंग्रेज़ी (English)",
            nameEn = "English Grammar & Phonics",
            teacherName = "Mr. Oliver",
            teacherTitle = "English Expert 👨‍🏫",
            teacherEmoji = "👨‍🏫",
            teacherGender = "MALE",
            primaryColorHex = 0xFF2979FF,
            accentColorHex = 0xFFBBDEFB,
            iconEmoji = "🔤",
            welcomeGreetingHi = "Hello! मैं Mr. Oliver हूँ। चलो मजेदार English rhymes और vocabulary सीखते हैं!",
            welcomeGreetingEn = "Hello there! I am Mr. Oliver. Let's make English your superpower with fun rhymes and words!",
            encouragePraiseHi = listOf("Great job! Perfect grammar!", "Fantastic! You nailed it!", "You speak like a champ!"),
            encouragePraiseEn = listOf("Fantastic job! Brilliant vocabulary!", "You nailed it champ!", "Splendid work!"),
            gentleHelpHi = "कोई बात नहीं! Phonics और spelling की practice से सब आसान हो जाएगा।",
            gentleHelpEn = "No worries at all! With phonics practice, words become super friendly."
        ),
        Subject(
            id = "gk",
            nameHi = "सामान्य ज्ञान (GK)",
            nameEn = "General Knowledge",
            teacherName = "Priya Mam",
            teacherTitle = "Discovery Guide 👩‍🏫",
            teacherEmoji = "👩‍🏫",
            teacherGender = "FEMALE",
            primaryColorHex = 0xFF00B0FF,
            accentColorHex = 0xFFB3E5FC,
            iconEmoji = "🌍",
            welcomeGreetingHi = "Hello नन्हें वैज्ञानिकों! मैं Priya Mam हूँ। चलो पूरी दुनिया और अंतरिक्ष की सैर करें!",
            welcomeGreetingEn = "Hello little explorers! I am Priya Mam. Let's journey across planets and our world!",
            encouragePraiseHi = listOf("अरे वाह! आपका ज्ञान तो अद्भुत है!", "शाबाश! आपने सही पहचाना!", "सुपर एक्सप्लोरर! 🚀"),
            encouragePraiseEn = listOf("Incredible curiosity and knowledge!", "Spot on exploration! 🚀", "Master of wonders!"),
            gentleHelpHi = "दुनिया रहस्यों से भरी है! चलो इस रोचक तथ्य को मिलकर जानते हैं।",
            gentleHelpEn = "Our world is full of fun facts! Let's discover this one together."
        ),
        Subject(
            id = "science",
            nameHi = "विज्ञान (Science)",
            nameEn = "Science & Nature",
            teacherName = "Dr. Vigyan",
            teacherTitle = "Science Explorer 👨‍🔬",
            teacherEmoji = "👨‍🔬",
            teacherGender = "MALE",
            primaryColorHex = 0xFF00C853,
            accentColorHex = 0xFFC8E6C9,
            iconEmoji = "🔬",
            welcomeGreetingHi = "स्वागत है लैब में! मैं हूँ Dr. Vigyan। चलो पौधे, जानवर और अंतरिक्ष के रहस्य सुलझाएँ!",
            welcomeGreetingEn = "Welcome to the Lab! I am Dr. Vigyan. Let's unlock wonders of nature, plants and space!",
            encouragePraiseHi = listOf("वाह! वैज्ञानिक दिमाग! एकदम सही जवाब!", "अद्भुत अवलोकन!", "भविष्य के वैज्ञानिक! 🧪"),
            encouragePraiseEn = listOf("Brilliant scientific observation!", "You think like a true scientist! 🧪", "Fantastic deduction!"),
            gentleHelpHi = "प्रयोग में गलतियाँ ही नई खोज की शुरुआत होती हैं। चलो फिर से देखते हैं!",
            gentleHelpEn = "Every experiment teaches us something new! Let's check how nature works here."
        ),
        Subject(
            id = "computer",
            nameHi = "कंप्यूटर (Computer)",
            nameEn = "Computer & Coding Fun",
            teacherName = "Ananya Mam",
            teacherTitle = "Tech Whiz 👩‍💻",
            teacherEmoji = "👩‍💻",
            teacherGender = "FEMALE",
            primaryColorHex = 0xFF7C4DFF,
            accentColorHex = 0xFFD1C4E9,
            iconEmoji = "💻",
            welcomeGreetingHi = "Hello Tech Wizards! मैं Ananya Mam हूँ। चलो कंप्यूटर, कीबोर्ड और कोडिंग की दुनिया में चलें!",
            welcomeGreetingEn = "Hello Tech Wizards! I am Ananya Mam. Let's code and master computers with games!",
            encouragePraiseHi = listOf("सुपर कोडर! बिल्कुल सही कमांड!", "कमाल की लॉजिकल सोच!", "Tech Master! ⚡"),
            encouragePraiseEn = listOf("Awesome logical thinking!", "Code compiled with 100% accuracy! ⚡", "Future Tech Leader!"),
            gentleHelpHi = "कंप्यूटर बहुत सीधा है, स्टेप-बाय-स्टेप सोचेंगे तो तुरंत समझ आ जाएगा।",
            gentleHelpEn = "Computers follow simple logical steps! Let's trace it together."
        )
    )

    fun getById(id: String): Subject {
        return subjects.find { it.id == id } ?: subjects.first()
    }
}
