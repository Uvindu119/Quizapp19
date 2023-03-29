package com.example.quizapp19


data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String,
    val imagePath: String = ""
) {
    val option1: String
        get() = options[0]

    val option2: String
        get() = options[1]

    val option3: String
        get() = options[2]

    val option4: String
        get() = options[3]
}