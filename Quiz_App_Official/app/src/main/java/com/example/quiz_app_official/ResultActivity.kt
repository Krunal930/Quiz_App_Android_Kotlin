package com.example.quiz_app_official

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ResultActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var scoreTextView: TextView
    private lateinit var totalScoreTextView: TextView
    private lateinit var buttonBackToHome: Button
    private lateinit var buttonSeeAnswers: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        scoreTextView = findViewById(R.id.scoreTextView)
        totalScoreTextView = findViewById(R.id.totalScoreTextView)
        buttonBackToHome = findViewById(R.id.buttonBackToHome)
        buttonSeeAnswers = findViewById(R.id.buttonSeeAnswers)

        calculateUserScore()

        buttonBackToHome.setOnClickListener {
            val intent = Intent(this, UserHomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // In ResultActivity.kt
        buttonSeeAnswers.setOnClickListener {
            val intent = Intent(this, AnswersActivity::class.java)
            startActivity(intent)
        }
    }

    private fun calculateUserScore() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        // Reference to the "user_answers" table in Firebase
        database = FirebaseDatabase.getInstance().getReference("user_answers")

        // Query to get answers of the current user
        val query = database.orderByChild("userId").equalTo(userId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalScore = 0
                for (data in snapshot.children) {
                    val mark = data.child("mark").getValue(Int::class.java) ?: 0
                    totalScore += mark
                }

                // Display the total score
                totalScoreTextView.text = totalScore.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ResultActivity, "Failed to calculate score", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
