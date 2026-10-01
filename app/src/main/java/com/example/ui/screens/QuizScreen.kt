package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.example.model.Difficulty
import com.example.model.DynamicQuestionGenerator
import com.example.model.QuestionType
import com.example.model.QuizQuestion
import com.example.model.Subject
import com.example.ui.theme.LearnzoAmber
import com.example.ui.theme.LearnzoGold
import com.example.ui.theme.LearnzoIndigo
import com.example.ui.theme.LearnzoMint
import com.example.util.AppStrings

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuizScreen(
    questions: List<QuizQuestion>,
    subject: Subject,
    isHindi: Boolean,
    consecutiveCorrect: Int,
    board: String = "CBSE",
    studentClass: String = "Class 6",
    onBack: () -> Unit,
    onAnswerSubmitted: (QuizQuestion, Int, Boolean) -> Unit,
    onSpeakText: (String) -> Unit
) {
    BackHandler { onBack() }

    // Live Unlimited Practice Tracking
    var questionsAttempted by remember { mutableIntStateOf(1) }
    var sessionCorrectCount by remember { mutableIntStateOf(0) }
    var consecutiveStreak by remember { mutableIntStateOf(0) }
    var currentDifficulty by remember { mutableStateOf(Difficulty.EASY) }

    var selectedOptionIndex by remember { mutableIntStateOf(-1) }
    var hasAnswered by remember { mutableStateOf(false) }
    var showDetailedExplanation by remember { mutableStateOf(false) }

    // Weak & Mastered topics in current session
    var weakTopics by remember { mutableStateOf(setOf<String>()) }
    var masteredTopics by remember { mutableStateOf(setOf<String>()) }
    var topicSuccessMap by remember { mutableStateOf(mapOf<String, Int>()) }

    // Dynamic question queue
    var currentQuestion by remember {
        mutableStateOf(
            questions.firstOrNull() ?: DynamicQuestionGenerator.generateQuestion(
                subjectId = subject.id,
                topicName = null,
                currentDifficulty = Difficulty.EASY,
                board = board,
                studentClass = studentClass
            )
        )
    }

    LaunchedEffect(questionsAttempted) {
        selectedOptionIndex = -1
        hasAnswered = false
        showDetailedExplanation = false
        val qText = if (isHindi) currentQuestion.questionHi else currentQuestion.questionEn
        onSpeakText(qText)
    }

    val liveAccuracy = remember(questionsAttempted, sessionCorrectCount) {
        if (questionsAttempted > 1) {
            val answeredSoFar = questionsAttempted - 1
            ((sessionCorrectCount.toFloat() / answeredSoFar.toFloat()) * 100).toInt()
        } else {
            100
        }
    }

    val difficultyColor = when (currentDifficulty) {
        Difficulty.EASY -> Color(0xFF2E7D32)
        Difficulty.MEDIUM -> Color(0xFF1565C0)
        Difficulty.HARD -> Color(0xFFE65100)
        Difficulty.ADVANCED -> Color(0xFF6A1B9A)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("quiz_screen_root")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .testTag("quiz_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = LearnzoIndigo
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${subject.iconEmoji} " + (if (isHindi) subject.nameHi else subject.nameEn),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(subject.primaryColorHex)
                )
                Text(
                    text = "Question #$questionsAttempted • Unlimited Practice ♾️",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Audio Replay Button
            IconButton(
                onClick = {
                    val qText = if (isHindi) currentQuestion.questionHi else currentQuestion.questionEn
                    onSpeakText(qText)
                },
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .testTag("quiz_replay_speech_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = "Read Aloud",
                    tint = LearnzoIndigo
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Live Progress & Adaptive Difficulty Dashboard
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Accuracy badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFE8F5E9))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "🎯 $liveAccuracy% Accuracy",
                            color = Color(0xFF2E7D32),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Difficulty badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(difficultyColor.copy(alpha = 0.12f))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "Level: ${currentDifficulty.name}",
                            color = difficultyColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Correct score badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFFFF8E1))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "⭐ $sessionCorrectCount Correct",
                            color = Color(0xFFF57F17),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Active Topic Focus & Badges
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Topic: ${currentQuestion.topicName}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LearnzoIndigo
                    )

                    Text(
                        text = "$board • $studentClass",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Question Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE0E0FF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Visual Hint
                if (currentQuestion.visualHint.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFFF8E1))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = currentQuestion.visualHint,
                            fontSize = 13.sp,
                            color = Color(0xFFF57F17),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Question Text
                Text(
                    text = if (isHindi) currentQuestion.questionHi else currentQuestion.questionEn,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = LearnzoIndigo,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Options List
        val options = if (isHindi) currentQuestion.optionsHi else currentQuestion.optionsEn
        options.forEachIndexed { index, optionText ->
            val isSelected = selectedOptionIndex == index
            val isCorrect = index == currentQuestion.correctIndex

            val cardBg = when {
                hasAnswered && isCorrect -> Color(0xFFE8F5E9)
                hasAnswered && isSelected && !isCorrect -> Color(0xFFFFEBEE)
                isSelected -> Color(0xFFEDE7F6)
                else -> Color.White
            }

            val borderColor = when {
                hasAnswered && isCorrect -> LearnzoMint
                hasAnswered && isSelected && !isCorrect -> Color(0xFFFF5252)
                isSelected -> LearnzoIndigo
                else -> Color(0xFFE0E0E0)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
                    .clickable(enabled = !hasAnswered) {
                        selectedOptionIndex = index
                        hasAnswered = true
                        val correct = (index == currentQuestion.correctIndex)

                        // Adaptive Difficulty Progression logic
                        if (correct) {
                            sessionCorrectCount += 1
                            consecutiveStreak += 1
                            // Promote difficulty after 2 consecutive correct answers
                            if (consecutiveStreak >= 2) {
                                currentDifficulty = when (currentDifficulty) {
                                    Difficulty.EASY -> Difficulty.MEDIUM
                                    Difficulty.MEDIUM -> Difficulty.HARD
                                    Difficulty.HARD -> Difficulty.ADVANCED
                                    Difficulty.ADVANCED -> Difficulty.ADVANCED
                                }
                                consecutiveStreak = 0
                            }
                            // Update topic mastery
                            val count = (topicSuccessMap[currentQuestion.topicName] ?: 0) + 1
                            topicSuccessMap = topicSuccessMap + (currentQuestion.topicName to count)
                            if (count >= 2) {
                                masteredTopics = masteredTopics + currentQuestion.topicName
                                weakTopics = weakTopics - currentQuestion.topicName
                            }
                        } else {
                            consecutiveStreak = 0
                            // Gracefully step down difficulty upon mistakes
                            currentDifficulty = when (currentDifficulty) {
                                Difficulty.ADVANCED -> Difficulty.HARD
                                Difficulty.HARD -> Difficulty.MEDIUM
                                Difficulty.MEDIUM -> Difficulty.EASY
                                Difficulty.EASY -> Difficulty.EASY
                            }
                            // Record weak topic for reinforcement
                            weakTopics = weakTopics + currentQuestion.topicName
                        }

                        onAnswerSubmitted(currentQuestion, index, correct)

                        if (correct) {
                            val praise = if (isHindi) currentQuestion.teacherAudioHi else currentQuestion.teacherAudioEn
                            onSpeakText(praise)
                        } else {
                            val help = if (isHindi)
                                "सही जवाब है ${options.getOrNull(currentQuestion.correctIndex) ?: ""}. " + currentQuestion.explanationHi
                            else
                                "Correct answer is ${options.getOrNull(currentQuestion.correctIndex) ?: ""}. " + currentQuestion.explanationEn
                            onSpeakText(help)
                        }
                    }
                    .testTag("quiz_option_$index"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg),
                border = androidx.compose.foundation.BorderStroke(2.dp, borderColor),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = optionText,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isSelected || (hasAnswered && isCorrect)) FontWeight.Bold else FontWeight.Medium,
                        color = Color.Black
                    )

                    if (hasAnswered) {
                        if (isCorrect) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Correct",
                                tint = LearnzoMint,
                                modifier = Modifier.size(24.dp)
                            )
                        } else if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Wrong",
                                tint = Color(0xFFFF5252),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }

        // Post-Answer Feedback & Action Buttons
        if (hasAnswered) {
            Spacer(modifier = Modifier.height(14.dp))

            val isUserCorrect = selectedOptionIndex == currentQuestion.correctIndex

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isUserCorrect) Color(0xFFF1F8E9) else Color(0xFFFFF3E0)
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isUserCorrect) LearnzoMint else LearnzoAmber
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (isUserCorrect) "🎉 " + AppStrings.quizCorrectPraise(isHindi) else "💡 " + AppStrings.quizMistakeNote(isHindi),
                            fontWeight = FontWeight.Bold,
                            color = if (isUserCorrect) LearnzoMint else Color(0xFFE65100),
                            fontSize = 15.sp
                        )
                    }

                    if (!isUserCorrect) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Correct Answer: ${options.getOrNull(currentQuestion.correctIndex) ?: ""}",
                            fontWeight = FontWeight.Bold,
                            color = LearnzoIndigo,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = if (isHindi) currentQuestion.explanationHi else currentQuestion.explanationEn,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.DarkGray,
                        lineHeight = 20.sp
                    )

                    // Optional Deep Concept Explanation
                    if (showDetailedExplanation && currentQuestion.conceptSummary.isNotBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .padding(10.dp)
                        ) {
                            Column {
                                Text(
                                    text = "🧠 Core Concept Breakdown:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = LearnzoIndigo
                                )
                                Text(
                                    text = currentQuestion.conceptSummary,
                                    fontSize = 12.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Action Buttons: "Next Question", "Explain Answer", "End Practice"
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // "Explain Answer" toggle button
                    OutlinedButton(
                        onClick = { showDetailedExplanation = !showDetailedExplanation },
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("quiz_explain_btn")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = LearnzoAmber,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (showDetailedExplanation) "Hide Details" else "Explain Answer",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    // "Next Question" button
                    Button(
                        onClick = {
                            questionsAttempted++
                            currentQuestion = DynamicQuestionGenerator.generateQuestion(
                                subjectId = subject.id,
                                topicName = null,
                                currentDifficulty = currentDifficulty,
                                board = board,
                                studentClass = studentClass,
                                weakTopics = weakTopics.toList()
                            )
                        },
                        modifier = Modifier
                            .weight(1.3f)
                            .height(52.dp)
                            .testTag("quiz_next_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = LearnzoIndigo),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = if (isHindi) "अगला सवाल 🚀" else "Next Question 🚀",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // "End Practice" button (graceful exit)
                Button(
                    onClick = onBack,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("quiz_finish_early_btn")
                ) {
                    Text(
                        text = if (isHindi) "अभ्यास समाप्त करें 🛑" else "End Practice 🛑",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
