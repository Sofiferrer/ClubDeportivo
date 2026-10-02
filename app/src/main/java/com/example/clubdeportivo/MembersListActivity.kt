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
        lblSectionTitle.setText(R.string.member_section_title)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnViewCredential = findViewById<Button>(R.id.btnViewCredential)
        btnViewCredential.setOnClickListener {
            val intent = Intent(this, MemberCredentialActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnPay = findViewById<Button>(R.id.btnPay)
        btnPay.setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}