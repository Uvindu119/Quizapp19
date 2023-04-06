package com.example.quizapp19

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    private lateinit var dbHelper: QuizDatabaseHelper
    private lateinit var startButton: Button
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        dbHelper = QuizDatabaseHelper(this)
        emailTextView = findViewById(R.id.emailTextView)
        startButton = findViewById(R.id.startButton)
        nameTextView = findViewById(R.id.nameTextView)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        // Get user ID from the intent
        userId = intent.getIntExtra("userId", 0)
        Log.d("UserActivity", "User ID: $userId") // Log the user ID

        // Set the click listener for the logout button
        logoutButton.setOnClickListener {
            // Go back to the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
            finish()
        }

        startButton.setOnClickListener {
            val intent = Intent(this, CategoryActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        // Get the user from the database
        val user = dbHelper.getUserById(userId)

        // Set the user's name and email
        nameTextView.text = user?.name
        emailTextView.text = user?.email
        correctAnswersTextView.text = user?.correctAnswers.toString()
        incorrectAnswersTextView.text = user?.incorrectAnswers.toString()
        correctPercentageTextView.text = user?.correctPercentage.toString()
        incorrectPercentageTextView.text = user?.incorrectPercentage.toString()
        questionSetsAnsweredTextView.text = user?.questionsetsAnswered.toString()
        questionsAnsweredTextView.text = user?.questionsAnswered.toString()
    }
}
