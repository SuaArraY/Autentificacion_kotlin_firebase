package com.example.loginfirebase

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

@Suppress("DEPRECATION")
class crearCuenta : AppCompatActivity() {

    //variable que conecta a Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private val REQUEST_IMAGE_CAPTURE = 1
    private lateinit var mImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cuenta)
        var txtUser : TextView = findViewById(R.id.txtUsuario)
        val txtContra : TextView = findViewById(R.id.txtContraCC)
        val txtContra2 : TextView = findViewById(R.id.txtContraCC2)
        val txtCorreo: TextView = findViewById(R.id.txtCorreoCC)
        val btnCrearCuentaNueva : Button = findViewById(R.id.btnRegistrarCC)
        val btnFoto : Button = findViewById(R.id.btnImagenCC)
        val btnGaleria = findViewById<Button>(R.id.btnescogerImg)
         mImageView = findViewById(R.id.imgCrearCuenta)

        btnFoto.setOnClickListener(){
            dispatchTakePictureIntent()
        }


        btnCrearCuentaNueva.setOnClickListener(){

            val pass1 = txtContra.text.toString()
            val pass2 = txtContra2.text.toString()

            if (pass1.equals(pass2)){
crearAccount(txtCorreo.text.toString(),txtContra.text.toString())
            }
            else{
                Toast.makeText(baseContext, "ERROR: las contraseñas no coinciden", Toast.LENGTH_LONG).show()
                txtContra.requestFocus()
                txtContra2.setText("")
            }
        }




        firebaseAuth = Firebase.auth
    }

    private  fun crearAccount(email: String, password: String){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    Toast.makeText(baseContext, "Cuenta creada correctamente", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(baseContext,"Algo salio mal, ERROR: " + task.exception, Toast.LENGTH_LONG).show()
                }

            }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val extras = data?.extras
            val imageBitmap = extras?.get("data") as Bitmap
            mImageView.setImageBitmap(imageBitmap)
        }
    }


}