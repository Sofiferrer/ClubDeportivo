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
import com.example.clubdeportivo.db.DatabaseHelper
import com.example.clubdeportivo.models.Alumno
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CreateStudentActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_create_student)

        dbHelper = DatabaseHelper.getInstance(this)

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
            val name = inputName.text.toString().trim()
            val surname = inputSurname.text.toString().trim()
            val dni = inputId.text.toString().trim()

            if (name.isNotEmpty() && surname.isNotEmpty() && dni.isNotEmpty()) {
                val existing = dbHelper.getAlumnoByDni(dni)
                if (existing != null) {
                    Toast.makeText(
                        this,
                        "Ya existe un alumno con el DNI ingresado",
                        Toast.LENGTH_LONG
                    ).show()
                    return@setOnClickListener
                }

                val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val newAlumno = Alumno(
                    nombre = name,
                    apellido = surname,
                    dni = dni,
                    esSocio = swIsPartner.isChecked,
                    aptoFisico = swHealthCheckDone.isChecked,
                    fechaAlta = todayStr,
                    fechaVencimiento = null
                )

                val resultId = dbHelper.insertAlumno(newAlumno)
                if (resultId > 0) {
                    clear()
                    Toast.makeText(
                        this,
                        R.string.create_success,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Error al guardar el alumno",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    R.string.app_login_error,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
