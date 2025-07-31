package com.example.controlhorasmobile.network

import android.util.Log
import com.example.controlhorasmobile.model.Registro
import com.example.controlhorasmobile.model.dto.toRegistro
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

suspend fun recargarRegistros(
    idUsuario: Long,
    desde: LocalDate,
    hasta: LocalDate,
    tokenProvider:() -> String?

): List<Registro> {
    val formatter = DateTimeFormatter.ISO_LOCAL_DATE
    val desdeStr = desde.format(formatter)
    val hastaStr = hasta.format(formatter)
    return try {
        val usuarioService = RetrofitClient.getUsuarioService(tokenProvider)
        val registrosDTO = usuarioService.obtenerRegistros(
            idUsuario,
            fechaDesde = desdeStr,
            fechaHasta = hastaStr
        )
        Log.d("RegistroNetwork", "✅ Registros recibidos: $registrosDTO")
        registrosDTO.map { it.toRegistro() }


    } catch (e: Exception) {
        Log.e("RegistroNetwork", "Error al cargar registros: ${e.message}")
        emptyList()
    }
}