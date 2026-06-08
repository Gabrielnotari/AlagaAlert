package br.com.fiap.alagaalert.screens

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.fiap.alagaalert.databinding.ActivityReportBinding

class ReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvBack.setOnClickListener { finish() }

        binding.btnGetLocation.setOnClickListener {
            // Simula pegar localização — você pode expandir com GPS real depois
            binding.etLocation.setText("São Paulo, SP — Localização atual")
            Toast.makeText(this, "📡 Localização obtida!", Toast.LENGTH_SHORT).show()
        }

        binding.btnSend.setOnClickListener {
            submitReport()
        }
    }

    private fun submitReport() {
        val location = binding.etLocation.text.toString().trim()
        val observation = binding.etObservation.text.toString().trim()

        val waterLevel = when (binding.rgWaterLevel.checkedRadioButtonId) {
            binding.rbCalcada.id  -> "Calçada"
            binding.rbRua.id      -> "Rua"
            binding.rbVeiculos.id -> "Veículos"
            else                  -> null
        }

        // Validações
        if (location.isEmpty()) {
            binding.etLocation.error = "Informe a localização"
            return
        }
        if (waterLevel == null) {
            Toast.makeText(this, "Selecione o nível da água", Toast.LENGTH_SHORT).show()
            return
        }

        // Aqui você poderia enviar para um backend — por ora mostra confirmação
        Toast.makeText(
            this,
            "✅ Relato enviado!\nLocal: $location\nNível: $waterLevel",
            Toast.LENGTH_LONG
        ).show()

        // Limpa o formulário
        binding.etLocation.setText("")
        binding.etObservation.setText("")
        binding.rgWaterLevel.clearCheck()

        // Volta para a tela anterior
        finish()
    }
}