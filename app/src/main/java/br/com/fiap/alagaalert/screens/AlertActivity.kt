package br.com.fiap.alagaalert.screens

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.com.fiap.alagaalert.data.api.RetrofitInstance
import br.com.fiap.alagaalert.data.model.RiskLevel
import br.com.fiap.alagaalert.databinding.ActivityAlertBinding
import br.com.fiap.alagaalert.utils.RiskCalculator
import kotlinx.coroutines.launch

class AlertActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlertBinding
    private val API_KEY = "dd4fd3f8f71c0b0dc46cb24b2dfc0c78"
    private val CITY = "São Paulo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvBack.setOnClickListener { finish() }

        loadWeatherData()
    }

    private fun loadWeatherData() {
        lifecycleScope.launch {
            try {
                val response = RetrofitInstance.api.getWeatherByCity(CITY, API_KEY)
                val rain = response.rain?.oneHour ?: 0.0
                val risk = RiskCalculator.calculateRisk(rain)

                binding.tvCityHeader.text = "📍 $CITY · Atualizado agora"
                binding.tvRain.text = rain.toString()
                binding.tvTemp.text = String.format("%.0f", response.main.temp)
                binding.tvHumidity.text = response.main.humidity.toString()
                binding.tvDescription.text = response.weather.firstOrNull()?.description ?: "--"

                // Nível de risco
                binding.tvRiskLevel.text = RiskCalculator.getRiskLabel(risk)
                binding.progressRisk.progress = when (risk) {
                    RiskLevel.HIGH   -> 90
                    RiskLevel.MEDIUM -> 55
                    RiskLevel.LOW    -> 20
                }

                // Orientações por nível
                binding.tvOrientation.text = when (risk) {
                    RiskLevel.HIGH ->
                        "• Evite áreas baixas e próximas a córregos\n" +
                                "• Não tente atravessar ruas alagadas\n" +
                                "• Siga as orientações da Defesa Civil\n" +
                                "• Ligue 199 em caso de emergência"
                    RiskLevel.MEDIUM ->
                        "• Fique atento a alagamentos na sua região\n" +
                                "• Evite áreas historicamente problemáticas\n" +
                                "• Acompanhe as atualizações climáticas"
                    RiskLevel.LOW ->
                        "• Condições estáveis no momento\n" +
                                "• Continue monitorando as atualizações\n" +
                                "• Em caso de mudança, você será alertado"
                }

            } catch (e: Exception) {
                binding.tvCityHeader.text = "Erro ao carregar dados"
                binding.tvRiskLevel.text = "⚠️ Sem conexão com a internet"
                binding.tvOrientation.text = "Verifique sua conexão e tente novamente."
            }
        }
    }
}