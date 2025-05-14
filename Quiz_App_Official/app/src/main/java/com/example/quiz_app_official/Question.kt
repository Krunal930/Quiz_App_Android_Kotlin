package com.example.quiz_app_official

data class Question(
    val id: String,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val answer: String
)