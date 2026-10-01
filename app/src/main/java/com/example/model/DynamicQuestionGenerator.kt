package com.example.model

import kotlin.random.Random

object DynamicQuestionGenerator {

    private val askedQuestionHashes = mutableSetOf<String>()

    fun generateQuestion(
        subjectId: String,
        topicName: String? = null,
        currentDifficulty: Difficulty = Difficulty.EASY,
        board: String = "CBSE",
        studentClass: String = "Class 6",
        weakTopics: List<String> = emptyList()
    ): QuizQuestion {
        // If weak topics exist and no explicit topic is requested, 60% chance to target weak topic to reinforce concept
        val targetTopic = if (!topicName.isNullOrBlank()) {
            topicName
        } else if (weakTopics.isNotEmpty() && Random.nextFloat() < 0.65f) {
            weakTopics.random()
        } else {
            null
        }

        // Try up to 6 attempts to ensure non-repetitive dynamic questions
        for (attempt in 0..5) {
            val q = when (subjectId.lowercase()) {
                "maths" -> generateMathsQuestion(targetTopic, currentDifficulty, studentClass, board)
                "science" -> generateScienceQuestion(targetTopic, currentDifficulty, studentClass, board)
                "english" -> generateEnglishQuestion(targetTopic, currentDifficulty, studentClass, board)
                "hindi" -> generateHindiQuestion(targetTopic, currentDifficulty, studentClass, board)
                "computer" -> generateComputerQuestion(targetTopic, currentDifficulty, studentClass, board)
                "gk" -> generateGkQuestion(targetTopic, currentDifficulty, studentClass, board)
                else -> generateMathsQuestion(targetTopic, currentDifficulty, studentClass, board)
            }

            val hash = "${q.subjectId}_${q.topicName}_${q.questionEn}_${q.correctIndex}"
            if (!askedQuestionHashes.contains(hash) || attempt == 5) {
                askedQuestionHashes.add(hash)
                return q
            }
        }

        return generateMathsQuestion(targetTopic, currentDifficulty, studentClass, board)
    }

    private fun extractClassNumber(studentClass: String): Int {
        val digits = studentClass.filter { it.isDigit() }
        return digits.toIntOrNull() ?: 6
    }

    // -------------------------------------------------------------
    // MATHEMATICS GENERATOR (Class 3 to Class 10)
    // -------------------------------------------------------------
    private fun generateMathsQuestion(
        topic: String?,
        difficulty: Difficulty,
        studentClass: String,
        board: String
    ): QuizQuestion {
        val classNum = extractClassNumber(studentClass)

        return when {
            classNum <= 4 -> generatePrimaryMaths(difficulty, classNum, board)
            classNum in 5..7 -> generateMiddleMaths(topic, difficulty, classNum, board)
            else -> generateSecondaryMaths(topic, difficulty, classNum, board)
        }
    }

