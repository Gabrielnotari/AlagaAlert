package br.com.fiap.alagaalert.utils

import br.com.fiap.alagaalert.data.model.RiskLevel

object RiskCalculator {
    fun calculateRisk(rainVolumeMm: Double): RiskLevel {
        return when {
            rainVolumeMm >= 20.0 -> RiskLevel.HIGH
            rainVolumeMm >= 8.0  -> RiskLevel.MEDIUM
            else                 -> RiskLevel.LOW
        }
    }

    fun getRiskLabel(level: RiskLevel): String {
        return when (level) {
            RiskLevel.HIGH   -> "⚠️ Alto Risco"
            RiskLevel.MEDIUM -> "⚡ Risco Moderado"
            RiskLevel.LOW    -> "✅ Baixo Risco"
        }
    }

    fun getRiskColor(level: RiskLevel): Int {
        return when (level) {
            RiskLevel.HIGH   -> android.R.color.holo_red_light
            RiskLevel.MEDIUM -> android.R.color.holo_orange_light
            RiskLevel.LOW    -> android.R.color.holo_green_light
        }
    }
}