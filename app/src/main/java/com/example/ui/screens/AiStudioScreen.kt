package com.example.ui.screens

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.service.AiResult
import com.example.service.GeminiAiService
import com.example.service.GeneratedImage
import com.example.service.GeneratedMusic
import com.example.service.GeneratedVideo
import com.example.ui.theme.LearnzoAmber
import com.example.ui.theme.LearnzoGold
import com.example.ui.theme.LearnzoIndigo
import com.example.ui.theme.LearnzoMint
import kotlinx.coroutines.launch
import java.io.InputStream

enum class AiStudioTab(val titleHi: String, val titleEn: String, val icon: String) {
    MUSIC("🎵 म्यूज़िक", "🎵 Music (Lyria)", "🎵"),
    IMAGE("🎨 चित्र बनाएँ", "🎨 Image (Gemini 3.1)", "🎨"),
    ANIMATE_IMAGE("🎬 फ़ोटो से वीडियो", "🎬 Animate (Veo)", "🎬"),
    TEXT_VIDEO("📽️ टेक्स्ट से वीडियो", "📽️ Video (Veo)", "📽️")
}

@Composable
fun AiStudioScreen(
    aiService: GeminiAiService,
    isHindi: Boolean,
    onBack: () -> Unit
) {
    BackHandler { onBack() }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var selectedTab by remember { mutableStateOf(AiStudioTab.MUSIC) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFF))
            .testTag("ai_studio_screen_root")
    ) {
        // Top Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF4A148C), Color(0xFF6A1B9A))
                    )
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f))
                            .testTag("ai_studio_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isHindi) "✨ Learnzo जादुई AI लैब" else "✨ Learnzo AI Magic Lab",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }
                        Text(
                            text = if (isHindi) "Lyria 🎵 • Gemini 3.1 🎨 • Veo 3 🎬" else "Lyria 🎵 • Gemini 3.1 🎨 • Veo 3 🎬",
                            fontSize = 11.sp,
                            color = Color(0xFFFFD54F)
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFB300))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(text = "⚡ Pro AI", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                }
            }
        }

        // Tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab.ordinal,
            containerColor = Color.White,
            contentColor = LearnzoIndigo,
            edgePadding = 12.dp
        ) {
            AiStudioTab.entries.forEach { tab ->
                Tab(
                    selected = selectedTab == tab,
                    onClick = { selectedTab = tab },
                    text = {
                        Text(
                            text = if (isHindi) tab.titleHi else tab.titleEn,
                            fontWeight = if (selectedTab == tab) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    },
                    modifier = Modifier.testTag("tab_${tab.name}")
                )
            }
        }

        // Tab Content
        Box(modifier = Modifier.weight(1f)) {
            when (selectedTab) {
                AiStudioTab.MUSIC -> MusicGenerationSection(aiService, isHindi)
                AiStudioTab.IMAGE -> ImageCreationSection(aiService, isHindi)
                AiStudioTab.ANIMATE_IMAGE -> AnimateImageSection(aiService, isHindi)
                AiStudioTab.TEXT_VIDEO -> TextToVideoSection(aiService, isHindi)
            }
        }
    }
}

