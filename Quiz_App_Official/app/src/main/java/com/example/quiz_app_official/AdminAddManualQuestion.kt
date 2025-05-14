package com.example.quiz_app_official

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AdminAddManualQuestion : AppCompatActivity() {

    // Firebase database reference
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_manual_question)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("Questions")

        val questionField = findViewById<EditText>(R.id.editTextQuestion)
        val optionAField = findViewById<EditText>(R.id.editTextOptionA)
        val optionBField = findViewById<EditText>(R.id.editTextOptionB)
        val optionCField = findViewById<EditText>(R.id.editTextOptionC)
        val optionDField = findViewById<EditText>(R.id.editTextOptionD)
        val answerField = findViewById<EditText>(R.id.editTextAnswer)
        val submitButton = findViewById<Button>(R.id.buttonSubmitQuestion)

        // Handle the form submission
        submitButton.setOnClickListener {
            val question = questionField.text.toString()
            val optionA = optionAField.text.toString()
            val optionB = optionBField.text.toString()
            val optionC = optionCField.text.toString()
            val optionD = optionDField.text.toString()
            val answer = answerField.text.toString()

            // Input validation
            if (question.isEmpty() || optionA.isEmpty() || optionB.isEmpty() ||
                optionC.isEmpty() || optionD.isEmpty() || answer.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (answer !in listOf("A", "B", "C", "D")) {
                Toast.makeText(this, "Answer must be A, B, C, or D", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a Question object
            val questionId = database.push().key
            val questionObject = Question(
                questionId!!,
                question, optionA, optionB, optionC, optionD, answer
            )

            // Store the question in Firebase
            questionId?.let {
                database.child(it).setValue(questionObject).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Question added successfully", Toast.LENGTH_SHORT).show()
                        // Clear the fields after submission
                        questionField.text.clear()
                        optionAField.text.clear()
                        optionBField.text.clear()
                        optionCField.text.clear()
                        optionDField.text.clear()
                        answerField.text.clear()
                    } else {
                        Toast.makeText(this, "Failed to add question", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
