package com.mbdev.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.mbdev.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var hasAnsweredAllQuestions = false

    private var questionBank = mutableListOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, true),
        Question(R.string.question_africa, true),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true),
    )
    private var currentIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnTrue.setOnClickListener {
            checkAnswer(true)
        }

        binding.btnFalse.setOnClickListener {
            checkAnswer(false)
        }

        binding.btnNext.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
        updateQuestion()

    }

    override fun onStop() {
        super.onStop()
        resetGame()
    }

    private fun updateQuestion() {
        if (hasAnsweredAllQuestions) {
            val messageResId = R.string.game_over
            Snackbar.make(binding.llMain, messageResId, Snackbar.LENGTH_LONG).show()
            resetGame()
        }

        if (questionBank[currentIndex].wasPreviouslyAsked) {
            binding.btnTrue.isEnabled = false
            binding.btnFalse.isEnabled = false
            val questionTextResId = questionBank[currentIndex].textResId
            binding.tvQuestion.setText(questionTextResId)
        } else {
            binding.btnTrue.isEnabled = true
            binding.btnFalse.isEnabled = true
            val questionTextResId = questionBank[currentIndex].textResId
            binding.tvQuestion.setText(questionTextResId)
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        questionBank[currentIndex].wasPreviouslyAsked = true
        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_snackbar
        } else {
            R.string.incorrect_snackbar
        }

        Snackbar.make(binding.llMain, messageResId, Snackbar.LENGTH_SHORT).show()
    }

    private fun resetGame() {
        for (question in questionBank) {
            question.wasPreviouslyAsked = false
        }
    }
}