// -------------------------------------------------------------
// 1. Music Generation Section (Lyria)
// -------------------------------------------------------------
@Composable
fun MusicGenerationSection(aiService: GeminiAiService, isHindi: Boolean) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var prompt by remember { mutableStateOf("Cheerful kindergarten alphabet rhyme with acoustic guitar and happy flute") }
    var selectedModel by remember { mutableStateOf("lyria-3-clip-preview") } // or lyria-3-pro-preview
    var isLoading by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf("") }
    var resultMusic by remember { mutableStateOf<GeneratedMusic?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var isPlaying by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    val presets = listOf(
        "Cheerful alphabet counting rhyme with ukulele 🎶",
        "Upbeat multiplication tables song with clap beat 🧮",
        "Peaceful ambient study music for focus & reading 📚",
        "Fun celebration fanfare with trumpets & bells 🎺"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("music_section")
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = "🎵", fontSize = 28.sp)
                Column {
                    Text(
                        text = if (isHindi) "Lyria म्यूज़िक जनरेटर" else "Lyria Music Generation",
                        fontWeight = FontWeight.Bold,
                        color = LearnzoIndigo,
                        fontSize = 15.sp
                    )
                    Text(
                        text = if (isHindi)
                            "छोटा 30s क्लिप (lyria-3-clip-preview) या पूरा गाना (lyria-3-pro-preview) बनाएँ"
                        else
                            "Generate short clips (lyria-3-clip-preview) or full tracks (lyria-3-pro-preview)",
                        fontSize = 12.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Model Selector
        Text(
            text = if (isHindi) "मॉडल चुनें:" else "Select Model:",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = LearnzoIndigo
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedModel == "lyria-3-clip-preview",
                onClick = { selectedModel = "lyria-3-clip-preview" },
                label = { Text("lyria-3-clip-preview (≤30s Clip)") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = LearnzoIndigo,
                    selectedLabelColor = Color.White
                ),
                modifier = Modifier.testTag("model_lyria_clip")
            )
            FilterChip(
                selected = selectedModel == "lyria-3-pro-preview",
                onClick = { selectedModel = "lyria-3-pro-preview" },
                label = { Text("lyria-3-pro-preview (Full Track)") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = LearnzoIndigo,
                    selectedLabelColor = Color.White
                ),
                modifier = Modifier.testTag("model_lyria_pro")
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = if (isHindi) "पसंदीदा थीम चुनें या टाइप करें:" else "Quick Themes or Custom Prompt:",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        presets.forEach { preset ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(10.dp))
                    .clickable { prompt = preset.replace(Regex("[^a-zA-Z0-9 ]"), "") }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(text = preset, fontSize = 12.sp, color = Color(0xFF333333))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            label = { Text(if (isHindi) "म्यूजिक का विवरण (Prompt)" else "Music Description Prompt") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("music_prompt_input"),
            shape = RoundedCornerShape(14.dp),
            minLines = 2
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = {
                if (prompt.isBlank()) return@Button
                isLoading = true
                errorMessage = null
                statusMessage = if (isHindi) "Lyria AI से संगीत बनाया जा रहा है..." else "Generating music with $selectedModel..."

                scope.launch {
                    val result = aiService.generateMusic(prompt, selectedModel)
                    isLoading = false
                    when (result) {
                        is AiResult.Success -> {
                            resultMusic = result.data
                            mediaPlayer?.release()
                            if (result.data.audioFile != null && result.data.audioFile.exists()) {
                                mediaPlayer = MediaPlayer().apply {
                                    setDataSource(result.data.audioFile.absolutePath)
                                    prepare()
                                    setOnCompletionListener { isPlaying = false }
                                }
                            }
                        }
                        is AiResult.Error -> {
                            errorMessage = result.message
                        }
                        else -> Unit
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("generate_music_btn"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LearnzoIndigo)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = statusMessage, fontSize = 14.sp)
            } else {
                Icon(imageVector = Icons.Default.MusicNote, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isHindi) "संगीत बनाएँ (Generate Music)" else "Generate Music Track",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "⚠️ $errorMessage",
                color = Color(0xFFFF5252),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        // Music Playback Card
        resultMusic?.let { music ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, LearnzoMint),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "🎉 " + (if (isHindi) "नया ट्रैक तैयार!" else "Music Generated!"),
                                fontWeight = FontWeight.Bold,
                                color = LearnzoMint,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Model: ${music.modelUsed} • ~${music.durationSeconds}s",
                                fontSize = 11.sp,
                                color = Color.Gray
                            )
                        }

                        IconButton(
                            onClick = {
                                if (mediaPlayer != null) {
                                    if (isPlaying) {
                                        mediaPlayer?.pause()
                                        isPlaying = false
                                    } else {
                                        mediaPlayer?.start()
                                        isPlaying = true
                                    }
                                }
                            },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(LearnzoMint)
                                .testTag("play_pause_music_btn")
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = if (isPlaying) "Pause" else "Play",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"${music.prompt}\"",
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 2. Create & Edit Images Section (gemini-3.1-flash-image-preview)
// -------------------------------------------------------------
@Composable
fun ImageCreationSection(aiService: GeminiAiService, isHindi: Boolean) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var isEditMode by remember { mutableStateOf(false) }
    var prompt by remember { mutableStateOf("A friendly 3D cartoon boy with glasses studying with a glowing magic book and colorful stars") }
    var editInstruction by remember { mutableStateOf("Add a superhero cape and a bright golden star badge") }
    var selectedAspectRatio by remember { mutableStateOf("1:1") }
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var isLoading by remember { mutableStateOf(false) }
    var resultImage by remember { mutableStateOf<GeneratedImage?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Image Picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                selectedBitmap = BitmapFactory.decodeStream(inputStream)
                isEditMode = true
            } catch (e: Exception) {
                errorMessage = "Failed to load image: ${e.message}"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("image_section")
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EAF6))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = "🎨", fontSize = 28.sp)
                Column {
                    Text(
                        text = if (isHindi) "Gemini 3.1 फ़्लैश इमेज स्टूडियो" else "Gemini 3.1 Flash Image Studio",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF283593),
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Model: gemini-3.1-flash-image-preview • Create & Edit Images",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Mode Switch: Create New vs Edit Existing
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = !isEditMode,
                onClick = { isEditMode = false },
                label = { Text(if (isHindi) "✨ नया चित्र बनाएँ" else "✨ Create Image") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = LearnzoIndigo, selectedLabelColor = Color.White),
                modifier = Modifier.testTag("mode_create_image")
            )
            FilterChip(
                selected = isEditMode,
                onClick = { isEditMode = true },
                label = { Text(if (isHindi) "✏️ फ़ोटो एडिट करें" else "✏️ Edit Image") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = LearnzoIndigo, selectedLabelColor = Color.White),
                modifier = Modifier.testTag("mode_edit_image")
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (isEditMode) {
            // Edit Mode UI
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = if (isHindi) "एडिट करने के लिए चित्र चुनें:" else "Select Image to Edit:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        if (selectedBitmap != null) {
                            Image(
                                bitmap = selectedBitmap!!.asImageBitmap(),
                                contentDescription = "Source to edit",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .border(2.dp, LearnzoIndigo, RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.img_mascot),
                                contentDescription = "Default mascot",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Button(
                                onClick = { imagePickerLauncher.launch("image/*") },
                                colors = ButtonDefaults.buttonColors(containerColor = LearnzoIndigo),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.testTag("pick_image_to_edit_btn")
                            ) {
                                Text(text = if (isHindi) "फ़ोन से चुनें 📁" else "Choose Photo 📁", fontSize = 12.sp)
                            }
                            Button(
                                onClick = {
                                    val bmp = BitmapFactory.decodeResource(context.resources, R.drawable.img_mascot)
                                    selectedBitmap = bmp
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7E57C2)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(text = if (isHindi) "स्कूल मैस्कॉट लें 👦" else "Use Mascot 👦", fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = editInstruction,
                        onValueChange = { editInstruction = it },
                        label = { Text(if (isHindi) "एडिट निर्देश (Edit Prompt)" else "Edit Instructions Prompt") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("edit_image_prompt_input"),
                        shape = RoundedCornerShape(12.dp),
                        minLines = 2
                    )
                }
            }
        } else {
            // Create Mode UI
            Text(
                text = if (isHindi) "आस्पेक्ट रेशियो (Aspect Ratio):" else "Aspect Ratio:",
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("1:1", "9:16", "16:9").forEach { ratio ->
                    FilterChip(
                        selected = selectedAspectRatio == ratio,
                        onClick = { selectedAspectRatio = ratio },
                        label = { Text(ratio) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = LearnzoIndigo, selectedLabelColor = Color.White)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = prompt,
                onValueChange = { prompt = it },
                label = { Text(if (isHindi) "इमेज प्रॉम्प्ट (Text Prompt)" else "Image Text Prompt") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("create_image_prompt_input"),
                shape = RoundedCornerShape(14.dp),
                minLines = 2
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = {
                val p = if (isEditMode) editInstruction else prompt
                if (p.isBlank()) return@Button

                isLoading = true
                errorMessage = null
                scope.launch {
                    val bmpToPass = if (isEditMode) {
                        selectedBitmap ?: BitmapFactory.decodeResource(context.resources, R.drawable.img_mascot)
                    } else null

                    val result = aiService.generateOrEditImage(
                        prompt = p,
                        sourceBitmap = bmpToPass,
                        aspectRatio = selectedAspectRatio
                    )
                    isLoading = false
                    when (result) {
                        is AiResult.Success -> {
                            resultImage = result.data
                        }
                        is AiResult.Error -> {
                            errorMessage = result.message
                        }
                        else -> Unit
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("generate_image_action_btn"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LearnzoIndigo)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = if (isHindi) "Gemini 3.1 इमेज तैयार कर रहा है..." else "Rendering image with Gemini 3.1...", fontSize = 14.sp)
            } else {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isEditMode)
                        (if (isHindi) "चित्र एडिट करें (Edit Image)" else "Edit Image with Gemini 3.1")
                    else
                        (if (isHindi) "चित्र बनाएँ (Generate Image)" else "Generate Image with Gemini 3.1"),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "⚠️ $errorMessage",
                color = Color(0xFFFF5252),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        // Image Result Card
        resultImage?.let { img ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, LearnzoMint),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (img.isEdited) "✨ इमेज सफलतापूर्वक एडिट हुई!" else "✨ नई इमेज तैयार हुई!",
                        fontWeight = FontWeight.Bold,
                        color = LearnzoMint,
                        fontSize = 15.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Image(
                        bitmap = img.bitmap.asImageBitmap(),
                        contentDescription = "Generated art",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(260.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"${img.prompt}\"",
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 3. Animate Images into Video Section (Veo 3.1)
// -------------------------------------------------------------
@Composable
fun AnimateImageSection(aiService: GeminiAiService, isHindi: Boolean) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var prompt by remember { mutableStateOf("The friendly school student cartoon waves hands cheerfully with golden sparkles glowing all around") }
    var selectedAspectRatio by remember { mutableStateOf("16:9") } // "16:9" or "9:16"
    var selectedBitmap by remember { mutableStateOf<Bitmap?>(null) }

    var isLoading by remember { mutableStateOf(false) }
    var progressMessage by remember { mutableStateOf("") }
    var resultVideo by remember { mutableStateOf<GeneratedVideo?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            try {
                val input: InputStream? = context.contentResolver.openInputStream(uri)
                selectedBitmap = BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                errorMessage = "Failed to load image: ${e.message}"
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("animate_image_section")
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2F1))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = "🎬", fontSize = 28.sp)
                Column {
                    Text(
                        text = if (isHindi) "फ़ोटो को वीडियो में एनिमेट करें" else "Animate Photo into Video",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF00695C),
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Model: veo-3.1-fast-generate-preview • 16:9 Landscape & 9:16 Portrait",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Step 1: Upload or Choose Image
        Text(
            text = if (isHindi) "1. एनिमेट करने के लिए फ़ोटो चुनें:" else "1. Choose Photo to Animate:",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (selectedBitmap != null) {
                Image(
                    bitmap = selectedBitmap!!.asImageBitmap(),
                    contentDescription = "Selected photo",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(2.dp, LearnzoMint, RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.img_mascot),
                    contentDescription = "Default mascot",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(
                    onClick = { imagePicker.launch("image/*") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("upload_photo_for_video_btn")
                ) {
                    Text(text = if (isHindi) "गैलरी से फ़ोटो लें 📸" else "Upload Photo 📸", fontSize = 12.sp)
                }

                Button(
                    onClick = {
                        val bmp = BitmapFactory.decodeResource(context.resources, R.drawable.img_mascot)
                        selectedBitmap = bmp
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = LearnzoIndigo),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(text = if (isHindi) "मैस्कॉट चुनें 👧" else "Use Mascot 👧", fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Step 2: Aspect Ratio (16:9 landscape or 9:16 portrait)
        Text(
            text = if (isHindi) "2. वीडियो का Aspect Ratio चुनें:" else "2. Select Video Aspect Ratio:",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilterChip(
                selected = selectedAspectRatio == "16:9",
                onClick = { selectedAspectRatio = "16:9" },
                label = { Text("16:9 (Landscape)") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF00897B), selectedLabelColor = Color.White),
                modifier = Modifier.testTag("veo_ratio_16_9")
            )
            FilterChip(
                selected = selectedAspectRatio == "9:16",
                onClick = { selectedAspectRatio = "9:16" },
                label = { Text("9:16 (Portrait)") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFF00897B), selectedLabelColor = Color.White),
                modifier = Modifier.testTag("veo_ratio_9_16")
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Step 3: Animation Prompt
        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            label = { Text(if (isHindi) "एनिमेशन का विवरण (Prompt)" else "Animation Movement Prompt") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("animate_image_prompt_input"),
            shape = RoundedCornerShape(14.dp),
            minLines = 2
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = {
                if (prompt.isBlank()) return@Button
                isLoading = true
                errorMessage = null
                progressMessage = if (isHindi) "Veo 3.1 वीडियो रेंडर कर रहा है..." else "Rendering video with Veo 3.1..."

                scope.launch {
                    val bmp = selectedBitmap ?: BitmapFactory.decodeResource(context.resources, R.drawable.img_mascot)
                    val result = aiService.generateVeoVideo(
                        prompt = prompt,
                        sourceImage = bmp,
                        aspectRatio = selectedAspectRatio,
                        onProgress = { msg -> progressMessage = msg }
                    )
                    isLoading = false
                    when (result) {
                        is AiResult.Success -> {
                            resultVideo = result.data
                        }
                        is AiResult.Error -> {
                            errorMessage = result.message
                        }
                        else -> Unit
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("animate_video_generate_btn"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00897B))
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = progressMessage, fontSize = 13.sp)
            } else {
                Icon(imageVector = Icons.Default.Movie, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isHindi) "Veo से वीडियो एनिमेट करें 🚀" else "Animate with Veo 3.1 🚀",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "⚠️ $errorMessage",
                color = Color(0xFFFF5252),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        // Video Result Card
        resultVideo?.let { vid ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, LearnzoMint),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎉 " + (if (isHindi) "Veo वीडियो तैयार!" else "Veo Video Generated!"),
                        fontWeight = FontWeight.Bold,
                        color = LearnzoMint,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Aspect Ratio: ${vid.aspectRatio} • Model: veo-3.1-fast-generate-preview",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Video Preview Frame
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(if (vid.aspectRatio == "16:9") 16f / 9f else 9f / 16f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedBitmap != null) {
                            Image(
                                bitmap = selectedBitmap!!.asImageBitmap(),
                                contentDescription = "Video preview frame",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.img_mascot),
                                contentDescription = "Video preview frame",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Play overlay
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(32.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"${vid.prompt}\"",
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// 4. Generate Video from Text Section (Veo 3.1)
// -------------------------------------------------------------
@Composable
fun TextToVideoSection(aiService: GeminiAiService, isHindi: Boolean) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var prompt by remember { mutableStateOf("A 3D cartoon rocket launching into deep space surrounded by glowing colorful stars and planets") }
    var selectedAspectRatio by remember { mutableStateOf("16:9") } // "16:9" or "9:16"

    var isLoading by remember { mutableStateOf(false) }
    var progressMessage by remember { mutableStateOf("") }
    var resultVideo by remember { mutableStateOf<GeneratedVideo?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val presets = listOf(
        "3D cartoon dinosaur teaching counting in prehistoric forest 🦖",
        "A cute school bus flying across rainbow clouds with happy kids 🚌",
        "A glowing cartoon astronaut landing on the moon with a waving flag 🌕",
        "A happy baby elephant splashing water in a sunny jungle pond 🐘"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("text_to_video_section")
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(text = "📽️", fontSize = 28.sp)
                Column {
                    Text(
                        text = if (isHindi) "टेक्स्ट से Veo 3.1 वीडियो बनाएँ" else "Text to Video with Veo 3.1",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFE65100),
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Model: veo-3.1-fast-generate-preview • Aspect ratio: 16:9 or 9:16",
                        fontSize = 11.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Aspect Ratio
        Text(
            text = if (isHindi) "वीडियो का Aspect Ratio चुनें:" else "Select Aspect Ratio:",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilterChip(
                selected = selectedAspectRatio == "16:9",
                onClick = { selectedAspectRatio = "16:9" },
                label = { Text("16:9 (Landscape)") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFE65100), selectedLabelColor = Color.White),
                modifier = Modifier.testTag("text_veo_ratio_16_9")
            )
            FilterChip(
                selected = selectedAspectRatio == "9:16",
                onClick = { selectedAspectRatio = "9:16" },
                label = { Text("9:16 (Portrait)") },
                colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Color(0xFFE65100), selectedLabelColor = Color.White),
                modifier = Modifier.testTag("text_veo_ratio_9_16")
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = if (isHindi) "पसंदीदा कहानी या दृश्य चुनें:" else "Popular Educational Scenes:",
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(6.dp))

        presets.forEach { preset ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.White)
                    .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(10.dp))
                    .clickable { prompt = preset.replace(Regex("[^a-zA-Z0-9 ]"), "") }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(text = preset, fontSize = 12.sp, color = Color(0xFF333333))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = prompt,
            onValueChange = { prompt = it },
            label = { Text(if (isHindi) "वीडियो का विवरण (Prompt)" else "Video Description Prompt") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("text_to_video_prompt_input"),
            shape = RoundedCornerShape(14.dp),
            minLines = 2
        )

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = {
                if (prompt.isBlank()) return@Button
                isLoading = true
                errorMessage = null
                progressMessage = if (isHindi) "Veo 3.1 वीडियो बना रहा है..." else "Generating video with Veo 3.1..."

                scope.launch {
                    val result = aiService.generateVeoVideo(
                        prompt = prompt,
                        sourceImage = null,
                        aspectRatio = selectedAspectRatio,
                        onProgress = { msg -> progressMessage = msg }
                    )
                    isLoading = false
                    when (result) {
                        is AiResult.Success -> {
                            resultVideo = result.data
                        }
                        is AiResult.Error -> {
                            errorMessage = result.message
                        }
                        else -> Unit
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("generate_text_video_btn"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100))
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = progressMessage, fontSize = 13.sp)
            } else {
                Icon(imageVector = Icons.Default.VideoLibrary, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (isHindi) "Veo 3.1 वीडियो बनाएँ 🚀" else "Generate Video with Veo 3.1 🚀",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "⚠️ $errorMessage",
                color = Color(0xFFFF5252),
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        // Video Result Card
        resultVideo?.let { vid ->
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, LearnzoMint),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎉 " + (if (isHindi) "Veo वीडियो सफलतापूर्वक बना!" else "Veo Video Generated Successfully!"),
                        fontWeight = FontWeight.Bold,
                        color = LearnzoMint,
                        fontSize = 15.sp
                    )
                    Text(
                        text = "Aspect Ratio: ${vid.aspectRatio} • Model: veo-3.1-fast-generate-preview",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Video Frame
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(if (vid.aspectRatio == "16:9") 16f / 9f else 9f / 16f)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_teachers),
                            contentDescription = "Video preview",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(Color.Black.copy(alpha = 0.6f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Play", tint = Color.White, modifier = Modifier.size(32.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "\"${vid.prompt}\"",
                        fontSize = 12.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
