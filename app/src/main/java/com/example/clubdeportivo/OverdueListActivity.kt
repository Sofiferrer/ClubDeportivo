package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubdeportivo.db.DatabaseHelper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OverdueListActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_overdue_list)

        dbHelper = DatabaseHelper.getInstance(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<TextView>(R.id.lblSectionTitle).setText(R.string.overdue_section_title)

        val dateFormat = SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("es"))
        val currentDateFormatted = dateFormat.format(Date()).replaceFirstChar { it.uppercase() }
        findViewById<TextView>(R.id.lblToday).text = currentDateFormatted

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        loadOverdueList()
    }

    private fun loadOverdueList() {
        val containerOverdue = findViewById<LinearLayout>(R.id.containerOverdue)
        containerOverdue.removeAllViews()

        val todaySql = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val overdueList = dbHelper.getOverdueAlumnos(todaySql)

        val summaryView = findViewById<TextView>(R.id.lblOverdueSummary)
        val count = overdueList.size

        if (count > 0) {
            summaryView.text = if (count == 1) {
                "Cuota vencida o pendiente: 1 alumno"
            } else {
                "Cuotas vencidas o pendientes: $count alumnos"
            }
            summaryView.visibility = View.VISIBLE
        } else {
            summaryView.visibility = View.GONE
        }

        if (overdueList.isEmpty()) {
            val emptyTextView = TextView(this).apply {
                text = "No hay alumnos con cuotas vencidas hoy"
                textSize = 16f
                setPadding(0, 32, 0, 0)
            }
            containerOverdue.addView(emptyTextView)
            return
        }

        val inflater = LayoutInflater.from(this)
        for (alumno in overdueList) {
            val cardView = inflater.inflate(R.layout.member_card_overdue, containerOverdue, false)

            val lblName = cardView.findViewById<TextView>(R.id.lblOverdueName)
            val lblDni = cardView.findViewById<TextView>(R.id.lblOverdueDni)
            val lblType = cardView.findViewById<TextView>(R.id.lblOverdueType)
            val lblLastPayment = cardView.findViewById<TextView>(R.id.lblOverdueLastPayment)
            val btnPay = cardView.findViewById<Button>(R.id.btnOverduePay)

            lblName.text = alumno.nombreCompleto
            lblDni.text = "DNI: ${alumno.dni}"
            lblType.text = if (alumno.esSocio) "SOCIO" else "NO SOCIO"
            lblLastPayment.text = alumno.fechaVencimiento ?: "Sin pagos registrados"

            btnPay.setOnClickListener {
                val intent = Intent(this, PaymentActivity::class.java).apply {
                    putExtra("ALUMNO_ID", alumno.id)
                }
                startActivity(intent)
                finish()
            }

            containerOverdue.addView(cardView)
        }
    }
}
