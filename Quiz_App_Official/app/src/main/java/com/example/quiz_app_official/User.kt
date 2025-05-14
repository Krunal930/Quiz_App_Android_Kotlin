package com.example.quiz_app_official


data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val role: Int = 0 // 0 for user, 1 for admin
)
