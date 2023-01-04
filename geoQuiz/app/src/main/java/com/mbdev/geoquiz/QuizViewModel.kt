package com.mbdev.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    var currentIndex: Int
        get() = savedStateHandle[CURRENT_INDEX_KEY] ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    var score = 0
    var numQuestionsAsked = 0
    var hasAnsweredAllQuestions: Boolean = false

    var questionBank = mutableListOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, true),
        Question(R.string.question_africa, true),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
    )

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer
    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNextQuestion() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun resetGame() {
        for (question in questionBank) {
            question.wasPreviouslyAsked = false
        }
        numQuestionsAsked = 0
        currentIndex = 0
        score = 0
        hasAnsweredAllQuestions = false
    }

}