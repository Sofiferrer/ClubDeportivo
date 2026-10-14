package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class CreateMemberActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_member)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        val inputName = findViewById<EditText>(R.id.inputName)
        val inputSurname = findViewById<EditText>(R.id.inputSurname)
        val inputId = findViewById<EditText>(R.id.inputId)

        val swIsPartner = findViewById<SwitchCompat>(R.id.swIsPartner)
        val swHealthCheckDone = findViewById<SwitchCompat>(R.id.swHealthCheckDone)

        fun clear() {
            inputName.text.clear()
            inputSurname.text.clear()
            inputId.text.clear()
            swIsPartner.isChecked = false
            swHealthCheckDone.isChecked = false
        }

        val btnSave = findViewById<Button>(R.id.btnSave)
        btnSave.setOnClickListener {
            if (inputName.text.isNotEmpty() &&
                inputSurname.text.isNotEmpty() &&
                inputId.text.isNotEmpty()
            ) {
                val name = inputName.text.toString().trim()
                val surname = inputSurname.text.toString().trim()
                val id = inputId.text.toString().trim()
                val isPartner = swIsPartner.isChecked
                val healthCheckDone = swHealthCheckDone.isChecked

                val member = Member(
                    name = name,
                    surname = surname,
                    id = id,
                    isPartner = isPartner,
                    healthCheckDone = healthCheckDone
                )

                Data.members.add(member)
                Log.d("Nuevo MIEMBRO", member.toString())

                Toast.makeText(this, R.string.create_success, Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, HomeActivity::class.java))
                finish()
                clear()

            } else {
                Toast.makeText(this, R.string.app_login_error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}