package com.example.controlhorasmobile.ui.screens.informes.components

import android.content.Context
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File

suspend fun guardarPdfDesdeResponse2(
    response: Response<ResponseBody>,
    context: Context,
    nombreArchivo: String = "Informe.pdf"
) : File? {
    if (!response.isSuccessful || response.body() == null) return null

    val file = File(context.filesDir, nombreArchivo)
    response.body()?.byteStream().use { input ->
        file.outputStream().use { output ->
            input?.copyTo(output)
            output.flush()
        }
    }
    return file
}