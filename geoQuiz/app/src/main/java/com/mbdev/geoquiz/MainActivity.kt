package com.mbdev.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.mbdev.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.btnTrue.setOnClickListener {
            checkAnswer(true)
        }

        binding.btnFalse.setOnClickListener {
            checkAnswer(false)
        }

        binding.btnNext.setOnClickListener {
            quizViewModel.moveToNextQuestion()
            updateQuestion()
        }
        updateQuestion()

    }

    override fun onStop() {
        super.onStop()
        quizViewModel.resetGame()
    }

    private fun updateQuestion() {
        quizViewModel.hasAnsweredAllQuestions = quizViewModel.numQuestionsAsked == 6
        if (quizViewModel.hasAnsweredAllQuestions) {
            val calculatedScore = 100 * (quizViewModel.score / 6)
            val messageResId = "Your score was ${calculatedScore}%!"
            Snackbar.make(binding.llMain, messageResId, Snackbar.LENGTH_LONG).show()
            quizViewModel.resetGame()
            return
        }

        if (quizViewModel.questionBank[quizViewModel.currentIndex].wasPreviouslyAsked) {
            binding.btnTrue.isEnabled = false
            binding.btnFalse.isEnabled = false
            val questionTextResId = quizViewModel.questionBank[quizViewModel.currentIndex].textResId
            binding.tvQuestion.setText(questionTextResId)
        } else {
            binding.btnTrue.isEnabled = true
            binding.btnFalse.isEnabled = true
            val questionTextResId = quizViewModel.questionBank[quizViewModel.currentIndex].textResId
            binding.tvQuestion.setText(questionTextResId)
        }
        quizViewModel.numQuestionsAsked++
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.questionBank[quizViewModel.currentIndex].answer
        quizViewModel.questionBank[quizViewModel.currentIndex].wasPreviouslyAsked = true
        val messageResId = if (userAnswer == correctAnswer) {
            quizViewModel.score++
            R.string.correct_snackbar
        } else {
            R.string.incorrect_snackbar
        }

        binding.tvScore.text = quizViewModel.score.toString()

        binding.btnTrue.isEnabled = false
        binding.btnFalse.isEnabled = false
        Snackbar.make(binding.llMain, messageResId, Snackbar.LENGTH_SHORT).show()
    }
}