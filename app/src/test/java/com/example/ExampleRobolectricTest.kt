package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.model.RewardPool
import com.example.model.SubjectCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Learnzo", appName)
  }

  @Test
  fun `verify subject catalog contains all 6 core subjects`() {
    assertEquals(6, SubjectCatalog.subjects.size)
    assertNotNull(SubjectCatalog.getById("maths"))
    assertNotNull(SubjectCatalog.getById("hindi"))
    assertNotNull(SubjectCatalog.getById("english"))
    assertNotNull(SubjectCatalog.getById("science"))
    assertNotNull(SubjectCatalog.getById("gk"))
    assertNotNull(SubjectCatalog.getById("computer"))
  }

  @Test
  fun `verify random surprise reward generation`() {
    val reward = RewardPool.getRandomReward()
    assertNotNull(reward)
    assertTrue(reward.titleHi.isNotBlank())
    assertTrue(reward.value > 0)
  }

  @Test
  fun `verify dynamic question generation for unlimited practice`() {
    val q1 = com.example.model.DynamicQuestionGenerator.generateQuestion(
      subjectId = "maths",
      currentDifficulty = com.example.model.Difficulty.EASY,
      board = "CBSE",
      studentClass = "Class 5"
    )
    assertNotNull(q1)
    assertTrue(q1.questionEn.isNotBlank())
    assertTrue(q1.optionsEn.isNotEmpty())
    assertTrue(q1.correctIndex >= 0)

    val q2 = com.example.model.DynamicQuestionGenerator.generateQuestion(
      subjectId = "science",
      currentDifficulty = com.example.model.Difficulty.MEDIUM,
      board = "ICSE",
      studentClass = "Class 7"
    )
    assertNotNull(q2)
    assertTrue(q2.questionEn.isNotBlank())
    assertTrue(q2.optionsEn.isNotEmpty())
  }
}
