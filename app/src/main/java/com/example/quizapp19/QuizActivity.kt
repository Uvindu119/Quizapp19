package com.example.quizapp19

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_quiz.*

class QuizActivity : AppCompatActivity() {

    private lateinit var questionText: TextView
    private lateinit var optionsGroup: RadioGroup
    private lateinit var option1: RadioButton
    private lateinit var option2: RadioButton
    private lateinit var option3: RadioButton
    private lateinit var option4: RadioButton
    private lateinit var submitButton: Button
    private lateinit var questionImage: ImageView
    private lateinit var quizTimer: QuizTimer
    private lateinit var timerTextView: TextView


    private var currentQuestionIndex = 0
    private lateinit var questions: List<Question>
    private var totalQuestions = 0
    private var correctAnswers = 0
    private val incorrectAnswers = arrayListOf<String>()
    private var isActivityActive = false
    private var isQuizFinished = false
    private var scores = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        questionText = findViewById(R.id.questionTextView)
        questionImage = findViewById(R.id.questionImageView)
        optionsGroup = findViewById(R.id.optionsRadioGroup)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        submitButton = findViewById(R.id.submitButton)
        timerTextView = findViewById(R.id.timerTextView)


        val dbHelper = QuizDatabaseHelper(this)
        questions = dbHelper.getAllQuestions()
        displayQuestion()
        quizTimer = QuizTimer(1 * 60, { time ->
            timerTextView.text = time
        }, {
            isQuizFinished = true
            if (isActivityActive) {
                runOnUiThread {
                    // Handle the end of the quiz
                    showQuizSummary()
                }
            }
        })
        quizTimer.start()

        submitButton.setOnClickListener {
            val selectedOptionId = optionsGroup.checkedRadioButtonId
            if (selectedOptionId == -1) {
                Snackbar.make(it, "Please select an option", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            totalQuestions++
            val selectedOption = findViewById<RadioButton>(selectedOptionId)
            val selectedAnswer = selectedOption.text.toString()
            val correctAnswer = questions[currentQuestionIndex].correctAnswer
            if (selectedAnswer == correctAnswer) {
                correctAnswers++

                scores += 2
            } else {
                incorrectAnswers.add("Q: ${questions[currentQuestionIndex].questionText}\nA: $correctAnswer")
            }

            currentQuestionIndex++
            if (currentQuestionIndex >= questions.size) {
                finishQuiz()
            } else {
                displayQuestion()
            }


        }

    }

    override fun onResume() {
        super.onResume()
        isActivityActive = true
        if (isQuizFinished) {
            showQuizSummary()
        }
    }

    override fun onPause() {
        super.onPause()
        isActivityActive = false
    }

    override fun onDestroy() {
        super.onDestroy()
        quizTimer.cancel()
    }


    private fun displayQuestion() {
        val question = questions[currentQuestionIndex]
        questionText.text = question.questionText
        if (question.imagePath.isNotBlank()) {
            Picasso.get().load(question.imagePath).into(questionImageView)
            questionImageView.visibility = View.VISIBLE
        } else {
            questionImageView.visibility = View.GONE
        }

        val shuffledOptions = question.options.shuffled()
        option1.text = shuffledOptions[0]
        option2.text = shuffledOptions[1]
        option3.text = shuffledOptions[2]
        option4.text = shuffledOptions[3]
        optionsGroup.clearCheck()
        submitButton.visibility = View.VISIBLE
    }

    private fun showQuizSummary() {
        // Cancel the timer to prevent further updates
        quizTimer.cancel()

        // Calculate the total score
        val totalScore = correctAnswers * 2

        // Create a summary message
        val summaryMessage = "Time finished!\nYou scored $totalScore out of ${questions.size*2}."

        // Show an AlertDialog with the summary message and options to restart or exit
        AlertDialog.Builder(this)
            .setTitle("Quiz Summary")
            .setMessage(summaryMessage)
            .setPositiveButton("Restart") { _, _ ->
                // Restart the quiz
                currentQuestionIndex = 0
                correctAnswers = 0
                totalQuestions = 0
                displayQuestion()
                quizTimer.start()
            }
            .setNegativeButton("Exit") { _, _ ->
                // Exit the app
                finish()
            }
            .setCancelable(false)
            .create()
            .show()
    }
    private fun finishQuiz() {
        val intent = Intent(this, ResultsActivity::class.java)
        intent.putExtra("correctAnswers", correctAnswers*2)
        intent.putExtra("totalQuestions", questions.size*2)
        intent.putStringArrayListExtra("incorrectAnswers", incorrectAnswers)
        startActivity(intent)
        finish()
        quizTimer.cancel()
    }

}

