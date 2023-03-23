package com.example.loginfirebase

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database



@Suppress("DEPRECATION", "NAME_SHADOWING")
class MainActivity2 : AppCompatActivity() {

    private  lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)


        val btnCamara : Button = findViewById(R.id.btnCamara)
        val btnBaseDatos : Button = findViewById(R.id.btnCrearBaseDatos)


        btnBaseDatos.setOnClickListener(){
            crearBaseDeDatos()
        }

        btnCamara.setOnClickListener(){
            val i = Intent(this, tomarFoto::class.java)
            startActivity(i)
        }
        
        firebaseAuth = Firebase.auth

    }


    private  fun crearBaseDeDatos(){

        // Crea una instancia de Firebase Database
        val database = Firebase.database

        // Crea una referencia a la raíz de la base de datos
        val myRef = database.reference

        // Crea un objeto de tipo Producto con los datos que deseas almacenar
        val producto = Producto("1234", "Descripción del producto", 10)

        // Almacena el objeto en la base de datos bajo la referencia "productos"
        myRef.child("productos").push().setValue(producto)

    }


    data class Producto(val codigo: String, val descripcion: String, val stock: Int)

    //para cuando retrocedamos no salga del menu principal
    //pasos
    //CODE - OVERRIDE METHODS
    //y buscamos  OnBackPressed

    //override fun onBackPressed() {
       // super.onBackPressed()             - el super permite retroceder con el boton retroceder
    //}

    //para agregar el menu creado en la carpeta res - menu -nav_menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        return true
    //super.onCreateOptionsMenu(menu)
    }

    // ´para dar funcionalidad a los elementos del menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.menu_buscar ->{
                Toast.makeText(baseContext,"Buscar Info", Toast.LENGTH_LONG).show()
            }
            R.id.menu_salir ->{
                signOut()
            }
        }


        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        return
    }


    private fun signOut(){
        firebaseAuth.signOut()
        Toast.makeText(baseContext,"Se cerro exitosamente la cuenta", Toast.LENGTH_LONG).show()
        val i = Intent(this,MainActivity::class.java)
        startActivity(i)
    }

}