package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.LearnzoAmber
import com.example.ui.theme.LearnzoGold
import com.example.ui.theme.LearnzoIndigo
import com.example.ui.theme.LearnzoMint
import com.example.util.AppStrings

@Composable
fun StudyAlarmScreen(
    childName: String,
    isHindi: Boolean,
    onStartStudy: () -> Unit,
    onDismissAlarm: () -> Unit,
    onSpeakAlarm: () -> Unit
) {
    LaunchedEffect(Unit) {
        onSpeakAlarm()
    }

    // Bell swinging animation
    val infiniteTransition = rememberInfiniteTransition(label = "bell_swing")
    val bellRotation by infiniteTransition.animateFloat(
        initialValue = -15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(280),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bell"
    )

    // Back button dismisses alarm directly
    BackHandler {
        onDismissAlarm()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFF8F00), Color(0xFFFF6D00), Color(0xFFD84315))
                )
            )
            .padding(24.dp)
            .testTag("study_alarm_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Ringing Alarm Bell
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .rotate(bellRotation)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "⏰", fontSize = 72.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = AppStrings.studyAlarmScreenSub(isHindi),
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (isHindi)
                    "प्यारे $childName, आपके टीचर्स आपका इंतज़ार कर रहे हैं! आज का गोल पूरा करके Surprise Box जीतें!"
                else
                    "Dear $childName, your teachers are waiting for you! Finish today's tasks and win a Surprise Box!",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFFFF9C4),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Teacher Callout Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_mascot),
                        contentDescription = "Teacher Mascot",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Column {
                        Text(
                            text = if (isHindi) "👩‍🏫 Shruti Mam कहती हैं:" else "👩‍🏫 Teacher says:",
                            fontWeight = FontWeight.Bold,
                            color = LearnzoIndigo,
                            fontSize = 14.sp
                        )
                        Text(
                            text = if (isHindi)
                                "\"सिर्फ 15-20 मिनट मजेदार पढ़ाई, और फिर Surprise Box का जश्न! 🎁\""
                            else
                                "\"Just 15-20 minutes of fun learning, then celebrate with a Surprise Box! 🎁\"",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // Primary Start Study Button
            Button(
                onClick = onStartStudy,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("alarm_start_study_btn"),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LearnzoMint),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
            ) {
                Text(
                    text = AppStrings.studyAlarmStartButton(isHindi),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Dismiss button (Direct dismiss, no PIN)
            Button(
                onClick = onDismissAlarm,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("alarm_skip_btn"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.25f))
            ) {
                Text(
                    text = AppStrings.studyAlarmLaterButton(isHindi),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }
        }
    }
}
