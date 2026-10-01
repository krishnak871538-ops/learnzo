package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import com.example.util.AppStrings
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.entity.DailyPlanEntity
import com.example.data.entity.UserProfile
import com.example.model.Subject
import com.example.model.SubjectCatalog
import com.example.ui.components.KidCharacterAvatar
import com.example.ui.theme.LearnzoAmber
import com.example.ui.theme.LearnzoGold
import com.example.ui.theme.LearnzoIndigo
import com.example.ui.theme.LearnzoMint

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    profile: UserProfile,
    dailyPlan: List<DailyPlanEntity>,
    isHindi: Boolean,
    isAudioMuted: Boolean,
    onToggleLanguage: () -> Unit = {},
    onToggleMute: () -> Unit,
    onOpenSurpriseBox: () -> Unit,
    onSelectSubjectLesson: (Subject) -> Unit,
    onSelectSubjectQuiz: (Subject) -> Unit,
    onStartPracticeTopic: (String, String) -> Unit, // topic, subjectId
    onOpenParentDashboard: () -> Unit,
    onOpenStudyAlarm: () -> Unit,
    onOpenLeaderboard: () -> Unit,
    onWatchRewardAd: () -> Unit = {},
    onOpenAiStudio: () -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Pulse animation for Surprise Box ready alert
    val infiniteTransition = rememberInfiniteTransition(label = "box_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFF))
            .verticalScroll(scrollState)
            .padding(bottom = 32.dp)
            .testTag("home_screen_root")
    ) {
        // Top Header Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF5C35D6), Color(0xFF673AB7))
                    )
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Kid Avatar & Greeting
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        KidCharacterAvatar(
                            gender = profile.characterGender,
                            nickname = profile.characterNickname,
                            sizeDp = 48,
                            showBadge = false
                        )
                        Column {
                            Text(
                                text = AppStrings.homeGreeting(isHindi, profile.childName),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = AppStrings.homeStreakBadge(isHindi, profile.streakDays),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFFFD54F)
                                )
                            }
                        }
                    }

                    // Action Controls: Mute, AI Studio, Parent Zone
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Audio Mute toggle
                        IconButton(
                            onClick = onToggleMute,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f))
                                .testTag("toggle_mute_btn")
                        ) {
                            Icon(
                                imageVector = if (isAudioMuted) Icons.AutoMirrored.Filled.VolumeMute else Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Audio toggle",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        // Parent PTM button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFFB300))
                                .clickable { onOpenParentDashboard() }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                                .testTag("open_parent_zone_btn")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = "Parent PTM",
                                    tint = Color.Black,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = AppStrings.homeParentPtm(isHindi),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Stats Bar (Coins, Stars, Study Alarm)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Coins Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "🪙", fontSize = 14.sp)
                        Text(
                            text = "${profile.coins}",
                            color = Color(0xFFFFE082),
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    // Stars Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Stars",
                            tint = Color(0xFFFFD54F),
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${profile.stars} ${AppStrings.homeStarsLabel(isHindi)}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    // Study Alarm Badge
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable { onOpenStudyAlarm() }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                            .testTag("study_alarm_header_pill"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Alarm,
                            contentDescription = "Alarm",
                            tint = Color(0xFF80DEEA),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = String.format("%02d:%02d PM", if (profile.studyAlarmHour > 12) profile.studyAlarmHour - 12 else profile.studyAlarmHour, profile.studyAlarmMinute),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }

                    // Leaderboard Pill
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.2f))
                            .clickable { onOpenLeaderboard() }
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                            .testTag("open_leaderboard_pill"),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "🏆", fontSize = 13.sp)
                        Text(
                            text = if (isHindi) "रैंक" else "Rank",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Hero Banner Card with Teachers Art
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(22.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.img_teachers),
                    contentDescription = "Teachers Team",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color(0xCC311B92))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = if (isHindi) "मज़ेदार पढ़ाई का नया सफर! 🚀" else "Exciting Learning Adventure! 🚀",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = if (isHindi) "सभी विषय कार्टून टीचर्स के साथ सीखें" else "Learn all subjects with your favorite teachers",
                        fontSize = 12.sp,
                        color = Color(0xFFFFD54F)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 🎁 Surprise Box Ready Banner (If available) or Progress Meter
        if (profile.availableSurpriseBoxes > 0) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .scale(pulseScale)
                    .clickable { onOpenSurpriseBox() }
                    .testTag("surprise_box_ready_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                border = androidx.compose.foundation.BorderStroke(2.dp, LearnzoAmber),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_surprise_box),
                        contentDescription = "Surprise Box",
                        modifier = Modifier
                            .size(54.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = AppStrings.surpriseBoxReadyTitle(isHindi),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFE65100)
                        )
                        Text(
                            text = AppStrings.surpriseBoxReadySub(isHindi),
                            fontSize = 12.sp,
                            color = Color.DarkGray
                        )
                    }
                    Button(
                        onClick = onOpenSurpriseBox,
                        colors = ButtonDefaults.buttonColors(containerColor = LearnzoAmber),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("tap_open_box_banner_btn")
                    ) {
                        Text(text = AppStrings.surpriseBoxOpenButton(isHindi), fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // Next Box Progress Meter
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = AppStrings.nextBoxProgressTitle(isHindi),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = LearnzoIndigo
                        )
                        Text(
                            text = AppStrings.nextBoxProgressCount(isHindi, profile.correctAnswersCount),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LearnzoMint
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { (profile.correctAnswersCount % 10) / 10f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = LearnzoAmber,
                        trackColor = Color(0xFFFFECB3)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = AppStrings.nextBoxProgressSub(isHindi),
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = onWatchRewardAd,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8F00)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                            .testTag("watch_reward_ad_btn")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = "🎬", fontSize = 14.sp)
                            Text(
                                text = if (isHindi) "बोनस Surprise Box खोलें (AdMob Test Ad)" else "Unlock Bonus Surprise Box (Test Ad)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🎯 Today's Personal Study Plan (From Interview Q20)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .testTag("personal_study_plan_card"),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE0E0FF)),
            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = AppStrings.personalizedPlanTitle(isHindi, profile.childName),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = LearnzoIndigo
                        )
                        Text(
                            text = AppStrings.personalizedPlanSub(isHindi),
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                    Text(text = "🧠 Smart", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = LearnzoMint)
                }

                Spacer(modifier = Modifier.height(12.dp))

                dailyPlan.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (item.isWeakTopic) Color(0xFFFFF3E0) else Color(0xFFF7F8FC))
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = if (item.isCompleted) Icons.Default.CheckCircle else Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = if (item.isCompleted) LearnzoMint else if (item.isWeakTopic) LearnzoAmber else LearnzoIndigo,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = if (isHindi) item.titleHi else item.titleEn,
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (item.isWeakTopic) FontWeight.Bold else FontWeight.Normal,
                                color = if (item.isCompleted) Color.Gray else Color.Black,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        if (item.isWeakTopic && !item.isCompleted) {
                            Button(
                                onClick = { onStartPracticeTopic("Multiplication", "maths") },
                                colors = ButtonDefaults.buttonColors(containerColor = LearnzoAmber),
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .height(32.dp)
                                    .testTag("practice_weak_topic_btn")
                            ) {
                                Text(
                                    text = AppStrings.practiceButton(isHindi),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 📚 All 6 Subjects Header
        Text(
            text = AppStrings.chooseSubjectSectionTitle(isHindi),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = LearnzoIndigo,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Grid of 6 Subjects
        SubjectCatalog.subjects.forEach { subject ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("subject_card_${subject.id}"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(subject.primaryColorHex).copy(alpha = 0.25f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // Subject Emoji & Badge
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color(subject.primaryColorHex).copy(alpha = 0.15f))
                            .border(2.dp, Color(subject.primaryColorHex), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = subject.iconEmoji, fontSize = 28.sp)
                    }

                    // Subject info & Teacher name
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isHindi) subject.nameHi else subject.nameEn,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(subject.primaryColorHex)
                        )
                        Text(
                            text = "${subject.teacherEmoji} ${subject.teacherName} (${subject.teacherTitle})",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )
                    }

                    // Action Buttons: Lesson & Quiz
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Button(
                            onClick = { onSelectSubjectLesson(subject) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(subject.primaryColorHex)),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .height(34.dp)
                                .testTag("start_lesson_${subject.id}")
                        ) {
                            Text(
                                text = AppStrings.lessonButton(isHindi),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = { onSelectSubjectQuiz(subject) },
                            colors = ButtonDefaults.buttonColors(containerColor = LearnzoAmber),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .height(34.dp)
                                .testTag("start_quiz_${subject.id}")
                        ) {
                            Text(
                                text = AppStrings.quizButton(isHindi),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
