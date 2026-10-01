package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MemberCredentialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_member_credential)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TextView>(R.id.lblSectionTitle).setText(R.string.credential_section_title)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, MembersListActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnSaveCredential = findViewById<Button>(R.id.btnSaveCredential)
        btnSaveCredential.setOnClickListener {
            Toast.makeText(
                this,
                R.string.credential_save_success,
                Toast.LENGTH_SHORT
            ).show()
        }

        val btnShareCredential = findViewById<Button>(R.id.btnShareCredential)
        btnShareCredential.setOnClickListener {
            Toast.makeText(
                this,
                R.string.app_function_unavailable,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}