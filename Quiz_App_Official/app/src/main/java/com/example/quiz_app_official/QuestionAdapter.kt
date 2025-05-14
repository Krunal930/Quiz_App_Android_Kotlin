package com.example.quiz_app_official

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView

class QuestionAdapter(private val questionList: List<QuizQuestion>) : RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_question, parent, false)
        return QuestionViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
        val question = questionList[position]
        holder.bind(question)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    class QuestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvQuestion: TextView = itemView.findViewById(R.id.tvQuestion)
        private val tvOptionA: TextView = itemView.findViewById(R.id.tvOptionA)
        private val tvOptionB: TextView = itemView.findViewById(R.id.tvOptionB)
        private val tvOptionC: TextView = itemView.findViewById(R.id.tvOptionC)
        private val tvOptionD: TextView = itemView.findViewById(R.id.tvOptionD)

        fun bind(quizQuestion: QuizQuestion) {
            tvQuestion.text = quizQuestion.question
            tvOptionA.text = quizQuestion.optionA
            tvOptionB.text = quizQuestion.optionB
            tvOptionC.text = quizQuestion.optionC
            tvOptionD.text = quizQuestion.optionD
        }
    }
}

