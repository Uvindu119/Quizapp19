package com.example.quizapp19

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserActivity : AppCompatActivity() {

    private lateinit var dbHelper: QuizDatabaseHelper
    private var userId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        dbHelper = QuizDatabaseHelper(this)

        val nameTextView = findViewById<TextView>(R.id.nameTextView)
        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        val logoutButton = findViewById<Button>(R.id.logoutButton)

        // Get user ID from the intent
        userId = intent.getIntExtra("USER_ID", 0)

        // Get the user from the database
        val user = dbHelper.getUserById(userId)

        // Set the user's name and email
        nameTextView.text = user?.name
        emailTextView.text = user?.email

        // Set the click listener for the logout button
        logoutButton.setOnClickListener {
            // Go back to the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
