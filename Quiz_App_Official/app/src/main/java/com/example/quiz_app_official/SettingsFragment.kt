package com.example.quiz_app_official

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.btnAboutUs)?.setOnClickListener {
            startActivity(Intent(requireActivity(), pages_aboutus::class.java))
            requireActivity().finish()
        }

        view.findViewById<Button>(R.id.btnPrivacyPolicy)?.setOnClickListener {
            startActivity(Intent(requireActivity(), pages_privacypolicy::class.java))
            requireActivity().finish()
        }

        view.findViewById<Button>(R.id.btnHelp)?.setOnClickListener {
            startActivity(Intent(requireActivity(), pages_help::class.java))
            requireActivity().finish()
        }

        view.findViewById<Button>(R.id.btnLogout)?.setOnClickListener {
            val sharedPreferences = requireActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().apply()
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }
}