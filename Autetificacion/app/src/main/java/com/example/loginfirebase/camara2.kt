package com.example.loginfirebase

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream

class camara2 : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var photoView: ImageView

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camara2)

        // Obtener referencia al ImageView
        photoView = findViewById(R.id.imgCamara2)

        database = FirebaseDatabase.getInstance()
        databaseRef = database.reference

        val btnGuardar : Button = findViewById(R.id.buttGuardar)
        val txtNombreProducto: TextView = findViewById(R.id.txtNombreProducto)
        val txtCantidad : TextView = findViewById(R.id.txtCantidadProducto)
       // val imgCamara2: ImageView = findViewById(R.id.imgCamara2)


        // Configurar el botón de la cámara
        val cameraButton: Button = findViewById(R.id.buutAbrir)
        cameraButton.setOnClickListener { dispatchTakePictureIntent() }

        btnGuardar.setOnClickListener(){
            val nombreProducto = txtNombreProducto.text.toString()
            val stock = txtCantidad.text.toString()
            val image = (photoView.drawable as BitmapDrawable).bitmap

            // Convertir la imagen en un array de bytes
            val baos = ByteArrayOutputStream()
            image.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val data = baos.toByteArray()

            // Crear un nuevo objeto de datos para guardar en la base de datos
            val newData = Data(nombreProducto, stock, data)

            // Agregar los datos a la base de datos
            databaseRef.child("productos").push().setValue(newData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Datos guardados correctamente", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Log.e("Firebase", "Error al guardar datos: ${it.message}")
                    Toast.makeText(this, "Error al guardar datos", Toast.LENGTH_SHORT).show()
                }
        }

    }
    data class Data(val nombreProducto: String = "", val stock: String = "", val image: ByteArray? = null)



    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        // Verificar que haya una aplicación de cámara disponible
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Verificar si se tomó una foto
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Obtener la imagen tomada
            val imageBitmap = data?.extras?.get("data") as Bitmap

            // Mostrar la imagen en el ImageView
            photoView.setImageBitmap(imageBitmap)
        }
    }

}