package com.example.quizapp19
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: QuizDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dbHelper = QuizDatabaseHelper(this)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                showSnackbar("Please enter a username and password")
            } else {
                val user = dbHelper.getUser(username)
                if (user != null && user.password == password) {
                    val intent = Intent(this, CategoryActivity::class.java)
                    startActivity(intent)
                    // Login successful
                    showSnackbar("Login successful")
                } else {
                    // Login failed
                    showSnackbar(if (user == null) "User not found" else "Incorrect username or password")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(loginButton, message, Snackbar.LENGTH_SHORT)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(ContextCompat.getColor(this, R.color.primaryVariant))
        snackbar.show()
    }
}
