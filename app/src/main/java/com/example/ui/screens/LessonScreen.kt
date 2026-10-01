package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Lesson
import com.example.model.Subject
import com.example.ui.components.CheckInReason
import com.example.ui.components.TeacherCardBanner
import com.example.ui.components.TeacherCheckInDialog
import com.example.ui.theme.LearnzoAmber
import com.example.ui.theme.LearnzoGold
import com.example.ui.theme.LearnzoIndigo
import com.example.ui.theme.LearnzoMint
import com.example.util.AppStrings
import kotlinx.coroutines.delay

@Composable
fun LessonScreen(
    lesson: Lesson,
    subject: Subject,
    isHindi: Boolean,
    onBack: () -> Unit,
    onLessonComplete: (minutes: Int) -> Unit,
    onSpeakText: (String) -> Unit,
    onTeacherCheckInResult: (subjectId: String, topic: String, isCorrect: Boolean, consecutiveWrong: Int) -> Unit = { _, _, _, _ -> }
) {
    BackHandler { onBack() }

    var currentSlideIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableIntStateOf(-1) }
    var isAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }

    var showTeacherCheckIn by remember { mutableStateOf(false) }
    var checkInReason by remember { mutableStateOf(CheckInReason.PROGRESS_CHECK) }

    val currentSlide = lesson.slides.getOrNull(currentSlideIndex) ?: lesson.slides.first()

    // Speak teacher dialogue on slide transition
    LaunchedEffect(currentSlideIndex) {
        val speech = if (isHindi) currentSlide.teacherSpeechHi else currentSlide.teacherSpeechEn
        onSpeakText(speech)
        selectedOptionIndex = -1
        isAnswerCorrect = null

        // Inactivity/boredom detection: gentle check-in after 20 seconds
        delay(20000)
        checkInReason = CheckInReason.INACTIVITY_BOREDOM
        showTeacherCheckIn = true
    }

    // Animation for visuals
    val infiniteTransition = rememberInfiniteTransition(label = "visual_bounce")
    val bounceScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("lesson_screen_root")
    ) {
        // Top Navigation Bar
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
                    .testTag("lesson_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = LearnzoIndigo
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (isHindi) lesson.titleHi else lesson.titleEn,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(subject.primaryColorHex)
                )
                Text(
                    text = AppStrings.lessonSlideCounter(isHindi, currentSlideIndex + 1, lesson.slides.size),
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                // Teacher Check-in trigger button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(subject.primaryColorHex).copy(alpha = 0.15f))
                        .clickable {
                            checkInReason = CheckInReason.PROGRESS_CHECK
                            showTeacherCheckIn = true
                        }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("manual_teacher_checkin_btn")
                ) {
                    Text(
                        text = if (isHindi) "समझ आया? 😊" else "Got it? 😊",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(subject.primaryColorHex)
                    )
                }

                IconButton(
                    onClick = {
                        val speech = if (isHindi) currentSlide.teacherSpeechHi else currentSlide.teacherSpeechEn
                        onSpeakText(speech)
                    },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .testTag("lesson_replay_audio_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = "Replay audio",
                        tint = Color(subject.primaryColorHex)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Slide Progress Bar
        LinearProgressIndicator(
            progress = { (currentSlideIndex + 1) / lesson.slides.size.toFloat() },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
            color = Color(subject.primaryColorHex),
            trackColor = Color(subject.accentColorHex).copy(alpha = 0.4f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Teacher Dialogue Card
        TeacherCardBanner(
            subject = subject,
            speechText = if (isHindi) currentSlide.teacherSpeechHi else currentSlide.teacherSpeechEn,
            isSpeaking = true,
            onSpeakClick = {
                val speech = if (isHindi) currentSlide.teacherSpeechHi else currentSlide.teacherSpeechEn
                onSpeakText(speech)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Animated Cartoon Concept Visual Scene
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(2.dp, Color(subject.accentColorHex).copy(alpha = 0.5f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isHindi) currentSlide.sceneTitleHi else currentSlide.sceneTitleEn,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = LearnzoIndigo,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Giant Animated Visual
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .scale(bounceScale)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                listOf(
                                    Color(subject.accentColorHex).copy(alpha = 0.6f),
                                    Color(subject.primaryColorHex).copy(alpha = 0.1f)
                                )
                            )
                        )
                        .border(3.dp, Color(subject.primaryColorHex), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentSlide.visualEmoji,
                        fontSize = 32.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = currentSlide.interactiveVisualDescription,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.DarkGray,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Mini Check Question (if present on slide)
        if (currentSlide.miniCheckQuestionHi != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9E6)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, LearnzoAmber)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🎯 " + (if (isHindi) currentSlide.miniCheckQuestionHi else currentSlide.miniCheckQuestionEn ?: ""),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8A4B00)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    currentSlide.options.forEachIndexed { index, option ->
                        val isSelected = selectedOptionIndex == index
                        val isCorrectChoice = index == currentSlide.correctIndex
                        val btnColor = when {
                            isSelected && isAnswerCorrect == true -> LearnzoMint
                            isSelected && isAnswerCorrect == false -> Color(0xFFFF5252)
                            else -> Color.White
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(btnColor)
                                .border(
                                    1.5.dp,
                                    if (isSelected) Color.Transparent else Color(0xFFFFD54F),
                                    RoundedCornerShape(12.dp)
                                )
                                .clickable {
                                    selectedOptionIndex = index
                                    val correct = isCorrectChoice
                                    isAnswerCorrect = correct
                                    if (correct) {
                                        onSpeakText(if (isHindi) "वाह! बिल्कुल सही!" else "Super! Absolutely right!")
                                    } else {
                                        onSpeakText(if (isHindi) "फिर से कोशिश करो प्यारे बच्चे!" else "Try once more, dear learner!")
                                    }
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                                .testTag("mini_check_opt_$index")
                        ) {
                            Text(
                                text = option,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else Color.Black
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Bottom Controls: Next Slide or Finish Lesson
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentSlideIndex > 0) {
                Button(
                    onClick = { currentSlideIndex-- },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.testTag("prev_slide_btn")
                ) {
                    Text(text = AppStrings.lessonPreviousButton(isHindi), color = Color.Black)
                }
            } else {
                Spacer(modifier = Modifier.size(1.dp))
            }

            if (currentSlideIndex < lesson.slides.size - 1) {
                Button(
                    onClick = { currentSlideIndex++ },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(subject.primaryColorHex)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.testTag("next_slide_btn")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = AppStrings.lessonNextSlideButton(isHindi), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.size(6.dp))
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                }
            } else {
                Button(
                    onClick = { onLessonComplete(lesson.estimatedMinutes) },
                    colors = ButtonDefaults.buttonColors(containerColor = LearnzoMint),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.testTag("finish_lesson_btn")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null)
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = AppStrings.lessonCompleteButton(isHindi),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Interactive Caring Teacher Check-In Dialog
        if (showTeacherCheckIn) {
            TeacherCheckInDialog(
                subject = subject,
                currentLesson = lesson,
                reason = checkInReason,
                isHindi = isHindi,
                onAnswerEvaluated = { isCorrect, wrongCount ->
                    onTeacherCheckInResult(subject.id, lesson.topicName, isCorrect, wrongCount)
                },
                onDismiss = { showTeacherCheckIn = false },
                onSpeak = onSpeakText
            )
        }
    }
}
