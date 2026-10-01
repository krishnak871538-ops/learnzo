package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.UserProfile
import com.example.ui.theme.LearnzoAmber
import com.example.ui.theme.LearnzoGold
import com.example.ui.theme.LearnzoIndigo
import com.example.ui.theme.LearnzoMint
import com.example.util.AppStrings

data class SafeLeaderboardItem(
    val nickname: String,
    val avatarEmoji: String,
    val stars: Int,
    val streak: Int,
    val badge: String,
    val isUser: Boolean = false
)

@Composable
fun WeeklyChallengeScreen(
    profile: UserProfile,
    isHindi: Boolean,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val leaderboard = listOf(
        SafeLeaderboardItem("Aarav (Space Explorer 🚀)", "👦", 38, 7, "Galaxy Champ 🌌"),
        SafeLeaderboardItem("${profile.characterNickname} (${profile.childName})", if (profile.characterGender == "BOY") "👦" else "👧", profile.stars, profile.streakDays, "Rising Star 🌟", isUser = true),
        SafeLeaderboardItem("Ananya (Math Ninja 🥷)", "👧", 22, 4, "Number Wizard 🧙‍♀️"),
        SafeLeaderboardItem("Vihaan (Curious Panda 🐼)", "👦", 19, 3, "Science Explorer 🧪"),
        SafeLeaderboardItem("Diya (Word Wizard 📚)", "👧", 16, 2, "Hindi Ratna 🏅"),
        SafeLeaderboardItem("Reyansh (Coding Champ ⚡)", "👦", 14, 2, "Tech Whiz 💻")
    ).sortedByDescending { it.stars }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFF))
            .padding(16.dp)
            .testTag("weekly_challenge_screen")
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
                    .testTag("leaderboard_back_btn")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = LearnzoIndigo
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = AppStrings.weeklyChallengeTitle(isHindi),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = LearnzoIndigo
                )
                Text(
                    text = AppStrings.weeklyChallengeSub(isHindi),
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }

            Box(modifier = Modifier.size(42.dp))
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Privacy Guarantee Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
            border = androidx.compose.foundation.BorderStroke(1.dp, LearnzoMint)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = "🛡️", fontSize = 24.sp)
                Text(
                    text = if (isHindi)
                        "100% सुरक्षित: यहाँ केवल उपनाम (Nicknames) और Stars दिखते हैं। कोई भी व्यक्तिगत जानकारी साझा नहीं होती।"
                    else
                        "100% Kid-Safe: Only playful nicknames & stars are visible. No personal data is ever shared.",
                    fontSize = 12.sp,
                    color = Color(0xFF1B5E20),
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Leaderboard List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(leaderboard) { index, item ->
                val rank = index + 1
                val rankColor = when (rank) {
                    1 -> Color(0xFFFFD700) // Gold
                    2 -> Color(0xFFC0C0C0) // Silver
                    3 -> Color(0xFFCD7F32) // Bronze
                    else -> Color.Transparent
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (item.isUser) Color(0xFFEDE7F6) else Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        if (item.isUser) 2.dp else 1.dp,
                        if (item.isUser) LearnzoIndigo else Color(0xFFEEEEEE)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = if (item.isUser) 4.dp else 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Rank
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(if (rank <= 3) rankColor else Color(0xFFF0F0F0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (rank <= 3) "🏅 $rank" else "$rank",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = if (rank <= 3) Color.Black else Color.Gray
                            )
                        }

                        // Avatar
                        Text(text = item.avatarEmoji, fontSize = 28.sp)

                        // Name & Badge
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = item.nickname,
                                fontWeight = if (item.isUser) FontWeight.ExtraBold else FontWeight.SemiBold,
                                color = if (item.isUser) LearnzoIndigo else Color.Black,
                                fontSize = 14.sp
                            )
                            Text(
                                text = item.badge,
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        // Stars & Streak
                        Column(horizontalAlignment = Alignment.End) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = LearnzoAmber, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.size(2.dp))
                                Text(
                                    text = "${item.stars}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }
                            Text(
                                text = "🔥 ${item.streak}d",
                                fontSize = 11.sp,
                                color = Color(0xFFFF6D00),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}
