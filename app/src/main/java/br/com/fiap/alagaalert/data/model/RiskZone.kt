package br.com.fiap.alagaalert.data.model

data class RiskZone(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val riskLevel: RiskLevel,
    val description: String
)

enum class RiskLevel {
    HIGH, MEDIUM, LOW
}