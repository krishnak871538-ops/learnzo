package com.example.model

data class LessonSlide(
    val id: Int,
    val sceneTitleHi: String,
    val sceneTitleEn: String,
    val teacherSpeechHi: String,
    val teacherSpeechEn: String,
    val visualEmoji: String,
    val interactiveVisualDescription: String,
    val animationType: String, // "COUNTING_APPLES", "SHAPES_MATCH", "WORD_SPELL", "PLANT_GROWTH", "KEYBOARD_KEYS", "SOLAR_SYSTEM"
    val miniCheckQuestionHi: String? = null,
    val miniCheckQuestionEn: String? = null,
    val options: List<String> = emptyList(),
    val correctIndex: Int = 0
) {
    fun getLocalizedSceneTitle(lang: String): String = if (lang == "en") sceneTitleEn else sceneTitleHi
    fun getLocalizedTeacherSpeech(lang: String): String = if (lang == "en") teacherSpeechEn else teacherSpeechHi
    fun getLocalizedQuestion(lang: String): String? = if (lang == "en") miniCheckQuestionEn else miniCheckQuestionHi
}

data class Lesson(
    val id: String,
    val subjectId: String,
    val titleHi: String,
    val titleEn: String,
    val topicName: String,
    val estimatedMinutes: Int,
    val slides: List<LessonSlide>
) {
    fun getLocalizedTitle(lang: String): String = if (lang == "en") titleEn else titleHi
}

