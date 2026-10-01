package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.KidCharacterAvatar
import com.example.ui.theme.LearnzoAmber
import com.example.ui.theme.LearnzoGold
import com.example.ui.theme.LearnzoIndigo
import com.example.ui.theme.LearnzoMint
import com.example.util.AppStrings

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    initialName: String = "Riya",
    initialGender: String = "GIRL",
    initialNickname: String = "Super Star",
    initialLanguage: String = "en",
    initialBoard: String = "CBSE",
    initialClass: String = "Class 6",
    initialGoals: String = "Score 95%+, Build Strong Concepts",
    initialUserType: String = "Parent with Student",
    isHindi: Boolean = false,
    onBack: () -> Unit = {},
    onSaveProfile: (
        name: String,
        gender: String,
        nickname: String,
        language: String,
        board: String,
        studentClass: String,
        goals: String,
        userType: String
    ) -> Unit
) {
    BackHandler { onBack() }

    var selectedLanguage by remember { mutableStateOf(if (initialLanguage == "hi") "Hindi" else if (initialLanguage == "hinglish") "Hinglish" else "English") }
    var selectedBoard by remember { mutableStateOf(initialBoard) }
    var selectedClass by remember { mutableStateOf(initialClass) }
    val initialGoalList = remember { initialGoals.split(", ").filter { it.isNotBlank() }.toSet() }
    var selectedGoals by remember { mutableStateOf(if (initialGoalList.isEmpty()) setOf("Score 95%+", "Build Strong Concepts") else initialGoalList) }
    var selectedUserType by remember { mutableStateOf(initialUserType) }

    var name by remember { mutableStateOf(initialName.ifEmpty { "Riya" }) }
    var gender by remember { mutableStateOf(initialGender) }
    var nickname by remember { mutableStateOf(initialNickname.ifEmpty { "Super Star" }) }

    val languages = listOf("English", "Hinglish", "Hindi")
    val boards = listOf("CBSE", "ICSE", "State Board", "IB", "IGCSE")
    val classes = (3..10).map { "Class $it" }
    val availableGoals = listOf(
        "Score 95%+",
        "Build Strong Concepts",
        "Daily Practice Habit",
        "Olympiad Prep",
        "Homework Helper",
        "Exam Confidence"
    )
    val userTypes = listOf(
        "Parent with Student" to "👨‍👩‍👧 Parent with Student",
        "Only Student" to "🎒 Only Student",
        "Only Parent" to "👩‍🏫 Only Parent"
    )

    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFEDE7F6), Color(0xFFF3E5F5), Color(0xFFE8F5E9))
                )
            )
            .padding(16.dp)
            .testTag("onboarding_screen_root")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "🚀 Welcome to Learnzo!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = LearnzoIndigo,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Customize your AI personal learning journey in a few quick steps",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 1. Language Selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("onboarding_language_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🌐 1. Select App Language",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LearnzoIndigo
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        languages.forEach { lang ->
                            val isSelected = selectedLanguage == lang
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) LearnzoIndigo else Color(0xFFF5F5F5))
                                    .border(
                                        1.5.dp,
                                        if (isSelected) LearnzoIndigo else Color(0xFFE0E0E0),
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable { selectedLanguage = lang }
                                    .padding(vertical = 12.dp)
                                    .testTag("onboarding_lang_${lang.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = lang,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = if (isSelected) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Educational Board Selection
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("onboarding_board_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🏫 2. Select Board",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LearnzoIndigo
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        boards.forEach { board ->
                            val isSelected = selectedBoard == board
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) Color(0xFF1E88E5) else Color(0xFFF5F5F5))
                                    .border(
                                        1.5.dp,
                                        if (isSelected) Color(0xFF1565C0) else Color(0xFFE0E0E0),
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable { selectedBoard = board }
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                                    .testTag("onboarding_board_${board.lowercase().replace(" ", "_")}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = board,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Class Selection (Class 3 to Class 10)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("onboarding_class_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🎒 3. Select Class (Class 3 to Class 10)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LearnzoIndigo
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        classes.forEach { cls ->
                            val isSelected = selectedClass == cls
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) LearnzoMint else Color(0xFFF5F5F5))
                                    .border(
                                        1.5.dp,
                                        if (isSelected) Color(0xFF00897B) else Color(0xFFE0E0E0),
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable { selectedClass = cls }
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                                    .testTag("onboarding_class_${cls.lowercase().replace(" ", "_")}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cls,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isSelected) Color.White else Color.Black
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Learning Goals (Multiple Selection)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("onboarding_goals_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "🎯 4. Select Goals (Pick 1 or more)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LearnzoIndigo
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        availableGoals.forEach { goal ->
                            val isSelected = selectedGoals.contains(goal)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) Color(0xFFFFF3E0) else Color(0xFFF5F5F5))
                                    .border(
                                        1.5.dp,
                                        if (isSelected) LearnzoAmber else Color(0xFFE0E0E0),
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable {
                                        selectedGoals = if (isSelected) {
                                            if (selectedGoals.size > 1) selectedGoals - goal else selectedGoals
                                        } else {
                                            selectedGoals + goal
                                        }
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                                    .testTag("onboarding_goal_${goal.take(8).lowercase().replace(" ", "_")}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = LearnzoAmber,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.size(4.dp))
                                    }
                                    Text(
                                        text = goal,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 12.sp,
                                        color = if (isSelected) Color(0xFFE65100) else Color.DarkGray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Select User Type
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("onboarding_usertype_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "👥 5. Select User Type",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LearnzoIndigo
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        userTypes.forEach { (typeKey, label) ->
                            val isSelected = selectedUserType == typeKey
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isSelected) Color(0xFFEDE7F6) else Color(0xFFF9F9F9))
                                    .border(
                                        1.5.dp,
                                        if (isSelected) LearnzoIndigo else Color(0xFFE0E0E0),
                                        RoundedCornerShape(14.dp)
                                    )
                                    .clickable { selectedUserType = typeKey }
                                    .padding(horizontal = 16.dp, vertical = 12.dp)
                                    .testTag("onboarding_usertype_${typeKey.take(6).lowercase()}"),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = label,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = if (isSelected) LearnzoIndigo else Color.Black
                                    )
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = null,
                                            tint = LearnzoIndigo,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Character Selection & Student Name
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "⭐ 6. Student Profile & Character",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = LearnzoIndigo
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Boy Character Card
                        val isBoySelected = gender.equals("BOY", ignoreCase = true)
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { gender = "BOY" }
                                .testTag("select_boy_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isBoySelected) Color(0xFFE3F2FD) else Color.White
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                if (isBoySelected) 2.5.dp else 1.dp,
                                if (isBoySelected) Color(0xFF1E88E5) else Color(0xFFE0E0E0)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                KidCharacterAvatar(
                                    gender = "BOY",
                                    nickname = if (isBoySelected) nickname else "Kabir",
                                    sizeDp = 64,
                                    showBadge = false
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Boy 👦",
                                    fontWeight = FontWeight.Bold,
                                    color = if (isBoySelected) Color(0xFF1565C0) else Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        // Girl Character Card
                        val isGirlSelected = gender.equals("GIRL", ignoreCase = true)
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { gender = "GIRL" }
                                .testTag("select_girl_card"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isGirlSelected) Color(0xFFFCE4EC) else Color.White
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                if (isGirlSelected) 2.5.dp else 1.dp,
                                if (isGirlSelected) Color(0xFFE91E63) else Color(0xFFE0E0E0)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                KidCharacterAvatar(
                                    gender = "GIRL",
                                    nickname = if (isGirlSelected) nickname else "Riya",
                                    sizeDp = 64,
                                    showBadge = false
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Girl 👧",
                                    fontWeight = FontWeight.Bold,
                                    color = if (isGirlSelected) Color(0xFFC2185B) else Color.Gray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("child_name_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        label = { Text("Student Name") },
                        placeholder = { Text("e.g. Riya, Aarav, Kabir") }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = nickname,
                        onValueChange = { nickname = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("character_nickname_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        label = { Text("Hero Nickname") },
                        placeholder = { Text("e.g. Super Star, Math Ninja, Champ") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save & Start Button
            Button(
                onClick = {
                    val langCode = when (selectedLanguage) {
                        "Hindi" -> "hi"
                        "Hinglish" -> "hinglish"
                        else -> "en"
                    }
                    val finalGoals = selectedGoals.joinToString(", ")
                    onSaveProfile(
                        name.trim().ifEmpty { "Riya" },
                        gender,
                        nickname.trim().ifEmpty { "Super Star" },
                        langCode,
                        selectedBoard,
                        selectedClass,
                        finalGoals,
                        selectedUserType
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("save_character_and_start_btn"),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LearnzoMint)
            ) {
                Text(
                    text = "Start Learning Now 🚀",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
