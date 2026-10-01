package com.example.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.util.Base64
import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

sealed class AiResult<out T> {
    data class Success<out T>(val data: T) : AiResult<T>()
    data class Error(val message: String) : AiResult<Nothing>()
    data class Loading(val progressMessage: String) : AiResult<Nothing>()
}

data class GeneratedMusic(
    val title: String,
    val modelUsed: String,
    val audioFile: File?,
    val durationSeconds: Int,
    val prompt: String
)

data class GeneratedImage(
    val prompt: String,
    val bitmap: Bitmap,
    val base64: String,
    val isEdited: Boolean = false
)

data class GeneratedVideo(
    val prompt: String,
    val videoUri: String?,
    val videoFile: File?,
    val aspectRatio: String, // "16:9" or "9:16"
    val isFromImage: Boolean = false
)

class GeminiAiService(private val context: Context) {

    private val client = OkHttpClient.Builder()
        .connectTimeout(90, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .writeTimeout(90, TimeUnit.SECONDS)
        .build()

    private val apiKey: String
        get() = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

    fun hasApiKey(): Boolean {
        val key = apiKey
        return key.isNotBlank() && key != "MY_GEMINI_API_KEY"
    }

    /**
     * 1. Music Generation using Lyria
     * Models: "lyria-3-clip-preview" (short clips up to 30s) or "lyria-3-pro-preview" (full-length tracks)
     */
    suspend fun generateMusic(
        prompt: String,
        modelName: String = "lyria-3-clip-preview"
    ): AiResult<GeneratedMusic> = withContext(Dispatchers.IO) {
        val chosenModel = if (modelName == "lyria-3-pro-preview") "lyria-3-pro-preview" else "lyria-3-clip-preview"
        val key = apiKey

        if (!hasApiKey()) {
            return@withContext AiResult.Error("Gemini API key is required. Please add your key in the AI Studio Secrets panel.")
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$chosenModel:generateContent?key=$key"

            val jsonBody = JSONObject().apply {
                val contents = JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", prompt))
                        })
                    })
                }
                put("contents", contents)

                val genConfig = JSONObject().apply {
                    put("responseModalities", JSONArray().apply {
                        put("AUDIO")
                    })
                }
                put("generationConfig", genConfig)
            }

            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                val errorMsg = try {
                    JSONObject(responseBody).getJSONObject("error").getString("message")
                } catch (e: Exception) {
                    "HTTP ${response.code}: $responseBody"
                }
                return@withContext AiResult.Error(errorMsg)
            }

            val respJson = JSONObject(responseBody)
            val candidates = respJson.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")

            var audioBase64: String? = null
            var mimeType = "audio/mp3"

            if (parts != null) {
                for (i in 0 until parts.length()) {
                    val part = parts.getJSONObject(i)
                    if (part.has("inlineData")) {
                        val inline = part.getJSONObject("inlineData")
                        audioBase64 = inline.optString("data")
                        mimeType = inline.optString("mimeType", "audio/mp3")
                        break
                    }
                }
            }

            if (audioBase64.isNullOrBlank()) {
                return@withContext AiResult.Error("No audio received from $chosenModel")
            }

            val audioBytes = Base64.decode(audioBase64, Base64.DEFAULT)
            val ext = if (mimeType.contains("wav")) "wav" else "mp3"
            val file = File(context.cacheDir, "music_${System.currentTimeMillis()}.$ext")
            FileOutputStream(file).use { it.write(audioBytes) }

            val duration = if (chosenModel == "lyria-3-clip-preview") 30 else 90

            AiResult.Success(
                GeneratedMusic(
                    title = prompt.take(30),
                    modelUsed = chosenModel,
                    audioFile = file,
                    durationSeconds = duration,
                    prompt = prompt
                )
            )
        } catch (e: Exception) {
            Log.e("GeminiAiService", "Music generation failed", e)
            AiResult.Error(e.message ?: "Music generation failed")
        }
    }

    /**
     * 2. Create & Edit Images using gemini-3.1-flash-image-preview
     */
    suspend fun generateOrEditImage(
        prompt: String,
        sourceBitmap: Bitmap? = null,
        aspectRatio: String = "1:1"
    ): AiResult<GeneratedImage> = withContext(Dispatchers.IO) {
        val modelName = "gemini-3.1-flash-image-preview"
        val key = apiKey

        if (!hasApiKey()) {
            return@withContext AiResult.Error("Gemini API key is required. Please configure it in the Secrets panel.")
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$key"

            val jsonBody = JSONObject().apply {
                val partsArray = JSONArray()

                // If editing existing image, send base64 inlineData first
                if (sourceBitmap != null) {
                    val outputStream = ByteArrayOutputStream()
                    sourceBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                    val base64Img = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)

                    partsArray.put(JSONObject().apply {
                        put("inlineData", JSONObject().apply {
                            put("mimeType", "image/jpeg")
                            put("data", base64Img)
                        })
                    })
                }

                // Add text prompt
                partsArray.put(JSONObject().apply {
                    put("text", prompt)
                })

                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", partsArray)
                    })
                })

                val genConfig = JSONObject().apply {
                    put("responseModalities", JSONArray().apply {
                        put("TEXT")
                        put("IMAGE")
                    })
                    put("imageConfig", JSONObject().apply {
                        put("aspectRatio", aspectRatio)
                        put("imageSize", "1K")
                    })
                }
                put("generationConfig", genConfig)
            }

            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                val errorMsg = try {
                    JSONObject(responseBody).getJSONObject("error").getString("message")
                } catch (e: Exception) {
                    "HTTP ${response.code}: $responseBody"
                }
                return@withContext AiResult.Error(errorMsg)
            }

            val respJson = JSONObject(responseBody)
            val candidates = respJson.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")

            var imageBase64: String? = null
            if (parts != null) {
                for (i in 0 until parts.length()) {
                    val part = parts.getJSONObject(i)
                    if (part.has("inlineData")) {
                        val inline = part.getJSONObject("inlineData")
                        val data = inline.optString("data")
                        if (data.isNotBlank()) {
                            imageBase64 = data
                            break
                        }
                    }
                }
            }

            if (imageBase64.isNullOrBlank()) {
                return@withContext AiResult.Error("No image returned from $modelName")
            }

            val imageBytes = Base64.decode(imageBase64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                ?: return@withContext AiResult.Error("Failed to decode image data")

            AiResult.Success(
                GeneratedImage(
                    prompt = prompt,
                    bitmap = bitmap,
                    base64 = imageBase64,
                    isEdited = sourceBitmap != null
                )
            )
        } catch (e: Exception) {
            Log.e("GeminiAiService", "Image generation/edit failed", e)
            AiResult.Error(e.message ?: "Image generation failed")
        }
    }

    /**
     * 3 & 4. Veo Video Generation:
     * - Generate video from text (aspectRatio: "16:9" or "9:16")
     * - Animate image into video (aspectRatio: "16:9" or "9:16")
     * Model: veo-3.1-fast-generate-preview
     */
    suspend fun generateVeoVideo(
        prompt: String,
        sourceImage: Bitmap? = null,
        aspectRatio: String = "16:9", // "16:9" or "9:16"
        onProgress: (String) -> Unit
    ): AiResult<GeneratedVideo> = withContext(Dispatchers.IO) {
        val model = "veo-3.1-fast-generate-preview"
        val key = apiKey

        if (!hasApiKey()) {
            return@withContext AiResult.Error("Gemini API key is required. Please configure it in the Secrets panel.")
        }

        try {
            onProgress("Initiating Veo 3.1 video generation request...")
            val url = "https://generativelanguage.googleapis.com/v1beta/models/$model:generateVideos?key=$key"

            val jsonBody = JSONObject().apply {
                put("prompt", prompt)

                if (sourceImage != null) {
                    val outputStream = ByteArrayOutputStream()
                    sourceImage.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
                    val base64Img = Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
                    put("image", JSONObject().apply {
                        put("imageBytes", base64Img)
                        put("mimeType", "image/jpeg")
                    })
                }

                put("config", JSONObject().apply {
                    put("numberOfVideos", 1)
                    put("resolution", "720p")
                    put("aspectRatio", aspectRatio)
                })
            }

            val request = Request.Builder()
                .url(url)
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string().orEmpty()

            if (!response.isSuccessful) {
                val errorMsg = try {
                    JSONObject(responseBody).getJSONObject("error").getString("message")
                } catch (e: Exception) {
                    "HTTP ${response.code}: $responseBody"
                }
                return@withContext AiResult.Error(errorMsg)
            }

            val initJson = JSONObject(responseBody)
            val operationName = initJson.optString("name")

            if (operationName.isBlank()) {
                // Check if video is returned directly
                val directVideos = initJson.optJSONArray("generatedVideos")
                if (directVideos != null && directVideos.length() > 0) {
                    val vidObj = directVideos.getJSONObject(0).optJSONObject("video")
                    val uri = vidObj?.optString("uri")
                    return@withContext AiResult.Success(
                        GeneratedVideo(
                            prompt = prompt,
                            videoUri = uri,
                            videoFile = null,
                            aspectRatio = aspectRatio,
                            isFromImage = sourceImage != null
                        )
                    )
                }
                return@withContext AiResult.Error("No operation ID or video returned")
            }

            // Poll the long-running operation
            var isDone = false
            var attempts = 0
            val maxAttempts = 30 // ~60 seconds polling

            var finalVideoUri: String? = null
            var finalVideoBytes: ByteArray? = null

            while (!isDone && attempts < maxAttempts) {
                attempts++
                delay(2000)
                onProgress("Rendering video with Veo 3.1 ($attempts/$maxAttempts)...")

                val pollUrl = "https://generativelanguage.googleapis.com/v1beta/$operationName?key=$key"
                val pollReq = Request.Builder().url(pollUrl).get().build()
                val pollResp = client.newCall(pollReq).execute()
                val pollBody = pollResp.body?.string().orEmpty()

                if (pollResp.isSuccessful && pollBody.isNotBlank()) {
                    val pollJson = JSONObject(pollBody)
                    isDone = pollJson.optBoolean("done", false)

                    if (isDone) {
                        if (pollJson.has("error")) {
                            val err = pollJson.getJSONObject("error").optString("message", "Veo generation failed")
                            return@withContext AiResult.Error(err)
                        }

                        val resp = pollJson.optJSONObject("response")
                        val genVideos = resp?.optJSONArray("generatedVideos")
                        if (genVideos != null && genVideos.length() > 0) {
                            val vid = genVideos.getJSONObject(0).optJSONObject("video")
                            finalVideoUri = vid?.optString("uri")
                            val base64Bytes = vid?.optString("videoBytes")
                            if (!base64Bytes.isNullOrBlank()) {
                                finalVideoBytes = Base64.decode(base64Bytes, Base64.DEFAULT)
                            }
                        }
                        break
                    }
                }
            }

            var videoFile: File? = null
            if (finalVideoBytes != null) {
                val f = File(context.cacheDir, "veo_${System.currentTimeMillis()}.mp4")
                FileOutputStream(f).use { it.write(finalVideoBytes) }
                videoFile = f
            }

            AiResult.Success(
                GeneratedVideo(
                    prompt = prompt,
                    videoUri = finalVideoUri,
                    videoFile = videoFile,
                    aspectRatio = aspectRatio,
                    isFromImage = sourceImage != null
                )
            )
        } catch (e: Exception) {
            Log.e("GeminiAiService", "Veo video generation failed", e)
            AiResult.Error(e.message ?: "Video generation failed")
        }
    }
}
