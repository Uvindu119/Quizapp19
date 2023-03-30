package com.example.quizapp19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ResultsActivity : AppCompatActivity() {
    private lateinit var scoreTextView: TextView
    private lateinit var gradeTextView: TextView
    private lateinit var incorrectAnswersTextView: TextView
    private lateinit var restartButton: Button
    private lateinit var exitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        scoreTextView = findViewById(R.id.scoreTextView)
        gradeTextView = findViewById(R.id.gradeTextView)
        incorrectAnswersTextView = findViewById(R.id.incorrectAnswersTextView)
        restartButton = findViewById(R.id.restartButton)
        exitButton = findViewById(R.id.exitButton)

        val correctAnswers = intent.getIntExtra("correctAnswers", 0)
        val totalQuestions = intent.getIntExtra("totalQuestions", 0)
        val incorrectAnswers = intent.getStringArrayListExtra("incorrectAnswers") ?: arrayListOf()

        val score = correctAnswers*2
        scoreTextView.text = getString(R.string.score_text, score, totalQuestions)

        if (score > 16) {
            gradeTextView.text = "A"
            gradeTextView.setTextColor(resources.getColor(R.color.green))
        }
        else if (score > 12) {
            gradeTextView.text = "B"
            gradeTextView.setTextColor(resources.getColor(R.color.light_green))
        }
        else if (score > 8) {
            gradeTextView.text = "C"
            gradeTextView.setTextColor(resources.getColor(R.color.yellow))
        }
        else {
            gradeTextView.text = "W"
            gradeTextView.setTextColor(resources.getColor(R.color.red))
        }

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