package com.example.controlhorasmobile.ui.screens.informes.components

import android.app.DatePickerDialog
import android.content.Context
import org.threeten.bp.LocalDate


// Función para mostrar los botones de rangos de fecha

fun mostrarDatePicker(
    context: Context,
    fechaActual: LocalDate,
    onFechaSeleccionada: (LocalDate) -> Unit
) {
    DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            val nuevaFecha = LocalDate.of(year, month + 1, dayOfMonth)
            onFechaSeleccionada(nuevaFecha)
        },
        fechaActual.year,
        fechaActual.monthValue - 1,
        fechaActual.dayOfMonth

    ).show()
}

fun mostrarFechaPicker(
    context: Context,
    fechaActual: LocalDate,
    onFechaSeleccionada: (LocalDate) -> Unit
) {
    val meses = arrayOf(
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    )
    val years = (2020..LocalDate.now().year).toList().reversed().toTypedArray()

    val builder = android.app.AlertDialog.Builder(context)
    builder.setTitle("Seleccionar mes y año")
    var mesSeleccionado = fechaActual.monthValue -1
    var anySeleccionado = fechaActual.year

    builder.setSingleChoiceItems(meses, mesSeleccionado) {_, index ->
        mesSeleccionado = index
    }

    builder.setPositiveButton("Aceptar") { _, _ ->
        val fecha = LocalDate.of(anySeleccionado, mesSeleccionado + 1,1)
        onFechaSeleccionada(fecha)
    }

    builder.setNeutralButton("Cambiar año") {_,_ ->
        val yearPicker = android.widget.NumberPicker(context).apply {
            minValue = 2020
            maxValue = LocalDate.now().year
            value = anySeleccionado
            setOnValueChangedListener {_, _, newVal ->
                anySeleccionado = newVal
            }
        }

        val yearDialog = android.app.AlertDialog.Builder(context)
            .setTitle("Seleccionar año")
            .setView(yearPicker)
            .setPositiveButton("Aceptar") {_, _ ->
                val fecha = LocalDate.of(anySeleccionado, mesSeleccionado +1, 1)
                onFechaSeleccionada(fecha)
            }
            .create()

        yearDialog.show()
    }

    builder.create().show()

}