    private fun generatePrimaryMaths(difficulty: Difficulty, classNum: Int, board: String): QuizQuestion {
        val type = Random.nextInt(4)
        return when (type) {
            0 -> {
                // Addition
                val max = when (difficulty) {
                    Difficulty.EASY -> 10
                    Difficulty.MEDIUM -> 30
                    Difficulty.HARD -> 70
                    Difficulty.ADVANCED -> 150
                }
                val a = Random.nextInt(2, max / 2 + 3)
                val b = Random.nextInt(2, max / 2 + 3)
                val sum = a + b
                val options = generateNumberOptions(sum)
                QuizQuestion(
                    id = "math_dyn_${System.currentTimeMillis()}_${Random.nextInt(1000)}",
                    subjectId = "maths",
                    topicName = "Addition",
                    difficulty = difficulty,
                    questionHi = "$a + $b = कितना होगा? 🧮",
                    questionEn = "What is $a + $b? 🧮",
                    visualHint = "🧠 $a में $b और जोड़ें!",
                    optionsHi = options,
                    optionsEn = options,
                    correctIndex = options.indexOf(sum.toString()),
                    explanationHi = "$a और $b को जोड़ने पर $sum आता है! ($a + $b = $sum)",
                    explanationEn = "Adding $a and $b gives $sum! ($a + $b = $sum)",
                    teacherAudioHi = "$a में $b मिलाने पर $sum बनता है!",
                    teacherAudioEn = "$a plus $b is equal to $sum!",
                    questionType = QuestionType.NUMERICAL,
                    board = board,
                    studentClass = "Class $classNum",
                    conceptSummary = "Basic Arithmetic Addition"
                )
            }
            1 -> {
                // Multiplication
                val table = when (difficulty) {
                    Difficulty.EASY -> Random.nextInt(2, 5)
                    Difficulty.MEDIUM -> Random.nextInt(5, 9)
                    Difficulty.HARD -> Random.nextInt(8, 13)
                    Difficulty.ADVANCED -> Random.nextInt(12, 19)
                }
                val multiplier = Random.nextInt(2, 10)
                val product = table * multiplier
                val options = generateNumberOptions(product)
                QuizQuestion(
                    id = "math_dyn_${System.currentTimeMillis()}_${Random.nextInt(1000)}",
                    subjectId = "maths",
                    topicName = "Multiplication",
                    difficulty = difficulty,
                    questionHi = "$table × $multiplier = कितना होता है? ✨",
                    questionEn = "What is $table × $multiplier? ✨",
                    visualHint = "$table का पहाड़ा $multiplier बार पढ़ें!",
                    optionsHi = options,
                    optionsEn = options,
                    correctIndex = options.indexOf(product.toString()),
                    explanationHi = "$table को $multiplier बार जोड़ने पर $product आता है! ($table × $multiplier = $product)",
                    explanationEn = "Multiplying $table by $multiplier gives $product! ($table × $multiplier = $product)",
                    teacherAudioHi = "$table गुना $multiplier होते हैं $product!",
                    teacherAudioEn = "$table multiplied by $multiplier equals $product!",
                    questionType = QuestionType.NUMERICAL,
                    board = board,
                    studentClass = "Class $classNum",
                    conceptSummary = "Multiplication Tables"
                )
            }
            2 -> {
                // Geometry / Shapes
                val shapes = listOf(
                    Triple("Triangle", "त्रिभुज", 3),
                    Triple("Quadrilateral", "चतुर्भुज", 4),
                    Triple("Pentagon", "पंचभुज", 5),
                    Triple("Hexagon", "षट्भुज", 6)
                )
                val picked = shapes.random()
                val options = listOf("3", "4", "5", "6")
                QuizQuestion(
                    id = "math_dyn_${System.currentTimeMillis()}_${Random.nextInt(1000)}",
                    subjectId = "maths",
                    topicName = "Shapes & Geometry",
                    difficulty = difficulty,
                    questionHi = "एक ${picked.second} में कितनी भुजाएं (sides) होती हैं? 📐",
                    questionEn = "How many sides does a ${picked.first} have? 📐",
                    visualHint = "📐 आकार के कोनों को गिनें!",
                    optionsHi = options,
                    optionsEn = options,
                    correctIndex = options.indexOf(picked.third.toString()),
                    explanationHi = "एक ${picked.second} में ठीक ${picked.third} सीधी भुजाएं होती हैं।",
                    explanationEn = "A ${picked.first} has exactly ${picked.third} sides.",
                    teacherAudioHi = "${picked.second} की ${picked.third} भुजाएँ होती हैं!",
                    teacherAudioEn = "A ${picked.first} has ${picked.third} sides!",
                    questionType = QuestionType.CONCEPTUAL,
                    board = board,
                    studentClass = "Class $classNum",
                    conceptSummary = "2D Shapes & Polygons"
                )
            }
            else -> {
                // True or False / Comparisons
                val num1 = Random.nextInt(10, 50)
                val num2 = Random.nextInt(10, 50)
                val isGreater = num1 > num2
                val questionHi = "क्या $num1 संख्या $num2 से बड़ी है? ⚖️"
                val questionEn = "Is $num1 greater than $num2? ⚖️"
                val options = listOf("True (हाँ)", "False (नहीं)")
                val correctIndex = if (isGreater) 0 else 1
                QuizQuestion(
                    id = "math_dyn_${System.currentTimeMillis()}_${Random.nextInt(1000)}",
                    subjectId = "maths",
                    topicName = "Number Comparison",
                    difficulty = difficulty,
                    questionHi = questionHi,
                    questionEn = questionEn,
                    visualHint = "दोनों संख्याओं के दहाई के अंक देखें!",
                    optionsHi = options,
                    optionsEn = listOf("True", "False"),
                    correctIndex = correctIndex,
                    explanationHi = if (isGreater) "$num1 संख्या $num2 से बड़ी है।" else "$num1 संख्या $num2 से बड़ी नहीं है।",
                    explanationEn = if (isGreater) "$num1 is indeed greater than $num2." else "$num1 is not greater than $num2.",
                    teacherAudioHi = "संख्याओं की तुलना सही तरीके से की!",
                    teacherAudioEn = "Great number comparison!",
                    questionType = QuestionType.TRUE_FALSE,
                    board = board,
                    studentClass = "Class $classNum",
                    conceptSummary = "Place Value & Comparison"
                )
            }
        }
    }

