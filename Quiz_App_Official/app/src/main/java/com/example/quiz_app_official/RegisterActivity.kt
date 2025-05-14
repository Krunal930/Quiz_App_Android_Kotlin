package com.example.quiz_app_official

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var nameField: EditText
    private lateinit var emailField: EditText
    private lateinit var passwordField: EditText
    private lateinit var confirmPasswordField: EditText
    private lateinit var registerButton: Button
    private lateinit var loginButton: Button

    // Firebase Authentication
    private lateinit var mAuth: FirebaseAuth

    // Firebase Realtime Database
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()

        // Initialize Firebase Realtime Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference("Users")

        // Initialize UI elements
        nameField = findViewById(R.id.name)
        emailField = findViewById(R.id.email)
        passwordField = findViewById(R.id.password)
        confirmPasswordField = findViewById(R.id.confirm_password)
        registerButton = findViewById(R.id.register_button)
        loginButton = findViewById(R.id.login_button)

        // Register button click listener
        registerButton.setOnClickListener {
            registerUser()
        }

        // Login button click listener (for existing users)
        loginButton.setOnClickListener {
            // Open login activity (if implemented)
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }

    private fun registerUser() {
        val name = nameField.text.toString().trim()
        val email = emailField.text.toString().trim()
        val password = passwordField.text.toString().trim()
        val confirmPassword = confirmPasswordField.text.toString().trim()

        // Validate form fields
        if (TextUtils.isEmpty(name)) {
            nameField.error = "Name is required."
            return
        }
        if (TextUtils.isEmpty(email)) {
            emailField.error = "Email is required."
            return
        }
        if (TextUtils.isEmpty(password)) {
            passwordField.error = "Password is required."
            return
        }
        if (password != confirmPassword) {
            confirmPasswordField.error = "Passwords do not match."
            return
        }

        // Create user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Get the user's unique ID
                    val userId = mAuth.currentUser?.uid

                    // Create a new user object
                    val user = hashMapOf(
                        "id" to userId,
                        "name" to name,
                        "role" to 0, // Default role (can be 0 or 1 based on your logic)
                        "email" to email,
                        "password" to password // Avoid storing plain text passwords in production
                    )

                    // Save user to Firebase Realtime Database
                    userId?.let {
                        mDatabase.child(it).setValue(user)
                            .addOnCompleteListener { databaseTask ->
                                if (databaseTask.isSuccessful) {
                                    Toast.makeText(this@RegisterActivity, "Registration Successful!", Toast.LENGTH_SHORT).show()
                                    // Redirect to login or main activity
                                    startActivity(Intent(this@RegisterActivity, UserHomeActivity::class.java))
                                } else {
                                    Toast.makeText(this@RegisterActivity, "Database Error: ${databaseTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                } else {
                    Toast.makeText(this@RegisterActivity, "Authentication Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
