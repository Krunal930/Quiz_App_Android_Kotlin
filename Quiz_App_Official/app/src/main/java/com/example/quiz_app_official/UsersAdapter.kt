package com.example.quiz_app_official

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UsersAdapter(
    private val usersList: List<User>,
    private val onDeleteClick: (User) -> Unit
) : RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = usersList[position]
        holder.bind(user, position, onDeleteClick)
    }

    override fun getItemCount(): Int = usersList.size

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val userIndex: TextView = itemView.findViewById(R.id.userIndex)
        private val userName: TextView = itemView.findViewById(R.id.userName)
        private val userImage: ImageView = itemView.findViewById(R.id.userImage)
        private val deleteButton: Button = itemView.findViewById(R.id.deleteButton)

        fun bind(user: User, position: Int, onDeleteClick: (User) -> Unit) {
            userIndex.text = (position + 1).toString()
            userName.text = user.name

            deleteButton.setOnClickListener {
                onDeleteClick(user)
            }
        }
    }
}
