package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MembersListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_members_list)

        val lblSectionTitle = findViewById<TextView>(R.id.lblSectionTitle)
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnViewCredential = findViewById<Button>(R.id.btnViewCredential)
        val btnPay = findViewById<Button>(R.id.btnPay)

        lblSectionTitle.setText(R.string.member_section_title)

        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnViewCredential.setOnClickListener {
            val intent = Intent(this, MemberCredentialActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}