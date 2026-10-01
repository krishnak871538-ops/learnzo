package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.Lesson
import com.example.model.Subject
import com.example.ui.theme.LearnzoAmber
import com.example.ui.theme.LearnzoGold
import com.example.ui.theme.LearnzoIndigo
import com.example.ui.theme.LearnzoMint

enum class CheckInReason {
    PROGRESS_CHECK,  // Routine friendly checkpoint
    INACTIVITY_BOREDOM, // Child idle or taking too long
    STRUGGLE_REMEDY  // Need simpler explanation
}

data class CheckInQuestion(
    val promptHi: String,
    val promptEn: String,
    val optionsHi: List<String>,
    val optionsEn: List<String>,
    val correctIndex: Int,
    val simpleExplanationHi: String,
    val simpleExplanationEn: String
)

@Composable
fun TeacherCheckInDialog(
    subject: Subject,
    currentLesson: Lesson,
    reason: CheckInReason = CheckInReason.PROGRESS_CHECK,
    isHindi: Boolean,
    onAnswerEvaluated: (isCorrect: Boolean, consecutiveWrong: Int) -> Unit,
    onDismiss: () -> Unit,
    onSpeak: (String) -> Unit
) {
    // Generate contextually relevant question for the current subject & lesson
    val question = remember(subject.id, currentLesson.id) {
        getCheckInQuestionForLesson(subject.id, currentLesson.topicName)
    }

    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var consecutiveWrongCount by remember { mutableIntStateOf(0) }
    var showExplanationStep by remember { mutableStateOf(false) }
    var wantsEasierExplanation by remember { mutableStateOf(false) }

    // Alternate between Shruti Mam (Female) and Prof. Raman (Male)
    val isFemaleTeacher = remember { (System.currentTimeMillis() % 2) == 0L }
    val teacherName = if (isFemaleTeacher) "Shruti Mam 👩‍🏫" else "Prof. Raman 👨‍🏫"

    // Construct encouraging teacher voice greeting
    val teacherGreeting = when (reason) {
        CheckInReason.INACTIVITY_BOREDOM -> {
            if (isHindi)
                "लगता है थोड़ा बोर हो रहे हो 😊 चलो एक मज़ेदार सवाल करते हैं!"
            else
                "Looks like you might need a little fun break 😊 Let's do a quick exciting question!"
        }
        CheckInReason.STRUGGLE_REMEDY -> {
            if (isHindi)
                "चलो इसे एक आसान तरीके से समझते हैं!"
            else
                "Let's explore this in an easier, super fun way!"
        }
        CheckInReason.PROGRESS_CHECK -> {
            if (isHindi)
                "समझ आया? 😊 क्या तुम्हें समझ में आया?"
            else
                "Did you understand? 😊 Let's do a quick fun check!"
        }
    }

    val currentPrompt = if (isHindi) question.promptHi else question.promptEn
    val fullSpeech = "$teacherGreeting $currentPrompt"

    LaunchedEffect(Unit) {
        onSpeak(fullSpeech)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(26.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color(subject.primaryColorHex).copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("teacher_check_in_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Header Row with Teacher Avatar & Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color(subject.primaryColorHex).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = if (isFemaleTeacher) "👩‍🏫" else "👨‍🏫", fontSize = 24.sp)
                        }
                        Column {
                            Text(
                                text = teacherName,
                                fontWeight = FontWeight.Bold,
                                color = LearnzoIndigo,
                                fontSize = 14.sp
                            )
                            Text(
                                text = if (isHindi) "टीचर चेक-इन • ${currentLesson.topicName}" else "Teacher Check-In • ${currentLesson.topicName}",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = { onSpeak(fullSpeech) },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Listen",
                                tint = Color(subject.primaryColorHex)
                            )
                        }
                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.Gray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Teacher Speech Balloon
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color(0xFFF7F8FE))
                        .border(1.dp, Color(subject.primaryColorHex).copy(alpha = 0.2f), RoundedCornerShape(18.dp))
                        .padding(14.dp)
                ) {
                    Text(
                        text = "\"$teacherGreeting\"",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF333344),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Check-in Question Prompt
                Text(
                    text = currentPrompt,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = LearnzoIndigo,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Options List
                val options = if (isHindi) question.optionsHi else question.optionsEn
                options.forEachIndexed { index, optionText ->
                    val isSelected = selectedIndex == index
                    val isCorrectOption = index == question.correctIndex

                    val cardBg = when {
                        selectedIndex == null -> Color.White
                        isSelected && isCorrectOption -> Color(0xFFE8F5E9)
                        isSelected && !isCorrectOption -> Color(0xFFFFF3E0)
                        else -> Color.White
                    }

                    val borderColor = when {
                        selectedIndex == null -> Color(0xFFE0E0E0)
                        isSelected && isCorrectOption -> LearnzoMint
                        isSelected && !isCorrectOption -> LearnzoAmber
                        else -> Color(0xFFEEEEEE)
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable(enabled = selectedIndex == null) {
                                selectedIndex = index
                                val isCorrect = index == question.correctIndex
                                if (isCorrect) {
                                    consecutiveWrongCount = 0
                                    val praise = if (isHindi)
                                        "वाह! अब तुम समझ रहे हो! 🎉 बहुत अच्छा प्रयास!"
                                    else
                                        "Awesome! You totally got it! 🎉 Great effort!"
                                    onSpeak(praise)
                                } else {
                                    consecutiveWrongCount++
                                    val encourage = if (isHindi)
                                        "कोई बात नहीं 😊 फिर से कोशिश करते हैं! चलो इसे एक आसान तरीके से समझते हैं!"
                                    else
                                        "No worries at all 😊 Let's try again! Let's understand this with a simple trick!"
                                    onSpeak(encourage)
                                }
                                showExplanationStep = true
                                onAnswerEvaluated(isCorrect, consecutiveWrongCount)
                            },
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = androidx.compose.foundation.BorderStroke(2.dp, borderColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = Color.Black
                            )
                            if (selectedIndex != null && isSelected) {
                                Text(
                                    text = if (isCorrectOption) "✅" else "💡",
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }
                }

                // Friendly Explanation & Easy Way Prompt
                if (showExplanationStep) {
                    Spacer(modifier = Modifier.height(12.dp))
                    val isCorrect = selectedIndex == question.correctIndex

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isCorrect) Color(0xFFE8F5E9) else Color(0xFFFFF8E1))
                            .padding(12.dp)
                    ) {
                        Column {
                            Text(
                                text = if (isCorrect) {
                                    if (isHindi) "वाह! अब तुम समझ रहे हो! 🎉" else "Awesome! You are getting it! 🎉"
                                } else {
                                    if (isHindi) "कोई बात नहीं 😊 बहुत अच्छा प्रयास!" else "No worries 😊 Great attempt!"
                                },
                                fontWeight = FontWeight.Bold,
                                color = if (isCorrect) Color(0xFF1B5E20) else Color(0xFFE65100),
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (isHindi) question.simpleExplanationHi else question.simpleExplanationEn,
                                fontSize = 12.sp,
                                color = Color.DarkGray
                            )
                        }
                    }

                    // Occasional "Do you want me to explain this in an easier way?"
                    if (!isCorrect && !wantsEasierExplanation) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                wantsEasierExplanation = true
                                val easierPrompt = if (isHindi)
                                    "चलो इसे ऐसे सोचते हैं: जब हम किसी चीज़ को गिनते हैं, तो एक-एक करके आगे बढ़ते हैं। अब बिल्कुल साफ है!"
                                else
                                    "Let's think of it like this: When we count, we step forward one by one. Now it's super easy!"
                                onSpeak(easierPrompt)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = LearnzoAmber),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isHindi) "क्या मैं इसे एक और आसान तरीके से समझाऊँ? 💡" else "Do you want me to explain this in an easier way? 💡",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = LearnzoIndigo),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("continue_lesson_after_checkin_btn")
                    ) {
                        Text(
                            text = if (isHindi) "चलो पाठ आगे बढ़ाते हैं! 🚀" else "Continue Lesson! 🚀",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

private fun getCheckInQuestionForLesson(subjectId: String, topicName: String): CheckInQuestion {
    return when (subjectId.lowercase()) {
        "maths" -> CheckInQuestion(
            promptHi = "हमने अभी 5 + 3 सीखा है। बताओ, 5 + 3 कितना होगा? 🧮",
            promptEn = "We just learned 5 + 3. Tell me, how much is 5 + 3? 🧮",
            optionsHi = listOf("7", "8", "9"),
            optionsEn = listOf("7", "8", "9"),
            correctIndex = 1,
            simpleExplanationHi = "5 के बाद 3 कदम आगे गिनें: 6, 7, 8! इसलिए 5 + 3 = 8.",
            simpleExplanationEn = "Count 3 steps after 5: 6, 7, 8! That's why 5 + 3 = 8."
        )
        "english" -> CheckInQuestion(
            promptHi = "हमने अभी 'Apple' सीखा। Apple का मतलब क्या है? 🍎",
            promptEn = "We just learned about 'Apple'. What does Apple mean? 🍎",
            optionsHi = listOf("सेब 🍎", "केला 🍌", "संतरा 🍊"),
            optionsEn = listOf("Apple (Red Sweet Fruit) 🍎", "Banana 🍌", "Orange 🍊"),
            correctIndex = 0,
            simpleExplanationHi = "Apple एक मीठा लाल फल होता है जिसे हिंदी में सेब कहते हैं!",
            simpleExplanationEn = "An Apple is a delicious, nutritious red fruit!"
        )
        "science" -> CheckInQuestion(
            promptHi = "हमने अभी पौधों के बारे में पढ़ा। पौधे अपना भोजन किस प्रक्रिया से बनाते हैं? 🌱",
            promptEn = "We just studied plants. By what process do plants make their food? 🌱",
            optionsHi = listOf("प्रकाश संश्लेषण (Photosynthesis)", "श्वसन (Breathing)", "दौड़ना (Running)"),
            optionsEn = listOf("Photosynthesis 🌱☀️", "Respiration", "Running"),
            correctIndex = 0,
            simpleExplanationHi = "हरे पौधे धूप, पानी और हवा से अपना भोजन 'प्रकाश संश्लेषण' द्वारा बनाते हैं!",
            simpleExplanationEn = "Green plants use sunlight, water, and air in Photosynthesis to make food!"
        )
        "gk" -> CheckInQuestion(
            promptHi = "हमने अभी भारत की राजधानी के बारे में पढ़ा। भारत की राजधानी क्या है? 🇮🇳",
            promptEn = "We just learned about India's capital. What is the capital of India? 🇮🇳",
            optionsHi = listOf("नई दिल्ली (New Delhi)", "मुंबई (Mumbai)", "कोलकाता (Kolkata)"),
            optionsEn = listOf("New Delhi 🇮🇳", "Mumbai", "Kolkata"),
            correctIndex = 0,
            simpleExplanationHi = "भारत की राजधानी नई दिल्ली है, जहाँ संसद भवन और राष्ट्रपति भवन स्थित हैं!",
            simpleExplanationEn = "New Delhi is the national capital of India!"
        )
        "computer" -> CheckInQuestion(
            promptHi = "हमने अभी कंप्यूटर के बारे में सीखा। कंप्यूटर का दिमाग (Brain) किसे कहते हैं? 💻",
            promptEn = "We just learned about computers. What is called the Brain of the Computer? 💻",
            optionsHi = listOf("CPU", "Mouse", "Keyboard"),
            optionsEn = listOf("CPU 🧠", "Mouse", "Keyboard"),
            correctIndex = 0,
            simpleExplanationHi = "CPU को Central Processing Unit कहते हैं, यही कंप्यूटर के सारे काम सोचता और करता है!",
            simpleExplanationEn = "CPU is the Central Processing Unit that processes everything!"
        )
        "hindi" -> CheckInQuestion(
            promptHi = "हमने अभी वर्णमाला सीखी। 'क' से क्या बनता है? 🕊️",
            promptEn = "We just learned Hindi Varnamala. What word starts with 'क' (Ka)? 🕊️",
            optionsHi = listOf("कबूतर 🕊️", "खरगोश 🐇", "गमला 🪴"),
            optionsEn = listOf("कबूतर (Kabutar / Pigeon) 🕊️", "खरगोश (Rabbit)", "गमला (Pot)"),
            correctIndex = 0,
            simpleExplanationHi = "'क' से कबूतर दाना चुगता है! बहुत सुंदर पहचान!",
            simpleExplanationEn = "'क' stands for Kabutar (Pigeon)!"
        )
        else -> CheckInQuestion(
            promptHi = "क्या तुम्हें यह पाठ समझ में आया? 😊",
            promptEn = "Did you understand this lesson? 😊",
            optionsHi = listOf("हाँ, बहुत अच्छे से! 🌟", "थोड़ा-थोड़ा समझ आया 👍", "फिर से बताइए 💡"),
            optionsEn = listOf("Yes, understood completely! 🌟", "Understood a bit 👍", "Explain once more 💡"),
            correctIndex = 0,
            simpleExplanationHi = "बहुत अच्छा! जब भी कोई संदेह हो, अपने टीचर से दोबारा पूछ सकते हैं!",
            simpleExplanationEn = "Great job! Whenever in doubt, ask your friendly teacher!"
        )
    }
}
