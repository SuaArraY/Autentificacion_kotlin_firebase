package com.example.loginfirebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity2 : AppCompatActivity() {

    private  lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        firebaseAuth = Firebase.auth

    }

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