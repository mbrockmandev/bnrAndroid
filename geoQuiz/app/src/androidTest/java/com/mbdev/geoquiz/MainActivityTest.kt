package com.mbdev.geoquiz

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.*
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.action.ViewActions.click
import androidx.test.core.app.ActivityScenario.launch
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private lateinit var scenario: ActivityScenario<MainActivity>
    @Before
    fun setUp() {
        scenario = launch(MainActivity::class.java)
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun showsFirstQuestionOnLaunch() {
        onView(withId(R.id.tvQuestion)) // view matcher
            .check(matches(withText(R.string.question_australia))) // view assertion
    }

    @Test
    fun showsSecondQuestionAfterNextPressed() {
        onView(withId(R.id.btnNext)).perform(click())
        onView(withId(R.id.tvQuestion))
            .check(matches(withText(R.string.question_oceans)))
    }

    @Test
    fun handlesActivityRecreation() {
        onView(withId(R.id.btnNext)).perform(click())
        scenario.recreate()
        onView(withId(R.id.tvQuestion))
            .check(matches(withText(R.string.question_oceans)))
    }

    @Test
    fun handlesCheaterStatusAfterActivityRecreation() {
        val savedStateHandle = SavedStateHandle(mapOf(IS_CHEATER_KEY to true))
        var quizViewModel = QuizViewModel(savedStateHandle)
        assertEquals(quizViewModel.isCheater, true)
        scenario.recreate()
        assertEquals(quizViewModel.isCheater, true)
    }
}