object LessonCatalog {
    val lessons: List<Lesson> = listOf(
        Lesson(
            id = "math_addition_fun",
            subjectId = "maths",
            titleHi = "जादुई जोड़ (Addition Magic)",
            titleEn = "Magical Addition Fun",
            topicName = "Addition",
            estimatedMinutes = 5,
            slides = listOf(
                LessonSlide(
                    id = 1,
                    sceneTitleHi = "सेबों की दावत 🍎",
                    sceneTitleEn = "The Apple Feast 🍎",
                    teacherSpeechHi = "नमस्ते प्यारे बच्चो! हमारे पास टोकरी में 2 मीठे लाल सेब 🍎🍎 हैं। और दोस्त ने 3 और सेब 🍎🍎🍎 दे दिए!",
                    teacherSpeechEn = "Welcome kids! Imagine we have 2 sweet red apples 🍎🍎 in our basket, and our buddy gave us 3 more apples 🍎🍎🍎!",
                    visualEmoji = "🍎 🍎 + 🍎 🍎 🍎",
                    interactiveVisualDescription = "2 Apples + 3 Apples",
                    animationType = "COUNTING_APPLES"
                ),
                LessonSlide(
                    id = 2,
                    sceneTitleHi = "सबको एक साथ गिनें! 🧮",
                    sceneTitleEn = "Let's Count Them All! 🧮",
                    teacherSpeechHi = "चलो मिलकर गिनते हैं: 1, 2... फिर 3, 4, 5! यानी 2 + 3 = 5 सेब! जोड़ का मतलब है सब चीजों को एक साथ मिलाना!",
                    teacherSpeechEn = "Let's count together: 1, 2... and then 3, 4, 5! So 2 + 3 equals 5 apples! Addition means putting items together!",
                    visualEmoji = "🍎 🍎 🍎 🍎 🍎 = 5",
                    interactiveVisualDescription = "Total 5 Delicious Apples",
                    animationType = "COUNTING_APPLES",
                    miniCheckQuestionHi = "अगर आपके पास 2 सेब थे और 3 और मिले, तो कुल कितने हुए?",
                    miniCheckQuestionEn = "If you have 2 apples and get 3 more, how many in total?",
                    options = listOf("4", "5", "6"),
                    correctIndex = 1
                ),
                LessonSlide(
                    id = 3,
                    sceneTitleHi = "आसान ट्रिक: उंगलियों पर जोड़ ✨",
                    sceneTitleEn = "Magic Finger Count Trick ✨",
                    teacherSpeechHi = "बड़ा नंबर हमेशा अपने दिमाग में रखें, और छोटा नंबर अपनी उंगलियों पर! जैसे 5 + 2 के लिए, दिमाग में 5, और उंगलियों पर 6, 7! वाह!",
                    teacherSpeechEn = "Keep the bigger number in your mind, and smaller number on your fingers! For 5 + 2, say 5 in mind, then count 6, 7! Magic!",
                    visualEmoji = "🧠 5 + ✌️ 2 = 7 ⭐",
                    interactiveVisualDescription = "Mental Addition Secret",
                    animationType = "COUNTING_APPLES"
                )
            )
        ),
        Lesson(
            id = "hindi_varnamala_joy",
            subjectId = "hindi",
            titleHi = "अ से अनार, आ से आम 🥭",
            titleEn = "Hindi Swar & Matras",
            topicName = "Swar & Varnamala",
            estimatedMinutes = 5,
            slides = listOf(
                LessonSlide(
                    id = 1,
                    sceneTitleHi = "स्वर की पहचान: अ और आ",
                    sceneTitleEn = "Recognizing Swar: A & Aa",
                    teacherSpeechHi = "सुप्रभात बच्चो! 'अ' से बनता है अनार दानेदार! और 'आ' से बनता है सबका प्यारा रसीला आम!",
                    teacherSpeechEn = "Good day children! 'अ' stands for Anar (Pomegranate) and 'आ' stands for sweet Aam (Mango)!",
                    visualEmoji = "अ 🍎 → आ 🥭",
                    interactiveVisualDescription = "अ से अनार, आ से आम",
                    animationType = "WORD_SPELL"
                ),
                LessonSlide(
                    id = 2,
                    sceneTitleHi = "इ से इमली, ई से ईख 🌾",
                    sceneTitleEn = "I se Imli, Ee se Eekh",
                    teacherSpeechHi = "'इ' से खट्टी-मीठी इमली होती है, और 'ई' से मीठा ईख (गन्ना)! दोनों की आवाज़ में छोटा और बड़ा अंतर समझिए।",
                    teacherSpeechEn = "'इ' brings tangy Imli, and 'ई' gives us sugary Sugarcane! Hear the musical difference between short and long vowel sound!",
                    visualEmoji = "इ 🌿 → ई 🎋",
                    interactiveVisualDescription = "इ vs ई",
                    animationType = "WORD_SPELL",
                    miniCheckQuestionHi = "'आ' से कौन सा फल बनता है?",
                    miniCheckQuestionEn = "Which fruit starts with 'आ' (Aa)?",
                    options = listOf("केला", "आम", "सेब"),
                    correctIndex = 1
                )
            )
        ),
        Lesson(
            id = "english_phonics_fun",
            subjectId = "english",
            titleHi = "Phonics और Rhyming Words 🎵",
            titleEn = "Phonics & Rhyming Words 🎵",
            topicName = "Phonics",
            estimatedMinutes = 5,
            slides = listOf(
                LessonSlide(
                    id = 1,
                    sceneTitleHi = "The 'AT' Family: Cat on a Mat 🐱",
                    sceneTitleEn = "The 'AT' Family: Cat on a Mat 🐱",
                    teacherSpeechHi = "Listen to the sounds! C-A-T is CAT! B-A-T is BAT! H-A-T is HAT! All these words rhyme together!",
                    teacherSpeechEn = "Listen carefully! C-A-T is Cat! B-A-T is Bat! H-A-T is Hat! They all share the same sweet ending sound -AT!",
                    visualEmoji = "🐱 Cat + 🧢 Hat + 🏏 Bat",
                    interactiveVisualDescription = "Rhyming Cat, Hat, Bat",
                    animationType = "WORD_SPELL"
                ),
                LessonSlide(
                    id = 2,
                    sceneTitleHi = "Magic Rhyme Challenge 🎯",
                    sceneTitleEn = "Magic Rhyme Challenge 🎯",
                    teacherSpeechHi = "When letters make music together, reading English stories becomes super easy and exciting!",
                    teacherSpeechEn = "When you join phonics sounds smoothly, reading any storybook feels like playing an easy game!",
                    visualEmoji = "📖 Read & Rhyme ⭐",
                    interactiveVisualDescription = "Phonics Master",
                    animationType = "WORD_SPELL",
                    miniCheckQuestionHi = "Which word rhymes with CAT?",
                    miniCheckQuestionEn = "Which word rhymes with CAT?",
                    options = listOf("DOG", "BAT", "SUN"),
                    correctIndex = 1
                )
            )
        ),
        Lesson(
            id = "science_plant_life",
            subjectId = "science",
            titleHi = "पौधे अपना खाना कैसे बनाते हैं? 🌱",
            titleEn = "How Plants Make Food 🌱",
            topicName = "Photosynthesis",
            estimatedMinutes = 6,
            slides = listOf(
                LessonSlide(
                    id = 1,
                    sceneTitleHi = "सूरज की रोशनी और नन्हा बीज ☀️",
                    sceneTitleEn = "Sunlight and the Tiny Seed ☀️",
                    teacherSpeechHi = "मिट्टी में एक नन्हा बीज सो रहा था। जब सूरज की धूप ☀️ और बारिश का पानी 💧 मिले, तो वह जाग गया!",
                    teacherSpeechEn = "A tiny seed slept in rich soil. When warm sunshine ☀️ and fresh raindrops 💧 touched it, it sprouted into life!",
                    visualEmoji = "🌱 + ☀️ + 💧 = 🌿",
                    interactiveVisualDescription = "Seed Sprouting with Sun & Water",
                    animationType = "PLANT_GROWTH"
                ),
                LessonSlide(
                    id = 2,
                    sceneTitleHi = "हरी पत्तियों की जादुई रसोई (Photosynthesis) 🍃",
                    sceneTitleEn = "The Magical Green Kitchen 🍃",
                    teacherSpeechHi = "पत्तियां पौधे की रसोई होती हैं! हरी पत्तियां धूप, हवा और पानी से खाना बनाती हैं और हमें मीठी ताजी ऑक्सीजन देती हैं!",
                    teacherSpeechEn = "Leaves are nature's kitchen! With green chlorophyll, they turn sunlight, air, and water into sweet food and fresh oxygen for us!",
                    visualEmoji = "🍃 ➡️ 🌬️ Oxygen for Us! 💚",
                    interactiveVisualDescription = "Photosynthesis in Action",
                    animationType = "PLANT_GROWTH",
                    miniCheckQuestionHi = "पौधों को भोजन बनाने के लिए किसकी ज़रूरत होती है?",
                    miniCheckQuestionEn = "What do plants need to prepare their food?",
                    options = listOf("सिर्फ चॉकलेट", "धूप, पानी और हवा", "सिर्फ रात का अंधेरा"),
                    correctIndex = 1
                )
            )
        ),
        Lesson(
            id = "computer_basics",
            subjectId = "computer",
            titleHi = "कंप्यूटर के अंग और कीबोर्ड ⌨️",
            titleEn = "Computer Parts & Keyboard ⌨️",
            topicName = "Hardware & Typing",
            estimatedMinutes = 5,
            slides = listOf(
                LessonSlide(
                    id = 1,
                    sceneTitleHi = "कंप्यूटर का दिमाग: CPU 🧠",
                    sceneTitleEn = "The Brain of Computer: CPU 🧠",
                    teacherSpeechHi = "जैसे आपका दिमाग सोचता है, वैसे ही CPU कंप्यूटर का Brain होता है! मॉनिटर हमें चित्र दिखाता है, और माउस से हम क्लिक करते हैं!",
                    teacherSpeechEn = "Just like you think with your brain, CPU is the Brain of the computer! The monitor shows pictures, and the mouse clicks!",
                    visualEmoji = "🖥️ Monitor + 🧠 CPU + 🖱️ Mouse",
                    interactiveVisualDescription = "Computer Main Parts",
                    animationType = "KEYBOARD_KEYS"
                ),
                LessonSlide(
                    id = 2,
                    sceneTitleHi = "सबसे बड़ी चाबी: Spacebar! 🚀",
                    sceneTitleEn = "The Longest Key: Spacebar! 🚀",
                    teacherSpeechHi = "कीबोर्ड पर सबसे लंबी की कौन सी है? वह है Spacebar! यह शब्दों के बीच प्यार से खाली जगह (gap) बनाती है!",
                    teacherSpeechEn = "What is the longest key on the keyboard? It's the Spacebar! It gives words room to breathe with space!",
                    visualEmoji = "⌨️ [       SPACEBAR       ]",
                    interactiveVisualDescription = "Keyboard Spacebar Key",
                    animationType = "KEYBOARD_KEYS",
                    miniCheckQuestionHi = "कंप्यूटर का Brain किसे कहते हैं?",
                    miniCheckQuestionEn = "Which part is called the Brain of the Computer?",
                    options = listOf("Mouse", "CPU", "Keyboard"),
                    correctIndex = 1
                )
            )
        ),
        Lesson(
            id = "gk_solar_system",
            subjectId = "gk",
            titleHi = "हमारा सौरमंडल और ग्रह 🪐",
            titleEn = "Our Solar System & Planets 🪐",
            topicName = "Solar System",
            estimatedMinutes = 6,
            slides = listOf(
                LessonSlide(
                    id = 1,
                    sceneTitleHi = "सूरज और 8 अद्भुत ग्रह ☀️",
                    sceneTitleEn = "Sun and 8 Amazing Planets ☀️",
                    teacherSpeechHi = "हमारे सौरमंडल के केंद्र में बड़ा चमकता सूरज ☀️ है, जिसके चारों तरफ 8 ग्रह चक्कर लगाते हैं! हमारी प्यारी पृथ्वी तीसरी है!",
                    teacherSpeechEn = "At the center shines the mighty Sun ☀️, surrounded by 8 dancing planets! Our beautiful blue Earth is the 3rd planet!",
                    visualEmoji = "☀️ 🌑 🌕 🌍 🔴 🪐",
                    interactiveVisualDescription = "Sun, Mercury, Venus, Earth, Mars, Jupiter, Saturn",
                    animationType = "SOLAR_SYSTEM"
                ),
                LessonSlide(
                    id = 2,
                    sceneTitleHi = "नीला ग्रह: हमारी पृथ्वी 🌍",
                    sceneTitleEn = "The Blue Planet: Earth 🌍",
                    teacherSpeechHi = "अंतरिक्ष से देखने पर हमारी पृथ्वी नीली दिखती है क्योंकि यहाँ 70% पानी और महासागर हैं! यही एकमात्र घर है जहाँ जीवन है!",
                    teacherSpeechEn = "From deep space, Earth sparkles like a blue marble because 70% is covered by water and oceans! Our lovely home!",
                    visualEmoji = "🌍 Blue Marble in Space ⭐",
                    interactiveVisualDescription = "Earth from Space",
                    animationType = "SOLAR_SYSTEM",
                    miniCheckQuestionHi = "अंतरिक्ष से पृथ्वी किस रंग की दिखाई देती है?",
                    miniCheckQuestionEn = "What color does Earth appear from outer space?",
                    options = listOf("लाल (Red)", "नीला (Blue)", "पीला (Yellow)"),
                    correctIndex = 1
                )
            )
        )
    )

    fun getBySubject(subjectId: String): List<Lesson> {
        return lessons.filter { it.subjectId == subjectId }
    }

    fun getById(id: String): Lesson {
        return lessons.find { it.id == id } ?: lessons.first()
    }
}
