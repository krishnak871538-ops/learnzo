package com.example.model

enum class Difficulty {
    EASY, MEDIUM, HARD, ADVANCED
}

enum class QuestionType {
    MULTIPLE_CHOICE, TRUE_FALSE, FILL_IN_BLANK, NUMERICAL, CONCEPTUAL
}

data class QuizQuestion(
    val id: String,
    val subjectId: String,
    val topicName: String,
    val difficulty: Difficulty,
    val questionHi: String,
    val questionEn: String,
    val visualHint: String,
    val optionsHi: List<String>,
    val optionsEn: List<String>,
    val correctIndex: Int,
    val explanationHi: String,
    val explanationEn: String,
    val teacherAudioHi: String,
    val teacherAudioEn: String,
    val questionType: QuestionType = QuestionType.MULTIPLE_CHOICE,
    val board: String = "CBSE",
    val studentClass: String = "Class 6",
    val conceptSummary: String = ""
) {
    fun getLocalizedQuestion(lang: String): String = if (lang == "en") questionEn else questionHi
    fun getLocalizedOptions(lang: String): List<String> = if (lang == "en") optionsEn else optionsHi
    fun getLocalizedExplanation(lang: String): String = if (lang == "en") explanationEn else explanationHi
    fun getLocalizedTeacherAudio(lang: String): String = if (lang == "en") teacherAudioEn else teacherAudioHi
}

