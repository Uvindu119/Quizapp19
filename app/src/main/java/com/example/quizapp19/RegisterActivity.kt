package com.example.quizapp19

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbHelper: QuizDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dbHelper = QuizDatabaseHelper(this)

        registerButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Snackbar.make(it, "Please enter a username, email, and password", Snackbar.LENGTH_SHORT).show()
            } else {
                if (dbHelper.addUser(username, email, password)) {
                    Snackbar.make(it, "User registered successfully", Snackbar.LENGTH_SHORT).show()
                    finish()
                } else {
                    Snackbar.make(it, "Error registering user", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}
