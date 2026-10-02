package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val inputUser = findViewById<EditText>(R.id.inputUsername)
        fun userIsValid(): Boolean {
            return inputUser.text.toString().isNotEmpty()
        }

        val inputPassword = findViewById<EditText>(R.id.inputPassword)
        fun passwordIsValid(): Boolean {
            return inputPassword.text.toString().isNotEmpty()
        }

        val btnLogin = findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            if(userIsValid() && passwordIsValid()) {
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            else {
                Toast.makeText(
                    this,
                    R.string.app_login_error,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        val btnBiometric = findViewById<ImageView>(R.id.imgFingerprint)
        btnBiometric.setOnClickListener {
            Toast.makeText(
                this,
                R.string.app_function_unavailable,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}