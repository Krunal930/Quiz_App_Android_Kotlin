package com.example.quiz_app_official

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.widget.Toast

class HomeFragment : Fragment() {

    // Declare global variables
    private lateinit var userNameTextView: TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize global variables
        userNameTextView = view.findViewById(R.id.textViewWelcome)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users") // Correct initialization

        // Get current user name
        getCurrentUserName()

        view.findViewById<Button>(R.id.butn1)?.setOnClickListener {
            checkIfUserHasTakenExam { hasTakenExam ->
                if (hasTakenExam) {
                    Toast.makeText(requireContext(), "You have already given the exam.", Toast.LENGTH_SHORT).show()
                } else {
                    startActivity(Intent(requireActivity(), quiz_main::class.java))
                    requireActivity().finish()
                }
            }
        }

    }

    private fun getCurrentUserName() {
        val userId = auth.currentUser?.uid
        userId?.let {
            database.child(it).child("name").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userName = snapshot.getValue(String::class.java) ?: "User"
                        userNameTextView.text = "Welcome, $userName!" // Display user name
                    } else {
                        userNameTextView.text = "User not found."
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    userNameTextView.text = "Failed to retrieve user name."
                }
            })
        }
    }


    private fun checkIfUserHasTakenExam(callback: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid
        userId?.let {
            // Query the user_answers table to check if the current userId exists
            database.child("user_answers").orderByChild("userId").equalTo(it)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        // Check if any record exists with the matching userId
                        callback(snapshot.exists())
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle possible errors
                        Log.e("FirebaseError", "Error retrieving data: ${error.message}")
                        callback(false)
                    }
                })
        } ?: callback(false) // If userId is null, callback with false
    }

}