    private fun generateMiddleMaths(topic: String?, difficulty: Difficulty, classNum: Int, board: String): QuizQuestion {
        val subTopics = listOf("Fractions", "Decimals", "Integers", "Perimeter & Area", "Exponents")
        val chosenTopic = topic ?: subTopics.random()

        return when (chosenTopic) {
            "Fractions" -> {
                // Fractions
                val denom = when (difficulty) {
                    Difficulty.EASY -> listOf(2, 4, 8).random()
                    Difficulty.MEDIUM -> listOf(3, 6, 9).random()
                    Difficulty.HARD -> listOf(5, 7, 10).random()
                    Difficulty.ADVANCED -> listOf(11, 12, 15).random()
                }
                val num = Random.nextInt(1, denom)
                val questionHi = "यदि एक पिज्जा के $denom बराबर टुकड़े किए जाएँ और $num खा लिए जाएँ, तो कौन सा भिन्न (fraction) बना? 🍕"
                val questionEn = "If a pizza is divided into $denom equal slices and $num slices are eaten, what fraction was eaten? 🍕"
                val correctAns = "$num/$denom"
                val options = listOf(
                    "$num/$denom",
                    "${denom - num}/$denom",
                    "1/$denom",
                    "${num + 1}/$denom"
                ).distinct()
                val finalOptions = if (options.size == 4) options.shuffled() else listOf(correctAns, "1/$denom", "2/$denom", "3/$denom").shuffled()
                QuizQuestion(
                    id = "math_dyn_frac_${System.currentTimeMillis()}_${Random.nextInt(1000)}",
                    subjectId = "maths",
                    topicName = "Fractions",
                    difficulty = difficulty,
                    questionHi = questionHi,
                    questionEn = questionEn,
                    visualHint = "अंश (Numerator) = चुने हुए टुकड़े, हर (Denominator) = कुल टुकड़े!",
                    optionsHi = finalOptions,
                    optionsEn = finalOptions,
                    correctIndex = finalOptions.indexOf(correctAns),
                    explanationHi = "खाए गए टुकड़े = $num और कुल टुकड़े = $denom, अतः भिन्न = $num/$denom है।",
                    explanationEn = "Eaten slices = $num out of total $denom slices, so the fraction is $num/$denom.",
                    teacherAudioHi = "भिन्न की संकल्पना बहुत अच्छी समझी!",
                    teacherAudioEn = "Superb understanding of fractions!",
                    questionType = QuestionType.CONCEPTUAL,
                    board = board,
                    studentClass = "Class $classNum",
                    conceptSummary = "Fractions as Part of a Whole"
                )
            }
            "Integers" -> {
                // Negative and Positive Integers
                val a = Random.nextInt(-15, 10)
                val b = Random.nextInt(-10, 15)
                val res = a + b
                val options = generateNumberOptions(res)
                QuizQuestion(
                    id = "math_dyn_int_${System.currentTimeMillis()}_${Random.nextInt(1000)}",
                    subjectId = "maths",
                    topicName = "Integers",
                    difficulty = difficulty,
                    questionHi = "गणना करें: ($a) + ($b) = ? 🔢",
                    questionEn = "Evaluate: ($a) + ($b) = ? 🔢",
                    visualHint = "संख्या रेखा (Number Line) पर शून्य से दिशा का ध्यान रखें!",
                    optionsHi = options,
                    optionsEn = options,
                    correctIndex = options.indexOf(res.toString()),
                    explanationHi = "($a) और ($b) को जोड़ने पर परिणाम $res प्राप्त होता है।",
                    explanationEn = "Adding ($a) and ($b) equals $res.",
                    teacherAudioHi = "पूर्णांकों (Integers) का जोड़ बहुत सटीक किया!",
                    teacherAudioEn = "Accurate integer arithmetic!",
                    questionType = QuestionType.NUMERICAL,
                    board = board,
                    studentClass = "Class $classNum",
                    conceptSummary = "Operations on Directed Integers"
                )
            }
            "Perimeter & Area" -> {
                val length = Random.nextInt(4, 15)
                val width = Random.nextInt(3, 10)
                val isArea = Random.nextBoolean()
                val ans = if (isArea) length * width else 2 * (length + width)
                val promptHi = if (isArea) "लंबाई = $length m और चौड़ाई = $width m वाले आयत का क्षेत्रफल (Area) क्या है? 📏"
                else "लंबाई = $length m और चौड़ाई = $width m वाले आयत का परिमाप (Perimeter) क्या है? 📏"
                val promptEn = if (isArea) "What is the area of a rectangle with length = $length m and breadth = $width m? 📏"
                else "What is the perimeter of a rectangle with length = $length m and breadth = $width m? 📏"
                val options = generateNumberOptions(ans)
                QuizQuestion(
                    id = "math_dyn_geo_${System.currentTimeMillis()}_${Random.nextInt(1000)}",
                    subjectId = "maths",
                    topicName = "Perimeter & Area",
                    difficulty = difficulty,
                    questionHi = promptHi,
                    questionEn = promptEn,
                    visualHint = if (isArea) "Area = लंबाई × चौड़ाई" else "Perimeter = 2 × (लंबाई + चौड़ाई)",
                    optionsHi = options,
                    optionsEn = options,
                    correctIndex = options.indexOf(ans.toString()),
                    explanationHi = if (isArea) "क्षेत्रफल = लंबाई × चौड़ाई = $length × $width = $ans वर्ग मीटर।"
                    else "परिमाप = 2 × (लंबाई + चौड़ाई) = 2 × ($length + $width) = $ans मीटर।",
                    explanationEn = if (isArea) "Area = length × breadth = $length × $width = $ans sq m."
                    else "Perimeter = 2 × (length + breadth) = 2 × ($length + $width) = $ans m.",
                    teacherAudioHi = "क्षेत्रफल और परिमाप का सूत्र सही लगाया!",
                    teacherAudioEn = "Spot on application of mensuration formulas!",
                    questionType = QuestionType.NUMERICAL,
                    board = board,
                    studentClass = "Class $classNum",
                    conceptSummary = "Mensuration & Geometry"
                )
            }
            else -> {
                // Exponents / Powers
                val base = Random.nextInt(2, 6)
                val exp = when (difficulty) {
                    Difficulty.EASY -> 2
                    Difficulty.MEDIUM -> 3
                    Difficulty.HARD -> 4
                    Difficulty.ADVANCED -> 5
                }
                var prod = 1
                for (i in 1..exp) prod *= base
                val options = generateNumberOptions(prod)
                QuizQuestion(
                    id = "math_dyn_exp_${System.currentTimeMillis()}_${Random.nextInt(1000)}",
                    subjectId = "maths",
                    topicName = "Exponents & Powers",
                    difficulty = difficulty,
                    questionHi = "$base^$exp का मान क्या होगा? ⚡",
                    questionEn = "What is the value of $base^$exp? ⚡",
                    visualHint = "$base को $exp बार आपस में गुणा करें!",
                    optionsHi = options,
                    optionsEn = options,
                    correctIndex = options.indexOf(prod.toString()),
                    explanationHi = "$base^$exp का अर्थ है $base को $exp बार गुणा करना = $prod.",
                    explanationEn = "$base raised to power $exp means multiplying $base by itself $exp times = $prod.",
                    teacherAudioHi = "घातांक (Exponent) की गणना बिल्कुल सही!",
                    teacherAudioEn = "Great work on powers and exponents!",
                    questionType = QuestionType.NUMERICAL,
                    board = board,
                    studentClass = "Class $classNum",
                    conceptSummary = "Exponents and Power Evaluation"
                )
            }
        }
    }

