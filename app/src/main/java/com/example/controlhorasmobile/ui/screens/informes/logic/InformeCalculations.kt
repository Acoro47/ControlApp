package com.example.controlhorasmobile.ui.screens.informes.logic

import android.content.Context
import android.util.Log
import org.threeten.bp.format.DateTimeFormatter
import com.example.controlhorasmobile.model.Registro
import com.example.controlhorasmobile.model.ResumenDia
import com.example.controlhorasmobile.network.recargarRegistros
import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.TextStyle
import java.util.Locale

fun registrosDia(registros: List<Registro>): List<ResumenDia> {
    val formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val formatoHora = DateTimeFormatter.ofPattern("HH:mm")

    return registros
        .groupBy { it.fecha }
        .toSortedMap(compareBy { LocalDate.parse(it, formatoFecha)})
        .map {
            (fechaStr, registrosDelDia) ->
            val fecha = LocalDate.parse(fechaStr, formatoFecha)
            val entradasValidas = registrosDelDia.filter { it.horaEntrada.isNotBlank() && it.horaSalida.isNotBlank()  }

            val primerTurno = entradasValidas.firstOrNull()
            val segundoTurno = if (entradasValidas.size >1) entradasValidas.getOrNull(1) else null

            val entrada1 = primerTurno?.horaEntrada
            val salida1 = primerTurno?.horaSalida
            val entrada2 = segundoTurno?.horaEntrada
            val salida2 = segundoTurno?.horaSalida

            // Convertir a localtime para calcular duración
            val (duracionTotal, duracionEnMinutos) = runCatching {
                val tEntrada1 = LocalTime.parse(entrada1, formatoHora)
                val tSalida1 = LocalTime.parse(salida1, formatoHora)
                val d1 = Duration.between(tEntrada1, tSalida1)

                val d2 = if (entrada2 != null && salida2 != null) {
                    val tEntrada2 = LocalTime.parse(entrada2, formatoHora)
                    val tSalida2 = LocalTime.parse(salida2, formatoHora)
                    Duration.between(tEntrada2, tSalida2)
                }
                else {
                    Duration.ZERO
                }

                val total = d1.plus(d2)
                val h = total.toHours()
                val m = total.toMinutesPart()
                val texto = "$h h $m m"
                val minutos = total.toMinutes().toInt()

                Pair(texto, minutos)

            }.getOrDefault(Pair(null,0))
            Log.d("Resumen", "diaMes: ${fecha.dayOfMonth}")
            Log.d("Resumen", "diaSemana: ${fecha.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es"))}")
            Log.d("Resumen", "entrada1: $entrada1")
            Log.d("Resumen", "salida1: $salida1")
            Log.d("Resumen", "entrada2: $entrada2")
            Log.d("Resumen", "salida2: $salida2")
            Log.d("Resumen", "Duración $duracionTotal")



            ResumenDia(
                diaMes = fecha.dayOfMonth.toString(),
                diaSemana = fecha.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("es")),
                entrada1 = entrada1,
                salida1 = salida1,
                entrada2 = entrada2,
                salida2 = salida2,
                duracion = duracionTotal,
                totalMinutos = duracionEnMinutos
            )

        }
}
