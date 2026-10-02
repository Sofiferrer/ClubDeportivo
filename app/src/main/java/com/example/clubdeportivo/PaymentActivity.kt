package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_payment)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnPay = findViewById<Button>(R.id.btnPay)
        btnPay.setOnClickListener {
            Toast.makeText(
                this,
                R.string.payment_success,
                Toast.LENGTH_SHORT
            ).show()
            val intent = Intent(this, MembersListActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnCancel = findViewById<Button>(R.id.btnCancel)
        btnCancel.setOnClickListener {
            val intent = Intent(this, MembersListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}