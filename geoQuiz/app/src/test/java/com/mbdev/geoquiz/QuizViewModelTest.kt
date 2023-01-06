package com.mbdev.geoquiz

import androidx.lifecycle.SavedStateHandle
import org.junit.Assert.*
import org.junit.Test

class QuizViewModelTest {
    private lateinit var quizVM: QuizViewModel

    @Test
    fun providesExpectedQuestionText() {
        val savedStateHandle = SavedStateHandle()
        quizVM = QuizViewModel(savedStateHandle)
        assertEquals(R.string.question_australia, quizVM.currentQuestionText)
    }

    @Test
    fun wrapsAroundQuestionBank() {
        val savedStateHandle = SavedStateHandle(mapOf(CURRENT_INDEX_KEY to 5))
        quizVM = QuizViewModel(savedStateHandle)
        assertEquals(R.string.question_asia, quizVM.currentQuestionText)
        quizVM.moveToNextQuestion()
        assertEquals(R.string.question_australia, quizVM.currentQuestionText)

    }

    @Test
    fun checkSavedStateHandleSetProperlyForGivenKey() {
        val savedStateHandle = SavedStateHandle(mapOf(IS_CHEATER_KEY to true))
        quizVM = QuizViewModel(savedStateHandle)
        assertEquals(quizVM.isCheater, true)
    }
}