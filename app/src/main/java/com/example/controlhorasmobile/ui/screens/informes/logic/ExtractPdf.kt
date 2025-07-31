package com.example.controlhorasmobile.ui.screens.informes.logic



import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.IOException

suspend fun guardarPdfDesdeResponse(
    response: Response<ResponseBody>,
    context: Context,
    filename: String
): File? = withContext(Dispatchers.IO){
    if (!response.isSuccessful) return@withContext null

    val body = response.body() ?: return@withContext null

    val archivo = File(context.filesDir, filename)

    try {
        archivo.outputStream().use { fileout ->
            body.byteStream().use { input ->
                input.copyTo(fileout)
                fileout.flush()
            }
        }
        return@withContext archivo

    }catch (e: IOException) {
        e.printStackTrace()
        return@withContext null
    }
}

fun openPdf(context: Context, file: File){
    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, "application/pdf")
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK
    }
    val pm = context.packageManager
    if (intent.resolveActivity(pm) != null){
        context.startActivity(intent)
    } else {
        Toast.makeText(context, "Necesitas descargar una app para abrir PDF", Toast.LENGTH_SHORT).show()
    }

}