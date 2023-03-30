package com.example.quizapp19

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class QuizDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Quiz.db"
        private const val DATABASE_VERSION = 19

        private const val TABLE_NAME = "questions"
        private const val COLUMN_ID = "id"
        private const val COLUMN_QUESTION_TEXT = "question_text"
        private const val COLUMN_IMAGE_PATH = "image_path"
        private const val COLUMN_OPTION_1 = "option_1"
        private const val COLUMN_OPTION_2 = "option_2"
        private const val COLUMN_OPTION_3 = "option_3"
        private const val COLUMN_OPTION_4 = "option_4"
        private const val COLUMN_CORRECT_ANSWER = "correct_answer"
        private const val COLUMN_SET_NUMBER = "set_number"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableSQL = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_QUESTION_TEXT TEXT,
                $COLUMN_OPTION_1 TEXT,
                $COLUMN_OPTION_2 TEXT,
                $COLUMN_OPTION_3 TEXT,
                $COLUMN_OPTION_4 TEXT,
                $COLUMN_CORRECT_ANSWER INTEGER,
                $COLUMN_IMAGE_PATH TEXT,
                $COLUMN_SET_NUMBER INTEGER
            )
        """.trimIndent()

        db?.execSQL(createTableSQL)
        insertSampleQuestions(db) // Insert sample questions
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { if (oldVersion < 19 && newVersion >= 19) {
        // Option 1: Drop the table and recreate it
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)

        // Option 2: Insert new questions
        // val newQuestion1 = Question(...)
        // val newQuestion2 = Question(...)
        // addQuestion(db, newQuestion1)
        // addQuestion(db, newQuestion2)
    }
    }

    private fun insertSampleQuestions(db: SQLiteDatabase?) {
        val sampleQuestions = listOf(
            Question("What is the capital of France?", listOf("Paris", "London", "Rome", "Berlin"), "Paris","",1),
            Question("Book is to Reading as Fork is to:", listOf("drawing", "writing", "stirring", "eating"), "eating","",2),
            Question("What comes next in the sequence: 1, 3, 9, 27, ___.", listOf("71", "73", "81", "83"), "81","android.resource://com.example.quizapp19/drawable/image1",2),
            Question("If 4 people can do a work in 40 minutes then 8 people can do the same work in ___ minutes.", listOf("20", "40", "60", "80"), "20","android.resource://com.example.quizapp19/drawable/image1",1),
            Question("Mary is 16 years old. She is 4 times older than her brother. How old will Mary be when she is twice his age? ", listOf("26", "20", "24", "28"), "24","android.resource://com.example.quizapp19/drawable/image1",2),
            Question("Which fraction is the biggest? ", listOf("3/5", "5/8", "1/2 ", "4/7 "), "5/8","",1),
            Question("The store reduces the price of one product by 20 percent. How many percent do you need to raise to the percentage to get the original price? ", listOf("25", "27", "30", "35"), "25","android.resource://com.example.quizapp19/drawable/image1",1),
            Question("There are 5 machines that make 5 parts in 5 minutes. How long does it take to make 100 parts on 100 machines? ", listOf("5", "10", "15", "30"), "5","android.resource://com.example.quizapp19/drawable/image1",2),
            Question("What is the name given to a group of HORSES? ", listOf("husk", "harras", "mute", "rush"), "husk","",1),
            Question("What is a CURRICLE? ", listOf("a vehicle", "a boat", "a curtain", "a vegetable"), "a vehicle","",2)
        )

        sampleQuestions.forEach { question ->
            val contentValues = ContentValues().apply {
                put(COLUMN_QUESTION_TEXT, question.questionText)
                put(COLUMN_OPTION_1, question.options[0])
                put(COLUMN_OPTION_2, question.options[1])
                put(COLUMN_OPTION_3, question.options[2])
                put(COLUMN_OPTION_4, question.options[3])
                put(COLUMN_CORRECT_ANSWER, question.correctAnswer)
                put(COLUMN_IMAGE_PATH, question.imagePath)
                put(COLUMN_SET_NUMBER, question.setNumber)
            }
            db?.insert(TABLE_NAME, null, contentValues)
        }
    }

    fun getQuestionsBySet(setNumber: Int): List<Question> {
        val questions = mutableListOf<Question>()

        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_SET_NUMBER = $setNumber", null)

        if (cursor.moveToFirst()) {
            do {
                val questionTextIndex = cursor.getColumnIndex(COLUMN_QUESTION_TEXT)
                val option1Index = cursor.getColumnIndex(COLUMN_OPTION_1)
                val option2Index = cursor.getColumnIndex(COLUMN_OPTION_2)
                val option3Index = cursor.getColumnIndex(COLUMN_OPTION_3)
                val option4Index = cursor.getColumnIndex(COLUMN_OPTION_4)
                val correctAnswerIndex = cursor.getColumnIndex(COLUMN_CORRECT_ANSWER)
                val imagePathIndex = cursor.getColumnIndex(COLUMN_IMAGE_PATH)

                if (questionTextIndex != -1 && option1Index != -1 && option2Index != -1 &&
                    option3Index != -1 && option4Index != -1 && correctAnswerIndex != -1 && imagePathIndex != -1) {

                    val questionText = cursor.getString(questionTextIndex)
                    val option1 = cursor.getString(option1Index)
                    val option2 = cursor.getString(option2Index)
                    val option3 = cursor.getString(option3Index)
                    val option4 = cursor.getString(option4Index)
                    val correctAnswer = cursor.getString(correctAnswerIndex)
                    val imagePath = cursor.getString(imagePathIndex) // Get image path

                    val question = Question(questionText, listOf(option1, option2, option3, option4), correctAnswer, imagePath , setNumber )
                    questions.add(question)
                }
            } while (cursor.moveToNext())
        }

        cursor.close()
        return questions
    }

}