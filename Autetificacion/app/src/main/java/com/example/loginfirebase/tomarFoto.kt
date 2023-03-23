package com.example.loginfirebase

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Instrumentation
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.icu.text.SimpleDateFormat
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import  android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*


@Suppress("DEPRECATION")
class tomarFoto : AppCompatActivity() {

    private val IMAGE_CAPTURE_REQUEST_CODE = 1
    private var photoFile: File? = null
    private var currentPhotoPath: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tomar_foto)

        val btnCamara = findViewById<Button>(R.id.btnAbrirCamara)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarFoto)
// Primero, crear un archivo temporal donde guardar la imagen
        val photoFile = createImageFile()

        val photoURI: Uri = FileProvider.getUriForFile(this, "com.example.loginfirebase.fileprovider", photoFile)
        btnCamara.setOnClickListener(){
            //startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            //takePictureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1)

            startForResult.launch(takePictureIntent)
        }

        btnGuardar.setOnClickListener(){
            val imageView = findViewById<ImageView>(R.id.imageView)
            val drawable = imageView.drawable
            if (drawable != null && drawable is BitmapDrawable) {
                guardarImagen(drawable.bitmap)
            } else {
                Toast.makeText(this, "No hay imagen para guardar", Toast.LENGTH_LONG).show()
            }
        }

    }
   /* private  val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
    {
       /* result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK){
            val intent = result.data
            val imageBitmap = intent?.extras?.get("data") as Bitmap
            val imageView = findViewById<ImageView>(R.id.imageView)
            imageView.setImageBitmap(imageBitmap)
        }*/



    }*/
   private val startForResult =
       registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
           if (result.resultCode == Activity.RESULT_OK) {
               val imageBitmap = result.data?.extras?.get("data") as Bitmap
               val imageView = findViewById<ImageView>(R.id.imageView)
               imageView.setImageBitmap(imageBitmap)
           }
       }

    //funcion para guardar la foto
    private fun guardarImagen(bitmap: Bitmap) {
        // Verifica si el almacenamiento externo está disponible
        val estadoAlmacenamiento = Environment.getExternalStorageState()
        if (estadoAlmacenamiento == Environment.MEDIA_MOUNTED) {
            // Crea un archivo en la carpeta de imágenes pública del almacenamiento externo
            val directorioPublico = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val nombreArchivo = "imagen_${System.currentTimeMillis()}.jpg"
            val archivo = File(directorioPublico, nombreArchivo)

            // Escribe el bitmap en el archivo
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(archivo)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()

                // Actualiza la galería de medios para que la imagen se pueda ver en la aplicación de fotos
                MediaScannerConnection.scanFile(this, arrayOf(archivo.path), arrayOf("image/jpeg"), null)

                // Muestra un mensaje de éxito
                Toast.makeText(this, "Imagen guardada en ${archivo.path}", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_LONG).show()
            } finally {
                out?.close()
            }
        } else {
            Toast.makeText(this, "Almacenamiento externo no disponible", Toast.LENGTH_LONG).show()
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Crea un nombre de archivo único
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"

        // Obtiene la carpeta de almacenamiento de imágenes externa
        val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        // Crea el archivo de imagen en la carpeta de almacenamiento de imágenes
        val imageFile = File.createTempFile(
            imageFileName,  /* prefijo */
            ".jpg",         /* extensión */
            storageDir      /* directorio */
        )

        // Guarda la ruta del archivo creado
        currentPhotoPath = imageFile.absolutePath
        return imageFile
    }

}