    private fun generateSecondaryMaths(topic: String?, difficulty: Difficulty, classNum: Int, board: String): QuizQuestion {
        // Class 8, 9, 10: Linear Equations, Pythagoras, Polynomials, Ratios
        val xVal = Random.nextInt(2, 12)
        val coeff = Random.nextInt(2, 6)
        val constTerm = Random.nextInt(1, 15)
        val rhs = coeff * xVal + constTerm

        val options = generateNumberOptions(xVal)
        return QuizQuestion(
            id = "math_dyn_alg_${System.currentTimeMillis()}_${Random.nextInt(1000)}",
            subjectId = "maths",
            topicName = "Linear Equations",
            difficulty = difficulty,
            questionHi = "समीकरण को हल करें: $coeff x + $constTerm = $rhs. x का मान क्या है? 🎯",
            questionEn = "Solve the equation: $coeff x + $constTerm = $rhs. What is the value of x? 🎯",
            visualHint = "पहले दोनों तरफ से $constTerm घटाएं, फिर $coeff से भाग दें!",
            optionsHi = options,
            optionsEn = options,
            correctIndex = options.indexOf(xVal.toString()),
            explanationHi = "$coeff x = $rhs - $constTerm = ${rhs - constTerm}, इसलिए x = ${rhs - constTerm} ÷ $coeff = $xVal.",
            explanationEn = "$coeff x = $rhs - $constTerm = ${rhs - constTerm}, so x = ${rhs - constTerm} ÷ $coeff = $xVal.",
            teacherAudioHi = "समीकरण को बहुत सरलता से हल किया!",
            teacherAudioEn = "Algebraic equation solved with precision!",
            questionType = QuestionType.NUMERICAL,
            board = board,
            studentClass = "Class $classNum",
            conceptSummary = "Linear Equations in One Variable"
        )
    }

