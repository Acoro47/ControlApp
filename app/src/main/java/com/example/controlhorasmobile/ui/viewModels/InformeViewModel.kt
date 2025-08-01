package com.example.controlhorasmobile.ui.viewModels

import android.content.ContentResolver
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controlhorasmobile.network.InformePdfService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.YearMonth
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

sealed class InformeUiState {
    data object Loading: InformeUiState()
    data class Success(val pdfBytes: ByteArray): InformeUiState() {
        override fun equals(other: Any?): Boolean =
            other is Success && pdfBytes.contentEquals(other.pdfBytes)

        override fun hashCode(): Int = pdfBytes.contentHashCode()
    }
    data class Error(val message: String): InformeUiState()
}

@HiltViewModel
class InformeViewModel @Inject constructor(
    private val informePdfService: InformePdfService
): ViewModel() {

    private val _uiState = MutableStateFlow<InformeUiState>(InformeUiState.Loading)
    val uiState: StateFlow<InformeUiState> = _uiState

    fun descargarInforme(mes: YearMonth? = null) {
        _uiState.value = InformeUiState.Loading

        viewModelScope.launch {
            try {
                val mesParam = mes?.format(DateTimeFormatter.ofPattern("yyyy-MM"))
                val response = informePdfService.descargarPdf(mesParam)
                if (response.isSuccessful && response.body() != null) {
                    Log.e("Http", "Respuesta: ${response.body()}")
                    Log.e("Http", "Respuesta: ${response.headers()}")
                    val pdfBytes = response.body()!!.bytes()
                    _uiState.value = InformeUiState.Success(pdfBytes)

                } else {
                    val errorMessage = "HTTP ${response.code()}: ${response.errorBody()?.string()}"
                    _uiState.value = InformeUiState.Error(errorMessage)
                }

            } catch (e: Exception){
                _uiState.value = InformeUiState.Error("Error al descargar: ${e.message}")
            }
        }
    }

    suspend fun guardarInforme(
        uri: Uri,
        resolver: ContentResolver,
        bytes: ByteArray
    ): Boolean = withContext(Dispatchers.IO) {
            try {
                resolver.openOutputStream(uri,"w")?.use { output ->
                    output.write(bytes)
                    output.flush()
                } ?: return@withContext false
                true
            } catch (e: Exception) {
                _uiState.value = InformeUiState.Error("Error al guardar el PDF: ${e.message}")
                false
            }
    }
}