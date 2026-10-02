package com.example.clubdeportivo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
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

        val rgPaymentMethod = findViewById<RadioGroup>(R.id.rgPaymentMethod)
        val rgPaymentParts = findViewById<RadioGroup>(R.id.rgPaymentParts)

        val rbCash = findViewById<RadioButton>(R.id.rbCash)
        val rbParts3 = findViewById<RadioButton>(R.id.rbParts3)
        val rbParts6 = findViewById<RadioButton>(R.id.rbParts6)

//        Escuchar cambios en el metodo de pago
        rgPaymentMethod.setOnCheckedChangeListener { _, checkedId ->

            if (checkedId == R.id.rbCash) {
                // Si elige efectivo, deshabilita las cuotas
                rbParts3.isEnabled = false
                rbParts6.isEnabled = false

                // Limpia cualquier selección anterior
                rgPaymentParts.clearCheck()

            } else {
                // Si elige tarjeta, habilita las cuotas
                rbParts3.isEnabled = true
                rbParts6.isEnabled = true
            }
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