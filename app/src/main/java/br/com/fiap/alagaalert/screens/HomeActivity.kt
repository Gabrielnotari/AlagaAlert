package br.com.fiap.alagaalert.screens.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.fiap.alagaalert.data.api.RetrofitInstance
import br.com.fiap.alagaalert.data.model.RiskLevel
import br.com.fiap.alagaalert.databinding.ActivityHomeBinding
import br.com.fiap.alagaalert.screens.AlertActivity
import br.com.fiap.alagaalert.screens.MapActivity
import br.com.fiap.alagaalert.screens.ReportActivity
import br.com.fiap.alagaalert.utils.RiskCalculator
import kotlinx.coroutines.launch
import kotlin.jvm.java

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val API_KEY = "dd4fd3f8f71c0b0dc46cb24b2dfc0c78"
    private val CITY = "São Paulo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvCityName.text = "📍 $CITY"

        loadWeatherAlert()

        binding.btnMap.setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }
        binding.btnAlert.setOnClickListener {
            startActivity(Intent(this, AlertActivity::class.java))
        }
        binding.btnReport.setOnClickListener {
            startActivity(Intent(this, ReportActivity::class.java))
        }
    }

    private fun loadWeatherAlert() {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getWeatherByCity(CITY, API_KEY)
                val rain = response.rain?.oneHour ?: 0.0
                val risk = RiskCalculator.calculateRisk(rain)

                when (risk) {
                    RiskLevel.HIGH -> {
                        binding.tvAlertTitle.text = "⚠️ Alerta ativo na região"
                        binding.tvAlertMessage.text =
                            "Chuva intensa: ${rain}mm/h. Evite zonas de risco."
                    }
                    RiskLevel.MEDIUM -> {
                        binding.tvAlertTitle.text = "⚡ Atenção — chuva moderada"
                        binding.tvAlertMessage.text =
                            "Chuva de ${rain}mm/h. Fique atento a alagamentos."
                    }
                    RiskLevel.LOW -> {
                        binding.tvAlertTitle.text = "✅ Sem alertas no momento"
                        binding.tvAlertMessage.text =
                            "Condições climáticas estáveis em $CITY."
                    }
                }
            } catch (e: Exception) {
                binding.tvAlertTitle.text = "⚠️ Sem conexão"
                binding.tvAlertMessage.text = "Verifique sua internet."
            }
        }
    }
}