package com.example.quizapp19

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import android.util.Log
import kotlin.random.Random

class ResultsActivity : AppCompatActivity() {
    private lateinit var dbHelper: QuizDatabaseHelper
    private lateinit var scoreTextView: TextView
    private lateinit var gradeTextView: TextView
    private lateinit var incorrectAnswersTextView: TextView
    private lateinit var exitButton: Button
    private lateinit var nextPaperButton: Button
    private val totalQuestionSets = 4



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        dbHelper = QuizDatabaseHelper(this)

        scoreTextView = findViewById(R.id.scoreTextView)
        gradeTextView = findViewById(R.id.gradeTextView)
        incorrectAnswersTextView = findViewById(R.id.incorrectAnswersTextView)
        exitButton = findViewById(R.id.exitButton)
        nextPaperButton = findViewById(R.id.nextPaperButton)


        val userId = intent.getIntExtra("userId", 0)
        val correctAnswers = intent.getIntExtra("correctAnswers", 0)
        val totalQuestions = intent.getIntExtra("totalQuestions", 0)
        val incorrectAnswers = intent.getStringArrayListExtra("incorrectAnswers") ?: arrayListOf()
        val categoryId = intent.getIntExtra("CATEGORY_ID", 0)


        val score = correctAnswers * 2
        scoreTextView.text = getString(R.string.score_text, score, totalQuestions*2)

        val grade = when (score) {
            in 9..10  -> {
                gradeTextView.text = "A"
                gradeTextView.setTextColor(ContextCompat.getColor(this, R.color.green))
                "A"
            }
            in 7..8 -> {
                gradeTextView.text = "B"
                gradeTextView.setTextColor(ContextCompat.getColor(this, R.color.light_green))
                "B"
            }
            in 4..6 -> {
                gradeTextView.text = "C"
                gradeTextView.setTextColor(ContextCompat.getColor(this, R.color.yellow))
                "C"
            }
            else -> {
                gradeTextView.text = "W"
                gradeTextView.setTextColor(ContextCompat.getColor(this, R.color.red))
                "W"
            }
        }
        val correctPercentage = (correctAnswers.toFloat() / totalQuestions.toFloat()) * 100
        val incorrectPercentage = (incorrectAnswers.size.toFloat() / totalQuestions.toFloat()) * 100

        // Get user information
        val user = dbHelper.getUserById(userId)
        Log.d("ResultsActivity", "User before update: $user")

        // Update user statistics
        if (user != null) {
            val updatedUser = user.copy(
                questionsAnswered = user.questionsAnswered + totalQuestions,
                questionsetsAnswered = user.questionsetsAnswered + 1,
                correctAnswers = user.correctAnswers + correctAnswers,
                incorrectAnswers = user.incorrectAnswers + incorrectAnswers.size,
                correctPercentage = correctPercentage,
                incorrectPercentage = incorrectPercentage,
                gradeA = if (grade == "A") user.gradeA + 1 else user.gradeA,
                gradeB = if (grade == "B") user.gradeB + 1 else user.gradeB,
                gradeC = if (grade == "C") user.gradeC + 1 else user.gradeC,
                gradeW = if (grade == "W") user.gradeW + 1 else user.gradeW,
            )

            dbHelper.updateUserStats(updatedUser)
            val updatedUserFromDB = dbHelper.getUserById(userId)
            Log.d("ResultsActivity", "User after update: $updatedUserFromDB")
        }

        incorrectAnswersTextView.text = getString(R.string.incorrect_answers_text, incorrectAnswers.joinToString("\n\n"))

        val isQualifiedForNextPaper = grade != "W"

        nextPaperButton.isEnabled = isQualifiedForNextPaper
        nextPaperButton.setOnClickListener {
            if (isQualifiedForNextPaper) {
                val questionSet = intent.getIntExtra("questionSet", Random.nextInt(1, totalQuestionSets + 1))
                val nextQuestionSet = (questionSet % totalQuestionSets) + 1
                val quizIntent = Intent(this, QuizActivity::class.java)
                quizIntent.putExtra("CATEGORY_ID", categoryId)
                quizIntent.putExtra("userId", userId)
                quizIntent.putExtra("questionSet", nextQuestionSet)
                startActivity(quizIntent)
                finish()
            }
        }



        exitButton.setOnClickListener {
            val intent = Intent(this, UserActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
            finish()
        }
    }
}