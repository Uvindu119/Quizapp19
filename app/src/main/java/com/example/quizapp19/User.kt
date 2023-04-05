package com.example.quizapp19

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val password: String,
    val questionsAnswered: Int,
    val questionsetsAnswered: Int,
    val correctAnswers: Int,
    val incorrectAnswers: Int,
    val correctPercentage:Float,
    val incorrectPercentage:Float,
    val gradeA: Int,
    val gradeB: Int,
    val gradeC: Int,
    val gradeW: Int,
    val specializedCategories: String?
)