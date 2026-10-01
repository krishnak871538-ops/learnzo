package com.example.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.LearnzoGold
import com.example.ui.theme.LearnzoIndigo
import com.example.ui.theme.LearnzoMint

@Composable
fun KidCharacterAvatar(
    gender: String, // "BOY" or "GIRL"
    nickname: String,
    modifier: Modifier = Modifier,
    sizeDp: Int = 80,
    showBadge: Boolean = true,
    stars: Int = 0
) {
    val infiniteTransition = rememberInfiniteTransition(label = "avatar_bounce")
    val bounceOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Column(
        modifier = modifier.offset(y = bounceOffset.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            // Glow aura
            Box(
                modifier = Modifier
                    .size((sizeDp + 12).dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            listOf(LearnzoGold.copy(alpha = 0.35f), Color.Transparent)
                        )
                    )
            )

            // Outer Circle Border with Uniform Accent
            val isBoy = gender.equals("BOY", ignoreCase = true)
            val baseColor = if (isBoy) Color(0xFF1E88E5) else Color(0xFFE91E63)

            Box(
                modifier = Modifier
                    .size(sizeDp.dp)
                    .shadow(8.dp, CircleShape)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFFFFF9C4), Color(0xFFFFECB3))
                        )
                    )
                    .border(3.dp, baseColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // School student face emoji representation
                    Text(
                        text = if (isBoy) "👦🎒" else "👧🎒",
                        fontSize = (sizeDp * 0.42).sp
                    )
                    // Uniform tie / collar indicator
                    Text(
                        text = "👔",
                        fontSize = (sizeDp * 0.22).sp,
                        modifier = Modifier.offset(y = (-4).dp)
                    )
                }
            }

            // Star or Level Badge on Corner
            if (showBadge && stars > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .offset(x = 4.dp, y = 4.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(LearnzoGold)
                        .border(1.5.dp, Color.White, RoundedCornerShape(12.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "$stars",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        if (nickname.isNotBlank()) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White.copy(alpha = 0.9f))
                    .border(1.dp, LearnzoIndigo.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = nickname,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = LearnzoIndigo,
                    maxLines = 1
                )
            }
        }
    }
}
