package com.example.quiz_app_official

import android.content.Intent
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AdminQuestions : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var questionAdapter: QuestionAdapter
    private lateinit var questionList: MutableList<QuizQuestion>
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_questions)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        questionList = mutableListOf()
        questionAdapter = QuestionAdapter(questionList)  // Ensure the adapter accepts List<QuizQuestion>
        recyclerView.adapter = questionAdapter

        database = FirebaseDatabase.getInstance().getReference("Questions")

        // Fetch questions from Firebase
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                questionList.clear()
                for (questionSnapshot in snapshot.children) {
                    val question = questionSnapshot.getValue(QuizQuestion::class.java)  // Ensure QuizQuestion is used
                    question?.let { questionList.add(it) }
                }
                questionAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}
