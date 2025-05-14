package com.example.quiz_app_official

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AnswerItemAdapter(
    private val questions: List<QuizQuestion>,
    private val userAnswers: Map<String, String>
) : RecyclerView.Adapter<AnswerItemAdapter.AnswerViewHolder>() {

    class AnswerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val questionTextView: TextView = view.findViewById(R.id.questionTextView)
        val userAnswerTextView: TextView = view.findViewById(R.id.userAnswerTextView)
        val correctAnswerTextView: TextView = view.findViewById(R.id.correctAnswerTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnswerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_answer_item_adapter, parent, false)
        return AnswerViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnswerViewHolder, position: Int) {
        val question = questions[position]
        val userAnswer = userAnswers[question.id]

        holder.questionTextView.text = question.question
        holder.correctAnswerTextView.text = "Correct Answer: ${question.answer}"

        // Display user's answer and highlight it based on correctness
        if (userAnswer == question.answer) {
            holder.userAnswerTextView.text = "Your Answer: $userAnswer"
            holder.userAnswerTextView.setTextColor(Color.GREEN)
        } else {
            holder.userAnswerTextView.text = "Your Answer: $userAnswer"
            holder.userAnswerTextView.setTextColor(Color.RED)
            holder.correctAnswerTextView.setTextColor(Color.GREEN) // Highlight correct answer
        }
    }

    override fun getItemCount(): Int = questions.size
}
