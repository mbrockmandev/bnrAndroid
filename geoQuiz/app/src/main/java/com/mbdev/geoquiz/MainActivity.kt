package com.mbdev.geoquiz

import android.app.Activity
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.mbdev.geoquiz.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateQuestion()

        binding.btnTrue.setOnClickListener {
            checkAnswer(true)
        }

        binding.btnFalse.setOnClickListener {
            checkAnswer(false)
        }

        binding.btnCheat.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }

        binding.btnNext.setOnClickListener {
            quizViewModel.moveToNextQuestion()
            updateQuestion()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            blurCheatButton()
        }

        binding.btnShowAPI.setOnClickListener {
            val message = (android.os.Build.VERSION.SDK_INT)
            Snackbar.make(binding.llMain, "Your current API is: $message", Snackbar.LENGTH_SHORT).show()

        }

    }

    private fun updateQuestion() {
        quizViewModel.hasAnsweredAllQuestions = quizViewModel.numQuestionsAsked == 6
        if (quizViewModel.hasAnsweredAllQuestions) {
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

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_snackbar
            userAnswer == correctAnswer -> R.string.correct_snackbar
            else -> R.string.incorrect_snackbar
        }

        binding.btnTrue.isEnabled = false
        binding.btnFalse.isEnabled = false
        Snackbar.make(binding.llMain, messageResId, Snackbar.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun blurCheatButton() {
        val effect = RenderEffect.createBlurEffect(
            10.0f,
            10.0f,
            Shader.TileMode.CLAMP
        )
        binding.btnCheat.setRenderEffect(effect)
    }
}