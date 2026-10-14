package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubdeportivo.db.DatabaseHelper

class HomeActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        dbHelper = DatabaseHelper.getInstance(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val userId = intent.getIntExtra("USER_ID", -1)
        val userNameExtra = intent.getStringExtra("USER_NAME")

        val lblUsername = findViewById<TextView>(R.id.lblUsername)
        if (!userNameExtra.isNullOrEmpty()) {
            lblUsername.text = userNameExtra
        } else if (userId != -1) {
            val user = dbHelper.getUserById(userId)
            if (user != null) {
                lblUsername.text = user.nombreCompleto
            }
        } else {
            val defaultUser = dbHelper.getUserById(1)
            lblUsername.text = defaultUser?.nombreCompleto ?: "Carlos Mendoza"
        }

        // Cartel de vencimientos de hoy
        val lblHomeOverdueBanner = findViewById<TextView>(R.id.lblHomeOverdueBanner)
        val todayCount = dbHelper.getCountVencimientoHoy()

        if (todayCount > 0) {
            val bannerMessage = if (todayCount == 1) {
                "Hoy vence la cuota de 1 socio"
            } else {
                "Hoy vencen las cuotas de $todayCount socios"
            }
            lblHomeOverdueBanner.text = bannerMessage
            lblHomeOverdueBanner.visibility = View.VISIBLE
        } else {
            lblHomeOverdueBanner.visibility = View.GONE
        }

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