    // -------------------------------------------------------------
    // SCIENCE GENERATOR
    // -------------------------------------------------------------
    private fun generateScienceQuestion(
        topic: String?,
        difficulty: Difficulty,
        studentClass: String,
        board: String
    ): QuizQuestion {
        val classNum = extractClassNumber(studentClass)
        val questionsPool = listOf(
            QuizQuestion(
                id = "sci_dyn_1",
                subjectId = "science",
                topicName = "Photosynthesis",
                difficulty = difficulty,
                questionHi = "पौधे प्रकाश संश्लेषण (Photosynthesis) के दौरान कौन सी गैस छोड़ते हैं? 🌿",
                questionEn = "Which gas do green plants release during photosynthesis? 🌿",
                visualHint = "यह गैस सभी जीवों के सांस लेने के लिए आवश्यक है! 💨",
                optionsHi = listOf("ऑक्सीजन (Oxygen)", "कार्बन डाइऑक्साइड", "नाइट्रोजन", "हाइड्रोजन"),
                optionsEn = listOf("Oxygen", "Carbon Dioxide", "Nitrogen", "Hydrogen"),
                correctIndex = 0,
                explanationHi = "पौधे सूर्य के प्रकाश और कार्बन डाइऑक्साइड का उपयोग करके भोजन बनाते हैं और ऑक्सीजन गैस मुक्त करते हैं।",
                explanationEn = "During photosynthesis, plants take in carbon dioxide and water to produce glucose and release oxygen.",
                teacherAudioHi = "पेड़-पौधे हमें जीवनदायिनी ऑक्सीजन देते हैं!",
                teacherAudioEn = "Plants produce oxygen which sustains animal life!",
                questionType = QuestionType.MULTIPLE_CHOICE,
                board = board,
                studentClass = "Class $classNum",
                conceptSummary = "Plant Physiology and Energy Flow"
            ),
            QuizQuestion(
                id = "sci_dyn_2",
                subjectId = "science",
                topicName = "Light & Shadows",
                difficulty = difficulty,
                questionHi = "प्रकाश हमेशा किस प्रकार की रेखा में गमन करता है? 🔦",
                questionEn = "In what kind of path does light always travel? 🔦",
                visualHint = "सीधे लेज़र बीम की दिशा सोचें!",
                optionsHi = listOf("सरल रेखा (Straight Line)", "घुमावदार रेखा", "वृत्ताकार रेखा", "टेढ़ी-मेढ़ी रेखा"),
                optionsEn = listOf("Straight line", "Curved line", "Circular line", "Zigzag line"),
                correctIndex = 0,
                explanationHi = "प्रकाश हमेशा एक सीधी (सरल) रेखा में गमन करता है। इसे Rectilinear Propagation कहते हैं।",
                explanationEn = "Light always travels in straight lines; this property is called rectilinear propagation.",
                teacherAudioHi = "प्रकाश हमेशा सीधी रेखा में चलता है!",
                teacherAudioEn = "Light propagates in straight lines!",
                questionType = QuestionType.CONCEPTUAL,
                board = board,
                studentClass = "Class $classNum",
                conceptSummary = "Optics & Light Propagation"
            ),
            QuizQuestion(
                id = "sci_dyn_3",
                subjectId = "science",
                topicName = "States of Matter",
                difficulty = difficulty,
                questionHi = "जल का हिमांक (Freezing Point of Water) कितना होता है? ❄️",
                questionEn = "What is the freezing point of pure water at standard pressure? ❄️",
                visualHint = "जिस तापमान पर पानी बर्फ बन जाता है!",
                optionsHi = listOf("0°C", "100°C", "37°C", "-10°C"),
                optionsEn = listOf("0°C", "100°C", "37°C", "-10°C"),
                correctIndex = 0,
                explanationHi = "शुद्ध जल 0°C तापमान पर जम कर बर्फ बन जाता है।",
                explanationEn = "Pure water freezes into solid ice at 0°C (32°F).",
                teacherAudioHi = "पानी शून्य डिग्री सेल्सियस पर जमता है!",
                teacherAudioEn = "Water freezes at 0 degrees Celsius!",
                questionType = QuestionType.NUMERICAL,
                board = board,
                studentClass = "Class $classNum",
                conceptSummary = "Thermal Properties of Matter"
            ),
            QuizQuestion(
                id = "sci_dyn_4",
                subjectId = "science",
                topicName = "Human Body",
                difficulty = difficulty,
                questionHi = "मानव शरीर में रक्त को पंप करने वाला मुख्य अंग कौन सा है? ❤️",
                questionEn = "Which main organ in the human body pumps blood through circulation? ❤️",
                visualHint = "यह सीने के बाईं ओर धड़कता है!",
                optionsHi = listOf("हृदय (Heart)", "फेफड़े (Lungs)", "यकृत (Liver)", "गुर्दे (Kidneys)"),
                optionsEn = listOf("Heart", "Lungs", "Liver", "Kidneys"),
                correctIndex = 0,
                explanationHi = "हृदय एक पेशीय अंग है जो पूरे शरीर में धमनियों और शिराओं के माध्यम से रक्त पंप करता है।",
                explanationEn = "The heart is a muscular organ that pumps oxygenated blood throughout the circulatory system.",
                teacherAudioHi = "हृदय पूरे शरीर में रक्त संचार करता है!",
                teacherAudioEn = "The heart powers our circulatory system!",
                questionType = QuestionType.MULTIPLE_CHOICE,
                board = board,
                studentClass = "Class $classNum",
                conceptSummary = "Human Circulatory System"
            )
        )

        return questionsPool.random()
    }

