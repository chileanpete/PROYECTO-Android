package com.example.proyecto_droid.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.util.*

object FileUtils {
    
    fun savePdfToDevice(context: Context, pdfContent: String, filename: String): File? {
        return try {
            println("Iniciando guardado de PDF: $filename")
            
            // Crear directorio si no existe
            val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            println("Directorio de descargas: ${downloadsDir.absolutePath}")
            
            if (!downloadsDir.exists()) {
                val created = downloadsDir.mkdirs()
                println("Directorio creado: $created")
            }
            
            // Crear archivo
            val file = File(downloadsDir, filename)
            println("Archivo a crear: ${file.absolutePath}")
            
            val fos = FileOutputStream(file)
            
            // Decodificar contenido base64 y escribir
            val pdfBytes = android.util.Base64.decode(pdfContent, android.util.Base64.DEFAULT)
            println("Tamaño del PDF: ${pdfBytes.size} bytes")
            
            fos.write(pdfBytes)
            fos.close()
            
            println("PDF guardado exitosamente en: ${file.absolutePath}")
            println("Archivo existe: ${file.exists()}")
            println("Tamaño del archivo: ${file.length()} bytes")
            
            file
        } catch (e: Exception) {
            println("Error al guardar PDF: ${e.message}")
            e.printStackTrace()
            null
        }
    }
    
    fun sharePdf(context: Context, file: File) {
        try {
            println("Iniciando compartir PDF: ${file.absolutePath}")
            
            if (!file.exists()) {
                println("Error: El archivo no existe")
                return
            }
            
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                file
            )
            
            println("URI generada: $uri")
            
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Reporte de registros")
                putExtra(Intent.EXTRA_TEXT, "Adjunto el reporte de registros generado por la app.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            
            println("Intent creado, iniciando selector de apps...")
            context.startActivity(Intent.createChooser(intent, "Compartir PDF"))
            println("Selector de apps iniciado")
            
        } catch (e: Exception) {
            println("Error al compartir PDF: ${e.message}")
            e.printStackTrace()
        }
    }
} 