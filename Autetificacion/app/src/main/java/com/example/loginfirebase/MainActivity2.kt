package com.example.loginfirebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
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

    

    override fun onBackPressed() {
        return
    }

}