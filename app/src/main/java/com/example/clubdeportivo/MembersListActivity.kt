package com.example.clubdeportivo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.clubdeportivo.db.DatabaseHelper

class MembersListActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_members_list)

        dbHelper = DatabaseHelper.getInstance(this)

        val lblSectionTitle = findViewById<TextView>(R.id.lblSectionTitle)
        lblSectionTitle?.setText(R.string.member_section_title)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack?.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        loadMembers()
    }

    private fun loadMembers() {
        val containerMembers = findViewById<LinearLayout>(R.id.containerMembers)
        containerMembers.removeAllViews()

        val alumnosList = dbHelper.getAllAlumnos()

        if (alumnosList.isEmpty()) {
            val emptyTextView = TextView(this).apply {
                text = "No hay alumnos registrados"
                textSize = 16f
                setPadding(0, 32, 0, 0)
            }
            containerMembers.addView(emptyTextView)
            return
        }

        val inflater = LayoutInflater.from(this)
        for (alumno in alumnosList) {
            val cardView = inflater.inflate(R.layout.member_card, containerMembers, false)

            val lblName = cardView.findViewById<TextView>(R.id.lblMemberName)
            val lblDni = cardView.findViewById<TextView>(R.id.lblMemberDni)
            val lblExpiry = cardView.findViewById<TextView>(R.id.lblMemberExpiry)
            val lblType = cardView.findViewById<TextView>(R.id.lblMemberType)
            val lblStatusBadge = cardView.findViewById<TextView>(R.id.lblMemberStatusBadge)
            val btnViewCredential = cardView.findViewById<Button>(R.id.btnViewCredential)
            val btnPay = cardView.findViewById<Button>(R.id.btnPay)
            val btnToggleStatus = cardView.findViewById<Button>(R.id.btnToggleStatus)

            lblName.text = alumno.nombreCompleto
            lblDni.text = "DNI: ${alumno.dni}"
            lblExpiry.text = if (alumno.fechaVencimiento != null) {
                "Vence: ${alumno.fechaVencimiento}"
            } else {
                "Vence: Sin pago registrado"
            }
            lblType.text = if (alumno.esSocio) "SOCIO" else "NO SOCIO"

            if (alumno.habilitado) {
                lblStatusBadge.visibility = View.GONE
                btnToggleStatus.text = "Baja"
                btnToggleStatus.setBackgroundColor(Color.parseColor("#EF4444")) // Rojo
            } else {
                lblStatusBadge.visibility = View.VISIBLE
                btnToggleStatus.text = "Alta"
                btnToggleStatus.setBackgroundColor(Color.parseColor("#10B981")) // Verde
            }

            btnViewCredential.setOnClickListener {
                val intent = Intent(this, MemberCredentialActivity::class.java).apply {
                    putExtra("ALUMNO_ID", alumno.id)
                }
                startActivity(intent)
                finish()
            }

            btnPay.setOnClickListener {
                val intent = Intent(this, PaymentActivity::class.java).apply {
                    putExtra("ALUMNO_ID", alumno.id)
                }
                startActivity(intent)
                finish()
            }

            btnToggleStatus.setOnClickListener {
                if (alumno.habilitado) {
                    AlertDialog.Builder(this)
                        .setTitle("Confirmar baja")
                        .setMessage("¿Esta seguro que desea dar de baja al alumno ${alumno.nombreCompleto}?")
                        .setPositiveButton("Sí, dar de baja") { _, _ ->
                            dbHelper.setAlumnoHabilitado(alumno.id, false)
                            Toast.makeText(this, "Alumno dado de baja exitosamente", Toast.LENGTH_SHORT).show()
                            loadMembers()
                        }
                        .setNegativeButton("Cancelar", null)
                        .show()
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("Confirmar alta")
                        .setMessage("¿Esta seguro que desea dar de alta al alumno ${alumno.nombreCompleto}?")
                        .setPositiveButton("Sí, dar de alta") { _, _ ->
                            dbHelper.setAlumnoHabilitado(alumno.id, true)
                            Toast.makeText(this, "Alumno dado de alta exitosamente", Toast.LENGTH_SHORT).show()
                            loadMembers()
                        }
                        .setNegativeButton("Cancelar", null)
                        .show()
                }
            }

            containerMembers.addView(cardView)
        }
    }
}
