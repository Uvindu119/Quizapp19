package com.example.quizapp19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat

class ResultsActivity : AppCompatActivity() {
    private lateinit var scoreTextView: TextView
    private lateinit var gradeTextView: TextView
    private lateinit var incorrectAnswersTextView: TextView
    private lateinit var restartButton: Button
    private lateinit var exitButton: Button
    private lateinit var nextPaperButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        scoreTextView = findViewById(R.id.scoreTextView)
        gradeTextView = findViewById(R.id.gradeTextView)
        incorrectAnswersTextView = findViewById(R.id.incorrectAnswersTextView)
        restartButton = findViewById(R.id.restartButton)
        exitButton = findViewById(R.id.exitButton)
        nextPaperButton = findViewById(R.id.nextPaperButton)

        val correctAnswers = intent.getIntExtra("correctAnswers", 0)
        val totalQuestions = intent.getIntExtra("totalQuestions", 0)
        val incorrectAnswers = intent.getStringArrayListExtra("incorrectAnswers") ?: arrayListOf()

        val score = correctAnswers*2
        scoreTextView.text = getString(R.string.score_text, score, totalQuestions)

        val isQualifiedForNextPaper = when (score) {
            in 9..totalQuestions -> {
                gradeTextView.text = "A"
                gradeTextView.setTextColor(ContextCompat.getColor(this, R.color.green))
                true
            }
            in 7..8 -> {
                gradeTextView.text = "B"
                gradeTextView.setTextColor(ContextCompat.getColor(this, R.color.light_green))
                true
            }
            in 4..6 -> {
                gradeTextView.text = "C"
                gradeTextView.setTextColor(ContextCompat.getColor(this, R.color.yellow))
                true
            }
            else -> {
                gradeTextView.text = "W"
                gradeTextView.setTextColor(ContextCompat.getColor(this, R.color.red))
                false

            }
        }

        incorrectAnswersTextView.text = getString(R.string.incorrect_answers_text, incorrectAnswers.joinToString("\n\n"))

        nextPaperButton.isEnabled = isQualifiedForNextPaper
        nextPaperButton.setOnClickListener {
            if (isQualifiedForNextPaper) {
                val intent = Intent(this, QuizActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        restartButton.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            val questionSet = getIntent().getIntExtra("questionSet",1)
            intent.putExtra("questionSet", questionSet)
            startActivity(intent)
            finish()
        }

        exitButton.setOnClickListener {
            finish()
        }
    }
}