    // -------------------------------------------------------------
    // ENGLISH GENERATOR
    // -------------------------------------------------------------
    private fun generateEnglishQuestion(
        topic: String?,
        difficulty: Difficulty,
        studentClass: String,
        board: String
    ): QuizQuestion {
        val classNum = extractClassNumber(studentClass)
        val questionsPool = listOf(
            QuizQuestion(
                id = "eng_dyn_1",
                subjectId = "english",
                topicName = "Parts of Speech",
                difficulty = difficulty,
                questionHi = "वाक्य में संज्ञा (Noun) पहचानें: 'The brave tiger ran fast.' 🐯",
                questionEn = "Identify the noun in the sentence: 'The brave tiger ran fast.' 🐯",
                visualHint = "किसी व्यक्ति, वस्तु, स्थान या जानवर का नाम!",
                optionsHi = listOf("tiger", "brave", "ran", "fast"),
                optionsEn = listOf("tiger", "brave", "ran", "fast"),
                correctIndex = 0,
                explanationHi = "'Tiger' एक जानवर का नाम है, इसलिए यह Noun (संज्ञा) है। 'Brave' adjective है और 'ran' verb है।",
                explanationEn = "'Tiger' is the name of an animal, making it a noun. 'Brave' is an adjective and 'ran' is a verb.",
                teacherAudioHi = "टाइगर एक संज्ञा (Noun) है!",
                teacherAudioEn = "Tiger is a noun naming an animal!",
                questionType = QuestionType.CONCEPTUAL,
                board = board,
                studentClass = "Class $classNum",
                conceptSummary = "Nouns and Identification"
            ),
            QuizQuestion(
                id = "eng_dyn_2",
                subjectId = "english",
                topicName = "Synonyms & Antonyms",
                difficulty = difficulty,
                questionHi = "शब्द 'Ancient' का विलोम शब्द (Antonym) क्या है? 🏛️",
                questionEn = "What is the antonym (opposite) of the word 'Ancient'? 🏛️",
                visualHint = "Ancient मतलब बहुत पुराना, इसका उल्टा क्या होगा?",
                optionsHi = listOf("Modern (आधुनिक)", "Old (पुराना)", "Historic (ऐतिहासिक)", "Past (अतीत)"),
                optionsEn = listOf("Modern", "Old", "Historic", "Past"),
                correctIndex = 0,
                explanationHi = "'Ancient' का अर्थ बहुत पुराना होता है, इसका विलोम 'Modern' (नया/आधुनिक) है।",
                explanationEn = "'Ancient' means belonging to the distant past. Its opposite is 'Modern'.",
                teacherAudioHi = "एंशियंट का विपरीत शब्द मॉडर्न होता है!",
                teacherAudioEn = "The opposite of ancient is modern!",
                questionType = QuestionType.MULTIPLE_CHOICE,
                board = board,
                studentClass = "Class $classNum",
                conceptSummary = "Vocabulary Building"
            ),
            QuizQuestion(
                id = "eng_dyn_3",
                subjectId = "english",
                topicName = "Tenses",
                difficulty = difficulty,
                questionHi = "खाली जगह भरें: 'She _____ to school every day.' 🏫",
                questionEn = "Fill in the blank: 'She _____ to school every day.' 🏫",
                visualHint = "Third person singular (She) के साथ Simple Present Tense का प्रयोग!",
                optionsHi = listOf("goes", "go", "going", "gone"),
                optionsEn = listOf("goes", "go", "going", "gone"),
                correctIndex = 0,
                explanationHi = "दैनिक आदत बताने के लिए Simple Present Tense लगता है। 'She' के साथ क्रिया में 'es' जुड़ेगा (goes).",
                explanationEn = "For third-person singular subjects (he/she/it) in simple present tense, we add -s/-es to the base verb (goes).",
                teacherAudioHi = "शी गोज़ टू स्कूल एवरी डे - सही व्याकरण!",
                teacherAudioEn = "Correct simple present tense agreement!",
                questionType = QuestionType.FILL_IN_BLANK,
                board = board,
                studentClass = "Class $classNum",
                conceptSummary = "Subject-Verb Agreement"
            )
        )

        return questionsPool.random()
    }

