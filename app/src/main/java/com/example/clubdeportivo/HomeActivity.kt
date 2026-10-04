package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val lblUsername = findViewById<TextView>(R.id.lblUsername)
        lblUsername.text = "Carlos Mendoza" // JUST FOR TEST //

        val cardAddStudent = findViewById<LinearLayout>(R.id.cardAddStudent)
        cardAddStudent.setOnClickListener {
            val intent = Intent(this, CreateStudentActivity::class.java)
            startActivity(intent)
            finish()
        }
        val cardViewStudents = findViewById<LinearLayout>(R.id.cardViewStudents)
        cardViewStudents.setOnClickListener {
            val intent = Intent(this, MembersListActivity::class.java)
            startActivity(intent)
            finish()
        }

        val cardListOverdue = findViewById<LinearLayout>(R.id.cardListOverdue)
        cardListOverdue.setOnClickListener {
            val intent = Intent(this, OverdueListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}