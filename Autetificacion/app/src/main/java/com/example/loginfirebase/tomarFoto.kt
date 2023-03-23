package com.example.loginfirebase

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Instrumentation
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
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
import java.util.*

@Suppress("DEPRECATION")
class tomarFoto : AppCompatActivity() {

    private val IMAGE_CAPTURE_REQUEST_CODE = 1
    private var photoFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tomar_foto)

        val btnCamara = findViewById<Button>(R.id.btnAbrirCamara)

        btnCamara.setOnClickListener(){
            //startForResult.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startForResult.launch(takePictureIntent)

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

}