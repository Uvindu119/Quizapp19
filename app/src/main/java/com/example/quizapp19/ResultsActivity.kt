package com.example.quizapp19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ResultsActivity : AppCompatActivity() {
    private lateinit var scoreTextView: TextView
    private lateinit var incorrectAnswersTextView: TextView
    private lateinit var restartButton: Button
    private lateinit var exitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        scoreTextView = findViewById(R.id.scoreTextView)
        incorrectAnswersTextView = findViewById(R.id.incorrectAnswersTextView)
        restartButton = findViewById(R.id.restartButton)
        exitButton = findViewById(R.id.exitButton)

        val correctAnswers = intent.getIntExtra("correctAnswers", 0)
        val totalQuestions = intent.getIntExtra("totalQuestions", 0)
        val incorrectAnswers = intent.getStringArrayListExtra("incorrectAnswers") ?: arrayListOf()

        scoreTextView.text = getString(R.string.score_text, correctAnswers, totalQuestions)
        incorrectAnswersTextView.text = getString(R.string.incorrect_answers_text, incorrectAnswers.joinToString("\n\n"))

        restartButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        exitButton.setOnClickListener {
            finish()
        }

    }
}