object QuizCatalog {
    val questions: List<QuizQuestion> = listOf(
        // MATHS - EASY
        QuizQuestion(
            id = "math_e_1",
            subjectId = "maths",
            topicName = "Addition",
            difficulty = Difficulty.EASY,
            questionHi = "2 + 2 = कितना होता है?",
            questionEn = "What is 2 + 2?",
            visualHint = "🍎🍎 + 🍎🍎",
            optionsHi = listOf("3", "4", "5", "6"),
            optionsEn = listOf("3", "4", "5", "6"),
            correctIndex = 1,
            explanationHi = "2 में 2 मिलाने पर 4 बनता है! 2 + 2 = 4.",
            explanationEn = "When you combine 2 and 2, you get 4! 2 + 2 = 4.",
            teacherAudioHi = "दो में दो मिलाएँगे तो बनेंगे चार! बिल्कुल सही!",
            teacherAudioEn = "Two plus two is four! Spot on calculation!"
        ),
        QuizQuestion(
            id = "math_e_2",
            subjectId = "maths",
            topicName = "Addition",
            difficulty = Difficulty.EASY,
            questionHi = "5 + 3 = कितना होता है?",
            questionEn = "What is 5 + 3?",
            visualHint = "⭐⭐⭐⭐⭐ + ⭐⭐⭐",
            optionsHi = listOf("7", "8", "9", "6"),
            optionsEn = listOf("7", "8", "9", "6"),
            correctIndex = 1,
            explanationHi = "5 में 3 जोड़ने पर 8 आता है! 5 + 3 = 8.",
            explanationEn = "Adding 3 to 5 gives 8! 5 + 3 = 8.",
            teacherAudioHi = "शाबाश! पाँच और तीन मिलकर बनते हैं आठ!",
            teacherAudioEn = "Well done! Five plus three equals eight!"
        ),
        // MATHS - MEDIUM
        QuizQuestion(
            id = "math_m_1",
            subjectId = "maths",
            topicName = "Multiplication",
            difficulty = Difficulty.MEDIUM,
            questionHi = "4 × 3 = कितना होता है?",
            questionEn = "What is 4 × 3?",
            visualHint = "🍬🍬🍬🍬 (3 बार / 3 times)",
            optionsHi = listOf("10", "12", "14", "16"),
            optionsEn = listOf("10", "12", "14", "16"),
            correctIndex = 1,
            explanationHi = "4 का पहाड़ा 3 बार पढ़ने पर 12 आता है: 4, 8, 12!",
            explanationEn = "Multiplication is repeated addition: 4 + 4 + 4 = 12!",
            teacherAudioHi = "अरे वाह! चार गुणा तीन होता है बारह! शानदार!",
            teacherAudioEn = "Outstanding! Four times three equals twelve!"
        ),
        QuizQuestion(
            id = "math_m_2",
            subjectId = "maths",
            topicName = "Subtraction",
            difficulty = Difficulty.MEDIUM,
            questionHi = "15 - 7 = कितना होता है?",
            questionEn = "What is 15 - 7?",
            visualHint = "🎈 (15 में से 7 उड़ गए)",
            optionsHi = listOf("6", "8", "7", "9"),
            optionsEn = listOf("6", "8", "7", "9"),
            correctIndex = 1,
            explanationHi = "15 में से 7 घटाने पर 8 बचता है!",
            explanationEn = "15 minus 7 leaves 8!",
            teacherAudioHi = "बहुत बढ़िया! पंद्रह में से सात घटाए तो बचे आठ!",
            teacherAudioEn = "Great job! Fifteen minus seven is eight!"
        ),
        // MATHS - HARD
        QuizQuestion(
            id = "math_h_1",
            subjectId = "maths",
            topicName = "Multiplication",
            difficulty = Difficulty.HARD,
            questionHi = "8 × 7 = कितना होता है?",
            questionEn = "What is 8 × 7?",
            visualHint = "🧮 8 groups of 7",
            optionsHi = listOf("54", "56", "58", "64"),
            optionsEn = listOf("54", "56", "58", "64"),
            correctIndex = 1,
            explanationHi = "8 × 7 = 56! आठ सत्ये छप्पन!",
            explanationEn = "8 times 7 is 56! Excellent math master!",
            teacherAudioHi = "वाह मेरे मैथ्स जीनियस! आठ गुणा सात होता है छप्पन!",
            teacherAudioEn = "Splendid math mastery! Eight times seven is 56!"
        ),

        // HINDI
        QuizQuestion(
            id = "hindi_e_1",
            subjectId = "hindi",
            topicName = "Varnamala",
            difficulty = Difficulty.EASY,
            questionHi = "'क' से क्या बनता है?",
            questionEn = "What word starts with 'क' (Ka)?",
            visualHint = "🕊️ / 🪷 / 🍌",
            optionsHi = listOf("कबूतर", "खरगोश", "गमला", "घड़ी"),
            optionsEn = listOf("Kabootar (Pigeon)", "Khargosh (Rabbit)", "Gamla (Pot)", "Ghadi (Clock)"),
            correctIndex = 0,
            explanationHi = "'क' से कबूतर होता है!",
            explanationEn = "'क' stands for Kabootar (Pigeon)!",
            teacherAudioHi = "शाबाश! क से कबूतर गुटर गूं करता है!",
            teacherAudioEn = "Superb! Ka stands for Kabootar!"
        ),
        QuizQuestion(
            id = "hindi_m_1",
            subjectId = "hindi",
            topicName = "Vilom Shabd",
            difficulty = Difficulty.MEDIUM,
            questionHi = "'दिन' का विलोम (उल्टा) शब्द क्या है?",
            questionEn = "What is the opposite of 'Din' (Day)?",
            visualHint = "☀️ ⇄ 🌙",
            optionsHi = listOf("सुबह", "रात", "शाम", "दोपहर"),
            optionsEn = listOf("Morning", "Raat (Night)", "Evening", "Noon"),
            correctIndex = 1,
            explanationHi = "दिन का उल्टा शब्द 'रात' होता है!",
            explanationEn = "The antonym of Day is Night (Raat)!",
            teacherAudioHi = "अति सुंदर! दिन का उल्टा होता है रात!",
            teacherAudioEn = "Terrific! Din opposite is Raat!"
        ),
        QuizQuestion(
            id = "hindi_h_1",
            subjectId = "hindi",
            topicName = "Samanarthi",
            difficulty = Difficulty.HARD,
            questionHi = "'सूर्य' का पर्यायवाची (समानार्थी) शब्द क्या है?",
            questionEn = "What is the synonym for 'Surya' (Sun)?",
            visualHint = "☀️ Sun in sky",
            optionsHi = listOf("चंद्रमा", "रवि", "पवन", "जल"),
            optionsEn = listOf("Moon", "Ravi", "Wind", "Water"),
            correctIndex = 1,
            explanationHi = "सूर्य को 'रवि', 'दिनकर', 'भास्कर' भी कहते हैं!",
            explanationEn = "Surya is also known as Ravi, Dinkar, and Bhaskar!",
            teacherAudioHi = "लाजवाब! सूर्य का पर्यायवाची शब्द रवि है!",
            teacherAudioEn = "Marvelous Hindi vocabulary! Surya means Ravi!"
        ),

        // ENGLISH
        QuizQuestion(
            id = "eng_e_1",
            subjectId = "english",
            topicName = "Phonics",
            difficulty = Difficulty.EASY,
            questionHi = "SUN शब्द का पहला अक्षर क्या है?",
            questionEn = "What is the starting letter of SUN?",
            visualHint = "☀️ S - U - N",
            optionsHi = listOf("A", "S", "M", "T"),
            optionsEn = listOf("A", "S", "M", "T"),
            correctIndex = 1,
            explanationHi = "SUN की शुरुआत 'S' से होती है!",
            explanationEn = "SUN begins with the letter 'S'!",
            teacherAudioHi = "बिल्कुल सही! SUN 'S' से शुरू होता है!",
            teacherAudioEn = "Spot on! S is for Sun!"
        ),
        QuizQuestion(
            id = "eng_m_1",
            subjectId = "english",
            topicName = "Opposites",
            difficulty = Difficulty.MEDIUM,
            questionHi = "What is the opposite of 'HOT'?",
            questionEn = "What is the opposite of 'HOT'?",
            visualHint = "🔥 ⇄ 🧊",
            optionsHi = listOf("Warm", "Cold", "Sweet", "Fast"),
            optionsEn = listOf("Warm", "Cold", "Sweet", "Fast"),
            correctIndex = 1,
            explanationHi = "HOT का उल्टा COLD होता है!",
            explanationEn = "The opposite of HOT is COLD!",
            teacherAudioHi = "Well done! Hot ka opposite Cold hota hai!",
            teacherAudioEn = "Excellent! The opposite of HOT is COLD!"
        ),
        QuizQuestion(
            id = "eng_h_1",
            subjectId = "english",
            topicName = "Plurals",
            difficulty = Difficulty.HARD,
            questionHi = "What is the plural of 'CHILD'?",
            questionEn = "What is the plural of 'CHILD'?",
            visualHint = "👦 ➡️ 👦👧👦",
            optionsHi = listOf("Childs", "Children", "Childrens", "Childes"),
            optionsEn = listOf("Childs", "Children", "Childrens", "Childes"),
            correctIndex = 1,
            explanationHi = "Child का बहुवचन (Plural) 'Children' होता है, Childs नहीं!",
            explanationEn = "One child, many children! Irregular plural rule.",
            teacherAudioHi = "शानदार! एक बच्चा Child, बहुत सारे बच्चे Children!",
            teacherAudioEn = "Superb English grammar! Plural of child is children!"
        ),

        // SCIENCE
        QuizQuestion(
            id = "sci_e_1",
            subjectId = "science",
            topicName = "Living Things",
            difficulty = Difficulty.EASY,
            questionHi = "इनमें से कौन सजीव (Living thing) है?",
            questionEn = "Which of these is a Living Thing?",
            visualHint = "🌱 vs 🪨",
            optionsHi = listOf("पत्थर (Stone)", "पौधा (Plant)", "कुर्सी (Chair)", "खिलौना (Toy)"),
            optionsEn = listOf("Stone", "Plant", "Chair", "Plastic Toy"),
            correctIndex = 1,
            explanationHi = "पौधे सांस लेते हैं और बढ़ते हैं, इसलिए वे Living thing हैं!",
            explanationEn = "Plants breathe, drink water and grow, so they are living!",
            teacherAudioHi = "शाबाश नन्हें वैज्ञानिक! पौधा एक सजीव है!",
            teacherAudioEn = "Great observation! A plant is a living thing!"
        ),
        QuizQuestion(
            id = "sci_m_1",
            subjectId = "science",
            topicName = "Five Senses",
            difficulty = Difficulty.MEDIUM,
            questionHi = "हम खुशबू और गंध किस अंग से सूंघते हैं?",
            questionEn = "Which organ do we use to smell flowers?",
            visualHint = "👃 Smelling rose",
            optionsHi = listOf("आंखें (Eyes)", "नाक (Nose)", "कान (Ears)", "जीभ (Tongue)"),
            optionsEn = listOf("Eyes", "Nose", "Ears", "Tongue"),
            correctIndex = 1,
            explanationHi = "नाक से हम सूंघते हैं, जीभ से स्वाद लेते हैं, आंखों से देखते हैं!",
            explanationEn = "We smell with our nose and taste with our tongue!",
            teacherAudioHi = "वाह! नाक से हम प्यारी-प्यारी खुशबू सूंघते हैं!",
            teacherAudioEn = "Super! Nose is our sense organ for smell!"
        ),

        // GK
        QuizQuestion(
            id = "gk_e_1",
            subjectId = "gk",
            topicName = "National Symbols",
            difficulty = Difficulty.EASY,
            questionHi = "भारत का राष्ट्रीय पक्षी कौन सा है?",
            questionEn = "What is the National Bird of India?",
            visualHint = "🦚 Peacock dancing",
            optionsHi = listOf("तोता (Parrot)", "मोर (Peacock)", "कौआ (Crow)", "कबूतर (Pigeon)"),
            optionsEn = listOf("Parrot", "Peacock", "Crow", "Pigeon"),
            correctIndex = 1,
            explanationHi = "मोर भारत का सुंदर राष्ट्रीय पक्षी है!",
            explanationEn = "The Peacock is the proud national bird of India!",
            teacherAudioHi = "अरे वाह! मोर पंख फैलाकर नाचता है, वही हमारा राष्ट्रीय पक्षी है!",
            teacherAudioEn = "Spot on explorer! The graceful Peacock is India's national bird!"
        ),
        QuizQuestion(
            id = "gk_m_1",
            subjectId = "gk",
            topicName = "Solar System",
            difficulty = Difficulty.MEDIUM,
            questionHi = "किस ग्रह को 'लाल ग्रह' (Red Planet) कहा जाता है?",
            questionEn = "Which planet is known as the 'Red Planet'?",
            visualHint = "🔴 Mars planet",
            optionsHi = listOf("शुक्र (Venus)", "मंगल (Mars)", "बृहस्पति (Jupiter)", "शनि (Saturn)"),
            optionsEn = listOf("Venus", "Mars", "Jupiter", "Saturn"),
            correctIndex = 1,
            explanationHi = "मंगल ग्रह (Mars) अपनी लाल मिट्टी के कारण लाल ग्रह कहलाता है!",
            explanationEn = "Mars is called the Red Planet because of iron-rich red dust!",
            teacherAudioHi = "कमाल की जानकारी! मंगल यानी Mars ही लाल ग्रह है!",
            teacherAudioEn = "Awesome astronomy! Mars is the Red Planet!"
        ),

        // COMPUTER
        QuizQuestion(
            id = "comp_e_1",
            subjectId = "computer",
            topicName = "Hardware",
            difficulty = Difficulty.EASY,
            questionHi = "कंप्यूटर पर टाइपिंग करने के लिए किसका उपयोग होता है?",
            questionEn = "Which device is used to type letters on a computer?",
            visualHint = "⌨️ Keyboard",
            optionsHi = listOf("माउस (Mouse)", "कीबोर्ड (Keyboard)", "स्पीकर (Speaker)", "प्रिंटर (Printer)"),
            optionsEn = listOf("Mouse", "Keyboard", "Speaker", "Printer"),
            correctIndex = 1,
            explanationHi = "कीबोर्ड पर विभिन्न बटन होते हैं जिनसे हम लिखते हैं!",
            explanationEn = "A keyboard has letter and number keys for typing!",
            teacherAudioHi = "शाबाश कोडर! कीबोर्ड से हम कंप्यूटर में लिखते हैं!",
            teacherAudioEn = "Perfect tech knowledge! Keyboard is used for typing!"
        ),
        QuizQuestion(
            id = "comp_m_1",
            subjectId = "computer",
            topicName = "Software",
            difficulty = Difficulty.MEDIUM,
            questionHi = "कंप्यूटर में किसी चीज़ को मिटाने के लिए कौन सी Key दबाते हैं?",
            questionEn = "Which key is pressed to erase or delete letters?",
            visualHint = "⌫ Backspace",
            optionsHi = listOf("Enter", "Backspace", "Shift", "Caps Lock"),
            optionsEn = listOf("Enter", "Backspace", "Shift", "Caps Lock"),
            correctIndex = 1,
            explanationHi = "Backspace की से बाईं ओर के अक्षर मिटाए जाते हैं!",
            explanationEn = "The Backspace key deletes characters to the left!",
            teacherAudioHi = "बिलकुल सही! Backspace चाबी से हम गलतियाँ मिटाते हैं!",
            teacherAudioEn = "Spot on! Backspace erases typed characters!"
        )
    )

    fun getQuestionsBySubject(subjectId: String): List<QuizQuestion> {
        return questions.filter { it.subjectId == subjectId }
    }

    fun getQuestionsByTopic(topicName: String): List<QuizQuestion> {
        return questions.filter { it.topicName.equals(topicName, ignoreCase = true) }
    }
}
