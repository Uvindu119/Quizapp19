package com.example.quizapp19


data class Question(
    val questionText: String,
    val options: List<String>,
    val correctAnswer: String,
    val imagePath: String = "",
    val setNumber: Int
)