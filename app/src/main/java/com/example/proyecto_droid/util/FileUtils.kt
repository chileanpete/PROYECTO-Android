package com.example.proyecto_droid.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Base64
import android.util.Log
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object FileUtils {
    
    /**
     * Guardar PDF desde contenido base64 (método original)
     */
    fun savePdfToDevice(context: Context, base64Content: String, filename: String): File? {
        return try {
            val pdfBytes = Base64.decode(base64Content, Base64.DEFAULT)
            val downloadsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
            val file = File(downloadsDir, filename)
            FileOutputStream(file).use { it.write(pdfBytes) }
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Descargar PDF desde URL y guardarlo en el dispositivo
     */
    suspend fun downloadPdfFromUrl(context: Context, downloadUrl: String, filename: String): File? {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("FileUtils", "Iniciando descarga de PDF desde: $downloadUrl")
                
                // Crear cliente HTTP
                val client = OkHttpClient.Builder()
                    .followRedirects(true)
                    .followSslRedirects(true)
                    .build()
                
                // Crear request
                val request = Request.Builder()
                    .url(downloadUrl)
                    .build()
                
                // Ejecutar request
                val response = client.newCall(request).execute()
                
                if (!response.isSuccessful) {
                    Log.e("FileUtils", "Error al descargar PDF: ${response.code}")
                    return@withContext null
                }
                
                // Obtener directorio de descargas
                val downloadsDir = File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "ProyectoDroid"
                )
                
                // Crear directorio si no existe
                if (!downloadsDir.exists()) {
                    downloadsDir.mkdirs()
                }
                
                // Crear archivo de destino
                val file = File(downloadsDir, filename)
                
                // Escribir contenido al archivo
                response.body?.byteStream()?.use { inputStream ->
                    FileOutputStream(file).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                }
                
                Log.d("FileUtils", "PDF descargado exitosamente en: ${file.absolutePath}")
                Log.d("FileUtils", "Tamaño del archivo: ${file.length()} bytes")
                
                file
                
            } catch (e: IOException) {
                Log.e("FileUtils", "Error de IO al descargar PDF: ${e.message}", e)
                null
            } catch (e: Exception) {
                Log.e("FileUtils", "Error inesperado al descargar PDF: ${e.message}", e)
                null
            }
        }
    }

    /**
     * Obtener directorio de PDFs en Downloads
     */
    fun getPdfDirectory(): File {
        return File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "ProyectoDroid"
        )
    }

    /**
     * Compartir PDF
     */
    fun sharePdf(context: Context, file: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            context.packageName + ".provider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Compartir PDF"))
    }

    /**
     * Abrir PDF con aplicación externa
     */
    fun openPdf(context: Context, file: File) {
        try {
            val uri: Uri = FileProvider.getUriForFile(
                context,
                context.packageName + ".provider",
                file
            )
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/pdf")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Log.e("FileUtils", "Error al abrir PDF: ${e.message}", e)
        }
    }

    /**
     * Verificar si un archivo PDF existe
     */
    fun pdfExists(filename: String): Boolean {
        val file = File(getPdfDirectory(), filename)
        return file.exists() && file.length() > 0
    }

    /**
     * Eliminar archivo PDF
     */
    fun deletePdf(filename: String): Boolean {
        return try {
            val file = File(getPdfDirectory(), filename)
            file.delete()
        } catch (e: Exception) {
            Log.e("FileUtils", "Error al eliminar PDF: ${e.message}", e)
            false
        }
    }
} 