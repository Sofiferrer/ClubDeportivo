package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnAddStudent = findViewById<Button>(R.id.btnAddStudent)
        val btnViewStudents = findViewById<Button>(R.id.btnViewStudents)
        val btnListOverdue = findViewById<Button>(R.id.btnListOverdue)

        btnAddStudent.setOnClickListener {
            val intent = Intent(this, CreateStudentActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnViewStudents.setOnClickListener {
            val intent = Intent(this, MembersListActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnListOverdue.setOnClickListener {
            val intent = Intent(this, OverdueListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}