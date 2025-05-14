package com.example.quiz_app_official

data class User(
    var id: String? = null,
    var name: String? = null,
    var email: String? = null,
    var role: Int? = null // Add this property for Firebase to map the role
) {
    constructor() : this(null, null, null, null) // Firebase requires an empty constructor
}
