package br.com.fiap.alagaalert.screens

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.fiap.alagaalert.data.model.RiskLevel
import br.com.fiap.alagaalert.data.model.RiskZone
import br.com.fiap.alagaalert.databinding.ActivityMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapBinding
    private lateinit var map: GoogleMap

    // Zonas de risco hardcodadas de São Paulo
    private val riskZones = listOf(
        RiskZone(
            "Jabaquara", -23.6768, -46.6658, RiskLevel.HIGH,
            "Histórico de alagamentos em chuvas acima de 20mm/h"
        ),
        RiskZone("Brooklin", -23.6199, -46.6965, RiskLevel.HIGH,
            "Região com drenagem insuficiente"),
        RiskZone("Vila Prudente", -23.5929, -46.5785, RiskLevel.HIGH,
            "Próximo ao Córrego Oratório"),
        RiskZone("Santo André", -23.6639, -46.5383, RiskLevel.MEDIUM,
            "Risco moderado em chuvas intensas"),
        RiskZone("Tatuapé", -23.5389, -46.5761, RiskLevel.MEDIUM,
            "Pontos de atenção na Av. Radial Leste"),
        RiskZone("Pinheiros", -23.5629, -46.6939, RiskLevel.MEDIUM,
            "Rio Pinheiros pode transbordar"),
        RiskZone("Santana", -23.5019, -46.6279, RiskLevel.LOW,
            "Infraestrutura de drenagem adequada"),
        RiskZone("Moema", -23.5999, -46.6639, RiskLevel.LOW,
            "Baixo histórico de alagamentos")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(br.com.fiap.alagaalert.R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Centraliza em São Paulo
        val saoPaulo = LatLng(-23.5505, -46.6333)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(saoPaulo, 11f))

        // Adiciona marcadores e círculos de risco
        riskZones.forEach { zone ->
            val position = LatLng(zone.latitude, zone.longitude)
            val (markerColor, circleColor) = getRiskColors(zone.riskLevel)

            // Círculo de área de risco
            map.addCircle(
                CircleOptions()
                    .center(position)
                    .radius(800.0)
                    .strokeColor(circleColor)
                    .fillColor(circleColor and 0x00FFFFFF or 0x33000000)
                    .strokeWidth(2f)
            )

            // Marcador
            map.addMarker(
                MarkerOptions()
                    .position(position)
                    .title(zone.name)
                    .snippet("${getRiskLabel(zone.riskLevel)} — ${zone.description}")
                    .icon(BitmapDescriptorFactory.defaultMarker(markerColor))
            )
        }

        // Ao clicar no marcador, atualiza card inferior
        map.setOnMarkerClickListener { marker ->
            val zone = riskZones.find { it.name == marker.title }
            zone?.let {
                binding.tvZoneName.text = "📍 ${it.name}"
                binding.tvZoneRisk.text = getRiskLabel(it.riskLevel)
                binding.tvZoneDescription.text = it.description
            }
            false
        }
    }

    private fun getRiskColors(level: RiskLevel): Pair<Float, Int> {
        return when (level) {
            RiskLevel.HIGH   -> Pair(BitmapDescriptorFactory.HUE_RED, Color.RED)
            RiskLevel.MEDIUM -> Pair(BitmapDescriptorFactory.HUE_ORANGE, Color.parseColor("#FB8C00"))
            RiskLevel.LOW    -> Pair(BitmapDescriptorFactory.HUE_GREEN, Color.GREEN)
        }
    }

    private fun getRiskLabel(level: RiskLevel): String {
        return when (level) {
            RiskLevel.HIGH   -> "🔴 Alto Risco de Alagamento"
            RiskLevel.MEDIUM -> "🟠 Risco Moderado"
            RiskLevel.LOW    -> "🟢 Baixo Risco"
        }
    }
}