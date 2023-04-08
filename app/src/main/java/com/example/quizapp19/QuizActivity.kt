package com.example.quizapp19

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import androidx.core.view.GestureDetectorCompat
import android.view.GestureDetector
import android.view.MotionEvent
import kotlin.math.abs
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlin.random.Random
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
    private lateinit var gestureDetector: GestureDetectorCompat


    private var categoryId: Int = 0
    private var currentQuestionIndex = 0
    private lateinit var questions: List<Question>
    private var totalQuestions = 0
    private var correctAnswers = 0
    private val incorrectAnswers = arrayListOf<String>()
    private var isActivityActive = false
    private var isQuizFinished = false
    private var scores = 0
    private val answeredQuestions = mutableSetOf<Int>()
    private val totalQuestionSets = 1
    private var userId: Int = 0
    private val questionSet = Random.nextInt(1, totalQuestionSets + 1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        gestureDetector = GestureDetectorCompat(this@QuizActivity, GestureListener())
        questionText = findViewById(R.id.questionTextView)
        questionImage = findViewById(R.id.questionImageView)
        optionsGroup = findViewById(R.id.optionsRadioGroup)
        option1 = findViewById(R.id.option1)
        option2 = findViewById(R.id.option2)
        option3 = findViewById(R.id.option3)
        option4 = findViewById(R.id.option4)
        submitButton = findViewById(R.id.submitButton)
        timerTextView = findViewById(R.id.timerTextView)

        val questionSet = intent.getIntExtra("questionSet", Random.nextInt(1, totalQuestionSets + 1))
        categoryId = intent.getIntExtra("CATEGORY_ID", 0)
        userId = intent.getIntExtra("userId", 0)
        val dbHelper = QuizDatabaseHelper(this)
        questions = dbHelper.getQuestionsBySet(questionSet, categoryId)
        displayQuestion()
        quizTimer = QuizTimer(2 * 60, { time ->
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
            answeredQuestions.add(currentQuestionIndex)
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
            if (totalQuestions == questions.size) {
                finishQuiz()
            } else if (currentQuestionIndex >= questions.size) {
                currentQuestionIndex = 0
                displayQuestion()
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

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

    private fun displayQuestion() {
        if (questions.isNotEmpty()) {
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

            if (answeredQuestions.contains(currentQuestionIndex)) {
                submitButton.visibility = View.GONE
            } else {
                submitButton.visibility = View.VISIBLE
            }
        } else {
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            var result = false
            try {
                val diffY = e2!!.y - e1!!.y
                val diffX = e2.x - e1.x
                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                        result = true
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }
    }

    private fun onSwipeRight() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--
            displayQuestion()
        }
    }

    private fun onSwipeLeft() {
        if (currentQuestionIndex < questions.size - 1) {
            currentQuestionIndex++
            displayQuestion()
        }
    }

    private fun showQuizSummary() {
        quizTimer.cancel()
        val totalScore = correctAnswers * 2
        val summaryMessage = "Time finished!\nYou scored $totalScore out of ${questions.size*2}."
        AlertDialog.Builder(this)
            .setTitle("Quiz Summary")
            .setMessage(summaryMessage)
            .setPositiveButton("Restart") { _, _ ->
                currentQuestionIndex = 0
                correctAnswers = 0
                totalQuestions = 0
                displayQuestion()
                quizTimer.start()
            }
            .setNegativeButton("Exit") { _, _ ->
                finish()
            }
            .setCancelable(false)
            .create()
            .show()
    }
    private fun finishQuiz() {
        if (totalQuestions < questions.size) {
            Snackbar.make(findViewById(android.R.id.content), "Please answer all questions.", Snackbar.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, ResultsActivity::class.java)
        intent.putExtra("correctAnswers", correctAnswers)
        intent.putExtra("totalQuestions", questions.size)
        intent.putStringArrayListExtra("incorrectAnswers", incorrectAnswers)
        intent.putExtra("questionSet", questionSet)
        intent.putExtra("CATEGORY_ID", categoryId)
        intent.putExtra("userId", userId)
        startActivity(intent)
        finish()
        quizTimer.cancel()
    }


}

