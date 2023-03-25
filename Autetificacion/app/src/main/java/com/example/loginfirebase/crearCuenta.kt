package com.example.loginfirebase

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

@Suppress("DEPRECATION")
class crearCuenta : AppCompatActivity() {

    //variable que conecta a Firebase
    private lateinit var firebaseAuth: FirebaseAuth
    private val REQUEST_IMAGE_CAPTURE = 1
    private val PICK_IMAGE = 1
    private val REQUEST_IMAGE_PICK = 2

    private lateinit var mImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE) // deshabilitar la barra de título
        setContentView(R.layout.activity_crear_cuenta)
        // resto del código de tu actividad


        setContentView(R.layout.activity_crear_cuenta)
        val txtUser : TextView = findViewById(R.id.txtUsuario)
        val txtContra : TextView = findViewById(R.id.txtContraCC)
        val txtContra2 : TextView = findViewById(R.id.txtContraCC2)
        val txtCorreo: TextView = findViewById(R.id.txtCorreoCC)
        val btnCrearCuentaNueva : Button = findViewById(R.id.btnRegistrarCC)
        val btnFoto : Button = findViewById(R.id.btnImagenCC)
        val btnGaleria = findViewById<Button>(R.id.btnescogerImg)
         mImageView = findViewById(R.id.imgCrearCuenta)

        btnGaleria.setOnClickListener(){
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        }

        btnFoto.setOnClickListener(){
            dispatchTakePictureIntent(REQUEST_IMAGE_CAPTURE)
        }


       btnCrearCuentaNueva.setOnClickListener(){

           val email = txtCorreo.text.toString()
           val password = txtContra.text.toString()
           val confirmPassword = txtContra2.text.toString()
           val name = txtUser.text.toString()


           if (password == confirmPassword) {
               crearAccount(email, password, name)
           } else {
           Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
       }

        }

        firebaseAuth = Firebase.auth
    }


    private fun crearAccount(email: String, password: String, name: String){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    val currentUser = firebaseAuth.currentUser
                    val userId = currentUser?.uid
                    if (userId != null) {
                        val storageRef = Firebase.storage.reference.child("users/$userId/profile_picture.jpg")
                        val bitmap = (mImageView.drawable as BitmapDrawable).bitmap
                        val baos = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                        val data = baos.toByteArray()
                        val uploadTask = storageRef.putBytes(data)
                        uploadTask.addOnSuccessListener {
                            val databaseRef = Firebase.database.reference.child("users").child(userId)
                            val userMap = HashMap<String, Any>()
                            userMap["name"] = name
                            userMap["email"] = email
                            userMap["profile_picture_url"] = it.metadata?.reference?.downloadUrl.toString()
                            databaseRef.setValue(userMap).addOnSuccessListener {
                                Toast.makeText(baseContext, "Cuenta creada correctamente", Toast.LENGTH_LONG).show()
                            }.addOnFailureListener {
                                Toast.makeText(baseContext, "Error al guardar datos en la base de datos: ${it.message}", Toast.LENGTH_LONG).show()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(baseContext, "Error al cargar imagen: ${it.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                else{
                    Toast.makeText(baseContext,"Algo salio mal, ERROR crearAccount: " + task.exception, Toast.LENGTH_LONG).show()
                }

            }
    }

    /*private  fun crearAccount(email: String, password: String){
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this){task ->
                if (task.isSuccessful){
                    Toast.makeText(baseContext, "Cuenta creada correctamente", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(baseContext,"Algo salio mal, ERROR: " + task.exception, Toast.LENGTH_LONG).show()
                }

            }
    }*/

    @SuppressLint("QueryPermissionsNeeded")
    private fun dispatchTakePictureIntent(requestCode: Int) {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, requestCode)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_IMAGE_CAPTURE -> {
                if (resultCode == RESULT_OK) {
                    val extras = data?.extras
                    val imageBitmap = extras?.get("data") as Bitmap
                    mImageView.setImageBitmap(imageBitmap)
                }
            }
            REQUEST_IMAGE_PICK -> {
                if (resultCode == RESULT_OK && data != null) {
                    val uri: Uri = data.data ?: return
                    val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    mImageView.setImageBitmap(imageBitmap)
                }
            }
            else -> {
                // Handle other requestCode cases here, if any
            }
        }
    }

    //para guardar en la base de datos
    private fun guardarUsuarioEnFirebase(user: String, correo: String, imagen: String) {
        // Referencia de la base de datos
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("usuarios")

        // Crear un objeto Usuario con los datos
        val usuario = User(user, correo, imagen)

        // Guardar el objeto Usuario en la tabla "usuarios"
        myRef.child(user).setValue(usuario).addOnSuccessListener {
            Toast.makeText(baseContext, "Usuario guardado correctamente", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(baseContext,"Algo salió mal al guardar el usuario, ERROR: " + it.message, Toast.LENGTH_LONG).show()
        }
    }
    data class User(
        val nombreUsuario: String,
        val email: String,
        val profileImageUrl: String?
    )

}
