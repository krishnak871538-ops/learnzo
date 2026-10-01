package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.MistakeEntity
import com.example.data.entity.UserProfile
import com.example.ui.theme.LearnzoAmber
import com.example.ui.theme.LearnzoGold
import com.example.ui.theme.LearnzoIndigo
import com.example.ui.theme.LearnzoMint

import com.example.util.AppStrings

@Composable
fun ParentDashboardScreen(
    profile: UserProfile,
    activeMistakes: List<MistakeEntity>,
    isHindi: Boolean,
    onBack: () -> Unit,
    onUpdateAlarm: (Int, Int) -> Unit,
    onSpeakTeacherReport: (String) -> Unit,
    onSelectLanguage: (String) -> Unit = {},
    onOpenMoreLanguages: () -> Unit = {}
) {
    BackHandler { onBack() }

    var selectedPtmQuestion by remember { mutableStateOf<String?>(null) }
    var teacherAnswerText by remember { mutableStateOf<String?>(null) }

    // Direct Full PTM & Parent Dashboard (No PIN required)
    val totalMinutes = profile.totalStudySeconds / 60
    val teacherFeedbackScript = if (isHindi) {
        "नमस्ते माता-पिता जी! इस महीने ${profile.childName} ने कुल $totalMinutes मिनट पढ़ाई की है और ${profile.streakDays} दिन की लगातार स्ट्रीक बनाई है। Maths और Hindi में बच्चे की समझ बहुत अच्छी हुई है। Multiplication में थोड़ी गलतियाँ नोट हुई हैं, जिसकी स्मार्ट प्रैक्टिस ऐप खुद करवा रहा है। घर पर बच्चे की हर छोटी सफलता की तारीफ करें!"
    } else {
        "Hello dear parents! This month ${profile.childName} has completed $totalMinutes minutes of active learning with a ${profile.streakDays}-day streak. Comprehension in Mathematics and Hindi is solid. Smart Learning Memory noticed some errors in Multiplication, which are being practiced automatically. Keep encouraging your child at home!"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFF))
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("parent_dashboard_root")
    ) {
            // Header
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
                        .testTag("ptm_back_btn")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = LearnzoIndigo
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = AppStrings.parentPtmHeaderTitle(isHindi),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = LearnzoIndigo
                    )
                    Text(
                        text = "Parent–Teacher Meeting • ${profile.childName}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(LearnzoMint.copy(alpha = 0.15f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "🔒 Verified", fontSize = 11.sp, color = LearnzoMint, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🌐 Small Language Option Inside Profile/Parent Settings Area
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("parent_language_settings_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFE0E0FF)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = AppStrings.languageSettingsTitle(isHindi),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LearnzoIndigo
                    )
                    Text(
                        text = AppStrings.languageSettingsSubtitle(isHindi),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Hindi Button
                        Button(
                            onClick = { onSelectLanguage("hi") },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("select_hindi_language_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isHindi) LearnzoIndigo else Color(0xFFF3E5F5)
                            ),
                            border = if (isHindi) androidx.compose.foundation.BorderStroke(2.dp, LearnzoAmber) else null
                        ) {
                            Text(
                                text = "Hindi 🇮🇳",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (isHindi) Color.White else LearnzoIndigo
                            )
                        }

                        // English Button
                        Button(
                            onClick = { onSelectLanguage("en") },
                            modifier = Modifier
                                .weight(1f)
                                .height(46.dp)
                                .testTag("select_english_language_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!isHindi) LearnzoIndigo else Color(0xFFE8EAF6)
                            ),
                            border = if (!isHindi) androidx.compose.foundation.BorderStroke(2.dp, LearnzoAmber) else null
                        ) {
                            Text(
                                text = "English 🇬🇧",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = if (!isHindi) Color.White else LearnzoIndigo
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = AppStrings.activeLanguageIndicator(isHindi),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LearnzoMint
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Button to open full Language Settings with all 14+ languages
                    Button(
                        onClick = onOpenMoreLanguages,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3E5F5)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(42.dp)
                            .testTag("open_all_languages_btn")
                    ) {
                        Text(
                            text = if (isHindi) "🌍 सभी भाषाएँ देखें (मराठी, தமிழ், తెలుగు, বাংলা...)" else "🌍 Browse All Languages (मराठी, தமிழ், etc.)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = LearnzoIndigo
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 👩‍🏫 Teacher Recorded Feedback Audio Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEDE7F6)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, LearnzoIndigo.copy(alpha = 0.3f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "👩‍🏫", fontSize = 28.sp)
                            Column {
                                Text(
                                    text = if (isHindi) "टीचर का मासिक वॉइस संदेश" else "Teacher's Monthly Voice Feedback",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = LearnzoIndigo
                                )
                                Text(
                                    text = "Shruti Mam & Prof. Raman",
                                    fontSize = 11.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        Button(
                            onClick = { onSpeakTeacherReport(teacherFeedbackScript) },
                            colors = ButtonDefaults.buttonColors(containerColor = LearnzoIndigo),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.testTag("play_teacher_ptm_voice_btn")
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = if (isHindi) "सुनें" else "Listen", fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "\"$teacherFeedbackScript\"",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF37474F),
                        lineHeight = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 7 Points of Monthly PTM (From Interview Q19)
            Text(
                text = if (isHindi) "📊 विस्तृत प्रगति समीक्षा (7 Key Insights)" else "📊 Detailed Progress Review (7 Key Insights)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = LearnzoIndigo
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 1. इस महीने क्या पढ़ा
            PtmReportCard(
                icon = "📚",
                title = if (isHindi) "1. इस महीने क्या पढ़ा" else "1. What Was Studied This Month",
                content = if (isHindi)
                    "कुल $totalMinutes मिनट सक्रिय अध्ययन। Maths में Addition और Multiplication, English Phonics, Hindi स्वर एवं विलोम शब्द, Science में Plants के पाठ पूरे किए गए।"
                else
                    "Total $totalMinutes minutes active study. Units covered: Math Addition & Multiplication, English Phonics, Hindi Vowels & Antonyms, Science Plant Life.",
                statusColor = LearnzoIndigo
            )

            // 2. किन subjects में अच्छा किया
            PtmReportCard(
                icon = "✅",
                title = if (isHindi) "2. किन विषयों में शानदार प्रदर्शन किया" else "2. Subjects with Strong Performance",
                content = if (isHindi)
                    "Hindi और English में 92% सही उत्तर दर! शब्दों की पहचान और Phonics में बच्चे का आत्मविश्वास बहुत ऊँचा है।"
                else
                    "Hindi and English have 92% accuracy rate! Child demonstrates remarkable vocabulary recall.",
                statusColor = LearnzoMint
            )

            // 3. कहाँ बार-बार गलती हुई (Smart Learning Memory)
            PtmReportCard(
                icon = "❌",
                title = if (isHindi) "3. कमजोर क्षेत्र (Smart Learning Memory)" else "3. Weak Topics Tracked by Smart Memory",
                content = if (activeMistakes.isNotEmpty()) {
                    val topics = activeMistakes.joinToString(", ") { "${it.topicName} (${it.mistakeCount} गलतियाँ)" }
                    if (isHindi) "पहचाने गए कमजोर टॉपिक्स: $topics। ऐप ने इन्हें विशेष अभ्यास सूची में जोड़ा है।"
                    else "Identified weak areas: $topics. Targeted drills are being scheduled."
                } else {
                    if (isHindi) "अभी कोई गंभीर कमजोरी नहीं है! सभी बुनियादी सवालों के सही जवाब दिए जा रहे हैं।"
                    else "No recurring mistakes found! Excellent foundational mastery."
                },
                statusColor = Color(0xFFFF5252)
            )

            // 4. Extra Practice की जरूरत
            PtmReportCard(
                icon = "🧠",
                title = if (isHindi) "4. Extra Practice की ज़रूरत" else "4. Extra Practice Focus",
                content = if (isHindi)
                    "Maths Multiplication के 2 से 10 तक के पहाड़ों पर 5-5 मिनट की दैनिक प्रैक्टिस सुझाई गई है।"
                else
                    "Daily 5-minute practice on multiplication tables (2 through 10) is advised.",
                statusColor = LearnzoAmber
            )

            // 5. बच्चे की Study Routine
            PtmReportCard(
                icon = "⏰",
                title = if (isHindi) "5. पढ़ाई की नियमितता (Routine & Streak)" else "5. Study Routine & Consistency",
                content = if (isHindi)
                    "🔥 लगातार ${profile.streakDays} दिन अध्ययन! शाम 06:00 बजे के Study Alarm पर बच्चा नियमित रूप से ऐप खोल रहा है।"
                else
                    "🔥 ${profile.streakDays}-day streak maintained! Child promptly attends daily study alarm sessions.",
                statusColor = LearnzoIndigo
            )

            // 6. अगले महीने का Plan
            PtmReportCard(
                icon = "🎯",
                title = if (isHindi) "6. अगले महीने का Study Plan" else "6. Next Month Study Roadmap",
                content = if (isHindi)
                    "अगले माह Division के बेसिक्स, English Sentences, GK Solar System भाग-2 और Computer Keyboard शॉर्टकट्स का लक्ष्य है।"
                else
                    "Next month roadmap includes Division fundamentals, sentence construction, Solar System Part-2, and keyboard shortcuts.",
                statusColor = Color(0xFF00B0FF)
            )

            // 7. Parent को Guidance
            PtmReportCard(
                icon = "👨‍👩‍👧",
                title = if (isHindi) "7. माता-पिता के लिए टीचर की सलाह" else "7. Teacher's Guidance for Home",
                content = if (isHindi)
                    "बच्चे पर ज्यादा दबाव न डालें। 20 मिनट पढ़ाई के बाद खेल-कूद का समय दें। Surprise Box से मिलने वाले रिवॉर्ड्स पर उनका उत्साह बढ़ाएँ।"
                else
                    "Avoid undue pressure. Encourage 20 minutes of gamified study followed by outdoor play. Celebrate surprise box milestones!",
                statusColor = LearnzoMint
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 🗣️ Interactive PTM Consultation Simulator
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFBBDEFB)),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isHindi) "🗣️ टीचर से लाइव सवाल पूछें (PTM Consultation)" else "🗣️ Ask Teacher Directly (PTM Consultation)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LearnzoIndigo
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val sampleQuestions = listOf(
                        "Maths में बच्चे का ध्यान और रुचि कैसे बढ़ाएं?",
                        "बच्चा मोबाइल में सिर्फ पढ़ाई का ऐप ही चलाए, इसके लिए क्या करें?",
                        "English बोलने में झिझक कैसे दूर करें?"
                    )

                    sampleQuestions.forEach { q ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF0F4FF))
                                .clickable {
                                    selectedPtmQuestion = q
                                    teacherAnswerText = when (q) {
                                        sampleQuestions[0] -> if (isHindi)
                                            "टीचर का जवाब: 'Maths को रोजमर्रा की चीजों से जोड़ें, जैसे फल गिनना या सिक्के जोड़ना। Learnzo के जादुई जोड़ वाले गेम्स रोज 10 मिनट खेलें!'"
                                        else
                                            "Teacher: 'Connect numbers to daily snacks and fruits. Play Learnzo addition lessons 10 mins daily!'"
                                        sampleQuestions[1] -> if (isHindi)
                                            "टीचर का जवाब: 'Study Alarm और पढ़ाई का नियमित नियम बनाएं। जब अलार्म बजे, सिर्फ Learnzo की पढ़ाई पूरी होने के बाद ही अन्य अनुमति दें!'"
                                        else
                                            "Teacher: 'Use the Study Alarm and daily study routine strictly so the child treats it like official study time!'"
                                        else -> if (isHindi)
                                            "टीचर का जवाब: 'रोज घर में 2-3 छोटे English वाक्य जैसे Good Morning, Thank You, Let's Play दोहराएं। ऐप के Phonics राइम्स जोर से बुलवाएं!'"
                                        else
                                            "Teacher: 'Practice simple friendly daily phrases and read rhyming phonics aloud with the app!'"
                                    }
                                    onSpeakTeacherReport(teacherAnswerText ?: "")
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(text = "❓ $q", fontSize = 13.sp, color = LearnzoIndigo, fontWeight = FontWeight.SemiBold)
                        }
                    }

                    if (teacherAnswerText != null) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFE8F5E9))
                                .border(1.dp, LearnzoMint, RoundedCornerShape(14.dp))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = teacherAnswerText ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF1B5E20),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

@Composable
fun PtmReportCard(
    icon: String,
    title: String,
    content: String,
    statusColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.3f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(text = icon, fontSize = 22.sp)
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Bold, color = statusColor, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = content, style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray, lineHeight = 19.sp)
            }
        }
    }
}
