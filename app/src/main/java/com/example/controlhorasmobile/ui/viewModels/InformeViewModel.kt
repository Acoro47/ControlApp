package com.example.controlhorasmobile.ui.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.controlhorasmobile.network.InformePdfService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
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
                val response: ResponseBody = informePdfService.descargarPdf(mesParam)
                val pdfBytes = response.bytes()

                _uiState.value = InformeUiState.Success(pdfBytes)
            } catch (e: Exception){
                _uiState.value = InformeUiState.Error("Error al descargar: ${e.message}")
            }
        }
    }

    fun guardarInforme(uri: android.net.Uri, resolver: android.content.ContentResolver, bytes: ByteArray){
        viewModelScope.launch {
            try {
                withContext(kotlinx.coroutines.Dispatchers.IO) {
                    resolver.openOutputStream(uri)?.use { output ->
                        output.write(bytes)
                        output.flush()
                    }
                }
            } catch (e: Exception){
                _uiState.value = InformeUiState.Error("Error al guardar el PDF: ${e.message}")
            }
        }
    }
}