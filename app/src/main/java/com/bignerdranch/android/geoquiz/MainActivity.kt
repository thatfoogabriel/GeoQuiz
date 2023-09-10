package com.bignerdranch.android.geoquiz

import QuizViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.view.View
import android.util.Log
import androidx.activity.viewModels
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val quizViewModel: QuizViewModel by viewModels()

    private var questionsAnswered = 0
    private var score = 0
    private var totalQuestions = 0
    private var answered = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val d = Log.d(TAG, "Got a QuizViewModel: $quizViewModel")
        totalQuestions = quizViewModel.questionBank.size

        binding.trueButton.setOnClickListener { view: View ->
            if (!answered)
                checkAnswer(true)
        }
        binding.falseButton.setOnClickListener { view: View ->
            if (!answered)
                checkAnswer(false)
        }
        binding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }
        binding.previousButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }
        updateQuestion()
    }

    private fun updateQuestion() {
        if (isCompleted()) {
            showQuizResult()
        } else {
            answered = false
            binding.trueButton.isEnabled = true
            binding.falseButton.isEnabled = true
            val questionTextResId = quizViewModel.currentQuestionText
            binding.questionTextView.setText(questionTextResId)
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = if (userAnswer == correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
            .show()

        binding.trueButton.isEnabled = false
        binding.falseButton.isEnabled = false
        answered = true
        if (userAnswer == correctAnswer) {
            score++
        }
        questionsAnswered++

        if (questionsAnswered == totalQuestions) {
            showQuizResult()
        }
    }

    private fun isCompleted(): Boolean {
        return questionsAnswered == totalQuestions
    }

    private fun showQuizResult() {
        val percentage = (score.toDouble() / totalQuestions.toDouble()) * 100
        val resultMessage = getString(R.string.score_message, percentage)
        Toast.makeText(this, resultMessage, Toast.LENGTH_LONG)
            .show()
    }
}