    // -------------------------------------------------------------
    // HINDI GENERATOR
    // -------------------------------------------------------------
    private fun generateHindiQuestion(
        topic: String?,
        difficulty: Difficulty,
        studentClass: String,
        board: String
    ): QuizQuestion {
        val classNum = extractClassNumber(studentClass)
        val questionsPool = listOf(
            QuizQuestion(
                id = "hin_dyn_1",
                subjectId = "hindi",
                topicName = "विलोम शब्द",
                difficulty = difficulty,
                questionHi = "'अमृत' शब्द का सही विलोम शब्द क्या होगा? 🏺",
                questionEn = "What is the antonym (opposite) of the Hindi word 'Amrit' (अमृत)? 🏺",
                visualHint = "अमृत जीवन देता है, इसका उल्टा क्या है?",
                optionsHi = listOf("विष", "जल", "दूध", "शहद"),
                optionsEn = listOf("Vish (विष)", "Jal (जल)", "Doodh (दूध)", "Shahad (शहद)"),
                correctIndex = 0,
                explanationHi = "अमृत का विलोम शब्द 'विष' (जहर) होता है।",
                explanationEn = "The antonym of 'Amrit' (nectar of life) is 'Vish' (poison).",
                teacherAudioHi = "अमृत का विलोम शब्द विष है! बहुत खूब!",
                teacherAudioEn = "Amrit and Vish are exact antonyms!",
                questionType = QuestionType.MULTIPLE_CHOICE,
                board = board,
                studentClass = "Class $classNum",
                conceptSummary = "हिंदी व्याकरण - विलोम शब्द"
            ),
            QuizQuestion(
                id = "hin_dyn_2",
                subjectId = "hindi",
                topicName = "पर्यायवाची शब्द",
                difficulty = difficulty,
                questionHi = "'सूर्य' का पर्यायवाची शब्द इनमें से कौन सा है? ☀️",
                questionEn = "Which of the following is a synonym for 'Surya' (सूर्य - Sun)? ☀️",
                visualHint = "दिन में चमकने वाला रवि या दिनकर!",
                optionsHi = listOf("दिनकर", "चंद्रमा", "पवन", "वारिद"),
                optionsEn = listOf("Dinkar (दिनकर)", "Chandrama", "Pawan", "Vaarid"),
                correctIndex = 0,
                explanationHi = "सूर्य के मुख्य पर्यायवाची दिनकर, रवि, भानु और भास्कर हैं।",
                explanationEn = "'Dinkar' is a widely used synonym for Surya (Sun) in Hindi.",
                teacherAudioHi = "दिनकर सूर्य का ही दूसरा नाम है!",
                teacherAudioEn = "Dinkar is indeed the synonym for Sun!",
                questionType = QuestionType.MULTIPLE_CHOICE,
                board = board,
                studentClass = "Class $classNum",
                conceptSummary = "हिंदी व्याकरण - पर्यायवाची शब्द"
            )
        )

        return questionsPool.random()
    }

    // -------------------------------------------------------------
    // COMPUTER GENERATOR
    // -------------------------------------------------------------
    private fun generateComputerQuestion(
        topic: String?,
        difficulty: Difficulty,
        studentClass: String,
        board: String
    ): QuizQuestion {
        val classNum = extractClassNumber(studentClass)
        val questionsPool = listOf(
            QuizQuestion(
                id = "comp_dyn_1",
                subjectId = "computer",
                topicName = "Hardware & Brain",
                difficulty = difficulty,
                questionHi = "कंप्यूटर का मस्तिष्क (Brain of Computer) किसे कहा जाता है? 💻",
                questionEn = "What is known as the 'Brain' of a computer? 💻",
                visualHint = "यह सभी गणनाएं और निर्देश प्रोसेस करता है!",
                optionsHi = listOf("CPU (सी.पी.यू.)", "Monitor", "Keyboard", "Mouse"),
                optionsEn = listOf("CPU (Central Processing Unit)", "Monitor", "Keyboard", "Mouse"),
                correctIndex = 0,
                explanationHi = "CPU (Central Processing Unit) कंप्यूटर के सभी कार्यों और गणनाओं को नियंत्रित करता है।",
                explanationEn = "The Central Processing Unit (CPU) executes instructions and performs arithmetic calculations.",
                teacherAudioHi = "सी.पी.यू. ही कंप्यूटर का असली दिमाग है!",
                teacherAudioEn = "The CPU handles all computational processing!",
                questionType = QuestionType.CONCEPTUAL,
                board = board,
                studentClass = "Class $classNum",
                conceptSummary = "Computer Architecture"
            ),
            QuizQuestion(
                id = "comp_dyn_2",
                subjectId = "computer",
                topicName = "Input Devices",
                difficulty = difficulty,
                questionHi = "इनमें से कौन सा एक इनपुट डिवाइस (Input Device) है? ⌨️",
                questionEn = "Which of the following is an input device? ⌨️",
                visualHint = "जिससे आप कंप्यूटर में डाटा या निर्देश डालते हैं!",
                optionsHi = listOf("Keyboard", "Monitor", "Speaker", "Printer"),
                optionsEn = listOf("Keyboard", "Monitor", "Speaker", "Printer"),
                correctIndex = 0,
                explanationHi = "Keyboard एक इनपुट डिवाइस है जिसकी मदद से हम कंप्यूटर में अक्षर और संख्याएं टाइप करते हैं।",
                explanationEn = "A keyboard sends data and keystroke commands into the computer as input.",
                teacherAudioHi = "कीबोर्ड से हम कंप्यूटर में टाइप करते हैं!",
                teacherAudioEn = "Keyboards feed alphanumeric input to computers!",
                questionType = QuestionType.MULTIPLE_CHOICE,
                board = board,
                studentClass = "Class $classNum",
                conceptSummary = "Input and Output Peripherals"
            )
        )

        return questionsPool.random()
    }

