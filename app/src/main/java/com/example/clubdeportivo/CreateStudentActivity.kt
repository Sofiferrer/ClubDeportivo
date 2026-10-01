package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreateStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create_student)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        val inputName = findViewById<EditText>(R.id.inputName)
        fun isNameValid(): Boolean {
            return inputName.text.isNotEmpty()
        }

        val inputSurname = findViewById<EditText>(R.id.inputSurname)
        fun isSurnameValid(): Boolean {
            return inputSurname.text.isNotEmpty()
        }

        val inputId = findViewById<EditText>(R.id.inputId)
        fun isIDValid(): Boolean {
            return inputId.text.isNotEmpty()
        }

        val swIsPartner = findViewById<SwitchCompat>(R.id.swIsPartner)

        val swHealthCheckDone = findViewById<SwitchCompat>(R.id.swHealthCheckDone)
        fun isHealthCheckDone(): Boolean {
            return swHealthCheckDone.isChecked
        }

        fun clear() {
            inputName.text.clear()
            inputSurname.text.clear()
            inputId.text.clear()
            swIsPartner.isChecked = false
            swHealthCheckDone.isChecked = false
        }

        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {
            if(isNameValid() && isSurnameValid() && isIDValid()) {
                clear()
                Toast.makeText(
                    this,
                    R.string.create_success,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else {
                Toast.makeText(
                    this,
                    R.string.app_login_error,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}