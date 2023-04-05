package com.example.quizapp19

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*

class QuizDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "Quiz.db"
        private const val DATABASE_VERSION = 44

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
        private const val TABLE_CATEGORIES = "categories"
        private const val COLUMN_CATEGORY_ID = "number"
        private const val COLUMN_CATEGORY_NAME = "name"

        private const val TABLE_NAME_USERS = "users"
        private const val COLUMN_USER_ID = "id"
        private const val COLUMN_USER_NAME = "name"
        private const val COLUMN_USER_EMAIL = "email"
        private const val COLUMN_USER_PASSWORD = "password"
        private const val COLUMN_QUESTIONS_ANSWERED = "questions_answered"
        private const val COLUMN_QUESTIONSETS_ANSWERED = "questionsets_answered"
        private const val COLUMN_CORRECT_ANSWERS = "correct_answers"
        private const val COLUMN_INCORRECT_ANSWERS = "incorrect_answers"
        private const val COLUMN_CORRECT_PERCENTAGE = "correct_percentage"
        private const val COLUMN_INCORRECT_PERCENTAGE = "incorrect_percentage"
        private const val COLUMN_GRADE_A = "grade_a"
        private const val COLUMN_GRADE_B = "grade_b"
        private const val COLUMN_GRADE_C = "grade_c"
        private const val COLUMN_GRADE_W = "grade_w"
        private const val COLUMN_SPECIALIZED_CATEGORIES = "specialized_categories"




    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableUsersSQL = """
    CREATE TABLE $TABLE_NAME_USERS (
    $COLUMN_USER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
    $COLUMN_USER_NAME TEXT NOT NULL,
    $COLUMN_USER_EMAIL TEXT NOT NULL,
    $COLUMN_USER_PASSWORD TEXT NOT NULL,
    $COLUMN_QUESTIONS_ANSWERED INTEGER DEFAULT 0,
    $COLUMN_QUESTIONSETS_ANSWERED INTEGER DEFAULT 0,
    $COLUMN_CORRECT_ANSWERS INTEGER DEFAULT 0,
    $COLUMN_INCORRECT_ANSWERS INTEGER DEFAULT 0,
    $COLUMN_CORRECT_PERCENTAGE FLOAT DEFAULT 0.0,
    $COLUMN_INCORRECT_PERCENTAGE FLOAT DEFAULT 0.0,
    $COLUMN_GRADE_A INTEGER DEFAULT 0,
    $COLUMN_GRADE_B INTEGER DEFAULT 0,
    $COLUMN_GRADE_C INTEGER DEFAULT 0,
    $COLUMN_GRADE_W INTEGER DEFAULT 0,
    $COLUMN_SPECIALIZED_CATEGORIES TEXT
    )
""".trimIndent()

        db?.execSQL(createTableUsersSQL)

        val createTableSQL = """
        CREATE TABLE IF NOT EXISTS $TABLE_NAME (
            $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_QUESTION_TEXT TEXT,
            $COLUMN_OPTION_1 TEXT,
            $COLUMN_OPTION_2 TEXT,
            $COLUMN_OPTION_3 TEXT,
            $COLUMN_OPTION_4 TEXT,
            $COLUMN_CORRECT_ANSWER INTEGER,
            $COLUMN_IMAGE_PATH TEXT,
            $COLUMN_SET_NUMBER INTEGER,
            $COLUMN_CATEGORY_ID INTEGER,
            FOREIGN KEY($COLUMN_CATEGORY_ID) REFERENCES $TABLE_CATEGORIES($COLUMN_CATEGORY_ID)
        )
    """.trimIndent()

        db?.execSQL(createTableSQL)

        val createCategoriesTableSQL = """
        CREATE TABLE IF NOT EXISTS $TABLE_CATEGORIES (
            $COLUMN_CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
            $COLUMN_CATEGORY_NAME TEXT
        )
    """.trimIndent()

        db?.execSQL(createCategoriesTableSQL)
        insertSampleQuestions(db)
        insertSampleCategories(db)
    }

    private fun insertSampleCategories(db: SQLiteDatabase?) {
        val cursor = db?.rawQuery("SELECT * FROM $TABLE_CATEGORIES", null)
        cursor?.let {
            if (it.count == 0) {
                val sampleCategories = listOf(
                    Category("General",1),
                    Category("Mathematics",2),
                    Category("Science",3),
                    Category("History",4)
                )
                sampleCategories.forEach { category ->
                    val contentValues = ContentValues().apply {
                        put(COLUMN_CATEGORY_NAME, category.name)
                    }
                    db.insert(TABLE_CATEGORIES, null, contentValues)
                }
            }
            it.close()
        }
    }
    private fun addQuestion(db: SQLiteDatabase?, question: Question) {
        val contentValues = ContentValues().apply {
            put(COLUMN_QUESTION_TEXT, question.questionText)
            put(COLUMN_OPTION_1, question.options[0])
            put(COLUMN_OPTION_2, question.options[1])
            put(COLUMN_OPTION_3, question.options[2])
            put(COLUMN_OPTION_4, question.options[3])
            put(COLUMN_CORRECT_ANSWER, question.correctAnswer)
            put(COLUMN_IMAGE_PATH, question.imagePath)
            put(COLUMN_SET_NUMBER, question.setNumber)
            put(COLUMN_CATEGORY_ID, question.categoryId)
        }
        db?.insert(TABLE_NAME, null, contentValues)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { if (oldVersion < 44 && newVersion >= 44) {
        // Option 1: Drop the table and recreate it
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
        val newQuestion = Question("What is the capital of Australia?", listOf("Sydney", "Canberra", "Melbourne", "Brisbane"), "Sydney", "", 3, 3)
        addQuestion(db, newQuestion)
    }
    }

    private fun insertSampleQuestions(db: SQLiteDatabase?) {
        val sampleQuestions = listOf(
            Question("What is the capital of France?", listOf("Paris", "London", "Rome", "Berlin"), "Paris","",1,1),
            Question("Book is to Reading as Fork is to:", listOf("drawing", "writing", "stirring", "eating"), "eating","",2,2),
            Question("What comes next in the sequence: 1, 3, 9, 27, ___.", listOf("71", "73", "81", "83"), "81","android.resource://com.example.quizapp19/drawable/image1",2,2),
            Question("If 4 people can do a work in 40 minutes then 8 people can do the same work in ___ minutes.", listOf("20", "40", "60", "80"), "20","android.resource://com.example.quizapp19/drawable/image1",1,1),
            Question("Mary is 16 years old. She is 4 times older than her brother. How old will Mary be when she is twice his age? ", listOf("26", "20", "24", "28"), "24","android.resource://com.example.quizapp19/drawable/image1",2,2),
            Question("Which fraction is the biggest? ", listOf("3/5", "5/8", "1/2 ", "4/7 "), "5/8","",1,1),
            Question("The store reduces the price of one product by 20 percent. How many percent do you need to raise to the percentage to get the original price? ", listOf("25", "27", "30", "35"), "25","android.resource://com.example.quizapp19/drawable/image1",1,1),
            Question("There are 5 machines that make 5 parts in 5 minutes. How long does it take to make 100 parts on 100 machines? ", listOf("5", "10", "15", "30"), "5","android.resource://com.example.quizapp19/drawable/image1",2,2),
            Question("What is the name given to a group of HORSES? ", listOf("husk", "harras", "mute", "rush"), "husk","",1,1),
            Question("What is a CURRICLE? ", listOf("a vehicle", "a boat", "a curtain", "a vegetable"), "a vehicle","",2,2)

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
                put(COLUMN_CATEGORY_ID, question.categoryId)
            }
            db?.insert(TABLE_NAME, null, contentValues)
        }
    }
    fun getCategories(): List<Category> {
        val categories = mutableListOf<Category>()
        val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_CATEGORIES", null)
        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex(COLUMN_CATEGORY_ID)
                val nameIndex = cursor.getColumnIndex(COLUMN_CATEGORY_NAME)
                if (idIndex != -1 && nameIndex != -1) {
                    val id = cursor.getString(idIndex).toInt() // Convert id to integer
                    val name = cursor.getString(nameIndex)
                    val category = Category(name, id)
                    categories.add(category)
                }
            } while (cursor.moveToNext())
        }
        cursor.close()
        return categories
    }

    fun addUser(username: String, email: String, password: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_USER_NAME, username)
        values.put(COLUMN_USER_EMAIL, email) // Added email parameter
        values.put(COLUMN_USER_PASSWORD, password)
        val newRowId = db.insert(TABLE_NAME_USERS, null, values)
        return newRowId != -1L
    }
    fun getUser(username: String): User? {
        val db = this.readableDatabase
        val selection = "$COLUMN_USER_NAME = ?"
        val selectionArgs = arrayOf(username)
        val cursor = db.query(
            TABLE_NAME_USERS,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        return if (cursor.moveToFirst()) {
            val columnIndexId = cursor.getColumnIndexOrThrow(COLUMN_USER_ID)
            val columnIndexName = cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)
            val columnIndexEmail = cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)
            val columnIndexPassword = cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)
            val columnIndexQuestionsAnswered = cursor.getColumnIndexOrThrow(COLUMN_QUESTIONS_ANSWERED)
            val columnIndexQuestionSetsAnswered = cursor.getColumnIndexOrThrow(COLUMN_QUESTIONSETS_ANSWERED)
            val columnIndexCorrectAnswers = cursor.getColumnIndexOrThrow(COLUMN_CORRECT_ANSWERS)
            val columnIndexIncorrectAnswers = cursor.getColumnIndexOrThrow(COLUMN_INCORRECT_ANSWERS)
            val columnIndexCorrectPercentage = cursor.getColumnIndexOrThrow(COLUMN_CORRECT_PERCENTAGE)
            val columnIndexIncorrectPercentage = cursor.getColumnIndexOrThrow(COLUMN_INCORRECT_PERCENTAGE)
            val columnIndexGradeA = cursor.getColumnIndexOrThrow(COLUMN_GRADE_A)
            val columnIndexGradeB = cursor.getColumnIndexOrThrow(COLUMN_GRADE_B)
            val columnIndexGradeC = cursor.getColumnIndexOrThrow(COLUMN_GRADE_C)
            val columnIndexGradeW = cursor.getColumnIndexOrThrow(COLUMN_GRADE_W)
            val columnIndexSpecializedCategories = cursor.getColumnIndexOrThrow(COLUMN_SPECIALIZED_CATEGORIES)

            val user = User(
                cursor.getInt(columnIndexId),
                cursor.getString(columnIndexName),
                cursor.getString(columnIndexEmail),
                cursor.getString(columnIndexPassword),
                cursor.getInt(columnIndexQuestionsAnswered),
                cursor.getInt(columnIndexQuestionSetsAnswered),
                cursor.getInt(columnIndexCorrectAnswers),
                cursor.getInt(columnIndexIncorrectAnswers),
                cursor.getFloat(columnIndexCorrectPercentage),
                cursor.getFloat(columnIndexIncorrectPercentage),
                cursor.getInt(columnIndexGradeA),
                cursor.getInt(columnIndexGradeB),
                cursor.getInt(columnIndexGradeC),
                cursor.getInt(columnIndexGradeW),
                cursor.getString(columnIndexSpecializedCategories)
            )
            cursor.close()
            user
        } else {
            cursor.close()
            null
        }
    }
    fun updateUserStats(user: User) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_QUESTIONS_ANSWERED, user.questionsAnswered)
            put(COLUMN_QUESTIONSETS_ANSWERED, user.questionsetsAnswered)
            put(COLUMN_CORRECT_ANSWERS, user.correctAnswers)
            put(COLUMN_INCORRECT_ANSWERS, user.incorrectAnswers)
            put(COLUMN_CORRECT_PERCENTAGE, user.correctPercentage)
            put(COLUMN_INCORRECT_PERCENTAGE, user.incorrectPercentage)
            put(COLUMN_GRADE_A, user.gradeA)
            put(COLUMN_GRADE_B, user.gradeB)
            put(COLUMN_GRADE_C, user.gradeC)
            put(COLUMN_GRADE_W, user.gradeW)
            put(COLUMN_SPECIALIZED_CATEGORIES, user.specializedCategories)
        }

        db.update(TABLE_NAME_USERS, values, "$COLUMN_USER_ID=?", arrayOf(user.id.toString()))
        db.close()
    }

    fun getUserById(userId: Int): User? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_NAME_USERS, arrayOf(COLUMN_USER_ID, COLUMN_USER_NAME, COLUMN_USER_EMAIL, COLUMN_USER_PASSWORD, COLUMN_QUESTIONS_ANSWERED, COLUMN_QUESTIONSETS_ANSWERED, COLUMN_CORRECT_ANSWERS, COLUMN_INCORRECT_ANSWERS, COLUMN_CORRECT_PERCENTAGE, COLUMN_INCORRECT_PERCENTAGE, COLUMN_GRADE_A, COLUMN_GRADE_B, COLUMN_GRADE_C,COLUMN_GRADE_W, COLUMN_SPECIALIZED_CATEGORIES),
            "$COLUMN_USER_ID=?", arrayOf(userId.toString()), null, null, null
        )

        var user: User? = null
        if (cursor.moveToFirst()) {
            user = User(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME)),
                email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL)),
                password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD)),
                questionsAnswered = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUESTIONS_ANSWERED)),
                questionsetsAnswered = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_QUESTIONSETS_ANSWERED)),
                correctAnswers = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CORRECT_ANSWERS)),
                incorrectAnswers = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_INCORRECT_ANSWERS)),
                correctPercentage = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_CORRECT_PERCENTAGE)),
                incorrectPercentage = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_INCORRECT_PERCENTAGE)),
                gradeA = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GRADE_A)),
                gradeB = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GRADE_B)),
                gradeC = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GRADE_C)),
                gradeW = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GRADE_W)),
                specializedCategories = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SPECIALIZED_CATEGORIES))
            )
        }
        cursor.close()
        return user
    }



    fun getQuestionsBySet(setNumber: Int, categoryId: Int): List<Question> {
        val questions = mutableListOf<Question>()

        // Step 1: Get all set numbers for the given category
        val setNumbers = mutableListOf<Int>()
        val setCursor = readableDatabase.rawQuery("SELECT DISTINCT $COLUMN_SET_NUMBER FROM $TABLE_NAME WHERE $COLUMN_CATEGORY_ID = $categoryId", null)
        if (setCursor.moveToFirst()) {
            do {
                val setNumberIndex = setCursor.getColumnIndex(COLUMN_SET_NUMBER)
                if (setNumberIndex != -1) {
                    setNumbers.add(setCursor.getInt(setNumberIndex))
                }
            } while (setCursor.moveToNext())
        }
        setCursor.close()

        // Step 2: Shuffle the set numbers
        setNumbers.shuffle(Random())

        // Step 3: Retrieve the questions for each set in the shuffled list
        for (shuffledSetNumber in setNumbers) {
            val cursor = readableDatabase.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_SET_NUMBER = $shuffledSetNumber AND $COLUMN_CATEGORY_ID = $categoryId", null)

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

                        val question = Question(questionText, listOf(option1, option2, option3, option4), correctAnswer, imagePath , setNumber,categoryId)
                        questions.add(question)
                    }
                } while (cursor.moveToNext())
            }

            cursor.close()
        }

        return questions
    }

}