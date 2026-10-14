package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.clubdeportivo.db.DatabaseHelper
import com.example.clubdeportivo.models.Pago
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class PaymentActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var alumnoId: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_payment)

        dbHelper = DatabaseHelper.getInstance(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        alumnoId = intent.getIntExtra("ALUMNO_ID", 1)
        val alumno = dbHelper.getAlumnoById(alumnoId)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val displayDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val calendar = Calendar.getInstance()
        val todayDate = calendar.time
        val periodoDesdeStr = dateFormat.format(todayDate)
        val periodoDesdeDisplay = displayDateFormat.format(todayDate)

        // Set expiry to 30 days from today
        calendar.add(Calendar.DAY_OF_MONTH, 30)
        val expiryDate = calendar.time
        val periodoHastaStr = dateFormat.format(expiryDate)
        val periodoHastaDisplay = displayDateFormat.format(expiryDate)

        val montoCuota = if (alumno?.esSocio == true) 5000.0 else 2500.0

        val lblStudentName = findViewById<TextView>(R.id.lblPaymentStudentName)
        val lblAmount = findViewById<TextView>(R.id.lblPaymentAmount)
        val lblPeriod = findViewById<TextView>(R.id.lblPaymentPeriod)

        if (alumno != null) {
            lblStudentName.text = alumno.nombreCompleto
        }
        lblAmount.text = "$ ${String.format(Locale.US, "%.2f", montoCuota)}"
        lblPeriod.text = "Periodo $periodoDesdeDisplay - $periodoHastaDisplay"

        val rgPaymentMethod = findViewById<RadioGroup>(R.id.rgPaymentMethod)
        val rgPaymentParts = findViewById<RadioGroup>(R.id.rgPaymentParts)
        val rbCash = findViewById<RadioButton>(R.id.rbCash)
        val rbParts3 = findViewById<RadioButton>(R.id.rbParts3)
        val rbParts6 = findViewById<RadioButton>(R.id.rbParts6)

        rgPaymentMethod.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbCash) {
                rbParts3.isEnabled = false
                rbParts6.isEnabled = false
                rgPaymentParts.clearCheck()
            } else {
                rbParts3.isEnabled = true
                rbParts6.isEnabled = true
            }
        }

        val btnPay = findViewById<Button>(R.id.btnPay)
        btnPay.setOnClickListener {
            val metodoPago = if (rbCash.isChecked) "Efectivo" else "Tarjeta"
            var cuotas = 1
            if (!rbCash.isChecked) {
                if (rbParts3.isChecked) {
                    cuotas = 3
                } else if (rbParts6.isChecked) {
                    cuotas = 6
                }
            }

            val pago = Pago(
                alumnoId = alumnoId,
                monto = montoCuota,
                metodoPago = metodoPago,
                cuotas = cuotas,
                periodoDesde = periodoDesdeStr,
                periodoHasta = periodoHastaStr
            )

            val pagoId = dbHelper.insertPago(pago)
            if (pagoId > 0) {
                dbHelper.updateFechaVencimiento(alumnoId, periodoHastaStr)

                Toast.makeText(
                    this,
                    R.string.payment_success,
                    Toast.LENGTH_SHORT
                ).show()

                val intent = Intent(this, MembersListActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(
                    this,
                    "Error al registrar el pago",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val btnCancel = findViewById<Button>(R.id.btnCancel)
        btnCancel.setOnClickListener {
            val intent = Intent(this, MembersListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
