package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubdeportivo.db.DatabaseHelper

class MemberCredentialActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_member_credential)

        dbHelper = DatabaseHelper.getInstance(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, MembersListActivity::class.java)
            startActivity(intent)
            finish()
        }

        val alumnoId = intent.getIntExtra("ALUMNO_ID", 1)
        val alumno = dbHelper.getAlumnoById(alumnoId)

        if (alumno != null) {
            val lblSectionTitle = findViewById<TextView>(R.id.lblSectionTitle)
            lblSectionTitle.text = "Credencial ${alumno.nombre}"

            val lblFullName = findViewById<TextView>(R.id.lblStudentFullName)
            lblFullName.text = alumno.nombreCompleto

            val lblDni = findViewById<TextView>(R.id.lblStudentDni)
            lblDni.text = "DNI ${alumno.dni}"

            val lblCategory = findViewById<TextView>(R.id.lblStudentCategory)
            lblCategory.text = if (alumno.esSocio) "SOCIO ACTIVO" else "NO SOCIO"

            val lblStartDate = findViewById<TextView>(R.id.lblStudentStartDate)
            lblStartDate.text = alumno.fechaAlta.ifEmpty { "MARZO 2025" }
        } else {
            findViewById<TextView>(R.id.lblSectionTitle).setText(R.string.credential_section_title)
        }

        val btnSaveCredential = findViewById<Button>(R.id.btnSaveCredential)
        btnSaveCredential.setOnClickListener {
            Toast.makeText(
                this,
                R.string.credential_save_success,
                Toast.LENGTH_SHORT
            ).show()
        }

        val btnShareCredential = findViewById<Button>(R.id.btnShareCredential)
        btnShareCredential.setOnClickListener {
            Toast.makeText(
                this,
                R.string.app_function_unavailable,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
