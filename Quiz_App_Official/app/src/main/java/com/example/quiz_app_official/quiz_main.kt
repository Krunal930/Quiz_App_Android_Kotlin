package com.example.quiz_app_official

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class quiz_main : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var questionsList: MutableList<QuizQuestion>
    private var currentQuestionIndex = 0

    // To store user selected answers (question ID -> selected option)
    private val userAnswers = HashMap<String, String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz_main)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("Questions")
        questionsList = mutableListOf()

        // Load questions from Firebase
        loadQuestions()

        // Button listener for next question or submit quiz
        val nextButton = findViewById<Button>(R.id.button)
        nextButton.setOnClickListener {
            if (currentQuestionIndex < questionsList.size - 1) {
                currentQuestionIndex++
                displayQuestion(questionsList[currentQuestionIndex])

                // Change button text to "Submit Quiz" for the last question
                if (currentQuestionIndex == questionsList.size - 1) {
                    nextButton.text = "Submit Quiz"
                }
            } else {
                submitQuiz()
                val intent = Intent(this, ResultActivity::class.java)
                startActivity(intent)
            }
        }
    }


    private fun loadQuestions() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (questionSnapshot in snapshot.children) {
                    val question = questionSnapshot.getValue(QuizQuestion::class.java)
                    question?.let { questionsList.add(it) }
                }
                if (questionsList.isNotEmpty()) {
                    displayQuestion(questionsList[currentQuestionIndex])
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@quiz_main, "Failed to load questions", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun displayQuestion(question: QuizQuestion) {
        val questionTextView = findViewById<TextView>(R.id.textView)
        val optionATextView = findViewById<TextView>(R.id.textViewOptionA)
        val optionBTextView = findViewById<TextView>(R.id.textViewOptionB)
        val optionCTextView = findViewById<TextView>(R.id.textViewOptionC)
        val optionDTextView = findViewById<TextView>(R.id.textViewOptionD)

        // Set question and options
        questionTextView.text = question.question
        optionATextView.text = question.optionA
        optionBTextView.text = question.optionB
        optionCTextView.text = question.optionC
        optionDTextView.text = question.optionD

        // Reset background color for all options
        resetOptionColors()

        // Set click listeners for options
        optionATextView.setOnClickListener { handleOptionSelection("A", optionATextView, question.id) }
        optionBTextView.setOnClickListener { handleOptionSelection("B", optionBTextView, question.id) }
        optionCTextView.setOnClickListener { handleOptionSelection("C", optionCTextView, question.id) }
        optionDTextView.setOnClickListener { handleOptionSelection("D", optionDTextView, question.id) }
    }

    private fun handleOptionSelection(option: String, selectedTextView: TextView, questionId: String) {
        // Store the selected option for the current question
        userAnswers[questionId] = option

        // Reset background color for all options
        resetOptionColors()

        // Change background color of the selected option
        selectedTextView.setBackgroundColor(Color.parseColor("#D3D3D3")) // Light gray

//        Toast.makeText(this, "You selected: $option", Toast.LENGTH_SHORT).show()
    }

    private fun resetOptionColors() {
        val optionATextView = findViewById<TextView>(R.id.textViewOptionA)
        val optionBTextView = findViewById<TextView>(R.id.textViewOptionB)
        val optionCTextView = findViewById<TextView>(R.id.textViewOptionC)
        val optionDTextView = findViewById<TextView>(R.id.textViewOptionD)

        // Reset the background color to default (you can change this color as per your design)
        optionATextView.setBackgroundColor(Color.parseColor("#FFFFFF")) // White
        optionBTextView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        optionCTextView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        optionDTextView.setBackgroundColor(Color.parseColor("#FFFFFF"))
    }

    private fun submitQuiz() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val userAnswersRef = FirebaseDatabase.getInstance().getReference("user_answers")


        for ((questionId, selectedOption) in userAnswers) {
            // Fetch the question from the questionsList to get the correct answer
            val correctAnswer = questionsList.find { it.id == questionId }?.answer

            // Calculate mark (1 if correct, 0 if incorrect)
            val mark = if (correctAnswer == selectedOption) 1 else 0

            // Create a map to store user answers
            val userAnswerData = mapOf(
                "userId" to userId,
                "questionId" to questionId,
                "selectedOption" to selectedOption,
                "mark" to mark
            )

            // Push the user's answer into the user_answers table in Firebase
            userAnswersRef.push().setValue(userAnswerData)
        }

        Toast.makeText(this, "Your answers have been submitted.", Toast.LENGTH_LONG).show()

    }

}
