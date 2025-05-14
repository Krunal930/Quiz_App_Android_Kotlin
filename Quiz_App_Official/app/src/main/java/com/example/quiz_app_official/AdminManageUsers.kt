package com.example.quiz_app_official


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class AdminManageUsers : AppCompatActivity() {

    private lateinit var usersRecyclerView: RecyclerView
    private lateinit var usersList: MutableList<User>
    private lateinit var usersAdapter: UsersAdapter
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_manage_users)

        usersRecyclerView = findViewById(R.id.usersRecyclerView)
        usersRecyclerView.layoutManager = LinearLayoutManager(this)

        usersList = mutableListOf()
        usersAdapter = UsersAdapter(usersList) { user ->
            deleteUser(user)
        }

        usersRecyclerView.adapter = usersAdapter

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().getReference("Users")

        loadUsers()
    }

    private fun loadUsers() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                usersList.clear()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null && user.role != 1) { // Only show users with role 0 (non-admin)
                        usersList.add(user)
                    }
                }
                usersAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@AdminManageUsers, "Failed to load users", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun deleteUser(user: User) {
        database.child(user.id).removeValue().addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to delete user", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
