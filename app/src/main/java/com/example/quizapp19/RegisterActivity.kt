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
                Snackbar.make(it, "Please enter user Details", Snackbar.LENGTH_SHORT).show()
            } else if (!email.isValidEmail()) {
                Snackbar.make(it, "Invalid email", Snackbar.LENGTH_SHORT).show()
            } else {
                val existingUser = dbHelper.getUser(username)

                if (existingUser != null) {
                    Snackbar.make(it, "Username already registered", Snackbar.LENGTH_SHORT).show()
                } else {
                    if (dbHelper.addUser(username, email, password)) {
                        finish()
                    } else {
                        Snackbar.make(it, "Error registering user", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }
    fun String.isValidEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}
