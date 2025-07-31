package com.example.controlhorasmobile.ui.screens.dashboard.logic

import android.util.Log
import com.example.controlhorasmobile.model.Registro
import com.example.controlhorasmobile.model.dto.RegistroDTO
import org.threeten.bp.LocalDate

import org.threeten.bp.format.DateTimeFormatter
import kotlin.math.roundToInt

object DashboardCalculations {

    private val formatoEntrada = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    fun obtenerRangoMesActual(): Pair<LocalDate, LocalDate> {
        val hoy = LocalDate.now()
        val inicio = hoy.withDayOfMonth(1)
        val fin = hoy.withDayOfMonth(hoy.lengthOfMonth())

        return Pair(inicio,fin)
    }

    fun calcularHorasMes(registros: List<Registro>): String {

        val hoy = LocalDate.now()
        val total = registros
            .filter {
                val fecha = LocalDate.parse(it.fecha, formatoEntrada)
                fecha.monthValue == hoy.monthValue && fecha.year == hoy.year
            }
            .sumOf { parseDuracion(it.duracion) }
        return formatearHoras(total)
    }

    fun calcularDiasTrabajados(registros: List<Registro>) : Int {
        val hoy = LocalDate.now()

        return registros
            .asSequence()
            .mapNotNull {
                try {
                    val fecha = LocalDate.parse(it.fecha.trim(), formatoEntrada)

                    fecha
                }
                catch (_: Exception){
                    Log.e("Trabajados", "Fecha inválida: `${it.fecha}`")
                    null
                }
            }
            .filter { fecha ->
                fecha.monthValue == hoy.monthValue && fecha.year == hoy.year
            }
            .map { it.toEpochDay() }
            .distinct()
            .count()
    }

    fun obtenerProximoMiercoles(): String {
        val dias = mapOf(
            1 to "Lunes",
            2 to "Martes",
            3 to "Miércoles",
            4 to "Jueves",
            5 to "Viernes",
            6 to "Sábado",
            7 to "Domingo"
        )

        val meses = mapOf(
            1 to "enero",
            2 to "febrero",
            3 to "marzo",
            4 to "abril",
            5 to "mayo",
            6 to "junio",
            7 to "julio",
            8 to "agosto",
            9 to "septiembre",
            10 to "octubre",
            11 to "noviembre",
            12 to "diciembre"
        )

        val hoy = LocalDate.now()
        val diaHoy = hoy.dayOfWeek.value
        val diasHastaMiercoles = 3 - diaHoy
        val offset = if (diasHastaMiercoles > 0) diasHastaMiercoles else 7 + diasHastaMiercoles
        val proximo = hoy.plusDays(offset.toLong())


        val nombreDia = dias[proximo.dayOfWeek.value] ?: "Miércoles"
        val nombreMes = meses[proximo.month.value] ?: "mes"

        return "$nombreDia ${proximo.dayOfMonth} de $nombreMes, faltan $offset dias"
    }

    private fun parseDuracion(duracion: String):Double {
        return try {
            val horas = duracion.substringBefore("h").trim().toIntOrNull() ?: 0
            val minutos = duracion.substringAfter("h").substringBefore("m").trim().toIntOrNull() ?: 0

            horas + (minutos / 60.0)
        }
        catch (e: Exception){
            Log.e("HorasMes", "Error al interpretar: '$duracion' -> ${e.message}")
            0.0
        }
    }

    private fun formatearHoras(decimal:Double): String {

        val totalMinutos = (decimal * 60).roundToInt()
        val horas = totalMinutos / 60
        Log.e("HorasMes","Horas int: $horas")
        val minutos = totalMinutos % 60
        Log.e("HorasMes","Minutos int: $minutos")

        return "%dh %02dm".format(horas, minutos)
    }

    fun LocalDate.toIsoString(): String = this.format(DateTimeFormatter.ISO_LOCAL_DATE)



}

