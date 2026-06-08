package br.com.fiap.alagaalert.data.model

data class WeatherData(
    val cityName: String,
    val temperature: Double,
    val description: String,
    val rainVolume: Double, // mm na última hora
    val humidity: Int
)