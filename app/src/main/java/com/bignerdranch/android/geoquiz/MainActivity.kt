package com.bignerdranch.android.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.view.View
import android.util.Log
import com.bignerdranch.android.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true))
    private var answered = false
    private var currentIndex = 0
    private var questionsAnswered = 0
    private var score = 0
    private var totalQuestions = questionBank.size

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trueButton.setOnClickListener { view: View ->
            if (!answered)
                checkAnswer(true)
        }
        binding.falseButton.setOnClickListener { view: View ->
            if (!answered)
                checkAnswer(false)
        }
        binding.nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }
        binding.previousButton.setOnClickListener {
            currentIndex = (currentIndex - 1) % questionBank.size
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
            val questionTextResId = questionBank[currentIndex].textResId
            binding.questionTextView.setText(questionTextResId)
        }
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
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
        return currentIndex == totalQuestions
    }

    private fun showQuizResult() {
        val percentage = (score.toDouble() / totalQuestions.toDouble()) * 100
        val resultMessage = getString(R.string.score_message, percentage)
        Toast.makeText(this, resultMessage, Toast.LENGTH_LONG)
            .show()
    }
}
