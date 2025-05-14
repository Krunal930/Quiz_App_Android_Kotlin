package com.example.quiz_app_official

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AnswersActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnswerItemAdapter
    private lateinit var database: DatabaseReference
    private val questionsList = mutableListOf<QuizQuestion>()
    private val userAnswers = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answers)

        recyclerView = findViewById(R.id.recyclerViewAnswers)
        recyclerView.layoutManager = LinearLayoutManager(this)

        loadUserAnswers()
    }

    private fun loadUserAnswers() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        database = FirebaseDatabase.getInstance().getReference("user_answers")

        // Fetch user's answers
        val query = database.orderByChild("userId").equalTo(userId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (answerSnapshot in snapshot.children) {
                    val questionId = answerSnapshot.child("questionId").getValue(String::class.java) ?: continue
                    val selectedOption = answerSnapshot.child("selectedOption").getValue(String::class.java) ?: continue
                    userAnswers[questionId] = selectedOption
                }
                loadQuestions()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AnswersActivity, "Failed to load answers", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadQuestions() {
        database = FirebaseDatabase.getInstance().getReference("Questions")
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (questionSnapshot in snapshot.children) {
                    val question = questionSnapshot.getValue(QuizQuestion::class.java)
                    question?.let { questionsList.add(it) }
                }

                adapter = AnswerItemAdapter(questionsList, userAnswers)
                recyclerView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AnswersActivity, "Failed to load questions", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
