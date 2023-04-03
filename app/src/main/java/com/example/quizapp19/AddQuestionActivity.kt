package com.example.quizapp19

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_question.*

class AddQuestionActivity : AppCompatActivity() {

    private lateinit var dbHelper: QuizDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_question)

        dbHelper = QuizDatabaseHelper(this)

        val categories = dbHelper.getCategories()

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        categorySpinner.adapter = adapter

        saveButton.setOnClickListener {
            val questionText = questionEditText.text.toString()
            val option1 = option1EditText.text.toString()
            val option2 = option2EditText.text.toString()
            val option3 = option3EditText.text.toString()
            val option4 = option4EditText.text.toString()
            val correctAnswer = correctAnswerEditText.text.toString()
            val imagePath = imagePathEditText.text.toString()
            val setNumber = setNumberEditText.text.toString().toInt()
            val category = categorySpinner.selectedItem as Category
            val categoryId = category.id

            val question = Question(
                questionText,
                listOf(option1, option2, option3, option4),
                correctAnswer,
                imagePath,
                setNumber,
                categoryId
            )

            dbHelper.addQuestion(dbHelper.writableDatabase, question)

            Toast.makeText(this, "Question added to database", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}