    // -------------------------------------------------------------
    // GENERAL KNOWLEDGE GENERATOR
    // -------------------------------------------------------------
    private fun generateGkQuestion(
        topic: String?,
        difficulty: Difficulty,
        studentClass: String,
        board: String
    ): QuizQuestion {
        val classNum = extractClassNumber(studentClass)
        val questionsPool = listOf(
            QuizQuestion(
                id = "gk_dyn_1",
                subjectId = "gk",
                topicName = "Solar System",
                difficulty = difficulty,
                questionHi = "हमारे सौरमंडल का सबसे बड़ा ग्रह कौन सा है? 🪐",
                questionEn = "Which is the largest planet in our solar system? 🪐",
                visualHint = "यह एक विशाल गैसीय ग्रह है जिसका नाम बृहस्पति है!",
                optionsHi = listOf("बृहस्पति (Jupiter)", "शनि (Saturn)", "पृथ्वी (Earth)", "मंगल (Mars)"),
                optionsEn = listOf("Jupiter", "Saturn", "Earth", "Mars"),
                correctIndex = 0,
                explanationHi = "बृहस्पति (Jupiter) सौरमंडल का सबसे बड़ा ग्रह है। इसका द्रव्यमान अन्य सभी ग्रहों के कुल द्रव्यमान से ढाई गुना अधिक है।",
                explanationEn = "Jupiter is the largest planet in the solar system, with a mass greater than all other planets combined.",
                teacherAudioHi = "बृहस्पति हमारे सौरमंडल का सबसे विशाल ग्रह है!",
                teacherAudioEn = "Jupiter is by far the largest planet in our solar system!",
                questionType = QuestionType.MULTIPLE_CHOICE,
                board = board,
                studentClass = "Class $classNum",
                conceptSummary = "Astronomy and Planetary Science"
            ),
            QuizQuestion(
                id = "gk_dyn_2",
                subjectId = "gk",
                topicName = "Indian Heritage",
                difficulty = difficulty,
                questionHi = "भारत का राष्ट्रीय जलीय जीव (National Aquatic Animal) कौन सा है? 🐬",
                questionEn = "What is the National Aquatic Animal of India? 🐬",
                visualHint = "यह पवित्र गंगा नदी में पाई जाने वाली डॉल्फिन है!",
                optionsHi = listOf("गंगा डॉल्फिन (Ganges River Dolphin)", "नीली व्हेल (Blue Whale)", "मगरमच्छ (Crocodile)", "कछुआ (Sea Turtle)"),
                optionsEn = listOf("Ganges River Dolphin", "Blue Whale", "Crocodile", "Sea Turtle"),
                correctIndex = 0,
                explanationHi = "गंगा नदी में पाई जाने वाली डॉल्फिन (Gangetic Dolphin) को भारत का राष्ट्रीय जलीय जीव घोषित किया गया है।",
                explanationEn = "The Ganges River Dolphin represents the purity of the holy river and is India's national aquatic animal.",
                teacherAudioHi = "गंगा डॉल्फिन भारत का राष्ट्रीय जलीय जीव है!",
                teacherAudioEn = "The Ganges River Dolphin is India's national aquatic animal!",
                questionType = QuestionType.MULTIPLE_CHOICE,
                board = board,
                studentClass = "Class $classNum",
                conceptSummary = "National Symbols of India"
            )
        )

        return questionsPool.random()
    }

    private fun generateNumberOptions(correct: Int): List<String> {
        val offsets = listOf(-2, -1, 1, 2, 3, -3, 4, -4).shuffled()
        val set = mutableSetOf(correct.toString())
        for (off in offsets) {
            val candidate = correct + off
            if (candidate >= 0) set.add(candidate.toString())
            if (set.size == 4) break
        }
        while (set.size < 4) {
            set.add((correct + set.size + 1).toString())
        }
        return set.toList().shuffled()
    }
}
