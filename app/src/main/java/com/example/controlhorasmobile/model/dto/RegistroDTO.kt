package com.example.controlhorasmobile.model.dto


import com.example.controlhorasmobile.model.Registro
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

data class RegistroDTO(
    val entrada: String?,
    val salida: String?,
    val duracionBase: String,
    val duracionExtra: String,
    val duracionTotal: String,
)

fun RegistroDTO.toRegistro(): Registro {
    val formatterFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val formatterHora = DateTimeFormatter.ofPattern("HH:mm")

    val fecha: String = entrada?.let {
        LocalDateTime.parse(it).format(formatterFecha)
    } ?: "—"
    val horaEntrada: String = entrada?.let {
        LocalDateTime.parse(it).format(formatterHora)
    } ?: "-"

    val horaSalida: String = salida?.let {
        LocalDateTime.parse(it).format(formatterHora)
    } ?: "-"

    val duracion = duracionTotal.ifBlank { "⏳ En curso..." }

    return Registro(
        fecha = fecha,
        horaEntrada = horaEntrada,
        horaSalida = horaSalida,
        duracion = duracion
    )
}

