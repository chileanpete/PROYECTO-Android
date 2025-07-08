package com.example.proyecto_droid.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.util.*
import android.widget.Toast

object FileUtils {
    
    fun savePdfToDevice(context: Context, pdfContent: String, filename: String): File? {
        return try {
            println("Iniciando guardado de PDF: $filename")
            
            // Guardar en el directorio interno de la app
            val file = File(context.filesDir, filename)
            println("Archivo a crear (interno): ${file.absolutePath}")
            
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
            Toast.makeText(context, "Intentando compartir PDF...", Toast.LENGTH_SHORT).show()
            println("Ruta del archivo: ${file.absolutePath}, existe: ${file.exists()}")

            if (!file.exists()) {
                println("Error: El archivo no existe")
                Toast.makeText(context, "El archivo PDF no existe", Toast.LENGTH_SHORT).show()
                return
            }

            val uri = try {
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    file
                )
            } catch (e: Exception) {
                println("Error al obtener URI: ${e.message}")
                Toast.makeText(context, "Error al obtener URI del PDF", Toast.LENGTH_LONG).show()
                return
            }

            println("URI generada: $uri")
            Toast.makeText(context, "URI generada: $uri", Toast.LENGTH_SHORT).show()

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                putExtra(Intent.EXTRA_SUBJECT, "Reporte de registros")
                putExtra(Intent.EXTRA_TEXT, "Adjunto el reporte de registros generado por la app.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) // Solución para contexto de aplicación
            }

            println("Intent creado, iniciando selector de apps...")
            Toast.makeText(context, "Abriendo selector de apps para compartir...", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent.createChooser(intent, "Compartir PDF"))
            println("Selector de apps iniciado")

        } catch (e: Exception) {
            println("Error al compartir PDF: ${e.message}")
            Toast.makeText(context, "Error al compartir PDF: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }
} 